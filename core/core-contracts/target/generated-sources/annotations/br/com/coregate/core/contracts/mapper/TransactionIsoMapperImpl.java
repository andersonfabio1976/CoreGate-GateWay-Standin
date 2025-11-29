package br.com.coregate.core.contracts.mapper;

import br.com.coregate.core.contracts.RequestTransactionIsoProto;
import br.com.coregate.core.contracts.ResponseTransactionIsoProto;
import br.com.coregate.core.contracts.dto.transaction.RequestTransactionIso;
import br.com.coregate.core.contracts.dto.transaction.ResponseTransactionIso;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-11-30T11:16:50-0300",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.8 (Oracle Corporation)"
)
@Component
public class TransactionIsoMapperImpl implements TransactionIsoMapper {

    @Autowired
    private ByteStringMapper byteStringMapper;

    @Override
    public RequestTransactionIso toDto(RequestTransactionIsoProto proto) {
        if ( proto == null ) {
            return null;
        }

        RequestTransactionIso.RequestTransactionIsoBuilder requestTransactionIso = RequestTransactionIso.builder();

        requestTransactionIso.transactionId( proto.getTransactionId() );
        requestTransactionIso.rawBytes( byteStringMapper.toBytes( proto.getRawBytes() ) );
        requestTransactionIso.hexString( proto.getHexString() );

        return requestTransactionIso.build();
    }

    @Override
    public RequestTransactionIsoProto toProto(RequestTransactionIso dto) {
        if ( dto == null ) {
            return null;
        }

        RequestTransactionIsoProto.Builder requestTransactionIsoProto = RequestTransactionIsoProto.newBuilder();

        requestTransactionIsoProto.setTransactionId( dto.getTransactionId() );
        requestTransactionIsoProto.setRawBytes( byteStringMapper.toByteString( dto.getRawBytes() ) );
        requestTransactionIsoProto.setHexString( dto.getHexString() );

        return requestTransactionIsoProto.build();
    }

    @Override
    public ResponseTransactionIso toDto(ResponseTransactionIsoProto proto) {
        if ( proto == null ) {
            return null;
        }

        ResponseTransactionIso.ResponseTransactionIsoBuilder responseTransactionIso = ResponseTransactionIso.builder();

        responseTransactionIso.transactionId( proto.getTransactionId() );
        responseTransactionIso.rawBytes( byteStringMapper.toBytes( proto.getRawBytes() ) );
        responseTransactionIso.hexString( proto.getHexString() );

        return responseTransactionIso.build();
    }

    @Override
    public ResponseTransactionIsoProto toProto(ResponseTransactionIso dto) {
        if ( dto == null ) {
            return null;
        }

        ResponseTransactionIsoProto.Builder responseTransactionIsoProto = ResponseTransactionIsoProto.newBuilder();

        responseTransactionIsoProto.setTransactionId( dto.getTransactionId() );
        responseTransactionIsoProto.setRawBytes( byteStringMapper.toByteString( dto.getRawBytes() ) );
        responseTransactionIsoProto.setHexString( dto.getHexString() );

        return responseTransactionIsoProto.build();
    }
}
