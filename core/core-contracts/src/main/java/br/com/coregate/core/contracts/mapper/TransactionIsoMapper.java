package br.com.coregate.core.contracts.mapper;

import br.com.coregate.core.contracts.RequestTransactionIsoProto;
import br.com.coregate.core.contracts.ResponseTransactionIsoProto;
import br.com.coregate.core.contracts.dto.transaction.RequestTransactionIso;
import br.com.coregate.core.contracts.dto.transaction.ResponseTransactionIso;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        uses = { ByteStringMapper.class },
        unmappedTargetPolicy = ReportingPolicy.IGNORE
        )
public interface TransactionIsoMapper {
    RequestTransactionIso toDto(RequestTransactionIsoProto proto);
    RequestTransactionIsoProto toProto(RequestTransactionIso dto);

    ResponseTransactionIso toDto(ResponseTransactionIsoProto proto);
    ResponseTransactionIsoProto toProto(ResponseTransactionIso dto);
}
