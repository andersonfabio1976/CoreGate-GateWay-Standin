package br.com.coregate.infrastructure.rabbitmq;

import lombok.Data;

@Data
public class RabbitProduceConfig {
    private String queue;
    private String exchange;
    private String routingKey;
}