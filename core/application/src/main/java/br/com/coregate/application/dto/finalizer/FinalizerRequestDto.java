package br.com.coregate.application.dto.finalizer;

import br.com.coregate.application.dto.transaction.TransactionCommand;
import lombok.Builder;

@Builder
public record FinalizerRequestDto(
        TransactionCommand transactionCommand
) {}

