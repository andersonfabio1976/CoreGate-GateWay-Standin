package br.com.coregate.application.dto.orquestrator;

import br.com.coregate.application.dto.transaction.TransactionCommand;
import lombok.Builder;

@Builder
public record OrquestratorRequestDto(
        TransactionCommand transactionCommand
) {}

