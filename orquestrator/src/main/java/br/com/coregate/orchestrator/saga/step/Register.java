package br.com.coregate.orchestrator.saga.step;

import br.com.coregate.application.dto.orquestrator.OrquestratorSagaContext;
import br.com.coregate.infrastructure.enums.RabbitQueueType;
import br.com.coregate.infrastructure.rabbitmq.RabbitFactory;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Register {

    private static RabbitFactory rabbitFactory;

    public static OrquestratorSagaContext execute(OrquestratorSagaContext tx) {
        log.info("💾 Registering transaction state...");

        rabbitFactory.publish(RabbitQueueType.REGISTER,tx.getTransaction());

        return tx;
    }

    public static OrquestratorSagaContext rollback(OrquestratorSagaContext tx) {
        if (tx == null) {
            log.warn("⚠️ Cannot rollback Register: context is null");
            return null;
        }

        log.warn("↩️ Rolling back transaction persistence for {}", tx);
        // desfaz persistência, remove cache, etc.
        return tx;
    }
}
