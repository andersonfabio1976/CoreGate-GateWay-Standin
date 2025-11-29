package br.com.coregate.core.contracts.mapper;

import br.com.coregate.core.contracts.RequestTransactionFlowProto;
import br.com.coregate.core.contracts.ResponseTransactionFlowProto;
import br.com.coregate.core.contracts.dto.transaction.RequestTransactionFlow;
import br.com.coregate.core.contracts.dto.transaction.ResponseTransactionFlow;
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
