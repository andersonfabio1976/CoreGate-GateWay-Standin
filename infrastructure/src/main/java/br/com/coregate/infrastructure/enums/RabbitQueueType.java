package br.com.coregate.infrastructure.enums;

/**
 * Enum com as filas declaradas no application.yml.
 * Nome do ENUM deve bater com as chaves dos blocos rabbit.produces e rabbit.consumes.
 */
public enum RabbitQueueType {
    NOTIFY(null),
    AUDIT(null),
    REGISTER(null),
    REPORT(null),
    MODE(null),
    STANDIN_REQUESTED(OperationalMode.STANDIN_REQUESTED),
    STANDIN_AUTOMATIC(OperationalMode.STANDIN_AUTOMATIC),
    GATEWAY(OperationalMode.GATEWAY);

    private final OperationalMode targetMode;


    public boolean isStandIn() {
        return this == STANDIN_AUTOMATIC || this == STANDIN_REQUESTED;
    }

    RabbitQueueType(OperationalMode targetMode) {
        this.targetMode = targetMode;
    }

    public OperationalMode getTargetMode() {
        return targetMode;
    }

    public static RabbitQueueType from(String value) {
        try {
            return RabbitQueueType.valueOf(value.toUpperCase());
        } catch (Exception e) {
            return null;
        }
    }
}