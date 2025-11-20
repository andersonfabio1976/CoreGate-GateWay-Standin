package br.com.coregate.rabbitmq.config;

import lombok.Data;

@Data
public class RabbitProduceConfig {
    private String queue;
    private String exchange;
    private String routingKey;
}