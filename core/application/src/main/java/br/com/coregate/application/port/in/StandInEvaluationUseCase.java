package br.com.coregate.application.port.in;

import br.com.coregate.application.dto.AuthorizationResult;
import br.com.coregate.application.dto.TransactionCommand;

public interface StandInEvaluationUseCase {
    AuthorizationResult evaluate(TransactionCommand command);
}
