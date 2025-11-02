package br.com.coregate.infrastructure.saga;

import br.com.coregate.infrastructure.enums.SagaStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SagaEventPublisher {

    public void publishStart(String sagaName, Object payload) {
        log.info("📡 [SAGA START] {} | payload={}", sagaName, payload);
    }

    public void publishSuccess(String sagaName, Object payload) {
        log.info("✅ [SAGA SUCCESS] {} | payload={}", sagaName, payload);
    }

    // Nova versão com status explícito
    public void publishSuccess(String sagaName, Object payload, SagaStatus status) {
        log.info("✅ [SAGA {}] {} | payload={}", status, sagaName, payload);
    }

    public void publishFailure(String sagaName, Object payload, Throwable error) {
        log.error("💥 [SAGA FAILURE] {} | payload={} | error={}", sagaName, payload, error.getMessage());
    }

    // Nova versão com status
    public void publishFailure(String sagaName, Object payload, Throwable error, SagaStatus status) {
        log.error("💥 [SAGA {}] {} | payload={} | error={}", status, sagaName, payload, error.getMessage());
    }

    public void publishCompensation(String sagaName, String step, Object payload) {
        log.warn("↩️ [SAGA COMPENSATION] {} | step={} | payload={}", sagaName, step, payload);
    }
}
