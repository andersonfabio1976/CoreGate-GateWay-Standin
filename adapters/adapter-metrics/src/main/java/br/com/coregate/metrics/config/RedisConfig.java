package br.com.coregate.metrics.config;

import br.com.coregate.metrics.monitoring.MonitoringModeChangeListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.StringRedisSerializer; // NOVO IMPORT

@Configuration
@Slf4j
public class RedisConfig {

    // ... (redisConnectionFactory, redisTemplate, stringRedisTemplate permanecem iguais)

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        return new LettuceConnectionFactory("localhost", 6379);
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        return template;
    }

    @Bean
    public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory connectionFactory) {
        return new StringRedisTemplate(connectionFactory);
    }

    // --- NOVO BEAN PARA SERIALIZAÇÃO ---
    @Bean
    public StringRedisSerializer stringRedisSerializer() {
        return new StringRedisSerializer();
    }

    // --- MUDANÇA CRÍTICA AQUI ---
    @Bean
    public MessageListenerAdapter listenerAdapter(
            MonitoringModeChangeListener listener,
            StringRedisSerializer stringRedisSerializer // Injete o Serializer
    ) {
        MessageListenerAdapter adapter = new MessageListenerAdapter(listener, "onMessage");

        // ********* SOLUÇÃO *********
        // O MessageListenerAdapter precisa de um conversor para tentar resolver os argumentos.
        // Se a assinatura for Message/byte[], ele precisa de um serializer definido.
        adapter.setSerializer(stringRedisSerializer);

        return adapter;
    }

    @Bean
    public RedisMessageListenerContainer redisContainer(
            RedisConnectionFactory connectionFactory,
            MessageListenerAdapter listenerAdapter) {

        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.addMessageListener(listenerAdapter, new PatternTopic("coregate:mode-change"));
        return container;
    }
}