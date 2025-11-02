package br.com.coregate.application.dto.orquestrator;

import br.com.coregate.application.dto.transaction.AuthorizationResult;
import lombok.Builder;

@Builder
public record OrquestratorResponseDto(
        AuthorizationResult authorizationResult
) {}


