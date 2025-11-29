package br.com.coregate.core.contracts.dto.transaction;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class RequestTransactionFlow {
    private TransactionCommand transactionCommand;
}
