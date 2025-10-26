package br.com.coregate.infrastructure.rabbitmq;

import br.com.coregate.infrastructure.enums.ModeChangeEventType;
import br.com.coregate.infrastructure.enums.OperationalMode;
import br.com.coregate.infrastructure.enums.RabbitQueueType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 🛰️ Listener responsável por reagir aos eventos de mudança de modo operacional
 * publicados em filas do RabbitMQ.
 */
@Slf4j
@Component
public class OperationalModeChangeListener {

    private final AtomicReference<OperationalMode> currentMode = new AtomicReference<>(OperationalMode.GATEWAY);

    /**
     * Escuta eventos de stand-in (tanto automáticos quanto solicitados)
     */
    @RabbitConsumer(RabbitQueueType.STANDIN_REQUESTED)
    public void handleStandinChange(String event) {
        handleEvent(event);
    }

    /**
     * Escuta eventos de retorno ao modo gateway
     */
    @RabbitConsumer(RabbitQueueType.GATEWAY)
    public void handleGatewayChange(String event) {
        handleEvent(event);
    }

    /**
     * Lógica compartilhada para tratamento de eventos
     */
    private void handleEvent(String event) {
        ModeChangeEventType eventType = ModeChangeEventType.from(event);
        if (eventType == null) {
            log.warn("⚠️ Evento de modo desconhecido recebido: {}", event);
            return;
        }

        OperationalMode newMode = eventType.getTargetMode();
        OperationalMode oldMode = currentMode.getAndSet(newMode);

        log.info("🔄 Modo alterado de [{}] para [{}] via evento [{}]", oldMode, newMode, eventType);
    }

    public OperationalMode getCurrentMode() {
        return currentMode.get();
    }

    public boolean isStandin() {
        OperationalMode mode = currentMode.get();
        return mode.isStandIn();
    }
}
