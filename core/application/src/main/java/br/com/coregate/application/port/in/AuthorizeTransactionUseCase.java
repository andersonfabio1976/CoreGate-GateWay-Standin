package br.com.coregate.application.port.in;

import br.com.coregate.application.dto.*;

public interface AuthorizeTransactionUseCase {
    AuthorizationResult authorize(TransactionCommand command);
}
