package br.com.coregate.application.service;

import br.com.coregate.application.port.in.StandInEvaluationUseCase;
import br.com.coregate.application.port.out.*;
import br.com.coregate.domain.model.*;
import br.com.coregate.domain.vo.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
public class TransactionService {

    private final TransactionRepositoryPort transactionRepository;

    public TransactionService(TransactionRepositoryPort transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public void register(Transaction tx) {

        try {
            transactionRepository.save(tx);
            log.info("✅ Transação autorizada {}", tx.getId());
        } catch (Exception ex) {
            log.warn("⚠️ Erro ao autorizar transação, avaliando StandIn...");
        }

    }

    private TransactionApproval transactionApproval(TransactionApproval transactionApproval) {
        return transactionApproval;
    }
}
