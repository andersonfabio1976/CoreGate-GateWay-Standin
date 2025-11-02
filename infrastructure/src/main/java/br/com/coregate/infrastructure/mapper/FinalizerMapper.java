package br.com.coregate.infrastructure.mapper;

import br.com.coregate.application.dto.finalizer.FinalizerRequestDto;
import br.com.coregate.application.dto.finalizer.FinalizerResponseDto;
import br.com.coregate.proto.finalizer.FinalizerRequestProto;
import br.com.coregate.proto.finalizer.FinalizerResponseProto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface FinalizerMapper {

    FinalizerResponseProto toProto(FinalizerResponseDto dto);
    FinalizerRequestProto toProto(FinalizerRequestDto dto);

    FinalizerResponseDto toDto(FinalizerResponseProto proto);
    FinalizerRequestDto toDto(FinalizerRequestProto proto);

}