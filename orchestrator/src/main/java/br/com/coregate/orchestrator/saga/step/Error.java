package br.com.coregate.orchestrator.saga.step;

import br.com.coregate.core.contracts.dto.orquestrator.OrchestratorSagaContext;
import br.com.coregate.core.contracts.dto.rabbit.RabbitQueueType;
import br.com.coregate.core.contracts.dto.transaction.AuthorizationResult;
import br.com.coregate.domain.enums.TransactionStatus;
import br.com.coregate.rabbitmq.factory.RabbitFactory;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@AllArgsConstructor
public class Error {

    private final RabbitFactory rabbitFactory;

    public OrchestratorSagaContext execute(OrchestratorSagaContext tx, Exception e) {
        log.error("❌ Saga failed for {}: {}", tx != null ? tx.getTransactionCommand() : "unknown", e.getMessage(), e);
        if (tx != null) {
            var responseCode = AuthorizationResult.builder()
                    .responseCode("99")
                    .authorizationCode(tx.getAuthorizationResult().authorizationCode())
                    .mti(tx.getAuthorizationResult().mti())
                    .status(TransactionStatus.ERROR)
                    .date(tx.getAuthorizationResult().date())
                    .transactionId(tx.getAuthorizationResult().transactionId())
                    .build();
            tx.setAuthorizationResult(responseCode);
            try {
                rabbitFactory.publish(RabbitQueueType.AUDIT, tx);
            } catch (Exception ex) {
                log.warn("⚠️ Audit publish failed: {}", ex.getMessage());
            }
        }
        return tx;
    }
}
