package br.com.coregate.adapters.publisher;

import br.com.coregate.application.port.out.AdvicePublisherPort;
import br.com.coregate.domain.model.Advice;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AdvicePublisherAdapter implements AdvicePublisherPort {

    @Override
    public void publishAdvice(Advice advice) {
        log.info("📡 Publishing advice for transaction {}", advice.getTransaction().getId());
        // Aqui entraria a publicação real — ex: RabbitMQ, Kafka, REST, gRPC...
    }
}
