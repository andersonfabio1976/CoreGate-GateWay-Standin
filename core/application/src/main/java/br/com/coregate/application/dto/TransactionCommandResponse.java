package br.com.coregate.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class TransactionCommandResponse {
    private AuthorizationResult authorizationResult;
}
