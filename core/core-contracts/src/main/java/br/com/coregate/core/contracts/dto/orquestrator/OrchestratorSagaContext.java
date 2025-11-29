package br.com.coregate.core.contracts.dto.orquestrator;

import br.com.coregate.core.contracts.dto.rules.TransactionFactDto;
import br.com.coregate.core.contracts.dto.transaction.AuthorizationResult;
import br.com.coregate.core.contracts.dto.transaction.TransactionCommand;
import lombok.Builder;
import lombok.Data;

/**
 * Contexto interno da Saga Orquestrator.
 * É mutável e trafega entre todos os steps.
 */
@Data
@Builder
public class OrchestratorSagaContext {
    private TransactionCommand transactionCommand;
    private TransactionFactDto transactionFactDto;
    private AuthorizationResult authorizationResult;
}
