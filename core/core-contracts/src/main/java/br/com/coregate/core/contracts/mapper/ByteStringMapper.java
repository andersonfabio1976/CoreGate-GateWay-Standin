package br.com.coregate.core.contracts.mapper;

import com.google.protobuf.ByteString;
import org.springframework.stereotype.Component;

@Component
public class ByteStringMapper {

    public byte[] toBytes(ByteString value) {
        return value != null ? value.toByteArray() : null;
    }

    public ByteString toByteString(byte[] value) {
        return value != null ? ByteString.copyFrom(value) : ByteString.EMPTY;
    }
}
