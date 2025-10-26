package br.com.coregate.infrastructure.mapper;

import br.com.coregate.application.dto.TransactionCommand;
import br.com.coregate.application.dto.TransactionCommandRequest;
import br.com.coregate.application.dto.TransactionCommandResponse;
import br.com.coregate.domain.model.Transaction;
import br.com.coregate.infrastructure.grpc.*;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = CommonValueObjectMapper.class)
public interface TransactionMapper {

    TransactionCommandProto toProto(TransactionCommand dto);
    TransactionCommand fromProto(TransactionCommandProto proto);
    TransactionCommandProtoRequest toProtoRequest(TransactionCommandRequest dto);
    TransactionCommandProtoResponse toProtoResponse(TransactionCommandResponse dto);
    TransactionCommandRequest fromProtoRequest(TransactionCommandProtoRequest proto);
    TransactionCommandResponse fromProtoResponse(TransactionCommandProtoResponse proto);

    Transaction fromProtoToDomain(TransactionCommandProto proto);
    Transaction fromDtoToDomain(TransactionCommand dto);
    TransactionCommandProto toProtoFromDomain(Transaction domain);
    TransactionCommand toDtoFromDomain(Transaction domain);

}
