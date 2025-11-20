package br.com.coregate.rabbitmq.config;

import br.com.coregate.rabbitmq.consumer.RabbitConsumer;
import org.springframework.amqp.rabbit.annotation.RabbitListenerConfigurer;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistrar;
import org.springframework.amqp.rabbit.listener.MethodRabbitListenerEndpoint;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.MethodIntrospector;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.messaging.handler.annotation.support.DefaultMessageHandlerMethodFactory;
import org.springframework.lang.NonNull;
import java.lang.reflect.Method;
import java.util.Map;

@Configuration
public class CustomRabbitListenerConfigurer implements RabbitListenerConfigurer {

    private final RabbitProperties rabbitProperties;
    private final ApplicationContext applicationContext;
    private final ConfigurableListableBeanFactory beanFactory;
    private final DefaultMessageHandlerMethodFactory messageHandlerMethodFactory;

    public CustomRabbitListenerConfigurer(RabbitProperties rabbitProperties,
                                          ApplicationContext applicationContext,
                                          ConfigurableListableBeanFactory beanFactory,
                                          DefaultMessageHandlerMethodFactory messageHandlerMethodFactory) {
        this.rabbitProperties = rabbitProperties;
        this.applicationContext = applicationContext;
        this.beanFactory = beanFactory;
        this.messageHandlerMethodFactory = messageHandlerMethodFactory;
    }

    @Override
    public void configureRabbitListeners(@NonNull RabbitListenerEndpointRegistrar registrar) {

        // Registra o factory central no registrar (escolhe o factory que conhece o conversor)
        registrar.setMessageHandlerMethodFactory(this.messageHandlerMethodFactory);

        // Varredura e registro dos Consumers
        String[] beanNames = applicationContext.getBeanNamesForType(Object.class);

        for (String beanName : beanNames) {
            final Class<?> beanType = applicationContext.getType(beanName);
            if (beanType == null) continue;

            Map<Method, RabbitConsumer> annotatedMethods = MethodIntrospector.selectMethods(beanType,
                    (MethodIntrospector.MetadataLookup<RabbitConsumer>) method ->
                            AnnotatedElementUtils.findMergedAnnotation(method, RabbitConsumer.class)
            );

            annotatedMethods.forEach((method, annotation) -> {

                String typeName = annotation.value().name().toLowerCase();
                RabbitProduceConfig cfg = rabbitProperties.getConsumes().get(typeName);

                if (cfg == null) {
                    System.err.printf("⚠️ Configuração de CONSUMER não encontrada em rabbit-application.yml para tipo: %s%n", typeName);
                    return;
                }

                MethodRabbitListenerEndpoint endpoint = new MethodRabbitListenerEndpoint();
                endpoint.setBean(applicationContext.getBean(beanName));
                endpoint.setMethod(method);
                endpoint.setBeanFactory(this.beanFactory);

                endpoint.setId(beanName + "#" + method.getName());
                endpoint.setQueueNames(cfg.getQueue());

                // Usa factory central (já inicializado)
                endpoint.setMessageHandlerMethodFactory(this.messageHandlerMethodFactory);

                registrar.registerEndpoint(endpoint);

                System.out.printf("🎯 Listener customizado registrado → [%s] Queue=%s%n",
                        typeName.toUpperCase(), cfg.getQueue());
            });
        }
    }
}