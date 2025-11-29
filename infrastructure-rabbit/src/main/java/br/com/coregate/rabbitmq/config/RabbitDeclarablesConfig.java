package br.com.coregate.rabbitmq.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.RabbitListenerContainerFactory;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 🔧 Responsável por registrar todas as filas, exchanges e bindings declaradas no application-infrastructure.yml.
 * Garante que toda a infraestrutura do RabbitMQ (producer e consumer) seja criada automaticamente.
 */
@Configuration
@DependsOn("rabbitAdmin")
public class RabbitDeclarablesConfig {

    private final RabbitProperties properties;
    private final RabbitAdmin rabbitAdmin;
    private final ObjectMapper objectMapper;

    public RabbitDeclarablesConfig(RabbitProperties properties, RabbitAdmin rabbitAdmin, ObjectMapper objectMapper) {
        this.properties = properties;
        this.rabbitAdmin = rabbitAdmin;
        this.objectMapper = objectMapper;
    }

    /**
     * Cria fisicamente as filas, exchanges e bindings definidos no application-infrastructure.yml.
     */
    private List<Declarables> createDeclarables(Map<String, RabbitProduceConfig> source, String type) {
        List<Declarables> list = new ArrayList<>();

        source.forEach((name, cfg) -> {
            Queue queue = new Queue(cfg.getQueue(), true);
            DirectExchange exchange = new DirectExchange(cfg.getExchange());
            Binding binding = BindingBuilder.bind(queue).to(exchange).with(cfg.getRoutingKey());
            list.add(new Declarables(queue, exchange, binding));

            // 🚀 Declara direto no broker (sem depender de eventos)
            rabbitAdmin.declareQueue(queue);
            rabbitAdmin.declareExchange(exchange);
            rabbitAdmin.declareBinding(binding);

            System.out.printf("✅ [%s] %s → Queue=%s | Exchange=%s | RoutingKey=%s%n",
                    type, name.toUpperCase(), cfg.getQueue(), cfg.getExchange(), cfg.getRoutingKey());
        });

        return list;
    }

    // ============================================================
    // 🏭 DECLARAÇÕES DE PRODUCER E CONSUMER
    // ============================================================

    @Bean
    public List<Declarables> producerDeclarables() {
        return createDeclarables(properties.getProduces(), "PRODUCER");
    }

    @Bean
    public List<Declarables> consumerDeclarables() {
        return createDeclarables(properties.getConsumes(), "CONSUMER");
    }

    // ============================================================
    // ⚙️ CONVERSORES E TEMPLATE
    // ============================================================

    /**
     * Conversor JSON usado pelo RabbitTemplate (publisher).
     */
    @Bean
    public MessageConverter jsonMessageConverter() {
        // Usa o ObjectMapper do Spring Boot, garantindo que suas configurações
        // (como formatação de data) sejam respeitadas.
        return new Jackson2JsonMessageConverter(objectMapper);
    }

    /**
     * Template usado para publicação de mensagens.
     */
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory coregateConnectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(coregateConnectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        return rabbitTemplate;
    }

    @Bean
    public RabbitListenerContainerFactory<?> rabbitListenerContainerFactory(ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);

        // ******* A linha crítica que resolve o erro *******
        factory.setMessageConverter(jsonMessageConverter());

        return factory;
    }

}