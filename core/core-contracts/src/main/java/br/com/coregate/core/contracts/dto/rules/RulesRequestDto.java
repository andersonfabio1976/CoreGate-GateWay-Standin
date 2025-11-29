package br.com.coregate.core.contracts.dto.rules;

import lombok.Builder;

@Builder
public record RulesRequestDto (
    TransactionFactDto transactionFactDto,
    Integer sendAtEpochMs
) {}
