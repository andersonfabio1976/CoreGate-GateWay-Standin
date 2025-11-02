package br.com.coregate.orchestrator.saga.step;

import br.com.coregate.application.dto.transaction.AuthorizationResult;
import br.com.coregate.infrastructure.enums.RabbitQueueType;
import br.com.coregate.infrastructure.rabbitmq.RabbitFactory;
import br.com.coregate.application.dto.orquestrator.OrquestratorSagaContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class Notify {

    private static RabbitFactory rabbitFactory;

    public static OrquestratorSagaContext execute(OrquestratorSagaContext tx) {
        try {
            log.info("📡 Notifying result for transaction {} (code={})", tx.getTransaction().getId(), tx.getAuthorizationResult().responseCode());

            // Envia mensagem ao Ingress via Rabbit
            rabbitFactory.publish(RabbitQueueType.NOTIFY, tx);

            return tx;

        } catch (Exception e) {
            log.error("❌ Notify step failed: {}", e.getMessage(), e);
            var responseCode = AuthorizationResult.builder()
                            .responseCode("98")
                            .authorizationCode(tx.getAuthorizationResult().authorizationCode())
                            .mti(tx.getAuthorizationResult().mti())
                            .status(tx.getAuthorizationResult().status())
                            .timestamp(tx.getAuthorizationResult().timestamp())
                            .transactionId(tx.getAuthorizationResult().transactionId())
                            .build();
            tx.setAuthorizationResult(responseCode);
            return tx;
        }
    }

    public static OrquestratorSagaContext rollback(OrquestratorSagaContext tx) {
        log.warn("↩️ Rolling back Notify for {}", tx);
        return tx;
    }
}
