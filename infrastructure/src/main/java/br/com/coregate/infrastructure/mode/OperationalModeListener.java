package br.com.coregate.infrastructure.mode;

import br.com.coregate.infrastructure.enums.OperationalMode;
import br.com.coregate.infrastructure.enums.RabbitQueueType;
import br.com.coregate.infrastructure.rabbitmq.RabbitConsumer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Escuta e aplica mudanças de modo operacional via RabbitMQ.
 * Implementa idempotência para evitar alternâncias repetidas quando
 * houver reentrega/duplicidade de mensagens no RabbitMQ.
 */
@Slf4j
@Component
public class OperationalModeListener {

    private final OperationalModeManager modeManager;

    public OperationalModeListener(OperationalModeManager modeManager) {
        this.modeManager = modeManager;
    }

    // -----------------------------
    // STANDIN_REQUESTED  (trava manual)
    // -----------------------------
    @RabbitConsumer(RabbitQueueType.STANDIN_REQUESTED)
    public void onStandInRequested(String rawMessage) {
        String cleaned = sanitizePayload(rawMessage);
        log.info("📩 Received STANDIN_REQUESTED event: {}", cleaned);

        RabbitQueueType eventType = RabbitQueueType.from(cleaned);
        if (eventType == null || eventType.getTargetMode() == null) {
            log.warn("⚠️ Unknown or invalid mode event: {}", cleaned);
            return;
        }

        OperationalMode target = eventType.getTargetMode();
        OperationalMode current = modeManager.getMode();

        // ✅ Idempotência: nada a fazer se já está no modo alvo
        if (target == current) {
            log.info("🔁 Ignored duplicate mode event: already in {}", current);
            return;
        }

        // Aplica a troca para STANDIN_REQUESTED (trava manual)
        modeManager.switchTo(target, "Manual Stand-In requested via API");
    }

    // -----------------------------
    // GATEWAY  (desbloqueio/retorno)
    // -----------------------------
    @RabbitConsumer(RabbitQueueType.GATEWAY)
    public void onGatewayRequested(String rawMessage) {
        String cleaned = sanitizePayload(rawMessage);
        log.info("📩 Received GATEWAY event: {}", cleaned);

        RabbitQueueType eventType = RabbitQueueType.from(cleaned);
        if (eventType == null || eventType.getTargetMode() == null) {
            log.warn("⚠️ Unknown or invalid mode event: {}", cleaned);
            return;
        }

        OperationalMode target = eventType.getTargetMode(); // sempre GATEWAY
        OperationalMode current = modeManager.getMode();

        // ✅ Idempotência: se já estamos em GATEWAY, ignore
        if (target == current) {
            log.info("🔁 Ignored duplicate mode event: already in {}", current);
            return;
        }

        // Se está travado por STANDIN_REQUESTED, forçamos o unlock seguro
        if (current.isLockedByRequest()) {
            log.warn("🔓 [MODE] Forcing unlock and switching to GATEWAY...");
            modeManager.forceGatewayUnlock("Gateway requested via RabbitMQ event");
            return;
        }

        // Caso normal (STANDIN_AUTOMATIC → GATEWAY)
        modeManager.switchTo(target, "Gateway requested via RabbitMQ event");
    }

    // -----------------------------
    // Utilitário: sanitiza payloads
    // -----------------------------
    private String sanitizePayload(String rawMessage) {
        if (rawMessage == null) return "";
        return rawMessage
                .replace("\"", "")
                .replace("{", "")
                .replace("}", "")
                .replace("payload=", "")
                .trim();
    }
}
