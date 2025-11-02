package br.com.coregate.ingress.saga.step;

import br.com.coregate.application.dto.context.ContextRequestDto;
import io.netty.buffer.Unpooled;
import lombok.extern.slf4j.Slf4j;
import java.nio.charset.StandardCharsets;

@Slf4j
public class EndStep {

    public static ContextRequestDto execute(ContextRequestDto ctx) {
        try {
            if (ctx == null || ctx.getContext().getChannel() == null) {
                log.warn("⚠️ EndStep chamado com contexto inválido (channel nulo).");
                return ctx;
            }

            String response = ctx.getHexString();
            byte[] bytes = response.getBytes(StandardCharsets.ISO_8859_1);
            int len = bytes.length;

            byte[] header = new byte[] {
                    (byte) ((len >> 8) & 0xFF),
                    (byte) (len & 0xFF)
            };

            ctx.getContext().getChannel().writeAndFlush(Unpooled.wrappedBuffer(header, bytes));

            log.info("✅ EndStep - Enviada resposta fake para POS: [{} bytes] '{}'", len, response);
            log.info("🏁 Saga finalizada com sucesso. Contexto: {}", ctx);

            return ctx;

        } catch (Exception e) {
            log.error("❌ Falha no EndStep: {}", e.getMessage(), e);
            throw new RuntimeException("Falha no EndStep", e);
        }
    }

    public static ContextRequestDto rollback(ContextRequestDto ctx) {
        log.warn("↩️ Rollback EndStep - Nenhuma resposta será reenviada.");
        return ctx;
    }
}
