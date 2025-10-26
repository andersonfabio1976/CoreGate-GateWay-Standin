package br.com.coregate.infrastructure.rabbitmq;

import br.com.coregate.infrastructure.enums.RabbitQueueType;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RabbitConsumer {
    RabbitQueueType value();
}
