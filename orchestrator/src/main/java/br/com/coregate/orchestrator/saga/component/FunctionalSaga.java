package br.com.coregate.orchestrator.saga.component;

import br.com.coregate.application.ports.out.PerformanceMetricsPort;
import br.com.coregate.domain.enums.SagaStatus;
import br.com.coregate.domain.model.Transaction;
import lombok.extern.slf4j.Slf4j;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * ⚙️ Saga funcional que executa steps sequenciais, com rollback reverso e listener.
 * - Instância por transação (criada via FunctionalSagaFactory)
 * - Suporte a métricas e listeners (onStart, onComplete, onRollback)
 * - Suporte a onError customizado e rollback reverso
 * - onEnd (função final, opcional, executada ao término da saga)
 */
@Slf4j
public final class FunctionalSaga<T> {

    private final String sagaName;
    private final PerformanceMetricsPort performanceMetrics;

    private final List<SagaStep<T>> steps = new ArrayList<>();
    private SagaMetricsListener<T> listener = new SagaMetricsListener<>() {};
    private BiFunction<T, Exception, T> errorHandler;
    private Function<T, T> endHandler = Function.identity();
    private boolean silentRollback = false;

    /**
     * Construtor usado pela FunctionalSagaFactory.
     */
    public FunctionalSaga(String sagaName, PerformanceMetricsPort performanceMetrics) {
        this.sagaName = sagaName;
        this.performanceMetrics = performanceMetrics;
    }

    // -----------------------
    // Builder fluente da saga
    // -----------------------

    /**
     * Mantém a mesma semântica do modelo antigo:
     * start(context) apenas inicia o encadeamento fluente.
     */
    public FunctionalSaga<T> start(T context) {
        // Não alteramos o contexto aqui. O processamento acontece em end().
        return this;
    }

    public FunctionalSaga<T> then(String name, Function<T, T> execute) {
        steps.add(SagaStep.of(name, execute));
        return this;
    }

    public FunctionalSaga<T> then(String name, Function<T, T> execute, Function<T, T> rollback) {
        steps.add(SagaStep.of(name, execute, rollback));
        return this;
    }

    public FunctionalSaga<T> withListener(SagaMetricsListener<T> listener) {
        this.listener = listener != null ? listener : new SagaMetricsListener<>() {};
        return this;
    }

    public FunctionalSaga<T> onError(BiFunction<T, Exception, T> handler) {
        this.errorHandler = handler;
        return this;
    }

    /**
     * Função final executada ao término da saga,
     * seja sucesso ou falha, após rollback e métricas.
     */
    public FunctionalSaga<T> onEnd(Function<T, T> handler) {
        this.endHandler = handler != null ? handler : Function.identity();
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

    /**
     * Executa todos os steps registrados, faz rollback reverso em caso de erro
     * e aplica métricas.
     */
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

            // registra tempo da transação, se for Transaction
            if (context instanceof Transaction tx) {
                tx.setElapsedMs(System.currentTimeMillis() - start);
            }

            listener.onComplete(sagaName, current, SagaStatus.SUCCESS);

            // handler final (onEnd)
            current = endHandler.apply(current);

            return current;

        } catch (Exception cause) {
            log.error("❌ Saga '{}' failed: {}", sagaName, cause.getMessage(), cause);

            if (context instanceof Transaction tx) {
                tx.setElapsedMs(System.currentTimeMillis() - start);
            }

            T rolled = rollback(executed, context, cause);
            listener.onComplete(sagaName, rolled, SagaStatus.ERROR);

            if (errorHandler != null) {
                T handled = errorHandler.apply(rolled, cause);
                handled = endHandler.apply(handled);
                return handled;
            }

            if (!silentRollback) {
                throw new RuntimeException(cause);
            }

            rolled = endHandler.apply(rolled);
            return rolled;
        }
    }

    // -----------------------
    // Rollback reverso
    // -----------------------

    private T rollback(Deque<SagaStep<T>> executed, T state, Exception cause) {
        log.warn("↩️ Starting rollback chain...");
        long start = System.currentTimeMillis();
        int stepsCount = 0;
        T current = state;

        while (!executed.isEmpty()) {
            var step = executed.pop();
            try {
                current = step.revert(current);
                stepsCount++;
                listener.onRollback(sagaName, step.getName(), current);
            } catch (Exception ex) {
                log.error("⚠️ Rollback failed for {}: {}", step.getName(), ex.getMessage(), ex);
            }
        }

        long elapsed = System.currentTimeMillis() - start;
        log.info("🧹 Rollback complete in {}ms with {} steps", elapsed, stepsCount);

        // Métrica de rollback, se Transaction estiver no fluxo
        if (current instanceof Transaction tx) {
            String tenant = tx.getTenantId() != null ? tx.getTenantId() : "unknown";
            String type = tx.getType() != null ? tx.getType().name() : "UNKNOWN";
            String mode = "UNKNOWN"; // se quiser pode ajustar para mode real

            performanceMetrics.recordRollbackStatic(tenant, type, mode, stepsCount, elapsed);
        }

        return current;
    }

    // -----------------------
    // Step interno da saga
    // -----------------------

    private static final class SagaStep<T> {
        private final String name;
        private final Function<T, T> execute;
        private final Function<T, T> rollback;

        private SagaStep(String name, Function<T, T> execute, Function<T, T> rollback) {
            this.name = name;
            this.execute = execute;
            this.rollback = rollback != null ? rollback : Function.identity();
        }

        static <T> SagaStep<T> of(String name, Function<T, T> execute) {
            return new SagaStep<>(name, execute, Function.identity());
        }

        static <T> SagaStep<T> of(String name, Function<T, T> execute, Function<T, T> rollback) {
            return new SagaStep<>(name, execute, rollback);
        }

        String getName() {
            return name;
        }

        T run(T state) {
            return execute.apply(state);
        }

        T revert(T state) {
            return rollback.apply(state);
        }
    }
}
