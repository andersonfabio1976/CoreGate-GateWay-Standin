package br.com.coregate.rules.auth;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class TenantRulesPubSubListener implements MessageListener {
    private final StringRedisTemplate redis;
    private final TenantRulesLoader loader;
    private final TenantRulesCache cache;

    @PostConstruct
    void subscribe() {
        redis.getConnectionFactory()
                .getConnection()
                .subscribe(this::onMessage, loader.pubSubChannel().getBytes());
        log.info("📡 Assinado canal Redis {}", loader.pubSubChannel());
    }

    public void onMessage(Message msg, byte[] pattern) {
        String tenantId = new String(msg.getBody());
        cache.invalidate(tenantId);
    }
}
