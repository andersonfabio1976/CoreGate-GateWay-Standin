package br.com.coregate.finalizer.client;

import br.com.coregate.application.dto.AuthorizationResult;
import br.com.coregate.application.dto.TransactionCommandRequest;
import br.com.coregate.application.dto.TransactionCommandResponse;
import br.com.coregate.infrastructure.rabbitmq.RabbitFactory;
import br.com.coregate.infrastructure.enums.RabbitQueueType;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 🔗 Conexão com o emissor real (via FeignClient).
 * - Se falhar, ativa CircuitBreaker e publica evento para modo STANDIN_AUTOMATIC.
 */
@Slf4j
@Service
public class ConnectionToIssuer {

    private final IssuerFeignClient issuerFeignClient;
    private final RabbitFactory rabbitFactory;

    public ConnectionToIssuer(IssuerFeignClient issuerFeignClient, RabbitFactory rabbitFactory) {
        this.issuerFeignClient = issuerFeignClient;
        this.rabbitFactory = rabbitFactory;
    }

    @CircuitBreaker(name = "issuerClientBreaker", fallbackMethod = "fallbackToStandIn")
    @Retry(name = "issuerClientRetry")
    public TransactionCommandResponse connect(TransactionCommandRequest tx) {
        log.info("🚀 [FINALIZER] Enviando transação ao emissor: {}", tx);
        try {
            TransactionCommandResponse response = issuerFeignClient.authorize(tx);
            log.info("✅ [FINALIZER] Resposta do emissor: {}", response);
            return response;
        } catch (Exception e) {
            log.error("❌ [FINALIZER] Falha ao chamar emissor: {}", e.getMessage(), e);
            AuthorizationResult txresult = AuthorizationResult.builder()
                    .responseCode("ISSUER_UNAVAILABLE")
                    .build();
            return TransactionCommandResponse.builder()
                    .authorizationResult(txresult)
                    .build();
        }
    }

    /**
     * 🧩 Fallback chamado se o circuito estiver aberto ou após várias falhas consecutivas.
     */
    public TransactionCommandResponse fallbackToStandIn(TransactionCommandRequest tx, Throwable e) {
        log.error("💥 [FINALIZER] CircuitBreaker acionado — Emissor indisponível: {}", e.getMessage());

        // ✅ Publica evento para ativar STANDIN_AUTOMATIC (modo automático)
        rabbitFactory.publish(RabbitQueueType.STANDIN_AUTOMATIC, RabbitQueueType.STANDIN_AUTOMATIC.name());

        AuthorizationResult result = AuthorizationResult.builder()
                .responseCode("ISSUER_UNAVAILABLE")
                .build();

        return TransactionCommandResponse.builder()
                .authorizationResult(result)
                .build();
    }
}
