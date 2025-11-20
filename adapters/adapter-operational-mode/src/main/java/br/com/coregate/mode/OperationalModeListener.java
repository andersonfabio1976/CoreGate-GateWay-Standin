package br.com.coregate.mode;

import br.com.coregate.core.contracts.dto.rabbit.RabbitQueueType;
import br.com.coregate.domain.enums.OperationalMode;
import br.com.coregate.rabbitmq.consumer.RabbitConsumer;
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

    @RabbitConsumer(RabbitQueueType.OPERATIONAL_MODE)
    public void onChangeOperationalMode(OperationalModeChangedEvent event) {

        if (event == null || event.getNewMode() == null) {
            log.warn("⚠️ Unknown or invalid mode event: {}", event);
            return;
        }

        OperationalMode target = event.getNewMode();
        OperationalMode current = modeManager.getMode();

        // ✅ Idempotência: nada a fazer se já está no modo alvo
        if (target == current) {
            log.info("🔁 Ignored duplicate mode event: already in {}", current);
            return;
        }

        if (target == OperationalMode.GATEWAY && current == OperationalMode.STANDIN_REQUESTED) {
            log.info("🔁 Ignored GATEWAY MODE in STANDIN_REQUESTED");
            return;
        }

        modeManager.switchTo(target, "Switch to OperationalMode "+target);
    }

}
