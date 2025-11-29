package br.com.coregate.core.contracts.dto.rabbit;

/**
 * Enum com as filas declaradas no application.yml.
 * Nome do ENUM deve bater com as chaves dos blocos rabbit.produces e rabbit.consumes.
 */
public enum RabbitQueueType {
    NOTIFY,
    AUDIT,
    REGISTER,
    OPERATIONAL_MODE,
    TRANSACTION_RESULT,
    TRANSACTION;
}