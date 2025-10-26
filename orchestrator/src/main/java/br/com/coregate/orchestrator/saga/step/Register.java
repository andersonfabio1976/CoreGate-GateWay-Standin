package br.com.coregate.orchestrator.saga.step;

import br.com.coregate.application.dto.TransactionCommand;
import br.com.coregate.domain.model.Transaction;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Register {

    public static Transaction execute(Transaction tx) {
        log.info("💾 Registering transaction state...");

        try {
            // Simula persistência
            log.info("🗃️ Saving to database...");

            // Simulando um erro proposital
            if (false) {
                throw new IllegalStateException("💣 Error persisting transaction");
            }

            return tx;

        } catch (Exception e) {
            log.error("❌ Register step failed: {}", e.getMessage());
            // repropaga para a Saga saber que deve iniciar rollback
            throw new RuntimeException("Saga rollback triggered by Register step", e);
        }
    }

    public static Transaction rollback(Transaction tx) {
        if (tx == null) {
            log.warn("⚠️ Cannot rollback Register: context is null");
            return null;
        }

        log.warn("↩️ Rolling back transaction persistence for {}", tx);
        // desfaz persistência, remove cache, etc.
        return tx;
    }
}
