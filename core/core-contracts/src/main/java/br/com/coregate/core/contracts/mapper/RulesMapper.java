package br.com.coregate.core.contracts.mapper;

import br.com.coregate.core.contracts.dto.rules.RulesRequestDto;
import br.com.coregate.core.contracts.dto.rules.RulesResponseDto;
import br.com.coregate.proto.rules.RulesRequestProto;
import br.com.coregate.proto.rules.RulesResponseProto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE // 👈 ignora enums extras como UNSPECIFIED/UNRECOGNIZED
)
public interface RulesMapper {

    RulesRequestProto toProto(RulesRequestDto dto);
    RulesRequestDto toDto(RulesRequestProto proto);

    RulesResponseProto toProto(RulesResponseDto dto);
    RulesResponseDto toDto(RulesResponseProto proto);

}