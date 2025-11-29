package br.com.coregate.orchestrator.saga.step;

import br.com.coregate.core.contracts.dto.orquestrator.OrchestratorSagaContext;
import br.com.coregate.core.contracts.dto.rabbit.RabbitQueueType;
import br.com.coregate.rabbitmq.factory.RabbitFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class Notify {

    private final RabbitFactory rabbitFactory;

    public OrchestratorSagaContext execute(OrchestratorSagaContext tx) {
        try {
            log.info("📡 Notifying result for transaction {} (result={})", tx.getTransactionCommand(), tx.getAuthorizationResult().responseCode());
            rabbitFactory.publish(RabbitQueueType.NOTIFY, tx.getTransactionCommand());
            return tx;
        } catch (Exception e) {
            log.error("❌ Notify step failed: {}", e.getMessage(), e);
            return tx;
        }
    }

    public OrchestratorSagaContext rollback(OrchestratorSagaContext tx) {
        log.warn("↩️ Rolling back Notify for {}", tx);
        return tx;
    }
}
