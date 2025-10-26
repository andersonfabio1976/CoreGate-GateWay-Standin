package br.com.coregate.infrastructure.mode;

import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Exposição de métricas numéricas Prometheus para monitoramento do modo operacional.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class OperationalModeMetrics {

    private final MeterRegistry meterRegistry;
    private final OperationalModeManager modeManager;

    @PostConstruct
    public void registerMetrics() {
        // Métrica 1: Estado operacional
        Gauge.builder("coregate_operational_mode_state", this, instance -> mapModeToNumeric())
                .description("Estado atual do modo operacional: 0=GATEWAY, 1=STANDIN_AUTOMATIC, 2=STANDIN_REQUESTED")
                .tag("service", "coregate")
                .register(meterRegistry);

        // Métrica 2: Estado do cache (Redis vs Volátil)
        Gauge.builder("coregate_operational_cache_mode", this, instance -> mapCacheToNumeric())
                .description("Modo de cache: 0=PERSISTENT (Redis), 1=VOLATILE (in-memory fallback)")
                .tag("service", "coregate")
                .register(meterRegistry);

        log.info("📊 CoreGate Operational Mode metrics registered (Prometheus)");
    }

    private double mapModeToNumeric() {
        return switch (modeManager.getMode()) {
            case GATEWAY -> 0;
            case STANDIN_AUTOMATIC -> 1;
            case STANDIN_REQUESTED -> 2;
        };
    }

    private double mapCacheToNumeric() {
        return modeManager.isVolatileCacheActive() ? 1 : 0;
    }
}
