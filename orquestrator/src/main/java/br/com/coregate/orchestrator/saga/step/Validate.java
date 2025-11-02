package br.com.coregate.orchestrator.saga.step;

import br.com.coregate.application.dto.orquestrator.OrquestratorSagaContext;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Validate {

    public static OrquestratorSagaContext execute(OrquestratorSagaContext tx) {
        log.info("✅ Validating ISO8583 structure "+tx);
        // ISO field, MTI, antifraud
        return tx;
    }

    public static OrquestratorSagaContext rollback(OrquestratorSagaContext tx) {
        log.warn("↩️ Rolling back fetched data for {}", tx);
        // Aqui você desfaz o que foi carregado (limpa o contexto, por exemplo)
        //tx.setMerchantData(null);
        return tx;
    }

}
