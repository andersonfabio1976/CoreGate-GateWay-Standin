package br.com.coregate.ingress.lifecycle.step;

import br.com.coregate.core.contracts.dto.transaction.TransactionIso;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * EndStep
 * -------
 * 🔥 Versão High-Performance:
 * - writeAndFlush zero-cópia
 * - valida canal antes do envio
 * - logs leves e sem ruído
 * - preparado para alta taxa de TPS
 *
 * Sem alterar absolutamente nada da semântica original.
 */
@Slf4j
@Component
public class EndStep {

    public TransactionIso execute(TransactionIso ctx, Channel channel) {
        try {
            if (ctx == null) {
                log.error("[INGRESS] EndStep recebeu contexto nulo — ignorando envio");
                return null;
            }

            if (channel == null || !channel.isActive()) {
                log.warn("[INGRESS] EndStep — canal nulo/inativo, não será enviado ao POS");
                return ctx;
            }

            byte[] raw = ctx.getRawBytes();
            if (raw == null) {
                log.error("[INGRESS] EndStep — rawBytes está nulo, nada a enviar ao POS");
                return ctx;
            }

            int len = raw.length;

            // Cabeçalho ISO8583 (2 bytes) — TCP length prefix
            byte[] header = new byte[] {
                    (byte) ((len >> 8) & 0xFF),
                    (byte) (len & 0xFF)
            };

            // ByteBuf composto (mais rápido e sem cópias)
            ByteBuf msg = Unpooled.wrappedBuffer(header, raw);

            channel.writeAndFlush(msg);

            log.info("[INGRESS] → POS | {} bytes enviados (TX={})",
                    len, ctx.getTransactionId());

            return ctx;

        } catch (Exception e) {
            log.error("[INGRESS] Erro no EndStep TX={}: {}", ctx.getTransactionId(), e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    public TransactionIso rollback(TransactionIso ctx) {
        log.warn("↩️ Rollback EndStep — resposta não enviada ao POS");
        return ctx;
    }
}
