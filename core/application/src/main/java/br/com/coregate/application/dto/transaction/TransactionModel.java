package br.com.coregate.application.dto.transaction;

import br.com.coregate.domain.model.Transaction;
import lombok.Builder;

@Builder
public record TransactionModel(
    Transaction transaction
){}
