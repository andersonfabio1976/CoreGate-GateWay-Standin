package br.com.coregate.ingress.saga.step;

import br.com.coregate.application.dto.TransactionCommand;
import br.com.coregate.ingress.iso8583.*;
import br.com.coregate.ingress.mapper.IsoToTransactionMapper;
import br.com.coregate.ingress.saga.service.IngressContext;
import br.com.coregate.ingress.saga.service.IngressSagaEventPublisher;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;

@Slf4j
public class DecodeIsoStep {

    private static final IngressSagaEventPublisher publisher = new IngressSagaEventPublisher();
    private static final IsoToTransactionMapper isoMapper = new IsoToTransactionMapper();

    public static IngressContext execute(IngressContext ctx) {
        log.info("🧩 DecodeIsoStep - Decodificando mensagem ISO8583...");
        publisher.publishStart("decodeIso", ctx);

        try {
            if (ctx.getRawBytes() == null)
                throw new IllegalStateException("Payload ausente no contexto.");

            String isoMessage = new String(ctx.getRawBytes(), StandardCharsets.ISO_8859_1);
            String mti = isoMessage.substring(0, 4).trim();
            log.info("🧠 MTI identificado: {}", mti);

            TransactionCommand command = switch (mti) {
                case "0200" -> isoMapper.fromFinancialRequest(FinancialRequestIsoModelMapper.decode(isoMessage));
                case "0210" -> isoMapper.fromFinancialResponse(FinancialResponseIsoModelMapper.decode(isoMessage));
                case "0400" -> isoMapper.fromReversalRequest(ReversalRequestIsoModelMapper.decode(isoMessage));
                case "0410" -> isoMapper.fromReversalResponse(ReversalResponseIsoModelMapper.decode(isoMessage));
                case "0100" -> isoMapper.fromAuthorizationRequest(AuthorizationRequestIsoModelMapper.decode(isoMessage));
                case "0110" -> isoMapper.fromAuthorizationResponse(AuthorizationResponseIsoModelMapper.decode(isoMessage));
                default -> throw new IllegalArgumentException("MTI não suportado: " + mti);
            };

            ctx.setTransactionCommand(command);
            log.info("✅ TransactionCommand definido no contexto: {}", command);
            publisher.publishStepSuccess("decodeIso", ctx);

            return ctx;

        } catch (Exception e) {
            log.error("❌ Erro ao decodificar mensagem ISO8583: {}", e.getMessage(), e);
            ctx.setTransactionCommand(null);
            publisher.publishStepFailure("decodeIso", ctx, e);
            throw new RuntimeException("Falha no DecodeIsoStep", e);
        }
    }

    public static IngressContext rollback(IngressContext ctx) {
        log.warn("↩️ Rollback DecodeIsoStep - Limpando TransactionCommand do contexto...");
        ctx.setTransactionCommand(null);
        publisher.publishCompensation("decodeIso", ctx);
        return ctx;
    }
}
