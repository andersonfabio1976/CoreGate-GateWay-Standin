package br.com.coregate.core.contracts.mapper;

import com.google.protobuf.ByteString;
import org.mapstruct.Mapper;

/**
 * 🧩 ProtoMapperUtils — utilitário central para conversões entre ByteString e byte[].
 * Registrado como mapper Spring para uso em outros mappers MapStruct.
 */
@Mapper(componentModel = "spring")
public interface ProtoMapperUtils {

    default byte[] fromByteString(ByteString value) {
        return value == null ? null : value.toByteArray();
    }

    default ByteString toByteString(byte[] value) {
        return value == null ? ByteString.EMPTY : ByteString.copyFrom(value);
    }
}
