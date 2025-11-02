package br.com.coregate.orchestrator.saga.step;

import br.com.coregate.application.dto.orquestrator.OrquestratorSagaContext;
import br.com.coregate.domain.model.Transaction;
import br.com.coregate.infrastructure.metrics.CoreGatePerformanceMetrics;
import br.com.coregate.infrastructure.mode.OperationalModeManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class End {

    private static CoreGatePerformanceMetrics metrics;
    private static OperationalModeManager manager;

    public static OrquestratorSagaContext execute(OrquestratorSagaContext tx) {
        long elapsedMs = tx.getTransaction().getElapsedMs() != null ? tx.getTransaction().getElapsedMs() : 0L;
        boolean success = "00".equalsIgnoreCase(tx.getAuthorizationResult().responseCode());
        String tenant = tx.getTransaction().getTenantId() != null ? tx.getTransaction().getTenantId() : "unknown";
        String type = tx.getTransaction().getType() != null ? tx.getTransaction().getType().name() : "UNKNOWN";
        String mode = manager != null ? manager.getProcessingMode().name() : "UNKNOWN";

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
