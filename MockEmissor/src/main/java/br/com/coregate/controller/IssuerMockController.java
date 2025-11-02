package br.com.coregate.controller;

import br.com.coregate.application.dto.transaction.AuthorizationResult;
import br.com.coregate.application.dto.transaction.TransactionCommand;
import br.com.coregate.domain.enums.TransactionStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.Random;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/v1/issuer")
public class IssuerMockController {

    private static final Random RANDOM = new Random();

    @PostMapping("/authorize")
    public AuthorizationResult authorize(@RequestBody TransactionCommand request) {
        double chance = RANDOM.nextDouble();
        String transactionId = UUID.randomUUID().toString();

        log.info("💳 [MOCK-ISSUER] Recebida transação: merchant={} amount={} pan={}",
                request.merchantId(),
                request.amount(),
                request.pan());

        // 40% aprovado, 40% negado, 20% erro
        if (chance < 0.4) {
            // ✅ Aprovado
            String authCode = generateAuthCode();
            log.info("✅ [MOCK-ISSUER] Transação aprovada [{}]", authCode);
            return AuthorizationResult.builder()
                            .responseCode("00")
                            .timestamp(LocalDateTime.now())
                            .status(TransactionStatus.AUTHORIZED)
                            .build();

        } else if (chance < 0.8) {
            // ❌ Negado
            log.warn("❌ [MOCK-ISSUER] Transação negada [{}]", request.pan());
            return AuthorizationResult.builder()
                    .responseCode("05")
                    .timestamp(LocalDateTime.now())
                    .status(TransactionStatus.REJECTED)
                    .build();

        } else {
            // 💥 Erro (timeout, servidor indisponível, etc.)
            log.error("💥 [MOCK-ISSUER] Falha ao processar transação [{}]", request.pan());
            return AuthorizationResult.builder()
                    .responseCode("91")
                    .timestamp(LocalDateTime.now())
                    .status(TransactionStatus.UNRECOGNIZED)
                    .build();
        }
    }

    private String generateAuthCode() {
        return String.valueOf(100000 + RANDOM.nextInt(900000)); // Ex: 6 dígitos
    }
}
