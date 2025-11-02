package br.com.coregate.domain.model;

import br.com.coregate.domain.enums.*;
import br.com.coregate.domain.vo.*;
import lombok.*;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Advice {

    @NotNull private String id;
    @NotNull private String tenantId;
    @NotNull private Transaction transaction;
    @NotNull private AdviceType type; // APPROVAL, REVERSAL, SETTLEMENT
    @NotNull private AdviceStatus status; // PENDING, SENT, FAILED, ACKNOWLEDGED
    @NotNull private LocalDateTime createdAt;
    private LocalDateTime sentAt;
    private LocalDateTime acknowledgedAt;
    private String authorizationCode;
    private String responseCode;
    private String message;

    public static Advice of(Transaction tx, AdviceType type, String message) {
        return Advice.builder()
                .tenantId(tx.getTenantId())
                .id(UUID.randomUUID().toString())
                .transaction(tx)
                .type(type)
                .status(AdviceStatus.PENDING)
                .authorizationCode(tx.getAuthorizationCode())
                .responseCode(tx.getResponseCode())
                .message(message)
                .createdAt(LocalDateTime.now())
                .build();
    }

    public void markSent() {
        this.status = AdviceStatus.SENT;
        this.sentAt = LocalDateTime.now();
    }

    public void acknowledge() {
        this.status = AdviceStatus.ACKNOWLEDGED;
        this.acknowledgedAt = LocalDateTime.now();
    }
}
