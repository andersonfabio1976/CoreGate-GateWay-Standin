package br.com.coregate.finalizer.event;

import br.com.coregate.infrastructure.enums.OperationalMode;
import br.com.coregate.infrastructure.rabbitmq.RabbitFactory;
import br.com.coregate.infrastructure.enums.RabbitQueueType;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ModeChangePublisher {

    private final RabbitFactory rabbitFactory;
    private final StringRedisTemplate redis;
    private final ObjectMapper mapper = new ObjectMapper();

    /**
     * Publica mudança de modo operacional.
     * 1️⃣ Envia evento confiável via RabbitMQ (para módulos do sistema)
     * 2️⃣ Replica via Redis Pub/Sub (para o painel de monitoramento NOC)
     */
    public void publishModeChange(OperationalMode mode, double tps, double sla) {
        try {
            var event = new ModeChangeEvent(mode.name(), tps, sla);
            String json = mapper.writeValueAsString(event);

            // 📨 1. RabbitMQ — para módulos operacionais (Ingress, Context, etc.)
            rabbitFactory.publish(RabbitQueueType.MODE, event);

            // 📡 2. Redis — para o dashboard NOC
            redis.convertAndSend("coregate:mode-change", json);

            log.info("📢 Modo alterado para '{}' (TPS={}, SLA={}) — enviado via RabbitMQ e Redis.",
                    mode, tps, sla);

        } catch (Exception e) {
            log.error("❌ Erro ao publicar mudança de modo: {}", e.getMessage(), e);
        }
    }

    /** Estrutura serializável do evento */
    public record ModeChangeEvent(String mode, double tps, double sla) {}
}
