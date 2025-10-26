package br.com.coregate.infrastructure.mode;

import br.com.coregate.infrastructure.rabbitmq.RabbitFactory;
import br.com.coregate.infrastructure.enums.RabbitQueueType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import br.com.coregate.infrastructure.enums.OperationalMode;

@Slf4j
@Component
public class OperationalModePublisher {

    private final RabbitFactory rabbitFactory;

    public OperationalModePublisher(RabbitTemplate rabbitTemplate, RabbitFactory rabbitFactory) {
        this.rabbitFactory = rabbitFactory;
    }

    public void publish(OperationalMode mode, String reason) {
        OperationalModeChangedEvent event = OperationalModeChangedEvent.builder()
                .newMode(mode)
                .reason(reason)
                .timestamp(LocalDateTime.now())
                .build();

        rabbitFactory.publish(RabbitQueueType.MODE,event);
        log.info("📤 Published OperationalModeChangedEvent: {}", mode);
    }
}
