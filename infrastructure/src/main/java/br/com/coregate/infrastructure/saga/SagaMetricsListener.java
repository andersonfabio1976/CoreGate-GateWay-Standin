package br.com.coregate.infrastructure.saga;

import br.com.coregate.infrastructure.enums.SagaStatus;

public interface SagaMetricsListener<T> {
    default void onStart(String sagaName, T state) {}
    default void onStepSuccess(String sagaName, String stepName, T state) {}
    default void onStepFailure(String sagaName, String stepName, Exception ex) {}
    default void onRollback(String sagaName, String stepName, T state) {}
    default void onComplete(String sagaName, T state, SagaStatus status) {}
}
