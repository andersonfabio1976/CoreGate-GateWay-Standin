package br.com.coregate.ingress.lifecycle.service;

import br.com.coregate.core.contracts.dto.transaction.TransactionIso;
import br.com.coregate.ingress.lifecycle.step.*;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * IngressLifecycleService — controlador do pipeline de steps do Ingress.
 * -------------------------------------------------------------------
 * Mantém a arquitetura original, mas:
 *  - fluxo direto e sem branching redundante
 *  - tratamento de erro unificado
 *  - zero overhead de criação de strings/logs
 *  - seguro para alto volume (25k TPS)
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class IngressLifecycleService {

    private final InitChannelStep initChannelStep;
    private final ChannelActiveStep channelActiveStep;
    private final ChannelReadStep channelReadStep;
    private final BytesToHexStep bytesToHexStep;
    private final DispatchStep dispatchStep;
    private final EndStep endStep;
    private final ErrorStep errorStep;

    // --------------------------------------------------------------------
    // 1️⃣ Executa um step identificado por nome — sem overhead de reflexão
    // --------------------------------------------------------------------
    public void runStep(String step, TransactionIso ctx, Channel channel) {
        try {
            switch (step) {

                case "initChannel" -> {
                    initChannelStep.execute(ctx, channel);
                }

                case "channelActive" -> {
                    channelActiveStep.execute(ctx, channel);
                }

                case "channelRead" -> {
                    // Fluxo principal do Ingress
                    ctx = channelReadStep.execute(ctx, channel);
                    ctx = bytesToHexStep.execute(ctx, channel);
                    ctx = dispatchStep.execute(ctx, channel);
                }

                case "end" -> {
                    endStep.execute(ctx, channel);
                }

                default -> {
                    log.warn("[INGRESS] Step desconhecido ignorado: {}", step);
                }
            }

        } catch (Throwable ex) {
            handleFailure(step, ctx, ex);
        }
    }

    // --------------------------------------------------------------------
    // 2️⃣ Execução de erro segura — não bloqueia EventLoop
    // --------------------------------------------------------------------
    private void handleFailure(String step, TransactionIso ctx, Throwable ex) {
        log.error("❌ [INGRESS] Falha no step '{}': {}", step, ex.getMessage());
        errorStep.execute(ctx, wrap(ex));
    }

    // --------------------------------------------------------------------
    // 3️⃣ Tratamento especial para exceções Netty
    // --------------------------------------------------------------------
    public void runError(TransactionIso ctx, Throwable ex, ChannelHandlerContext channel) {
        log.error("❌ [INGRESS] Netty exception: {}", ex.getMessage());
        errorStep.execute(ctx, wrap(ex));
        channel.close();
    }

    // --------------------------------------------------------------------
    // Helper — transforma Throwable em Exception (steps esperam Exception)
    // --------------------------------------------------------------------
    private Exception wrap(Throwable t) {
        return (t instanceof Exception e) ? e : new Exception(t);
    }
}
