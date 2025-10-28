package br.com.coregate.infrastructure.mode;

import br.com.coregate.infrastructure.enums.OperationalMode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import jakarta.annotation.PostConstruct;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

@Slf4j
@Component
public class OperationalModeManager {

    private final AtomicReference<OperationalMode> currentMode = new AtomicReference<>();
    private final StringRedisTemplate redisTemplate;
    private final String redisKey;
    private final String defaultMode;

    // Flag para saber se estamos operando sem Redis
    private final AtomicBoolean volatileMode = new AtomicBoolean(false);

    public OperationalModeManager(
            StringRedisTemplate redisTemplate,
            @Value("${coregate.mode.redis-key:coregate:operational-mode}") String redisKey,
            @Value("${coregate.mode.default:GATEWAY}") String defaultMode
    ) {
        this.redisTemplate = redisTemplate;
        this.redisKey = redisKey;
        this.defaultMode = defaultMode;
    }

    @PostConstruct
    public void init() {
        try {
            String persisted = redisTemplate.opsForValue().get(redisKey);
            OperationalMode mode = persisted != null
                    ? OperationalMode.valueOf(persisted)
                    : OperationalMode.valueOf(defaultMode);

            currentMode.set(mode);
            volatileMode.set(false);
            log.info("🔧 Operational mode initialized from Redis: {}", mode);
        } catch (Exception e) {
            currentMode.set(OperationalMode.valueOf(defaultMode));
            volatileMode.set(true);
            log.warn("⚠️ Redis unavailable at startup — running in volatile cache mode (default={})", defaultMode);
        }
    }

    /** Retorna o modo atual */
    public OperationalMode getMode() {
        return currentMode.get();
    }

    /** Retorna se o sistema está operando em STANDIN (qualquer tipo) */
    public boolean isStandIn() {
        return currentMode.get().isStandIn();
    }

    /**
     * Altera o modo operacional do sistema, persistindo no Redis se disponível.
     * @param newMode Novo modo operacional desejado
     * @param reason  Motivo da troca (para logs)
     */
    public synchronized void switchTo(OperationalMode newMode, String reason) {
        OperationalMode current = currentMode.get();

        // Protege contra troca indevida quando bloqueado manualmente
        if (current.isLockedByRequest() && newMode == OperationalMode.GATEWAY) {
            log.warn("⚠️ Ignored attempt to switch from STANDIN_REQUESTED to GATEWAY (locked by emitter)");
            return;
        }

        if (current != newMode) {
            currentMode.set(newMode);
            log.warn("⚙️ Operational mode changed: {} → {} | Reason: {}", current, newMode, reason);
            persistMode(newMode);
        } else {
            log.debug("ℹ️ Mode remains unchanged: {}", current);
        }
    }

    /** Força desbloqueio manual para GATEWAY */
    public void forceGatewayUnlock(String reason) {
        currentMode.set(OperationalMode.GATEWAY);
        persistMode(OperationalMode.GATEWAY);
        log.info("🔓 Mode manually unlocked by emitter: {}", reason);
    }

    /** Persiste o modo atual no Redis */
    private void persistMode(OperationalMode mode) {
        if (volatileMode.get()) {
            tryReconnectToRedis();
        }

        if (!volatileMode.get()) {
            try {
                redisTemplate.opsForValue().set(redisKey, mode.name());
                log.debug("💾 Persisted operational mode in Redis: {}", mode);
            } catch (Exception e) {
                volatileMode.set(true);
                log.error("❌ Failed to persist mode in Redis (switching to volatile mode): {}", e.getMessage());
            }
        } else {
            log.warn("⚠️ Volatile mode active — mode not persisted (current={})", mode);
        }
    }

    /** Tenta reconectar ao Redis caso esteja em modo volátil */
    private void tryReconnectToRedis() {
        try {
            redisTemplate.hasKey("healthcheck");
            volatileMode.set(false);
            log.info("✅ Redis connection restored — persistence re-enabled");
        } catch (Exception e) {
            log.debug("⏳ Redis still unavailable — staying in volatile mode");
        }
    }

    /** Indica se o modo volátil (sem Redis) está ativo */
    public boolean isVolatileCacheActive() {
        return volatileMode.get();
    }
}
