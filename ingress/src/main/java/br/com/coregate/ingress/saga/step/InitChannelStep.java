package br.com.coregate.ingress.saga.step;

import br.com.coregate.application.dto.context.ContextRequestDto;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class InitChannelStep {

    public static ContextRequestDto execute(ContextRequestDto ctx) {
        log.info("🚀 InitChannelStep - Inicializando canal Netty...");
        try {
            // Aqui pode ser feito handshake, validação, etc.
            return ctx;
        } catch (Exception e) {
            log.error("❌ Erro ao inicializar canal: {}", e.getMessage(), e);
            throw new RuntimeException("Falha no InitChannelStep", e);
        }
    }

    public static ContextRequestDto rollback(ContextRequestDto ctx) {
        log.warn("↩️ Rollback InitChannelStep - limpando recursos do canal...");
        return ctx;
    }
}
