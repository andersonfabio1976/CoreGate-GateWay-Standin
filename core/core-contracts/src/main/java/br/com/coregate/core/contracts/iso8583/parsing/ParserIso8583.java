package br.com.coregate.core.contracts.iso8583.parsing;

import br.com.coregate.core.contracts.dto.transaction.AuthorizationResult;
import br.com.coregate.core.contracts.dto.transaction.TransactionCommand;
import br.com.coregate.core.contracts.iso8583.mapper.IsoToTransactionMapper;
import br.com.coregate.core.contracts.iso8583.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Slf4j
@Component
public class ParserIso8583 {

    private final IsoToTransactionMapper isoMapper;

    public ParserIso8583(IsoToTransactionMapper isoMapper) {
        this.isoMapper = isoMapper;
    }

    // ===============================
    // 🔽 Decode: ISO → Transaction
    // ===============================
    public TransactionCommand decode(byte[] rawBytes) {
        log.info("🧩 DecodeIsoStep - Decodificando mensagem ISO8583 {}", rawBytes);

        try {
            if (rawBytes == null)
                throw new IllegalStateException("rawBytes message is null.");

            String isoMessage = new String(rawBytes, StandardCharsets.ISO_8859_1);
            String mti = isoMessage.substring(0, 4).trim();
            log.info("🧠 MTI identificado: {}", mti);

            return switch (mti) {
                case "0200" -> isoMapper.fromFinancialRequest(FinancialRequestIsoModelMapper.decode(isoMessage));
                case "0210" -> isoMapper.fromFinancialResponse(FinancialResponseIsoModelMapper.decode(isoMessage));
                case "0400" -> isoMapper.fromReversalRequest(ReversalRequestIsoModelMapper.decode(isoMessage));
                case "0410" -> isoMapper.fromReversalResponse(ReversalResponseIsoModelMapper.decode(isoMessage));
                case "0100" -> isoMapper.fromAuthorizationRequest(AuthorizationRequestIsoModelMapper.decode(isoMessage));
                case "0110" -> isoMapper.fromAuthorizationResponse(AuthorizationResponseIsoModelMapper.decode(isoMessage));
                default -> throw new IllegalArgumentException("MTI não suportado: " + mti);
            };

        } catch (Exception e) {
            log.error("❌ Erro ao decodificar mensagem ISO8583: {}", e.getMessage(), e);
            throw new RuntimeException("Falha no DecodeIsoStep", e);
        }
    }

    // ===============================
    // 🔼 Encode: Transaction → ISO
    // ===============================
    public byte[] encode(TransactionCommand command, AuthorizationResult result) {
        log.info("🧩 EncodeIsoStep - Regerando ISO8583 para MTI {}", result.mti());

        try {
            String isoMessage = switch (result.mti()) {
                case "0200" -> FinancialRequestIsoModelMapper.encode(isoMapper.toFinancialRequest(command, result));
                case "0210" -> FinancialResponseIsoModelMapper.encode(isoMapper.toFinancialResponse(command, result));
                case "0400" -> ReversalRequestIsoModelMapper.encode(isoMapper.toReversalRequest(command, result));
                case "0410" -> ReversalResponseIsoModelMapper.encode(isoMapper.toReversalResponse(command, result));
                case "0100" -> AuthorizationRequestIsoModelMapper.encode(isoMapper.toAuthorizationRequest(command, result));
                case "0110" -> AuthorizationResponseIsoModelMapper.encode(isoMapper.toAuthorizationResponse(command, result));
                default -> throw new IllegalArgumentException("MTI não suportado: " + result.mti());
            };

            log.info("✅ ISO8583 gerada: {}", isoMessage);
            return isoMessage.getBytes(StandardCharsets.ISO_8859_1);

        } catch (Exception e) {
            log.error("❌ Erro ao gerar mensagem ISO8583: {}", e.getMessage(), e);
            throw new RuntimeException("Falha no EncodeIsoStep", e);
        }
    }
}
