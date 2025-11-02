package br.com.coregate.application.dto.transaction;

import br.com.coregate.domain.enums.TransactionStatus;
import lombok.Builder;
import java.time.LocalDateTime;

@Builder
public record AuthorizationResult(
        String transactionId,
        TransactionStatus status,
        String authorizationCode,
        String responseCode,
        LocalDateTime timestamp,
        String mti
) {}
