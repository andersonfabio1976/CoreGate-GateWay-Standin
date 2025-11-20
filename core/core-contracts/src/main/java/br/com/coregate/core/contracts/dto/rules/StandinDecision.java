package br.com.coregate.core.contracts.dto.rules;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 📦 Representa a decisão final emitida pelo motor Stand-in.
 * É o resultado do processamento das regras Evrete.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StandinDecision {

    /**
     * Resultado principal da decisão:
     * - APPROVED
     * - DECLINED
     * - REVIEW
     */
    private DecisionOutcome outcome;

    /**
     * Motivo da decisão (ex: MCC_BLACKLIST, LOW_AMOUNT_LOW_RISK, etc.)
     */
    private String reason;

    /**
     * Código de autorização quando aprovado.
     * Pode ser nulo para REVIEW ou DECLINED.
     */
    private String authCode;

    /**
     * Identificador único da requisição original.
     */
    private String requestId;
}
