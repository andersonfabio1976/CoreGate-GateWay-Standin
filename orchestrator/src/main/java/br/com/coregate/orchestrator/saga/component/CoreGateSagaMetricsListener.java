package br.com.coregate.orchestrator.saga.component;

import br.com.coregate.core.contracts.dto.orquestrator.OrchestratorSagaContext;
import br.com.coregate.domain.enums.SagaStatus;
import br.com.coregate.mode.OperationalModeManager;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 🎯 CoreGate Saga Metrics Listener
 * Registra métricas de execução da Saga (tenant + tipo + modo operacional)
 * Ideal para dashboards Grafana/Prometheus.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class CoreGateSagaMetricsListener implements SagaMetricsListener<OrchestratorSagaContext> {

    private final MeterRegistry meter;
    private final OperationalModeManager modeManager;

    @Override
    public void onComplete(String sagaName, OrchestratorSagaContext tx, SagaStatus status) {
        try {
            String tenant = tx.getTransactionCommand().tenantId() != null ? tx.getTransactionCommand().tenantId() : "unknown";
            String type = tx.getTransactionCommand().type() != null ? tx.getTransactionCommand().type().name() : "UNKNOWN";
            String mode = modeManager != null ? modeManager.getMode().name() : "UNKNOWN";

            meter.counter("coregate.saga.completed",
                    "saga", sagaName,
                    "tenant", tenant,
                    "mode", mode,
                    "type", type,
                    "status", status.name()
            ).increment();

            log.info("📊 Saga '{}' completed: tenant={}, type={}, mode={}, status={}",
                    sagaName, tenant, type, mode, status);

        } catch (Exception e) {
            log.warn("⚠️ Failed to record saga metrics for '{}': {}", sagaName, e.getMessage());
        }
    }
}
