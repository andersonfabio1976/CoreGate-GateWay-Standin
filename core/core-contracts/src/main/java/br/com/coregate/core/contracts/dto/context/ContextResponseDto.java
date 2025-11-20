package br.com.coregate.core.contracts.dto.context;

import br.com.coregate.core.contracts.dto.context.CoreGateContextDto;
import lombok.Builder;
import lombok.Data;

/**
 * Request do Ingress enviado ao Context para decodificar a ISO.
 */
@Builder
@Data
public class ContextResponseDto {
    private CoreGateContextDto context;    // composição
    private byte[] rawBytes;               // ISO bruto
    private String hexString;              // ex: ISO-8859-1
    private long receivedAtEpochMs;        // observabilidade
}
