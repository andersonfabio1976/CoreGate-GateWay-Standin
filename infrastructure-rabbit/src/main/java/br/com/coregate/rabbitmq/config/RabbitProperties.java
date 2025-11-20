package br.com.coregate.rabbitmq.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
@ConfigurationProperties(prefix = "rabbit")
public class RabbitProperties {

    private Map<String, RabbitProduceConfig> produces = new LinkedHashMap<>();
    private Map<String, RabbitProduceConfig> consumes = new LinkedHashMap<>();

    public Map<String, RabbitProduceConfig> getProduces() { return produces; }
    public void setProduces(Map<String, RabbitProduceConfig> produces) { this.produces = produces; }

    public Map<String, RabbitProduceConfig> getConsumes() { return consumes; }
    public void setConsumes(Map<String, RabbitProduceConfig> consumes) { this.consumes = consumes; }
}
