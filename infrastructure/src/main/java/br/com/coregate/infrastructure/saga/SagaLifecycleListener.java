package br.com.coregate.infrastructure.saga;

import br.com.coregate.infrastructure.enums.SagaStatus;

/**
 * Permite observar o ciclo de vida da SAGA.
 * Todos os métodos são default (não obrigatórios de implementar).
 */
public interface SagaLifecycleListener<T> {

    default void onStart(String sagaName, T context) {}
    default void onStepStart(String sagaName, String stepName, T context) {}
    default void onStepSuccess(String sagaName, String stepName, T context) {}
    default void onStepFailure(String sagaName, String stepName, T context, Exception e) {}
    default void onRollback(String sagaName, String stepName, T context) {}
    default void onComplete(String sagaName, T context, SagaStatus status) {}
}
