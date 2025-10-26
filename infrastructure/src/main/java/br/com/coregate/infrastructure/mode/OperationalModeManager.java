package br.com.coregate.infrastructure.mode;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import jakarta.annotation.PostConstruct;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import br.com.coregate.infrastructure.enums.OperationalMode;

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

    public OperationalMode getMode() {
        return currentMode.get();
    }

    public boolean isStandIn() {
        return currentMode.get().isStandIn();
    }

    public synchronized void switchTo(OperationalMode newMode, String reason) {
        OperationalMode current = currentMode.get();

        if (current.isLockedByRequest() && newMode == OperationalMode.GATEWAY) {
            log.warn("⚠️ Ignored attempt to switch from STANDIN_REQUESTED to GATEWAY (locked by emitter)");
            return;
        }

        if (current != newMode) {
            currentMode.set(newMode);
            log.warn("⚙️ Operational mode changed: {} → {} | Reason: {}", current, newMode, reason);
            persistMode(newMode);
        }
    }

    public void forceGatewayUnlock(String reason) {
        currentMode.set(OperationalMode.GATEWAY);
        persistMode(OperationalMode.GATEWAY);
        log.info("🔓 Mode manually unlocked by emitter: {}", reason);
    }

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

    private void tryReconnectToRedis() {
        try {
            redisTemplate.hasKey("healthcheck");
            volatileMode.set(false);
            log.info("✅ Redis connection restored — persistence re-enabled");
        } catch (Exception e) {
            log.debug("⏳ Redis still unavailable — staying in volatile mode");
        }
    }

    public boolean isVolatileCacheActive() {
        return volatileMode.get();
    }

}
