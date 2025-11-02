package br.com.coregate.infrastructure.mapper;

import com.google.protobuf.ByteString;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ProtoMapperUtils {
    ProtoMapperUtils INSTANCE = Mappers.getMapper(ProtoMapperUtils.class);

    default byte[] fromByteString(ByteString value) {
        return value == null ? null : value.toByteArray();
    }

    default ByteString toByteString(byte[] value) {
        return value == null ? ByteString.EMPTY : ByteString.copyFrom(value);
    }
}
