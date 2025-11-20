package br.com.coregate.core.contracts.dto.rules;


/**
 * 🎯 DecisionOutcome — resultado da decisão do motor de regras Stand-In.
 * Representa o desfecho lógico que orienta o fluxo de autorização.
 */
public enum DecisionOutcome {

    DECISION_OUTCOME_UNSPECIFIED("-1",""),
    APPROVED("00", "Transação aprovada pelo Stand-In"),
    DECLINED("01", "Transação recusada pelas regras"),
    REVIEW("02", "Transação marcada para revisão manual"),
    UNRECOGNIZED("99","");

    private final String code;
    private final String description;

    DecisionOutcome(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    /**
     * 🔍 Converte um código numérico em DecisionOutcome.
     * Retorna DECLINED como padrão caso o código seja inválido.
     */
    public static DecisionOutcome fromCode(String code) {
        for (DecisionOutcome outcome : values()) {
            if (outcome.code.equals(code)) {
                return outcome;
            }
        }
        return DECLINED; // fallback seguro
    }

    @Override
    public String toString() {
        return "%s(%s) - %s".formatted(name(), code, description);
    }
}
