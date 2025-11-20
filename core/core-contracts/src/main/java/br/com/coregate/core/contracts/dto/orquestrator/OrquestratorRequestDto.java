package br.com.coregate.core.contracts.dto.orquestrator;

import br.com.coregate.core.contracts.dto.transaction.TransactionCommand;
import lombok.Builder;

@Builder
public record OrquestratorRequestDto(
        TransactionCommand transactionCommand
) {}

