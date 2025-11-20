package br.com.coregate.finalizer.service;

import br.com.coregate.core.contracts.dto.transaction.TransactionCommand;
import br.com.coregate.domain.enums.*;
import br.com.coregate.domain.vo.Pan;
import br.com.coregate.mode.OperationalModeManager;
import br.com.coregate.rabbitmq.factory.RabbitFactory;
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
        OperationalMode current = modeManager.getMode();

        // 🔒 Ignora execução se o modo estiver bloqueado manualmente
        if (current == OperationalMode.STANDIN_REQUESTED) {
            log.debug("⏸️ [HEALTHCHECK] Modo manual ativo — ignorando verificação.");
            return;
        }

        // Só roda se estiver em modo automático
        if (current != OperationalMode.STANDIN) {
            return;
        }

        log.info("🔍 [HEALTHCHECK] Verificando emissor via endpoint /authorize...");

        try {
            // Monta um request dummy leve
            TransactionCommand dummy = TransactionCommand.builder()
                            .tenantId("HEALTHCHECK")
                            .merchantId("HEALTHCHECK-MERCHANT")
                            .amount(BigDecimal.valueOf(1.00))
                            .currency(CurrencyCode.BRL)
                            .brand(CardBrandType.VISA)
                            .channel(ChannelType.ECOMMERCE)
                            .type(TransactionType.TRANSACTION_TYPE_PURCHASE)
                            .pan(Pan.of("4111111111111111"))
                            .build();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<TransactionCommand> entity = new HttpEntity<>(dummy, headers);

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

    /**
     * ✅ Emissor voltou a responder
     */
    private void handleIssuerRestored() {
        if (failCount > 0) {
            log.info("✅ [HEALTHCHECK] Emissor respondeu novamente. Resetando contador.");
            failCount = 0;
        }

        if (modeManager.getMode() == OperationalMode.STANDIN) {
            log.info("🔄 [HEALTHCHECK] Emissor ativo — alternando para modo GATEWAY.");
            modeManager.switchTo(OperationalMode.GATEWAY,"Emissor Ativo");
        } else {
            log.debug("ℹ️ [HEALTHCHECK] Ignorando restauração — modo atual: {}", modeManager.getMode());
        }
    }

    /**
     * ❌ Falha de comunicação com emissor
     */
    private void handleIssuerFailure(int statusCode) {
        failCount++;
        log.warn("⚠️ [HEALTHCHECK] Emissor não respondeu (falha #{}/{}). HTTP={}", failCount, maxFails, statusCode);

        if (failCount >= maxFails) {
            log.error("💥 [HEALTHCHECK] Emissor indisponível — publicando STANDIN_AUTOMATIC.");
            modeManager.switchTo(OperationalMode.STANDIN, "Falha de Comunicação Com Emissor");
        }
    }
}
