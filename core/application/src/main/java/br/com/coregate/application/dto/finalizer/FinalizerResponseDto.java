package br.com.coregate.application.dto.finalizer;

import br.com.coregate.application.dto.transaction.AuthorizationResult;
import lombok.Builder;

@Builder
public record FinalizerResponseDto(
        AuthorizationResult authorizationResult
) {}


