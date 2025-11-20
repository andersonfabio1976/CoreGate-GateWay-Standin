package br.com.coregate.mode;


import br.com.coregate.core.contracts.dto.rabbit.RabbitQueueType;
import br.com.coregate.domain.enums.OperationalMode;
import br.com.coregate.rabbitmq.factory.RabbitFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;


@Slf4j
@Component
public class OperationalModePublisher {

    private final RabbitFactory rabbitFactory;

    public OperationalModePublisher(RabbitFactory rabbitFactory) {
        this.rabbitFactory = rabbitFactory;
    }

    public void publish(OperationalMode mode, String reason) {
        OperationalModeChangedEvent event = OperationalModeChangedEvent.builder()
                .newMode(mode)
                .reason(reason)
                .timestamp(LocalDateTime.now())
                .build();

        rabbitFactory.publish(RabbitQueueType.OPERATIONAL_MODE,event);
        log.info("📤 Published OperationalModeChangedEvent: {}", mode);
    }
}
