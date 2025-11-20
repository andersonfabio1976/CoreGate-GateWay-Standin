package br.com.coregate.ingress.lifecycle.step;

import br.com.coregate.core.contracts.dto.context.ContextRequestDto;
import io.netty.buffer.Unpooled;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import java.nio.charset.StandardCharsets;

@Slf4j
@Component
public class EndStep {

    public ContextRequestDto execute(ContextRequestDto ctx) {
        try {
            if (ctx == null || ctx.getContext() == null || ctx.getContext().getChannel() == null) {
                log.warn("⚠️ EndStep chamado com contexto inválido (ctx/context/channel nulo).");
                return ctx;
            }

            String response = ctx.getHexString();

            if (response == null) {
                log.error("⚠️ EndStep - Nenhuma resposta disponível (hexString nulo). Nada será enviado ao POS.");
                return ctx;
            }

            byte[] bytes = response.getBytes(StandardCharsets.ISO_8859_1);
            int len = bytes.length;

            byte[] header = new byte[]{
                    (byte) ((len >> 8) & 0xFF),
                    (byte) (len & 0xFF)
            };

            ctx.getContext().getChannel().writeAndFlush(Unpooled.wrappedBuffer(header, bytes));

            log.info("✅ EndStep - Enviada resposta para POS: [{} bytes] '{}'", len, response);
            log.info("🏁 Saga finalizada com sucesso. Contexto: {}", ctx);

            return ctx;

        } catch (Exception e) {
            log.error("❌ Falha no EndStep: {}", e.getMessage(), e);
            throw new RuntimeException("Falha no EndStep", e);
        }
    }

    public ContextRequestDto rollback(ContextRequestDto ctx) {
        log.warn("↩️ Rollback EndStep - Nenhuma resposta será reenviada.");
        return ctx;
    }
}
