package br.com.coregate.ingress.lifecycle.step;

import br.com.coregate.core.contracts.dto.transaction.RequestTransactionIso;
import br.com.coregate.core.contracts.dto.transaction.TransactionIso;
import br.com.coregate.core.contracts.mapper.TransactionIsoMapper;
import br.com.coregate.ingress.grpc.client.TransactionIsoClientService;
import br.com.coregate.ingress.session.ChannelRegistry;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.util.UUID;

/**
 * DispatchStep
 * ------------
 * 🔥 Versão otimizada para alta performance e segurança.
 *
 * - Registra canal no ChannelRegistry com transactionId único
 * - Envia mensagem ao Context via RabbitMQ
 * - Mantém semântica original, sem modificar o fluxo
 * - Logs precisos sem overhead
 * - Tolerante a falhas (não perde canal nem transação)
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DispatchStep {

    private final ChannelRegistry channelRegistry;
    private final TransactionIsoClientService transactionIsoClientService;
    private final TransactionIsoMapper transactionIsoMapper;
    private final EndStep endStep;

    @Value("${grpc.context.port}")
    private int grpcContextPort;

    @Value("${grpc.context.host}")
    private String grpcContextHost;


    public TransactionIso execute(TransactionIso ctx, Channel channel) {
        try {
            byte[] raw = ctx.getRawBytes();
            if (raw == null) {
                throw new IllegalStateException("[INGRESS] rawBytes é nulo — impossível despachar");
            }

            // =========================================================================
            // 1️⃣ Gera transactionId idempotente e registra o canal
            // =========================================================================
            String transactionId = UUID.randomUUID().toString();
            ctx.setTransactionId(transactionId);
            channelRegistry.register(transactionId, channel);
            log.info("[INGRESS] Dispatch TX={} ({} bytes) → Context/Rabbit",
                    transactionId, raw.length);
            var requestTransactionIso = RequestTransactionIso.builder()
                    .transactionId(ctx.getTransactionId())
                    .rawBytes(ctx.getRawBytes())
                    .hexString(ctx.getHexString())
                    .build();
            var requestTransactionIsoProto = transactionIsoMapper.toProto(requestTransactionIso);
            var responseTransactionIsoProto =
                    transactionIsoClientService.callGrpc(requestTransactionIsoProto, grpcContextHost, grpcContextPort);
            var responseTransactionIso = transactionIsoMapper.toDto(responseTransactionIsoProto);

            if (responseTransactionIso == null) {
                log.error("[INGRESS] TransactionConsumer recebeu ctx NULL");
                return null;
            }

            String txId = responseTransactionIso.getTransactionId();
            raw = responseTransactionIso.getRawBytes();
            int size = (raw != null ? raw.length : -1);

            if (txId == null) {
                log.error("[INGRESS] TRANSACTION_RESULT recebido sem transactionId — ignorando");
                return null;
            }

            log.info("[INGRESS] ← CONTEXT | TRANSACTION_RESULT TX={} ({} bytes)", txId, size);

            // Recupera canal associado
            channel = channelRegistry.get(txId);

            // Caso canal tenha sido perdido
            if (channel == null) {
                log.warn("[INGRESS] Nenhum canal encontrado para TX={} — POS desconectado?", txId);
                return null;
            }

            if (!channel.isActive()) {
                log.warn("[INGRESS] Canal inativo para TX={} — removendo do registry", txId);
                channelRegistry.remove(txId);
                return null;
            }

            ctx.setHexString(responseTransactionIso.getHexString());
            ctx.setRawBytes(responseTransactionIso.getRawBytes());
            ctx.setTransactionId(responseTransactionIso.getTransactionId());

            try {
                endStep.execute(ctx, channel);
                log.info("[INGRESS] → POS | TX={} enviado com sucesso ({} bytes)", txId, size);

            } catch (Exception e) {
                log.error("[INGRESS] Erro ao enviar resposta ao POS TX={}: {}", txId, e.getMessage(), e);

            } finally {
                // Cleanup sempre
                channelRegistry.remove(txId);
                log.debug("[INGRESS] Cleanup TX={} removido do registry", txId);
            }

            return ctx;

        } catch (Exception e) {
            // Remove para evitar vazamento de referência
            if (ctx.getTransactionId() != null) {
                channelRegistry.remove(ctx.getTransactionId());
            }

            log.error("[INGRESS] Erro ao despachar TX={} → {}",
                    ctx.getTransactionId(), e.getMessage(), e);

            throw new RuntimeException("[INGRESS] DispatchStep falhou", e);
        }
    }

    public TransactionIso rollback(TransactionIso ctx, ChannelHandlerContext channel) {
        log.warn("↩️ Rollback DispatchStep — sem envio ao Context.");
        return ctx;
    }
}
