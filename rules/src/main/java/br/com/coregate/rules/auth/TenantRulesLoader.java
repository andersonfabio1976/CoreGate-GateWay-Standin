package br.com.coregate.rules.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TenantRulesLoader {
    private final StringRedisTemplate redis;
    private final ObjectMapper mapper = new ObjectMapper();

    private String key(String tenantId) { return "coregate:rules:tenant:" + tenantId; }
    private String channel()            { return "coregate:rules:tenant:updates"; }

    public ExternalRulesConfig load(String tenantId) {
        try {
            String json = redis.opsForValue().get(key(tenantId));
            if (json == null) {
                log.warn("⚠️ Tenant {} sem regras no Redis — usando defaults.", tenantId);
                return ExternalRulesConfig.defaults();
            }
            return mapper.readValue(json, ExternalRulesConfig.class);
        } catch (Exception e) {
            log.error("❌ Falha lendo regras do tenant {} — usando defaults.", tenantId, e);
            return ExternalRulesConfig.defaults();
        }
    }

    // opcional — para painel admin
    public void save(String tenantId, ExternalRulesConfig cfg) {
        try {
            String json = mapper.writeValueAsString(cfg);
            redis.opsForValue().set(key(tenantId), json);
            redis.convertAndSend(channel(), tenantId); // notifica listeners p/ invalidar cache
            log.info("✅ Regras atualizadas para tenant {}", tenantId);
        } catch (Exception e) {
            log.error("❌ Falha ao salvar regras do tenant {}", tenantId, e);
        }
    }

    public String pubSubChannel() { return channel(); }
}
