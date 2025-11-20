package br.com.coregate.orchestrator.saga.step;

import br.com.coregate.core.contracts.dto.orquestrator.OrquestratorSagaContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class Start {

    public OrquestratorSagaContext execute(OrquestratorSagaContext tx) {
        log.info("🏁 Starting saga for {}", tx);
        return tx;
    }

    public  OrquestratorSagaContext rollback(OrquestratorSagaContext tx) {
        log.warn("↩️ Rolling back Start data for {}", tx);
        // Aqui você desfaz o que foi carregado (limpa o contexto, por exemplo)
        //tx.setMerchantData(null);
        return tx;
    }

}
