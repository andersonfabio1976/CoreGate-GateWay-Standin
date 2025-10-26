package br.com.coregate.infrastructure.monitoring;// br.com.coregate.infrastructure.monitoring.MonitoringModeChangeListener.java

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MonitoringModeChangeListener {

    // Mude a assinatura para String/String, pois o MessageListenerAdapter
    // já fará a conversão do corpo da mensagem usando o StringRedisSerializer.
    public void onMessage(String body, String channel) {
        log.info("📡 [Redis] Mensagem recebida no canal '{}': {}", channel, body);
        // Não precisa mais de new String(message.getBody())
    }
}