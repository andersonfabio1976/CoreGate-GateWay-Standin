package br.com.coregate.core.contracts.mapper;

import br.com.coregate.core.contracts.dto.context.ContextRequestDto;
import br.com.coregate.core.contracts.dto.context.ContextResponseDto;
import br.com.coregate.proto.ingress.ContextRequestProto;
import br.com.coregate.proto.ingress.ContextResponseProto;
import com.google.protobuf.Descriptors;
import java.util.Map;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-11-19T21:41:46-0300",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.8 (Oracle Corporation)"
)
@Component
public class ContextMapperImpl implements ContextMapper {

    @Autowired
    private ProtoMapperUtils protoMapperUtils;

    @Override
    public ContextRequestProto toProto(ContextRequestDto dto) {
        if ( dto == null ) {
            return null;
        }

        ContextRequestProto.Builder contextRequestProto = ContextRequestProto.newBuilder();

        contextRequestProto.setRawBytes( protoMapperUtils.toByteString( dto.getRawBytes() ) );
        contextRequestProto.setHexString( dto.getHexString() );
        contextRequestProto.setReceivedAtEpochMs( dto.getReceivedAtEpochMs() );

        return contextRequestProto.build();
    }

    @Override
    public ContextResponseProto toProto(ContextResponseDto dto) {
        if ( dto == null ) {
            return null;
        }

        ContextResponseProto.Builder contextResponseProto = ContextResponseProto.newBuilder();

        contextResponseProto.setRawBytes( protoMapperUtils.toByteString( dto.getRawBytes() ) );
        contextResponseProto.setHexString( dto.getHexString() );
        contextResponseProto.setReceivedAtEpochMs( dto.getReceivedAtEpochMs() );

        return contextResponseProto.build();
    }

    @Override
    public ContextRequestDto toDto(ContextRequestProto proto) {
        if ( proto == null ) {
            return null;
        }

        ContextRequestDto.ContextRequestDtoBuilder contextRequestDto = ContextRequestDto.builder();

        contextRequestDto.rawBytes( protoMapperUtils.fromByteString( proto.getRawBytes() ) );
        contextRequestDto.hexString( proto.getHexString() );
        contextRequestDto.receivedAtEpochMs( proto.getReceivedAtEpochMs() );

        return contextRequestDto.build();
    }

    @Override
    public ContextResponseProto toDto(ContextResponseProto proto) {
        if ( proto == null ) {
            return null;
        }

        ContextResponseProto.Builder contextResponseProto = ContextResponseProto.newBuilder();

        if ( proto.hasCoreGateContext() ) {
            contextResponseProto.setCoreGateContext( proto.getCoreGateContext() );
        }
        contextResponseProto.setRawBytes( proto.getRawBytes() );
        contextResponseProto.setHexString( proto.getHexString() );
        contextResponseProto.setHexStringBytes( proto.getHexStringBytes() );
        contextResponseProto.setReceivedAtEpochMs( proto.getReceivedAtEpochMs() );
        contextResponseProto.setUnknownFields( proto.getUnknownFields() );
        if ( contextResponseProto.getAllFields() != null ) {
            Map<Descriptors.FieldDescriptor, Object> map = proto.getAllFields();
            if ( map != null ) {
                contextResponseProto.getAllFields().putAll( map );
            }
        }

        return contextResponseProto.build();
    }
}
