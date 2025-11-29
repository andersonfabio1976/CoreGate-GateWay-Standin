package br.com.coregate.ingress.lifecycle.step;

import br.com.coregate.core.contracts.dto.transaction.TransactionIso;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * ⚠️ Step de tratamento de erro da SAGA Netty.
 * Responsável por registrar falhas e preservar o canal.
 */
@Slf4j
@Component
public class ErrorStep {

    public TransactionIso execute(TransactionIso ctx, Exception e) {
        log.error("[INGRESS] Netty Step Error {} (ctx={})", e.getMessage(), ctx, e);
        // Aqui você pode decidir se quer enviar uma mensagem de erro ISO8583
        // ou apenas manter o canal aberto para retry.
        return ctx;
    }

//    public TransactionIso8583 rollback(TransactionIso8583 ctx, Channel channel) {
//        log.warn("↩️ Rollback do ErrorStep — sem ação específica.");
//        return ctx;
//    }
}
