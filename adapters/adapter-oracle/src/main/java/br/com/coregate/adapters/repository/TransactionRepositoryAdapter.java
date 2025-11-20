package br.com.coregate.adapters.repository;

import br.com.coregate.application.ports.out.TransactionRepositoryPort;
import br.com.coregate.domain.model.Transaction;
import br.com.coregate.domain.vo.TransactionId;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public class TransactionRepositoryAdapter implements TransactionRepositoryPort {

    @Override
    public Transaction save(Transaction tx) {
        // implementação
        return tx;
    }

    @Override
    public Optional<Transaction> findById(TransactionId id) {
        return Optional.empty();
    }
}
