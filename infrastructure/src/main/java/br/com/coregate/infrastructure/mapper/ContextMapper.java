package br.com.coregate.infrastructure.mapper;

import br.com.coregate.application.dto.context.ContextRequestDto;
import br.com.coregate.application.dto.context.ContextResponseDto;
import br.com.coregate.proto.ingress.ContextRequestProto;
import br.com.coregate.proto.ingress.ContextResponseProto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = ProtoMapperUtils.class)
public interface ContextMapper {

    ContextRequestProto toProto(ContextRequestDto dto);
    ContextResponseProto toProto(ContextResponseDto dto);
    ContextRequestDto toDto(ContextRequestProto proto);
    ContextResponseProto toDto(ContextResponseProto proto);

}