package br.com.coregate.core.contracts.mapper;

import br.com.coregate.core.contracts.dto.transaction.RequestTransactionFlow;
import br.com.coregate.core.contracts.dto.transaction.ResponseTransactionFlow;
import br.com.coregate.proto.transaction.flow.RequestTransactionFlowProto;
import br.com.coregate.proto.transaction.flow.ResponseTransactionFlowProto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring",
        uses = { DateTimeMapper.class }
        )
public interface TransactionFlowMapper {
    RequestTransactionFlow toDto(RequestTransactionFlowProto proto);
    RequestTransactionFlowProto toProto(RequestTransactionFlow dto);

    ResponseTransactionFlow toDto(ResponseTransactionFlowProto proto);
    ResponseTransactionFlowProto toProto(ResponseTransactionFlow dto);
}
