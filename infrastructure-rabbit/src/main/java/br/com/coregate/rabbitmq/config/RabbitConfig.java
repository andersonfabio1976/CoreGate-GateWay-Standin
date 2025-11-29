package br.com.coregate.rabbitmq.config;

import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.RabbitListenerContainerFactory;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    /**
     * ConnectionFactory com tuning de performance.
     */
    @Bean
    public ConnectionFactory coregateConnectionFactory() {
        CachingConnectionFactory cf = new CachingConnectionFactory("localhost", 5672);
        cf.setUsername("coregate");
        cf.setPassword("coregate");

        // 🔥 Tuning
        cf.setRequestedHeartBeat(30);          // compatível com rabbitmq.conf
        cf.setConnectionTimeout(30000);        // 30s
        cf.setChannelCacheSize(100);           // canais em cache
        cf.setCacheMode(CachingConnectionFactory.CacheMode.CHANNEL);

        return cf;
    }

    /**
     * Conversor JSON compartilhado.
     */
    @Bean
    public Jackson2JsonMessageConverter rabbitJsonConverter() {
        return new Jackson2JsonMessageConverter();
    }

    /**
     * Factory com concorrência e prefetch otimizados.
     */
    @Bean("coregateListenerFactory")
    public RabbitListenerContainerFactory<?> coregateListenerFactory(ConnectionFactory coregateConnectionFactory) {

        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();

        factory.setConnectionFactory(coregateConnectionFactory);
        factory.setMessageConverter(rabbitJsonConverter());

        // 🔥 A MÁGICA DO THROUGHPUT
        factory.setAcknowledgeMode(AcknowledgeMode.AUTO);
        factory.setConcurrentConsumers(4);
        factory.setMaxConcurrentConsumers(16);
        factory.setPrefetchCount(50);
        factory.setDefaultRequeueRejected(false); // evita redelivery infinito

        return factory;
    }
}
