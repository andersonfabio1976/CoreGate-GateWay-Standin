package br.com.coregate.rules.auth;

import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

@Slf4j
@Component
public class TenantRulesCache {
    private final Map<String, Entry> cache = new ConcurrentHashMap<>();
    private static final Duration TTL = Duration.ofSeconds(60);

    @Value
    private static class Entry {
        ExternalRulesConfig cfg;
        long expiresAtEpochMs;
    }

    public ExternalRulesConfig get(String tenantId, Function<String, ExternalRulesConfig> loader) {
        Entry e = cache.get(tenantId);
        long now = System.currentTimeMillis();
        if (e != null && e.expiresAtEpochMs > now) return e.getCfg();

        ExternalRulesConfig loaded = loader.apply(tenantId);
        cache.put(tenantId, new Entry(loaded, now + TTL.toMillis()));
        return loaded;
    }

    public void invalidate(String tenantId) {
        cache.remove(tenantId);
        log.info("🧼 Cache de regras invalidado para tenant {}", tenantId);
    }
}
