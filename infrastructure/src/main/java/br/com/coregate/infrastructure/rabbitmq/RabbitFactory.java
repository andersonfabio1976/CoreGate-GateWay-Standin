package br.com.coregate.infrastructure.rabbitmq;

import br.com.coregate.infrastructure.enums.RabbitQueueType;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import java.io.FileWriter;
import java.io.IOException;

@Component
@Slf4j
public class RabbitFactory {

    private final RabbitTemplate rabbitTemplate;
    private final RabbitProperties rabbitProperties;
    private final RabbitAdmin rabbitAdmin;

    public RabbitFactory(RabbitTemplate rabbitTemplate,
                         RabbitProperties rabbitProperties,
                         RabbitAdmin rabbitAdmin) {
        this.rabbitTemplate = rabbitTemplate;
        this.rabbitProperties = rabbitProperties;
        this.rabbitAdmin = rabbitAdmin;
    }

    @Retry(name = "rabbitPublish", fallbackMethod = "publishFallbackMessage")
    @CircuitBreaker(name = "rabbitPublish", fallbackMethod = "publishFallbackMessage")
    public void publish(RabbitQueueType type, Object message) {
        String key = type.name().toLowerCase();
        RabbitProduceConfig cfg = rabbitProperties.getProduces().get(key);
        if (cfg == null)
            throw new IllegalArgumentException("❌ Producer config not found for type: " + type);

        try {
            rabbitTemplate.convertAndSend(cfg.getExchange(), cfg.getRoutingKey(), message);
            System.out.printf("📤 [PUBLISH] %s → Exchange=%s | RoutingKey=%s | Payload=%s%n",
                    type, cfg.getExchange(), cfg.getRoutingKey(), message);
        } catch (AmqpException ex) {
            if (isNotFoundExchange(ex)) {
                log.error("⚠️ [AUTO-CREATE] Exchange inexistente (%s). Criando dinamicamente...%n",
                        cfg.getExchange());
                autoCreateRabbitInfra(cfg);

                // 🔁 Retry imediato, Resilience4j controlará novos reenvios
                rabbitTemplate.convertAndSend(cfg.getExchange(), cfg.getRoutingKey(), message);
                log.info("🔁 [RETRY] Mensagem reenviada após criação: %s%n", message);
            } else {
                throw ex; // Resilience4j tratará via retry/fallback
            }
        }
    }

    private boolean isNotFoundExchange(Throwable ex) {
        return ex.getMessage() != null && ex.getMessage().contains("no exchange");
    }

    private void autoCreateRabbitInfra(RabbitProduceConfig cfg) {
        Exchange exchange = new DirectExchange(cfg.getExchange(), true, false);
        Queue queue = new Queue(cfg.getQueue(), true);
        Binding binding = BindingBuilder.bind(queue)
                .to((DirectExchange) exchange)
                .with(cfg.getRoutingKey());

        rabbitAdmin.declareExchange(exchange);
        rabbitAdmin.declareQueue(queue);
        rabbitAdmin.declareBinding(binding);

        System.out.printf("✅ [AUTO-CREATE] Criado Exchange=%s | Queue=%s | RoutingKey=%s%n",
                cfg.getExchange(), cfg.getQueue(), cfg.getRoutingKey());
    }

    public void publishFallbackMessage(RabbitQueueType type, Object message, Throwable ex) {
        System.err.printf("💥 [FALLBACK] Falha ao publicar em %s → %s%n", type, ex.getMessage());
        try (FileWriter fw = new FileWriter("rabbit-fallback.log", true)) {
            fw.write("[%s] %s%n".formatted(type, message.toString()));
        } catch (IOException ignored) {}
    }

}
