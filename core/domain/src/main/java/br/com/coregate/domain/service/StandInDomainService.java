package br.com.coregate.domain.service;

import br.com.coregate.domain.model.Transaction;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class StandInDomainService {

    public boolean evaluate(Transaction tx) {
        log.info("🧠 Evaluating StandIn mode for transaction {}", tx.getId());
        // Lógica simplificada — no futuro, avaliação de regras STAND-IN real
        return tx.getMoney().getAmount().intValue() < 500;
    }
}
