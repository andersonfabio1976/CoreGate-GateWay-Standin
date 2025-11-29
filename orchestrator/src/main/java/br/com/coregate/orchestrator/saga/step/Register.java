package br.com.coregate.orchestrator.saga.step;

import br.com.coregate.core.contracts.dto.orquestrator.OrchestratorSagaContext;
import br.com.coregate.core.contracts.dto.rabbit.RabbitQueueType;
import br.com.coregate.rabbitmq.factory.RabbitFactory;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@AllArgsConstructor
@Component
public class Register {

    private final RabbitFactory rabbitFactory;

    public OrchestratorSagaContext execute(OrchestratorSagaContext tx) {
        log.info("💾 Registering transaction state...");
        rabbitFactory.publish(RabbitQueueType.REGISTER,tx.getTransactionCommand());
        return tx;
    }

    public  OrchestratorSagaContext rollback(OrchestratorSagaContext tx) {
        if (tx == null) {
            log.warn("⚠️ Cannot rollback Register: context is null");
            return null;
        }

        log.warn("↩️ Rolling back transaction persistence for {}", tx);
        // desfaz persistência, remove cache, etc.
        return tx;
    }
}
