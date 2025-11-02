package br.com.coregate.infrastructure.enums;

/**
 * Define o modo operacional do sistema.
 *
 * - GATEWAY → operação normal
 * - STANDIN_AUTOMATIC → ativado por falha detectada
 * - STANDIN_REQUESTED → solicitado manualmente (trava retorno automático)
 */
public enum OperationalMode {

    GATEWAY(false),
    STANDIN(false),
    STANDIN_AUTOMATIC(false),
    STANDIN_REQUESTED(true);

    private final boolean lockedByRequest;

    OperationalMode(boolean lockedByRequest) {
        this.lockedByRequest = lockedByRequest;
    }

    public boolean isLockedByRequest() {
        return lockedByRequest;
    }

    public boolean isStandIn() {
        return this == STANDIN || this == STANDIN_REQUESTED || this == STANDIN_AUTOMATIC;
    }

    public ProcessingMode toProcessingMode() {
        return isStandIn()
                ? br.com.coregate.infrastructure.enums.ProcessingMode.STANDIN
                : br.com.coregate.infrastructure.enums.ProcessingMode.GATEWAY;
    }

}
