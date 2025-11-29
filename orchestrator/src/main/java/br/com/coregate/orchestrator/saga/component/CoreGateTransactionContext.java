package br.com.coregate.orchestrator.saga.component;

import br.com.coregate.domain.enums.OperationalMode;
import br.com.coregate.domain.enums.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 🧩 Contexto de transação para o CoreGate Saga.
 * Contém metadados relevantes para métricas e auditoria.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CoreGateTransactionContext {
    private String tenant;
    private OperationalMode operationalMode;
    private TransactionType transactionType;
    private String sagaName;
}
