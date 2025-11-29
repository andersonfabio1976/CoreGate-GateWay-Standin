package br.com.coregate.core.contracts.dto.transaction;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ResponseTransactionFlow {
    private TransactionCommand transactionCommand;
    private AuthorizationResult authorizationResult;
}
