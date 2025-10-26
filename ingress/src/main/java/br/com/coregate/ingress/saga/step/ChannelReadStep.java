package br.com.coregate.ingress.saga.step;

import br.com.coregate.ingress.saga.service.IngressContext;
import io.netty.buffer.ByteBuf;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;

/**
 * Step responsável por ler os bytes crus recebidos via Netty
 * e armazená-los no contexto da SAGA para processamento posterior.
 */
@Slf4j
public class ChannelReadStep {

    public static IngressContext execute(IngressContext ctx) {
        try {
            // 🔎 Tenta recuperar a última mensagem do atributo do canal
            Object msg = ctx.getCtx()
                    .channel()
                    .attr(AttributeKey.valueOf("lastMessage"))
                    .get();

            if (msg instanceof ByteBuf buffer) {
                byte[] raw = new byte[buffer.readableBytes()];
                buffer.readBytes(raw);
                ctx.setRawBytes(raw);

                log.info("📥 ChannelReadStep - {} bytes recebidos do canal.", raw.length);
            } else if (ctx.getRawBytes() != null) {
                // Se já veio no contexto, apenas confirma leitura
                log.info("📥 ChannelReadStep - Buffer existente no contexto ({} bytes).", ctx.getRawBytes().length);
            } else {
                log.warn("⚠️ ChannelReadStep - Nenhum dado encontrado no canal ou contexto.");
            }

            return ctx;

        } catch (Exception e) {
            log.error("❌ Erro ao ler mensagem do canal: {}", e.getMessage(), e);
            throw new RuntimeException("Falha no ChannelReadStep", e);
        }
    }

    public static IngressContext rollback(IngressContext ctx) {
        log.warn("↩️ Rollback ChannelReadStep - Limpando rawBytes do contexto.");
        ctx.setRawBytes(null);
        return ctx;
    }
}
