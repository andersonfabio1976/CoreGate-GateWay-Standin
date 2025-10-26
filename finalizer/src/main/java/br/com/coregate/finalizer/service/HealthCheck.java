package br.com.coregate.finalizer.service;

import br.com.coregate.infrastructure.enums.ModeChangeEventType;
import br.com.coregate.infrastructure.enums.OperationalMode;
import br.com.coregate.infrastructure.mode.OperationalModeManager;
import br.com.coregate.infrastructure.rabbitmq.RabbitFactory;
import br.com.coregate.infrastructure.enums.RabbitQueueType;
import br.com.coregate.application.dto.TransactionCommandRequest;
import br.com.coregate.application.dto.TransactionCommand;
import br.com.coregate.domain.enums.*;
import br.com.coregate.domain.vo.Pan;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import java.math.BigDecimal;

/**
 * 💓 HealthCheck do emissor.
 * - Só roda quando modo == STANDIN_AUTOMATIC.
 * - Envia uma requisição "dummy" para o endpoint /api/v1/issuer/authorize.
 * - Se o emissor responder OK (00 ou 05), muda para modo GATEWAY.
 * - Se falhar consecutivamente N vezes, mantém STANDIN ativo.
 */
@Slf4j
@Controller
public class HealthCheck {

    private final OperationalModeManager modeManager;
    private final RabbitFactory rabbitFactory;
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${coregate.issuer.url:http://localhost:9095}")
    private String issuerUrl;

    @Value("${endpoint-authorize:/api/v1/issuer/authorize}")
    private String endPoint;

    @Value("${coregate.issuer.health-max-fails:3}")
    private int maxFails;

    @Value("${coregate.issuer.health-interval-ms:60000}")
    private long healthIntervalMs;

    private int failCount = 0;

    public HealthCheck(OperationalModeManager modeManager, RabbitFactory rabbitFactory) {
        this.modeManager = modeManager;
        this.rabbitFactory = rabbitFactory;
    }

    /**
     * 🔁 Executa automaticamente a cada N milissegundos.
     * Só roda quando modo == STANDIN_AUTOMATIC.
     */
    @Scheduled(fixedDelayString = "${coregate.issuer.health-interval-ms:60000}")
    public void checkIssuerHealth() {
        if (modeManager.getMode() != OperationalMode.STANDIN_AUTOMATIC) {
            return;
        }

        log.info("🔍 [HEALTHCHECK] Verificando emissor via endpoint /authorize...");

        try {
            // Monta um request dummy leve
            TransactionCommandRequest dummy = new TransactionCommandRequest(
                    TransactionCommand.builder()
                            .tenantId("HEALTHCHECK")
                            .merchantId("HEALTHCHECK-MERCHANT")
                            .amount(BigDecimal.valueOf(1.00))
                            .currency(CurrencyCode.BRL)
                            .brand(CardBrand.VISA)
                            .channel(ChannelType.ECOMMERCE)
                            .type(TransactionType.PURCHASE)
                            .pan(Pan.of("4111111111111111"))
                            .build()
            );

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<TransactionCommandRequest> entity = new HttpEntity<>(dummy, headers);

            ResponseEntity<String> response = restTemplate.exchange(
                    issuerUrl + endPoint, HttpMethod.POST, entity, String.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                handleIssuerRestored();
            } else {
                handleIssuerFailure(response.getStatusCode().value());
            }

        } catch (RestClientException e) {
            handleIssuerFailure(-1);
        }
    }

    private void handleIssuerRestored() {
        if (failCount > 0) {
            log.info("✅ [HEALTHCHECK] Emissor respondeu novamente. Resetando contador.");
            failCount = 0;
        }

        if (modeManager.getMode() == OperationalMode.STANDIN_AUTOMATIC) {
            log.info("🔄 [HEALTHCHECK] Emissor ativo — alternando para modo GATEWAY.");
            rabbitFactory.publish(RabbitQueueType.GATEWAY, ModeChangeEventType.RESTORE_MODE_GATEWAY_AUTOMATIC);
        }
    }

    private void handleIssuerFailure(int statusCode) {
        failCount++;
        log.warn("⚠️ [HEALTHCHECK] Emissor não respondeu (falha #{}/{}). HTTP={}", failCount, maxFails, statusCode);

        if (failCount >= maxFails) {
            log.error("💥 [HEALTHCHECK] Emissor indisponível — mantendo modo STANDIN_AUTOMATIC.");
            rabbitFactory.publish(RabbitQueueType.STANDIN_REQUESTED, ModeChangeEventType.CHANGE_MODE_STANDIN_AUTOMATIC.name());
        }
        rabbitFactory.publish(RabbitQueueType.STANDIN_REQUESTED, ModeChangeEventType.CHANGE_MODE_STANDIN_REQUEST_ON.name());
    }

}
