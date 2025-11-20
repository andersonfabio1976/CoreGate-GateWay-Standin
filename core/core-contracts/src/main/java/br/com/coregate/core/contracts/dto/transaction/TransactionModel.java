package br.com.coregate.core.contracts.dto.transaction;

import br.com.coregate.domain.model.Transaction;
import lombok.Builder;

@Builder
public record TransactionModel(
    Transaction transaction
){}
