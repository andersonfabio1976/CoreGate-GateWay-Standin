package br.com.coregate.core.contracts.dto.rules;

import br.com.coregate.core.contracts.dto.context.CoreGateContextDto;
import lombok.Builder;

@Builder
public record RulesRequestDto (
    CoreGateContextDto coreGateContextDto,
    TransactionFactDto transactionFactDto,
    Integer sendAtEpochMs
) {}
