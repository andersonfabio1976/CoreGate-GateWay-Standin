package br.com.coregate.orchestrator.saga.component;

import java.util.function.Consumer;

/**
 * Representa uma ação de compensação reutilizável dentro do ciclo SAGA.
 * Pode ser referenciada diretamente como lambda nos pipelines.
 */
@FunctionalInterface
public interface SagaCompensation<T> extends Consumer<T> {

    static <T> SagaCompensation<T> of(Consumer<T> compensation) {
        return compensation::accept;
    }

    default SagaCompensation<T> andThen(SagaCompensation<T> next) {
        return t -> {
            this.accept(t);
            next.accept(t);
        };
    }
}

