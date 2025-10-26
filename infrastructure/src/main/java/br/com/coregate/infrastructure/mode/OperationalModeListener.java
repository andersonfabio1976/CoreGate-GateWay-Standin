package br.com.coregate.infrastructure.mode;

import br.com.coregate.infrastructure.enums.OperationalMode;
import br.com.coregate.infrastructure.enums.RabbitQueueType;
import br.com.coregate.infrastructure.rabbitmq.RabbitConsumer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * Escuta mudanças de modo operacional enviadas via RabbitMQ.
 */
@Slf4j
@Component
public class OperationalModeListener {

    private final OperationalModeManager modeManager;

    public OperationalModeListener(OperationalModeManager modeManager) {
        this.modeManager = modeManager;
    }

    @RabbitConsumer(RabbitQueueType.MODE)
    public void onModeChange(OperationalModeChangedEvent event) {
        log.info("📩 Received operational mode event: {}", event.getNewMode());
        modeManager.switchTo(event.getNewMode(), event.getReason());
    }
}
