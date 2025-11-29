package br.com.coregate.core.contracts.dto.transaction;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ResponseTransactionIso {
    private String transactionId;
    private byte[] rawBytes;
    private String hexString;
}
