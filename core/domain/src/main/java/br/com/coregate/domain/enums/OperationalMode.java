package br.com.coregate.domain.enums;

/**
 * Define o modo operacional do sistema.
 *
 * - GATEWAY → operação normal
 * - STANDIN_AUTOMATIC → ativado por falha detectada
 * - STANDIN_REQUESTED → solicitado manualmente (trava retorno automático)
 */
public enum OperationalMode {

    GATEWAY,
    GATEWAY_REQUESTED,
    STANDIN,
    STANDIN_REQUESTED;

    public boolean isStandIn() {
        return this == STANDIN || this == STANDIN_REQUESTED ;
    }

}
