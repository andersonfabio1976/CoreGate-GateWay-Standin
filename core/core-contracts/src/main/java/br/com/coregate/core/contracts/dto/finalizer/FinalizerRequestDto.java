package br.com.coregate.core.contracts.dto.finalizer;

import br.com.coregate.core.contracts.dto.transaction.TransactionCommand;
import lombok.Builder;

@Builder
public record FinalizerRequestDto(
        TransactionCommand transactionCommand
) {}

