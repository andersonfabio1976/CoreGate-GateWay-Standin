package br.com.coregate.finalizer.client;

import br.com.coregate.application.dto.transaction.AuthorizationResult;
import br.com.coregate.application.dto.transaction.TransactionCommand;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.RestClientException;

/**
 * 🌐 Client HTTP que simula a comunicação com o emissor real.
 * Neste caso, chama o IssuerMockController no módulo Infrastructure.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class IssuerClient {

    private final RestTemplate restTemplate;

    @Value("${coregate.issuer.url:http://localhost:1110/api/v1/issuer/authorize}")
    private String issuerUrl;

    public AuthorizationResult authorize(TransactionCommand command) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<TransactionCommand> request = new HttpEntity<>(command, headers);

            log.info("📤 Enviando transação ao emissor: {}", issuerUrl);

            AuthorizationResult response = restTemplate
                    .postForObject(issuerUrl, request, AuthorizationResult.class);

            log.info("📥 Resposta do emissor: {}", response);
            return response;

        } catch (RestClientException e) {
            log.error("💥 Falha ao contatar o emissor: {}", e.getMessage(), e);
            return AuthorizationResult.builder()
                    .status(br.com.coregate.domain.enums.TransactionStatus.UNRECOGNIZED)
                    .responseCode("91")
                    .build();
        }
    }
}
