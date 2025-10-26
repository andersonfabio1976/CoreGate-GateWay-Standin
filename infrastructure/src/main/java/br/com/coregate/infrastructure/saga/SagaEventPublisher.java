package br.com.coregate.infrastructure.saga;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * SagaEventPublisher é responsável por notificar eventos importantes do ciclo de vida da SAGA.
 * Pode ser facilmente adaptado para publicar mensagens em RabbitMQ, Kafka ou Redis Streams.
 */
@Slf4j
@Service
public class SagaEventPublisher {

    public void publishStart(String sagaName, Object payload) {
        log.info("📡 [SAGA START] {} | payload={}", sagaName, payload);
        // -> RabbitMQ / Redis / Kafka integration point
    }

    public void publishSuccess(String sagaName, Object payload) {
        log.info("✅ [SAGA SUCCESS] {} | payload={}", sagaName, payload);
    }

    public void publishFailure(String sagaName, Object payload, Throwable error) {
        log.error("💥 [SAGA FAILURE] {} | payload={} | error={}", sagaName, payload, error.getMessage());
    }

    public void publishCompensation(String sagaName, String step, Object payload) {
        log.warn("↩️ [SAGA COMPENSATION] {} | step={} | payload={}", sagaName, step, payload);
    }
}
