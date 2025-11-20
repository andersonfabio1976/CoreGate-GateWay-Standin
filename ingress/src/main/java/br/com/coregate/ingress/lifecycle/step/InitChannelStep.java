package br.com.coregate.ingress.lifecycle.step;

import br.com.coregate.core.contracts.dto.context.ContextRequestDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class InitChannelStep {

    public ContextRequestDto execute(ContextRequestDto ctx) {
        log.info("🚀 InitChannelStep - Inicializando canal Netty...");
        try {
            // Aqui pode ser feito handshake, validação, etc.
            return ctx;
        } catch (Exception e) {
            log.error("❌ Erro ao inicializar canal: {}", e.getMessage(), e);
            throw new RuntimeException("Falha no InitChannelStep", e);
        }
    }

    public ContextRequestDto rollback(ContextRequestDto ctx) {
        log.warn("↩️ Rollback InitChannelStep - limpando recursos do canal...");
        return ctx;
    }
}
