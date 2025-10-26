package br.com.coregate.infrastructure.saga;

import lombok.Getter;

import java.util.function.Function;

public class SagaStep<T> {
    @Getter
    final String name;
    final Function<T, T> action;
    final Function<T, T> compensation;

    public SagaStep(String name, Function<T, T> action, Function<T, T> compensation) {
        this.name = name;
        this.action = action;
        this.compensation = compensation;
    }

    public T execute(T context) {
        return action != null ? action.apply(context) : context;
    }

    public T rollback(T context) {
        if (compensation != null) {
            return compensation.apply(context);
        }
        return context;
    }

}
