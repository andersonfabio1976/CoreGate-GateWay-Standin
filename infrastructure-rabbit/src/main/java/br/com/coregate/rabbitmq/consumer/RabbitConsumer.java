package br.com.coregate.rabbitmq.consumer;

import br.com.coregate.core.contracts.dto.rabbit.RabbitQueueType;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RabbitConsumer {
    RabbitQueueType value();
}
