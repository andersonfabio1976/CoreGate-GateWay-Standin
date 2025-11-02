package br.com.coregate.application.dto.rules;

import br.com.coregate.application.dto.common.CoreGateContextDto;
import lombok.Builder;

@Builder
public record RulesResponseDto (
        CoreGateContextDto coreGateContextDto,
        StandinDecision decision,
        Integer evaluatedAtEpochMs
) {}
