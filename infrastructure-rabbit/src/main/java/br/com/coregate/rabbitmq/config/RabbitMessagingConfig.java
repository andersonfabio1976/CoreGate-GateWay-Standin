package br.com.coregate.rabbitmq.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.handler.annotation.support.DefaultMessageHandlerMethodFactory;

@Configuration
public class RabbitMessagingConfig {

    @Bean
    public DefaultMessageHandlerMethodFactory messageHandlerMethodFactory(
            ConfigurableListableBeanFactory beanFactory,
            ObjectMapper objectMapper) {

        DefaultMessageHandlerMethodFactory factory = new DefaultMessageHandlerMethodFactory();

        // Conversor do pacote spring.messaging (usado pelo InvocableHandlerMethod)
        MappingJackson2MessageConverter messagingConverter = new MappingJackson2MessageConverter(objectMapper);
        factory.setMessageConverter(messagingConverter);

        // Necessário para que o factory registre os ArgumentResolvers que usam beans (ex.: Validator, @Payload resolvers)
        factory.setBeanFactory(beanFactory);

        // Inicializa os resolvers padrão (PayloadArgumentResolver, HeaderMethodArgumentResolver, etc.)
        factory.afterPropertiesSet();

        return factory;
    }
}