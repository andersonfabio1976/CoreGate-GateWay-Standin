package br.com.coregate.ingress.saga.service;

import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 📡 Publicador de eventos da SAGA Netty (Ingress)
 * Responsável por registrar logs e, opcionalmente,
 * enviar mensagens de status via socket Netty.
 */
@Slf4j
@Component
public class IngressSagaEventPublisher {

    private static final DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

    /** Evento de início da saga (por stepName) */
    public void publishStart(String stepName, IngressContext ctx) {
        log.info("▶️ [{}] [SAGA-START] Step iniciado: {}", timestamp(), stepName);
    }

    /** Evento de sucesso em um step */
    public void publishStepSuccess(String stepName, IngressContext ctx) {
        log.info("✅ [{}] [SAGA-SUCCESS] Step concluído: {}", timestamp(), stepName);
    }

    /** Evento de falha em um step (aciona rollback) */
    public void publishStepFailure(String stepName, IngressContext ctx, Throwable error) {
        log.error("💥 [{}] [SAGA-FAILURE] Step {} falhou: {}", timestamp(), stepName, error.getMessage(), error);
        // Opcional: notificar via socket para o POS
        sendNettyResponse(ctx.getCtx(), "[FAILURE] " + stepName + ": " + error.getMessage());
    }

    /** Evento de compensação (rollback) */
    public void publishCompensation(String stepName, IngressContext ctx) {
        log.warn("↩️ [{}] [SAGA-COMPENSATION] Step revertido: {}", timestamp(), stepName);
    }

    /** Evento de finalização completa da saga */
    public void publishSagaCompleted(IngressContext ctx) {
        log.info("🏁 [{}] [SAGA-END] Saga finalizada com sucesso. Contexto final: {}", timestamp(), ctx);
        sendNettyResponse(ctx.getCtx(), "[SAGA COMPLETED]");
    }

    /** Evento de rollback completo */
    public void publishSagaRollback(IngressContext ctx, Throwable cause) {
        log.warn("🧹 [{}] [SAGA-ROLLBACK] Fluxo revertido por erro: {}", timestamp(), cause.getMessage());
        sendNettyResponse(ctx.getCtx(), "[ROLLBACK] " + cause.getMessage());
    }

    // -------------------------------
    // Utilitário interno
    // -------------------------------

    private void sendNettyResponse(ChannelHandlerContext ctx, String message) {
        try {
            if (ctx != null && ctx.channel().isActive()) {
                ctx.writeAndFlush(message.getBytes());
                log.debug("📨 Enviado via Netty: {}", message);
            }
        } catch (Exception e) {
            log.error("⚠️ Falha ao enviar resposta Netty: {}", e.getMessage());
        }
    }

    private String timestamp() {
        return LocalDateTime.now().format(fmt);
    }
}
