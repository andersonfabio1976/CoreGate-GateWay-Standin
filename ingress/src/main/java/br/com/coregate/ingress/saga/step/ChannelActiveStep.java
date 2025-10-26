package br.com.coregate.ingress.saga.step;

import br.com.coregate.ingress.saga.service.IngressContext;
import io.netty.buffer.ByteBuf;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ChannelActiveStep {

    public static IngressContext execute(IngressContext ctx) {
        try {
            // Recupera a última mensagem armazenada no atributo do canal
            Object msg = ctx.getCtx()
                    .channel()
                    .attr(AttributeKey.valueOf("lastMessage"))
                    .get();

            if (msg instanceof ByteBuf buffer) {
                byte[] raw = new byte[buffer.readableBytes()];
                buffer.readBytes(raw);
                ctx.setRawBytes(raw);
                log.info("📩 Mensagem lida: {} bytes", raw.length);
            } else {
                log.warn("⚠️ Nenhum ByteBuf encontrado no atributo 'lastMessage'");
            }

            return ctx;
        } catch (Exception e) {
            log.error("❌ Erro ao ler mensagem do canal: {}", e.getMessage(), e);
            throw new RuntimeException("Falha no ChannelActiveStep", e);
        }
    }

    public static IngressContext rollback(IngressContext ctx) {
        log.warn("↩️ Rollback ChannelActiveStep - Limpando dados do contexto...");
        ctx.setRawBytes(null);
        return ctx;
    }
}
