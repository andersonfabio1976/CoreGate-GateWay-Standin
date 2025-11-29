package br.com.coregate.core.contracts.mapper;

import br.com.coregate.core.contracts.dto.transaction.TransactionCommand;
import br.com.coregate.domain.model.Transaction;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TransactionMapper {
    TransactionCommand toDto(Transaction domain);
    Transaction toDomain(TransactionCommand dto);
}
