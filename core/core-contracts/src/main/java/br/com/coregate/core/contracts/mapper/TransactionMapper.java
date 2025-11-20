package br.com.coregate.core.contracts.mapper;

import br.com.coregate.core.contracts.dto.orquestrator.OrquestratorRequestDto;
import br.com.coregate.core.contracts.dto.transaction.TransactionCommand;
import br.com.coregate.core.contracts.dto.transaction.TransactionModel;
import br.com.coregate.domain.model.Transaction;
import br.com.coregate.proto.Orquestrator.OrquestratorRequestProto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TransactionMapper {

    TransactionModel toModel(OrquestratorRequestProto proto);
    OrquestratorRequestProto toProto(TransactionModel model);
    TransactionModel toModel(OrquestratorRequestDto dto);
    OrquestratorRequestDto toDto(TransactionModel model);
    TransactionCommand toDto(Transaction domain);
    Transaction toDoman(TransactionCommand dto);

}
