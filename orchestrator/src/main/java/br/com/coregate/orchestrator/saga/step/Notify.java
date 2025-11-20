package br.com.coregate.orchestrator.saga.step;

import br.com.coregate.core.contracts.dto.orquestrator.OrquestratorSagaContext;
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

    public OrquestratorSagaContext execute(OrquestratorSagaContext tx) {
        tx.getTransaction().setResponseCode(tx.getAuthorizationResult().responseCode());
        tx.getTransaction().setAuthorizationCode(tx.getAuthorizationResult().authorizationCode());
        tx.getTransaction().setAuthorizedAt(tx.getAuthorizationResult().timestamp());
        try {
            log.info("📡 Notifying result for transaction {} (code={})", tx.getTransaction().getId(), tx.getAuthorizationResult().responseCode());
            rabbitFactory.publish(RabbitQueueType.NOTIFY, tx.getTransaction());
            return tx;
        } catch (Exception e) {
            log.error("❌ Notify step failed: {}", e.getMessage(), e);
            return tx;
        }
    }

    public OrquestratorSagaContext rollback(OrquestratorSagaContext tx) {
        log.warn("↩️ Rolling back Notify for {}", tx);
        return tx;
    }
}
