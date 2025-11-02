package br.com.coregate.infrastructure.saga;

import br.com.coregate.infrastructure.enums.SagaStatus;
import br.com.coregate.domain.model.Transaction;
import br.com.coregate.infrastructure.metrics.CoreGatePerformanceMetrics;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.function.*;

/**
 * ⚙️ Saga funcional que executa steps sequenciais, com rollback reverso e listener.
 * - Suporte a métricas e listeners (onStart, onComplete, onRollback)
 * - Suporte a onError customizado e rollback reverso
 * - onEnd (função final, opcional, executada ao término da saga)
 */
@Slf4j
public final class FunctionalSaga<T> {

    private final String sagaName;
    private final List<SagaStep<T>> steps = new ArrayList<>();
    private SagaMetricsListener<T> listener = new SagaMetricsListener<>() {};
    private BiFunction<T, Exception, T> errorHandler;
    private Function<T, T> endHandler = Function.identity(); // ✅ novo: função final
    private boolean silentRollback = false;

    private FunctionalSaga(String sagaName) {
        this.sagaName = sagaName;
    }

    public static <T> FunctionalSaga<T> start(T context) {
        return new FunctionalSaga<>("coregate-saga");
    }

    // -----------------------
    // Builder fluente da saga
    // -----------------------

    public FunctionalSaga<T> then(String name, Function<T, T> execute) {
        steps.add(SagaStep.of(name, execute));
        return this;
    }

    public FunctionalSaga<T> then(String name, Function<T, T> execute, Function<T, T> rollback) {
        steps.add(SagaStep.of(name, execute, rollback));
        return this;
    }

    public FunctionalSaga<T> withListener(SagaMetricsListener<T> listener) {
        this.listener = listener;
        return this;
    }

    public FunctionalSaga<T> onError(BiFunction<T, Exception, T> handler) {
        this.errorHandler = handler;
        return this;
    }

    /**
     * ✅ Novo: função final executada ao término da saga,
     * seja sucesso ou falha, após rollback e métricas.
     */
    public FunctionalSaga<T> onEnd(Function<T, T> handler) {
        this.endHandler = handler;
        return this;
    }

    public FunctionalSaga<T> withSilentMode(boolean silent) {
        this.silentRollback = silent;
        return this;
    }

    public FunctionalSaga<T> enableSilentRollback(boolean silent) {
        return withSilentMode(silent);
    }

    // -----------------------
    // Execução principal
    // -----------------------

    public T end(T context) {
        listener.onStart(sagaName, context);
        Deque<SagaStep<T>> executed = new ArrayDeque<>();

        long start = System.currentTimeMillis();
        try {
            T current = context;
            for (SagaStep<T> step : steps) {
                current = step.run(current);
                executed.push(step);
                listener.onStepSuccess(sagaName, step.getName(), current);
            }

            if (context instanceof Transaction tx)
                tx.setElapsedMs(System.currentTimeMillis() - start);

            listener.onComplete(sagaName, current, SagaStatus.SUCCESS);

            // ✅ executa handler final (onEnd)
            current = endHandler.apply(current);

            return current;

        } catch (Exception cause) {
            log.error("❌ Saga '{}' failed: {}", sagaName, cause.getMessage(), cause);
            if (context instanceof Transaction tx)
                tx.setElapsedMs(System.currentTimeMillis() - start);

            T rolled = rollback(executed, context, cause);
            listener.onComplete(sagaName, rolled, SagaStatus.ERROR);

            if (errorHandler != null) {
                T handled = errorHandler.apply(rolled, cause);
                handled = endHandler.apply(handled); // ✅ garante execução final
                return handled;
            }

            if (!silentRollback)
                throw new RuntimeException(cause);

            rolled = endHandler.apply(rolled); // ✅ mesmo em rollback silencioso
            return rolled;
        }
    }

    // -----------------------
    // Rollback reverso
    // -----------------------

    private T rollback(Deque<SagaStep<T>> executed, T state, Exception cause) {
        log.warn("↩️ Starting rollback chain...");
        long start = System.currentTimeMillis();
        int steps = 0;
        T current = state;

        while (!executed.isEmpty()) {
            var step = executed.pop();
            try {
                current = step.revert(current);
                steps++;
                listener.onRollback(sagaName, step.getName(), current);
            } catch (Exception ex) {
                log.error("⚠️ Rollback failed for {}: {}", step.getName(), ex.getMessage(), ex);
            }
        }

        long elapsed = System.currentTimeMillis() - start;
        log.info("🧹 Rollback complete in {}ms with {} steps", elapsed, steps);

        if (current instanceof Transaction tx) {
            String tenant = tx.getTenantId() != null ? tx.getTenantId() : "unknown";
            String type = tx.getType() != null ? tx.getType().name() : "UNKNOWN";
            String mode = "UNKNOWN";
            CoreGatePerformanceMetrics.recordRollbackStatic(tenant, type, mode, steps, elapsed);
        }

        return current;
    }
}
