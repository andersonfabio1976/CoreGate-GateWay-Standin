package br.com.coregate.application.ports.in;


import br.com.coregate.core.contracts.dto.transaction.AuthorizationResult;
import br.com.coregate.core.contracts.dto.transaction.TransactionCommand;

public interface AuthorizeTransactionUseCase {
    AuthorizationResult authorize(TransactionCommand command);
}
