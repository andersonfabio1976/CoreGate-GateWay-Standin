package br.com.coregate.orchestrator.saga.step;

import br.com.coregate.application.dto.orquestrator.OrquestratorSagaContext;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Start {

    public static OrquestratorSagaContext execute(OrquestratorSagaContext tx) {
        log.info("🏁 Starting saga for {}", tx);
        return tx;
    }

    public static OrquestratorSagaContext rollback(OrquestratorSagaContext tx) {
        log.warn("↩️ Rolling back Start data for {}", tx);
        // Aqui você desfaz o que foi carregado (limpa o contexto, por exemplo)
        //tx.setMerchantData(null);
        return tx;
    }

}
