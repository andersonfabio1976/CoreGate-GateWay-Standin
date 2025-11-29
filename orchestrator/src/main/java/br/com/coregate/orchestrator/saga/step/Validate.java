package br.com.coregate.orchestrator.saga.step;

import br.com.coregate.core.contracts.dto.orquestrator.OrchestratorSagaContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class Validate {

    public OrchestratorSagaContext execute(OrchestratorSagaContext tx) {
        log.info("✅ Validating ISO8583 structure "+tx);
        // ISO field, MTI, antifraud
        return tx;
    }

    public OrchestratorSagaContext rollback(OrchestratorSagaContext tx) {
        log.warn("↩️ Rolling back fetched data for {}", tx);
        return tx;
    }

}
