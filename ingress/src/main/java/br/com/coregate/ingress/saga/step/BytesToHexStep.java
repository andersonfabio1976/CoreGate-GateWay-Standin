package br.com.coregate.ingress.saga.step;

import br.com.coregate.ingress.saga.service.IngressContext;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BytesToHexStep {

    public static IngressContext execute(IngressContext ctx) {
        log.info("🔢 BytesToHexStep - Convertendo payload para HEX...");
        try {
            if (ctx.getRawBytes() == null)
                throw new IllegalStateException("Nenhum payload encontrado para conversão.");

            StringBuilder sb = new StringBuilder();
            for (byte b : ctx.getRawBytes()) {
                sb.append(String.format("%02X", b));
            }

            ctx.setHexString(sb.toString());
            log.info("✅ HEX gerado com sucesso: {}", ctx.getHexString());
            return ctx;

        } catch (Exception e) {
            log.error("❌ Falha ao converter bytes em HEX: {}", e.getMessage(), e);
            throw new RuntimeException("Erro no BytesToHexStep", e);
        }
    }

    public static IngressContext rollback(IngressContext ctx) {
        log.warn("↩️ Rollback BytesToHexStep - removendo string HEX do contexto...");
        ctx.setHexString(null);
        return ctx;
    }
}
