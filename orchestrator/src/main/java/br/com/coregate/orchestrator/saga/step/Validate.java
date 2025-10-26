package br.com.coregate.orchestrator.saga.step;

import br.com.coregate.application.dto.TransactionCommand;
import br.com.coregate.domain.model.Transaction;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Validate {

    public static Transaction execute(Transaction tx) {
        log.info("✅ Validating ISO8583 structure "+tx);
        // ISO field, MTI, antifraud
        return tx;
    }

    public static Transaction rollback(Transaction tx) {
        log.warn("↩️ Rolling back fetched data for {}", tx);
        // Aqui você desfaz o que foi carregado (limpa o contexto, por exemplo)
        //tx.setMerchantData(null);
        return tx;
    }

}
