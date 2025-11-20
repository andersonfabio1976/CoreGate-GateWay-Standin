package br.com.coregate.orchestrator.saga.step;

import br.com.coregate.application.ports.out.PerformanceMetricsPort;
import br.com.coregate.core.contracts.dto.orquestrator.OrquestratorSagaContext;
import br.com.coregate.mode.OperationalModeManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class End {

    private PerformanceMetricsPort metrics;
    private OperationalModeManager manager;

    public OrquestratorSagaContext execute(OrquestratorSagaContext tx) {
        long elapsedMs = tx.getTransaction().getElapsedMs() != null ? tx.getTransaction().getElapsedMs() : 0L;
        boolean success = "00".equalsIgnoreCase(tx.getAuthorizationResult().responseCode());
        String tenant = tx.getTransaction().getTenantId() != null ? tx.getTransaction().getTenantId() : "unknown";
        String type = tx.getTransaction().getType() != null ? tx.getTransaction().getType().name() : "UNKNOWN";
        String mode = manager != null ? manager.getMode().name() : "UNKNOWN";

        log.info("🏁 Saga ended successfully for {} in {}ms (tenant={}, mode={}, type={}, success={})",
                tx.getTransaction().getId(), elapsedMs, tenant, mode, type, success);

        try {
            metrics.recordTransaction(tenant, type, elapsedMs, success);
        } catch (Exception e) {
            log.warn("⚠️ Failed to record metrics: {}", e.getMessage());
        }
        return tx;
    }
}
