package br.com.coregate.orchestrator.saga.step;

import br.com.coregate.application.ports.out.PerformanceMetricsPort;
import br.com.coregate.core.contracts.dto.orquestrator.OrchestratorSagaContext;
import br.com.coregate.mode.OperationalModeManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;

@Slf4j
@Component
public class End {

    private PerformanceMetricsPort metrics;
    private OperationalModeManager manager;

    public OrchestratorSagaContext execute(OrchestratorSagaContext tx) {
        long elapsedMs = tx.getTransactionCommand().creaatedAt() != null ? Duration.between(tx.getTransactionCommand().creaatedAt(), Instant.now()).toMillis() : 0L;
        boolean success = "00".equalsIgnoreCase(tx.getAuthorizationResult().responseCode());
        String tenant = tx.getTransactionCommand().tenantId() != null ? tx.getTransactionCommand().tenantId() : "unknown";
        String type = tx.getTransactionCommand().type() != null ? tx.getTransactionCommand().type().name() : "UNKNOWN";
        String mode = manager != null ? manager.getMode().name() : "UNKNOWN";

        log.info("🏁 Saga ended successfully for {} in {}ms (tenant={}, mode={}, type={}, success={})",
                tx.getTransactionCommand(), elapsedMs, tenant, mode, type, success);

        try {
            metrics.recordTransaction(tenant, type, elapsedMs, success);
        } catch (Exception e) {
            log.warn("⚠️ Failed to record metrics: {}", e.getMessage());
        }
        return tx;
    }
}
