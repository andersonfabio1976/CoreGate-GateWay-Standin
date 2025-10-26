package br.com.coregate.application.port.out;

import br.com.coregate.domain.model.Transaction;
import br.com.coregate.domain.vo.TransactionId;
import java.util.Optional;

public interface TransactionRepositoryPort {
    Transaction save(Transaction transaction);
    Optional<Transaction> findById(TransactionId id);
}