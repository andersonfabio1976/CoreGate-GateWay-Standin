package br.com.coregate.domain.model;

import lombok.*;

@Value
@AllArgsConstructor
@Builder
public class TransactionApproval {
    String authorizationCode;
    String responseCode;

    public static TransactionApproval of(String authorizationCode, String responseCode) {
        return new TransactionApproval(authorizationCode.trim(), responseCode.trim());
    }
}
