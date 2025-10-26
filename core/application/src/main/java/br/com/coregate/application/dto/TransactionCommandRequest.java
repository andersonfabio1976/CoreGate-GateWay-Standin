package br.com.coregate.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TransactionCommandRequest {
    private TransactionCommand transactionCommand;
}
