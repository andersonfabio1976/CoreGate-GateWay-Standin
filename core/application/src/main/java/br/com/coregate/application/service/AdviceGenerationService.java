package br.com.coregate.application.service;

import br.com.coregate.application.port.in.AdviceGenerationUseCase;
import br.com.coregate.application.port.out.*;
import br.com.coregate.domain.enums.AdviceType;
import br.com.coregate.domain.model.*;
import br.com.coregate.domain.vo.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdviceGenerationService implements AdviceGenerationUseCase {

    private final TransactionRepositoryPort transactionRepository;
    private final AdvicePublisherPort advicePublisherPort;

    @Override
    public void generateAdvice(String tenantId, String transactionId) {
        Transaction txn = transactionRepository.findById(TransactionId.of(transactionId))
                .orElseThrow(() -> new IllegalArgumentException("Transação não encontrada"));

        Advice advice = Advice.builder()
                .tenantId(tenantId)
                .transaction(txn)
                .type(AdviceType.CLEARING)
                .createdAt(LocalDateTime.now())
                //.payload("ISO8583 Mock Payload")
                .build();

        advicePublisherPort.publishAdvice(advice);
        log.info("📨 Advice gerado para transação {}", transactionId);
    }
}
