package br.com.coregate.orchestrator.saga.step;

import br.com.coregate.core.contracts.dto.orquestrator.OrchestratorSagaContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class Start {

    public OrchestratorSagaContext execute(OrchestratorSagaContext tx) {
        log.info("🏁 Starting saga for {}", tx);
        return tx;
    }

    public  OrchestratorSagaContext rollback(OrchestratorSagaContext tx) {
        log.warn("↩️ Rolling back Start data for {}", tx);
        return tx;
    }

}
