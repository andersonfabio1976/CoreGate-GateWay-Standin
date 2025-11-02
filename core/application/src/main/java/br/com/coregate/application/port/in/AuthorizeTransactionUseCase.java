package br.com.coregate.application.port.in;

import br.com.coregate.application.dto.transaction.AuthorizationResult;
import br.com.coregate.application.dto.transaction.TransactionCommand;

public interface AuthorizeTransactionUseCase {
    AuthorizationResult authorize(TransactionCommand command);
}
