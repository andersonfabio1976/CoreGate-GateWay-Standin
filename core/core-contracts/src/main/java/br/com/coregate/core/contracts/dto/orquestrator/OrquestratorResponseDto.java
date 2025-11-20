package br.com.coregate.core.contracts.dto.orquestrator;

import br.com.coregate.core.contracts.dto.transaction.AuthorizationResult;
import lombok.Builder;

@Builder
public record OrquestratorResponseDto(
        AuthorizationResult authorizationResult
) {}


