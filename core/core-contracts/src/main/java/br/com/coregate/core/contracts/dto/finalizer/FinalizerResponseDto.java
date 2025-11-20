package br.com.coregate.core.contracts.dto.finalizer;

import br.com.coregate.core.contracts.dto.transaction.AuthorizationResult;
import lombok.Builder;

@Builder
public record FinalizerResponseDto(
        AuthorizationResult authorizationResult
) {}


