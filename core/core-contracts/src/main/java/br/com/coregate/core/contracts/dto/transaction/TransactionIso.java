package br.com.coregate.core.contracts.dto.transaction;

import lombok.Builder;
import lombok.Data;

/**
 * Request do Ingress enviado ao Context para decodificar a ISO.
 */
@Builder
@Data
public class TransactionIso {
    private String transactionId;
    private byte[] rawBytes;
    private String hexString;
}
