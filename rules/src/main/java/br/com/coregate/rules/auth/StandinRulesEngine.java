package br.com.coregate.rules.auth;

import br.com.coregate.rules.model.StandinDecision;
import br.com.coregate.rules.model.TransactionFact;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.evrete.KnowledgeService;
import org.evrete.api.Knowledge;
import org.evrete.api.StatefulSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import java.time.Duration;
import java.time.Instant;

/**
 * 🧠 Stand-in Rules Engine
 *
 * Engine central de decisão para o modo Stand-in:
 * - Usa Evrete para processar regras declarativas
 * - Multi-tenant (regras isoladas por tenant)
 * - Cache Redis para limites e velocity window
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class StandinRulesEngine {

    private final TenantRulesLoader rulesLoader;
    private final TenantRulesCache rulesCache;
    private final StringRedisTemplate redis;

    @Autowired(required = false)
    private br.com.coregate.rules.metrics.StandinMetrics metrics; // opcional

    private final Knowledge knowledge = buildKnowledge();

    /** 🔧 Constrói a base de conhecimento (Evrete). */
    private Knowledge buildKnowledge() {
        var builder = new KnowledgeService().newKnowledge().builder();

        builder.newRule("DECLINE_MCC_BLACKLIST")
                .forEach("$t", TransactionFact.class)
                .where("$t.isMccBlacklisted()")
                .execute(ctx -> decide(ctx.get("$t"), DecisionOutcome.DECLINED, "MCC_BLACKLIST"));

        builder.newRule("DECLINE_PAN_VELOCITY")
                .forEach("$t", TransactionFact.class)
                .where("$t.isPanExceedsVelocity()")
                .execute(ctx -> decide(ctx.get("$t"), DecisionOutcome.DECLINED, "VELOCITY"));

        builder.newRule("DECLINE_PAN_DAILY_LIMIT")
                .forEach("$t", TransactionFact.class)
                .where("$t.isPanExceedsDailyLimit()")
                .execute(ctx -> decide(ctx.get("$t"), DecisionOutcome.DECLINED, "DAILY_LIMIT"));

        builder.newRule("DECLINE_GAMBLING_BLOCKED")
                .forEach("$t", TransactionFact.class)
                .where("!$t.isGamblingAllowed() && $t.isMccGambling()")
                .execute(ctx -> decide(ctx.get("$t"), DecisionOutcome.DECLINED, "GAMBLING_BLOCKED"));

        builder.newRule("APPROVE_LOW_AMOUNT_LOW_RISK")
                .forEach("$t", TransactionFact.class)
                .where("$t.isAmountAutoApproves() && $t.isRiskOk() && !$t.isMccBlacklisted()")
                .execute(ctx -> decide(ctx.get("$t"), DecisionOutcome.APPROVED, "LOW_AMOUNT_LOW_RISK"));

        builder.newRule("APPROVE_MCC_WHITELIST")
                .forEach("$t", TransactionFact.class)
                .where("$t.isMccWhitelisted() && $t.isRiskOk()")
                .execute(ctx -> decide(ctx.get("$t"), DecisionOutcome.APPROVED, "MCC_WHITELIST"));

        // fallback
        builder.newRule("REVIEW_DEFAULT")
                .forEach("$t", TransactionFact.class)
                .where("$t.getDecision() == null")
                .execute(ctx -> decide(ctx.get("$t"), DecisionOutcome.REVIEW, "DEFAULT_REVIEW"));

        return builder.build();
    }

    /**
     * Avalia regras para um tenant específico.
     *
     * @param input    Fato de transação (entrada)
     * @param tenantId Identificador do tenant
     */
    public StandinDecision evaluate(TransactionFact input, String tenantId) {
        Instant start = Instant.now();

        // Carrega configuração dinâmica (cache + redis)
        ExternalRulesConfig cfg = rulesCache.get(tenantId, rulesLoader::load);

        // Enriquecimento
        TransactionFact fact = enrich(input, cfg, tenantId);
        log.debug("🧩 [{}] Fact enriquecido: {}", tenantId, fact);

        try (StatefulSession session = knowledge.newStatefulSession()) {
            session.insert(fact);
            session.fire();
        }

        Duration elapsed = Duration.between(start, Instant.now());

        StandinDecision decided = fact.getDecision();
        if (decided != null) {
            log.info("✅ [{}] Decisão final: {} ({}) em {} ms", tenantId,
                    decided.getOutcome(), decided.getReason(), elapsed.toMillis());
            return decided;
        }

        // fallback defensivo
        return StandinDecision.builder()
                .outcome(DecisionOutcome.REVIEW)
                .reason("DEFAULT_REVIEW")
                .requestId(input.getRequestId())
                .build();
    }

    // ---------------- helpers ----------------

    private StandinDecision decide(TransactionFact t, DecisionOutcome outcome, String reason) {
        if (t.getDecision() != null) return t.getDecision(); // não sobrescreve

        StandinDecision decision = StandinDecision.builder()
                .outcome(outcome)
                .reason(reason)
                .authCode(outcome == DecisionOutcome.APPROVED ? "STNDIN" : null)
                .requestId(t.getRequestId())
                .build();

        t.setDecision(decision);

        if (metrics != null) {
            metrics.countDecision(outcome, reason, t.getAmountCents(), t.getMcc());
        }

        log.info("⚡ Decisão emitida: outcome={} reason={} pan={} mcc={}",
                outcome, reason, mask(t.getPan()), t.getMcc());
        return decision;
    }

    private TransactionFact enrich(TransactionFact t, ExternalRulesConfig cfg, String tenantId) {
        TransactionFact f = TransactionFact.builder()
                .requestId(t.getRequestId())
                .pan(t.getPan())
                .merchantId(t.getMerchantId())
                .mcc(t.getMcc())
                .country(t.getCountry())
                .online(t.isOnline())
                .amountCents(t.getAmountCents())
                .riskScore(t.getRiskScore())
                .build();

        String mcc = t.getMcc() == null ? "" : t.getMcc();

        // listas / flags
        f.setMccBlacklisted(cfg.getMccBlacklist().contains(mcc));
        f.setMccWhitelisted(cfg.getMccWhitelist().contains(mcc));
        f.setMccGambling("7995".equals(mcc));
        f.setGamblingAllowed(cfg.isGamblingAllowed());

        // thresholds
        f.setAmountAutoApproves(t.getAmountCents() <= cfg.getAutoApproveMaxAmountCents());
        f.setRiskOk(t.getRiskScore() <= cfg.getMaxRiskScoreAutoApprove());

        String panKey = mask(t.getPan());
        String tenantPrefix = tenantId + ":" + panKey;

        // Velocity window (X segundos)
        String velocityKey = "cg:standin:velocity:" + tenantPrefix;
        Long velocityCount = redis.opsForValue().increment(velocityKey);
        if (velocityCount != null && velocityCount == 1L) {
            redis.expire(velocityKey, Duration.ofSeconds(cfg.getVelocityWindowSeconds()));
        }
        f.setPanExceedsVelocity(velocityCount != null && velocityCount > cfg.getVelocityMaxCount());

        // Daily sum (24h)
        String sumKey = "cg:standin:sum:" + tenantPrefix;
        Long newVal = redis.opsForValue().increment(sumKey, t.getAmountCents());
        if (newVal != null && newVal.equals((long) t.getAmountCents())) {
            redis.expire(sumKey, Duration.ofHours(24));
        }
        f.setPanExceedsDailyLimit(newVal != null && newVal > cfg.getPanDailyLimitCents());

        return f;
    }

    private String mask(String pan) {
        if (pan == null || pan.length() < 10) return "****";
        return pan.substring(0, 6) + "******" + pan.substring(pan.length() - 4);
    }
}
