package br.com.coregate.orchestrator.saga.step;

import br.com.coregate.application.dto.TransactionCommand;
import br.com.coregate.domain.model.Transaction;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class End {

    public static void execute(Transaction tx) {
        log.info("🏁 Saga ended successfully for {}", tx);
    }

}
