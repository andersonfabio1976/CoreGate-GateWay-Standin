package br.com.coregate.infrastructure.saga;

import lombok.extern.slf4j.Slf4j;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;

@Slf4j
public class FunctionalSaga<T> {

    private final List<SagaStep<T>> steps = new ArrayList<>();
    private Consumer<Exception> errorHandler = e -> log.error("❌ Saga failed: {}", e.getMessage());
    private Consumer<T> endHandler = tx -> log.info("✅ Saga completed successfully.");
    private boolean silentRollback = false;

    // -------------------------
    // Builder / Fluent Interface
    // -------------------------

    public static <T> FunctionalSaga<T> start(T context) {
        return new FunctionalSaga<>();
    }

    public FunctionalSaga<T> then(String name, Function<T, T> action) {
        steps.add(new SagaStep<>(name, action, null));
        return this;
    }

    public FunctionalSaga<T> then(String name, Function<T, T> action, Function<T, T> rollback) {
        steps.add(new SagaStep<>(name, action, rollback));
        return this;
    }

    public FunctionalSaga<T> onError(Consumer<Exception> handler) {
        this.errorHandler = handler;
        return this;
    }

    public FunctionalSaga<T> onEnd(Consumer<T> handler) {
        this.endHandler = handler;
        return this;
    }

    public FunctionalSaga<T> enableSilentRollback(boolean enabled) {
        this.silentRollback = enabled;
        return this;
    }

    // -------------------------
    // Execution Flow
    // -------------------------

    public void end(T context) {
        Deque<SagaStep<T>> executed = new ArrayDeque<>();
        Instant startTime = Instant.now();

        try {
            for (SagaStep<T> step : steps) {
                Instant stepStart = Instant.now();
                log.info("➡️ Executing step: {}", step.getName());
                context = step.execute(context);
                executed.push(step);
                log.debug("⏱️ Step '{}' completed in {} ms", step.getName(),
                        Duration.between(stepStart, Instant.now()).toMillis());
            }

            log.info("✅ Saga completed successfully in {} ms",
                    Duration.between(startTime, Instant.now()).toMillis());

            endHandler.accept(context);

        } catch (Exception e) {
            log.warn("❌ Saga failed: {}", e.getMessage());
            rollback(executed, context, e);
        }
    }

    private void rollback(Deque<SagaStep<T>> executed, T context, Exception cause) {
        log.warn("↩️ Starting rollback chain...");

        while (!executed.isEmpty()) {
            SagaStep<T> step = executed.pop();
            try {
                log.warn("↩️ Rolling back: {}", step.getName());
                step.rollback(context);
            } catch (Exception ex) {
                log.error("⚠️ Rollback failed for {}: {}", step.getName(), ex.getMessage());
            }
        }

        log.info("🧹 Rollback complete.");

        // silent mode: no stacktrace, just logs
        if (!silentRollback) {
            errorHandler.accept(cause);
        } else {
            log.warn("⚠️ Saga completed with rollback (silent mode). Cause: {}", cause.getMessage());
        }
    }
}
