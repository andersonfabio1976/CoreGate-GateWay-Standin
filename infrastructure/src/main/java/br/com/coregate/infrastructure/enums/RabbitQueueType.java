package br.com.coregate.infrastructure.enums;

/**
 * Enum com as filas declaradas no application.yml.
 * Nome do ENUM deve bater com as chaves dos blocos rabbit.produces e rabbit.consumes.
 */
public enum RabbitQueueType {
    NOTIFY,
    AUDIT,
    REGISTER,
    REPORT,
    MODE,
    STANDIN_REQUESTED,
    STANDIN_AUTOMATIC,
    GATEWAY
}
