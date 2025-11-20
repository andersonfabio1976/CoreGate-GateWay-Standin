package br.com.coregate.domain.enums;

/**
 * Define o estado final de uma execução SAGA.
 * Pode ser usado para métricas, logs, notificações ou integração com mensageria.
 */
public enum SagaStatus {
    STARTED,
    SUCCESS,
    FAILED,
    ROLLEDBACK,
    ERROR
}
