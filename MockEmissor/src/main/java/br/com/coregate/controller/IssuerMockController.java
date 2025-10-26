package br.com.coregate.controller;

import br.com.coregate.application.dto.TransactionCommandRequest;
import br.com.coregate.infrastructure.grpc.TransactionCommandProtoResponse;
import br.com.coregate.infrastructure.grpc.TransactionStatus;
import com.google.protobuf.util.Timestamps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import java.util.Random;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/v1/issuer")
public class IssuerMockController {

    private static final Random RANDOM = new Random();

    @PostMapping("/authorize")
    public TransactionCommandProtoResponse authorize(@RequestBody TransactionCommandRequest request) {
        double chance = RANDOM.nextDouble();
        String transactionId = UUID.randomUUID().toString();

        log.info("💳 [MOCK-ISSUER] Recebida transação: merchant={} amount={} pan={}",
                request.getTransactionCommand().merchantId(),
                request.getTransactionCommand().amount(),
                request.getTransactionCommand().pan());

        // 40% aprovado, 40% negado, 20% erro
        if (chance < 0.4) {
            // ✅ Aprovado
            String authCode = generateAuthCode();
            log.info("✅ [MOCK-ISSUER] Transação aprovada [{}]", authCode);
            return TransactionCommandProtoResponse.newBuilder()
                    .setTransactionId(transactionId)
                    .setAuthorizationCode(authCode)
                    .setResponseCode("00")
                    .setStatus(TransactionStatus.AUTHORIZED)
                    .setTimestamp(Timestamps.fromMillis(System.currentTimeMillis()))
                    .build();

        } else if (chance < 0.8) {
            // ❌ Negado
            log.warn("❌ [MOCK-ISSUER] Transação negada [{}]", request.getTransactionCommand().pan());
            return TransactionCommandProtoResponse.newBuilder()
                    .setTransactionId(transactionId)
                    .setAuthorizationCode("")
                    .setResponseCode("05")
                    .setStatus(TransactionStatus.REJECTED)
                    .setTimestamp(Timestamps.fromMillis(System.currentTimeMillis()))
                    .build();

        } else {
            // 💥 Erro (timeout, servidor indisponível, etc.)
            log.error("💥 [MOCK-ISSUER] Falha ao processar transação [{}]", request.getTransactionCommand().pan());
            return TransactionCommandProtoResponse.newBuilder()
                    .setTransactionId(transactionId)
                    .setAuthorizationCode("")
                    .setResponseCode("91")
                    .setStatus(TransactionStatus.UNRECOGNIZED)
                    .setTimestamp(Timestamps.fromMillis(System.currentTimeMillis()))
                    .build();
        }
    }

    private String generateAuthCode() {
        return String.valueOf(100000 + RANDOM.nextInt(900000)); // Ex: 6 dígitos
    }
}
