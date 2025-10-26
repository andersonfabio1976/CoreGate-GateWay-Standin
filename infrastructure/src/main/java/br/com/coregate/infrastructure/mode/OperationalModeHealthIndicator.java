package br.com.coregate.infrastructure.mode;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;
import br.com.coregate.infrastructure.enums.OperationalMode;

/**
 * Exibe no /actuator/health o estado atual do modo operacional.
 * Mostra o modo (GATEWAY, STANDIN_AUTOMATIC, STANDIN_REQUESTED)
 * e se o cache está persistente (Redis) ou volátil (in-memory fallback).
 */
@Component
public class OperationalModeHealthIndicator implements HealthIndicator {

    private final OperationalModeManager modeManager;

    public OperationalModeHealthIndicator(OperationalModeManager modeManager) {
        this.modeManager = modeManager;
    }

    @Override
    public Health health() {
        OperationalMode mode = modeManager.getMode();
        boolean volatileCache = modeManager.isVolatileCacheActive();

        Health.Builder builder = mode.isStandIn()
                ? Health.status("DEGRADED")
                : Health.up();

        builder.withDetail("operationalMode", mode.name())
                .withDetail("isStandIn", mode.isStandIn())
                .withDetail("lockedByRequest", mode.isLockedByRequest())
                .withDetail("cacheMode", volatileCache ? "VOLATILE (in-memory)" : "PERSISTENT (Redis)");

        return builder.build();
    }
}
