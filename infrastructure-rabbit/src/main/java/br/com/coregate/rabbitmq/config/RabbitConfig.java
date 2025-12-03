package br.com.coregate.rabbitmq.config;

import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.RabbitListenerContainerFactory;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    // 🔥 Agora vem do application.yml (AWS, dev, prod, docker…)
    @Value("${spring.rabbitmq.host}")
    private String rabbitHost;

    @Value("${spring.rabbitmq.port}")
    private int rabbitPort;

    @Value("${spring.rabbitmq.username}")
    private String rabbitUser;

    @Value("${spring.rabbitmq.password}")
    private String rabbitPass;

    /**
     * ConnectionFactory com tuning de performance.
     * Agora usa propriedades externas.
     */
    @Bean
    public ConnectionFactory coregateConnectionFactory() {

        CachingConnectionFactory cf = new CachingConnectionFactory(rabbitHost, rabbitPort);
        cf.setUsername(rabbitUser);
        cf.setPassword(rabbitPass);

        // 🔥 Tuning
        cf.setRequestedHeartBeat(30);
        cf.setConnectionTimeout(30000);
        cf.setChannelCacheSize(100);
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

        factory.setAcknowledgeMode(AcknowledgeMode.AUTO);
        factory.setConcurrentConsumers(4);
        factory.setMaxConcurrentConsumers(16);
        factory.setPrefetchCount(50);
        factory.setDefaultRequeueRejected(false);

        return factory;
    }
}
