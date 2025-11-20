package br.com.coregate.core.contracts.dto.rules;

import br.com.coregate.core.contracts.dto.context.CoreGateContextDto;
import lombok.Builder;

@Builder
public record RulesResponseDto (
        CoreGateContextDto coreGateContextDto,
        StandinDecision decision,
        Integer evaluatedAtEpochMs
) {}
