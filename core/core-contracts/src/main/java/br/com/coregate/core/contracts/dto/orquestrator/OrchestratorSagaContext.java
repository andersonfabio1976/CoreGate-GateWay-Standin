package br.com.coregate.core.contracts.dto.orquestrator;

import br.com.coregate.core.contracts.dto.rules.TransactionFactDto;
import br.com.coregate.core.contracts.dto.transaction.AuthorizationResult;
import br.com.coregate.core.contracts.dto.transaction.TransactionCommand;
import br.com.coregate.domain.model.Transaction;
import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * Contexto interno da Saga Orquestrator.
 * É mutável e trafega entre todos os steps.
 */
@Data
@Builder
public class OrquestratorSagaContext {
    private TransactionCommand transactionCommand;
    private TransactionFactDto transactionFactDto;
    private AuthorizationResult authorizationResult;
    private LocalDateTime createdAt;        // quando o Orquestrator iniciou
    private boolean standInTriggered;
}
