package br.com.coregate.application.dto.rules;

import br.com.coregate.application.dto.common.CoreGateContextDto;
import lombok.Builder;

@Builder
public record RulesRequestDto (
    CoreGateContextDto coreGateContextDto,
    TransactionFactDto transactionFactDto,
    Integer sendAtEpochMs
) {}
