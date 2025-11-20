package br.com.coregate.mode;

import br.com.coregate.domain.enums.OperationalMode;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 📊 Exposição de métricas Prometheus para monitoramento do modo operacional e cache.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class OperationalModeMetrics {

    private final MeterRegistry meterRegistry;
    private final OperationalModeManager modeManager;

    @PostConstruct
    public void registerMetrics() {
        // Métrica 1: Estado operacional (Gateway / StandIn)
        Gauge.builder("coregate_operational_mode_state", this, instance -> mapModeToNumeric())
                .description("Estado atual do modo operacional: 0=GATEWAY, 1=STANDIN_AUTOMATIC, 2=STANDIN_REQUESTED")
                .tag("service", "coregate")
                .register(meterRegistry);

        // Métrica 2: Estado do cache (Redis vs In-memory)
        Gauge.builder("coregate_operational_cache_mode", this, instance -> mapCacheToNumeric())
                .description("Modo de cache: 0=PERSISTENT (Redis), 1=VOLATILE (in-memory fallback)")
                .tag("service", "coregate")
                .register(meterRegistry);

        log.info("📊 CoreGate Operational Mode metrics registered (Prometheus)");
    }

    private double mapModeToNumeric() {
        return switch (modeManager.getMode()) {
            case OperationalMode.GATEWAY -> 0;
            case OperationalMode.STANDIN -> 1;
            case OperationalMode.STANDIN_REQUESTED -> 2;
            case OperationalMode.GATEWAY_REQUESTED -> 3;
            default -> -1; // segurança: caso novo enum apareça
        };
    }

    private double mapCacheToNumeric() {
        return modeManager.isVolatileCacheActive() ? 1 : 0;
    }
}
