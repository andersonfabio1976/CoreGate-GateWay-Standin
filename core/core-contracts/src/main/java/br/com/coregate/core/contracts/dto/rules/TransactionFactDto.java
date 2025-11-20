package br.com.coregate.core.contracts.dto.rules;

import br.com.coregate.domain.vo.TenantId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 🧾 Representa o fato de transação avaliado pelo StandinRulesEngine.
 *
 * Este objeto é o insumo (Fact) processado pelas regras Evrete.
 * Todas as propriedades "isXxx" são interpretadas diretamente como
 * condições lógicas dentro das regras declaradas.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionFactDto {

    // 🔑 Identificadores
    private String requestId;
    private String pan;
    private String merchantId;
    private String mcc;
    private String country;

    // 💰 Dados transacionais
    private boolean online;
    private long amountCents;
    private double riskScore;

    // ⚙️ Flags derivadas do contexto e regras externas
    private boolean mccBlacklisted;
    private boolean mccWhitelisted;
    private boolean mccGambling;
    private boolean gamblingAllowed;
    private boolean amountAutoApproves;
    private boolean riskOk;
    private boolean panExceedsVelocity;
    private boolean panExceedsDailyLimit;
    private StandinDecision standinDecision;
    private String tenantId;
    private DecisionOutcome decision;

    // 🧠 Decisão gerada após avaliação
    //private StandinDecision decision;

    // ----------------------------
    // 🧩 Métodos utilitários
    // ----------------------------

    public boolean isMccBlacklisted() {
        return mccBlacklisted;
    }

    public boolean isMccWhitelisted() {
        return mccWhitelisted;
    }

    public boolean isMccGambling() {
        return mccGambling;
    }

    public boolean isGamblingAllowed() {
        return gamblingAllowed;
    }

    public boolean isAmountAutoApproves() {
        return amountAutoApproves;
    }

    public boolean isRiskOk() {
        return riskOk;
    }

    public boolean isPanExceedsVelocity() {
        return panExceedsVelocity;
    }

    public boolean isPanExceedsDailyLimit() {
        return panExceedsDailyLimit;
    }
}
