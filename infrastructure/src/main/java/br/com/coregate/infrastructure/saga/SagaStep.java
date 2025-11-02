package br.com.coregate.infrastructure.saga;

import br.com.coregate.domain.model.Transaction;
import br.com.coregate.infrastructure.metrics.CoreGatePerformanceMetrics;
import br.com.coregate.infrastructure.mode.OperationalModeManager;

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
    private static OperationalModeManager modeManager;

    public static void setModeManager(OperationalModeManager manager) {
        modeManager = manager;
    }

    private SagaStep(String name, Function<T, T> execute, Function<T, T> rollback) {
        this.name = name;
        this.execute = execute;
        this.rollback = rollback;
    }

    public static <T> SagaStep<T> of(String name, Function<T, T> execute) {
        return new SagaStep<>(name, execute, Function.identity());
    }

    public static <T> SagaStep<T> of(String name, Function<T, T> execute, Function<T, T> rollback) {
        return new SagaStep<>(name, execute, rollback);
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
                CoreGatePerformanceMetrics.recordStepStatic(tenant, mode, name, elapsed, true);
            }
            return result;

        } catch (Exception e) {
            long elapsed = System.currentTimeMillis() - start;

            if (t instanceof Transaction tx) {
                String tenant = tx.getTenantId() != null ? tx.getTenantId() : "unknown";
                String mode = modeManager != null ? modeManager.getMode().name() : "UNKNOWN";
                CoreGatePerformanceMetrics.recordStepStatic(tenant, mode, name, elapsed, false);
            }

            throw e;
        }
    }

    public T revert(T t) {
        return rollback.apply(t);
    }
}
