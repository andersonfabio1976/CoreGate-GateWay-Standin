package br.com.coregate.ingress.saga.step;

import br.com.coregate.ingress.saga.service.IngressContext;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;

/**
 * Step final da SAGA Netty.
 * Aqui simulamos uma resposta ISO8583 "Approved" (MTI 0810, código 00)
 * enviada de volta ao POS. Mantém a conexão aberta para simular sessões contínuas.
 */
@Slf4j
public class EndStep {

    public static IngressContext execute(IngressContext ctx) {
        try {
            if (ctx == null || ctx.getCtx() == null) {
                log.warn("⚠️ EndStep chamado com contexto inválido (ctx nulo).");
                return ctx;
            }

            // 🔹 Monta uma resposta fake ISO8583 (simplificada)
            String mtiResponse = "0810";
            String responseCode = "00"; // 00 = Approved
            String fakeResponse = mtiResponse + "RESPCODE=" + responseCode;

            // 🔸 Converte para bytes ISO-8859-1 (mantém encoding legível)
            byte[] responseBytes = fakeResponse.getBytes(StandardCharsets.ISO_8859_1);
            int len = responseBytes.length;

            // 🔸 Adiciona cabeçalho de 2 bytes (tamanho)
            byte[] header = new byte[] {
                    (byte) ((len >> 8) & 0xFF),
                    (byte) (len & 0xFF)
            };

            // 🔹 Envia para o POS sem fechar o canal
            ctx.getCtx().writeAndFlush(Unpooled.wrappedBuffer(header, responseBytes));

            log.info("✅ EndStep - Enviada resposta fake para POS: [{} bytes] '{}'", len, fakeResponse);
            log.info("🏁 Saga finalizada com sucesso. Contexto: {}", ctx);

            return ctx;

        } catch (Exception e) {
            log.error("❌ Falha no EndStep ao enviar resposta: {}", e.getMessage(), e);
            throw new RuntimeException("Falha no EndStep", e);
        }
    }

    public static IngressContext rollback(IngressContext ctx) {
        log.warn("↩️ Rollback EndStep - Nenhuma resposta será reenviada.");
        return ctx;
    }
}
