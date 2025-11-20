package br.com.coregate.core.contracts.mapper;

import br.com.coregate.core.contracts.dto.orquestrator.OrquestratorRequestDto;
import br.com.coregate.core.contracts.dto.orquestrator.OrquestratorResponseDto;
import br.com.coregate.proto.Orquestrator.OrquestratorRequestProto;
import br.com.coregate.proto.Orquestrator.OrquestratorResponseProto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrquestratorMapper {

    OrquestratorResponseProto toProto(OrquestratorResponseDto dto);
    OrquestratorRequestProto toProto(OrquestratorRequestDto dto);

    OrquestratorResponseDto toDto(OrquestratorResponseProto proto);
    OrquestratorRequestDto toDto(OrquestratorRequestProto proto);


}