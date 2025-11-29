package br.com.coregate.ingress.lifecycle.step;

import br.com.coregate.core.contracts.dto.transaction.TransactionIso;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Step responsável por receber e validar o payload bruto ISO8583.
 *
 * 🔍 Melhorias:
 * - validação de null/empty
 * - logs mais precisos e leves
 * - prepara terreno para parser incremental (futuro)
 * - mantém 100% compatibilidade com seu fluxo atual
 */
@Slf4j
@Component
public class ChannelReadStep {

    public TransactionIso execute(TransactionIso ctx, Channel channel) {
        try {
            byte[] raw = ctx.getRawBytes();

            if (raw == null || raw.length == 0) {
                log.warn("[INGRESS] ChannelReadStep recebeu mensagem nula/vazia");
                return ctx;
            }

            // Log minimalista para alta performance
            if (raw.length < 150) {
                log.info("[INGRESS] Received From POS ({} bytes)", raw.length);
            } else {
                log.info("[INGRESS] Received Large Message From POS ({} bytes)", raw.length);
            }

            return ctx;

        } catch (Exception e) {
            log.error("[INGRESS] Error Reading Message From POS: {}", e.getMessage(), e);
            throw new RuntimeException("Falha no ChannelReadStep", e);
        }
    }

    public TransactionIso rollback(TransactionIso ctx, ChannelHandlerContext channel) {
        log.warn("↩️ Rollback ChannelReadStep - Limpando rawBytes do contexto.");
        ctx.setRawBytes(null);
        return ctx;
    }
}
