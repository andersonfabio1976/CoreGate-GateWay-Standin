package br.com.coregate.orchestrator.saga.component;

import br.com.coregate.application.ports.out.PerformanceMetricsPort;
import br.com.coregate.domain.model.Transaction;
import br.com.coregate.mode.OperationalModeManager;

import java.util.function.Function;

/**
 * 🔹 Define um step da Saga funcional.
 * Mede tempo e registra métricas de execução/falha por step.
 */
public final class SagaStep<T> {

    private final String name;
    private final Function<T, T> execute;
    private final Function<T, T> rollback;

    // Singleton estático do gerenciador de modo operacional
    private final OperationalModeManager modeManager;
    private final PerformanceMetricsPort performanceMetrics;

    private SagaStep(String name, Function<T, T> execute, Function<T, T> rollback, OperationalModeManager modeManager, PerformanceMetricsPort performanceMetrics) {
        this.name = name;
        this.execute = execute;
        this.rollback = rollback;
        this.modeManager = modeManager;
        this.performanceMetrics = performanceMetrics;
    }

    public <T> SagaStep<T> of(String name, Function<T, T> execute) {
        return new SagaStep<>(name, execute, Function.identity(), modeManager, performanceMetrics);
    }

    public  <T> SagaStep<T> of(String name, Function<T, T> execute, Function<T, T> rollback) {
        return new SagaStep<>(name, execute, rollback, modeManager, performanceMetrics);
    }

    public String getName() {
        return name;
    }

    public T run(T t) {
        long start = System.currentTimeMillis();
        try {
            T result = execute.apply(t);
            long elapsed = System.currentTimeMillis() - start;

            if (t instanceof Transaction tx) {
                String tenant = tx.getTenantId() != null ? tx.getTenantId() : "unknown";
                String mode = modeManager != null ? modeManager.getMode().name() : "UNKNOWN";
                performanceMetrics.recordStepStatic(tenant, mode, name, elapsed, true);
            }
            return result;

        } catch (Exception e) {
            long elapsed = System.currentTimeMillis() - start;

            if (t instanceof Transaction tx) {
                String tenant = tx.getTenantId() != null ? tx.getTenantId() : "unknown";
                String mode = modeManager != null ? modeManager.getMode().name() : "UNKNOWN";
                performanceMetrics.recordStepStatic(tenant, mode, name, elapsed, false);
            }

            throw e;
        }
    }

    public T revert(T t) {
        return rollback.apply(t);
    }
}
