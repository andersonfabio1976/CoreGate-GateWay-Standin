package br.com.coregate.metrics.config;

import br.com.coregate.metrics.monitoring.MonitoringModeChangeListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@Slf4j
public class RedisConfig {

    // 🔥 Agora vem do application.yml (dev, docker, AWS)
    @Value("${spring.redis.host}")
    private String redisHost;

    @Value("${spring.redis.port}")
    private int redisPort;

    // ================================================================
    // 🚀 Connection Factory dinâmico
    // ================================================================
    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        log.info("🔌 Conectando ao Redis em {}:{}", redisHost, redisPort);
        return new LettuceConnectionFactory(redisHost, redisPort);
    }

    // ================================================================
    // 🚀 RedisTemplate
    // ================================================================
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory cf) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(cf);
        template.setDefaultSerializer(new StringRedisSerializer());
        return template;
    }

    // ================================================================
    // 🚀 StringRedisTemplate
    // ================================================================
    @Bean
    public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory cf) {
        return new StringRedisTemplate(cf);
    }

    // ================================================================
    // 🚀 Serializer principal
    // ================================================================
    @Bean
    public StringRedisSerializer stringRedisSerializer() {
        return new StringRedisSerializer();
    }

    // ================================================================
    // 🚀 Listener
    // ================================================================
    @Bean
    public MessageListenerAdapter listenerAdapter(
            MonitoringModeChangeListener listener,
            StringRedisSerializer stringRedisSerializer
    ) {
        MessageListenerAdapter adapter = new MessageListenerAdapter(listener, "onMessage");
        adapter.setSerializer(stringRedisSerializer);
        return adapter;
    }

    // ================================================================
    // 🚀 Container (inscrição no canal)
    // ================================================================
    @Bean
    public RedisMessageListenerContainer redisContainer(
            RedisConnectionFactory cf,
            MessageListenerAdapter adapter
    ) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(cf);
        container.addMessageListener(adapter, new PatternTopic("coregate:mode-change"));
        return container;
    }
}
