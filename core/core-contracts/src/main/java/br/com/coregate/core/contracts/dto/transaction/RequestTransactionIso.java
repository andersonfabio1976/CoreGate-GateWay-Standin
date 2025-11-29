package br.com.coregate.core.contracts.dto.transaction;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class RequestTransactionIso {
    private String transactionId;
    private byte[] rawBytes;
    private String hexString;
}
