package br.com.coregate.core.contracts.dto.rules;

import lombok.Builder;

@Builder
public record RulesResponseDto (
        StandinDecision decision,
        Integer evaluatedAtEpochMs
) {}
