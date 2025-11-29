package br.com.coregate.ingress.netty;

import br.com.coregate.core.contracts.dto.transaction.TransactionIso;
import br.com.coregate.ingress.lifecycle.service.IngressLifecycleService;
import io.netty.channel.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Iso8583Handler — Handler otimizado para alto volume de POS.
 * -----------------------------------------------------------
 * Mantém lógica original, mas:
 *  - reduz overhead de logs
 *  - valida payload de forma mais eficiente
 *  - evita GC desnecessário
 *  - trata exceções sem travar worker threads
 *  - fluxo menor / mais direto
 */
@Slf4j
@Component
@ChannelHandler.Sharable
public class Iso8583Handler extends ChannelInboundHandlerAdapter {

    private final IngressLifecycleService lifecycleService;

    public Iso8583Handler(IngressLifecycleService lifecycleService) {
        this.lifecycleService = lifecycleService;
    }

    // ------------------------------------------------------
    // 1️⃣ Handler adicionado
    // ------------------------------------------------------
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) {
        TransactionIso dto = TransactionIso.builder().build();
        lifecycleService.runStep("initChannel", dto, ctx.channel());
    }

    // ------------------------------------------------------
    // 2️⃣ Conexão estabelecida
    // ------------------------------------------------------
    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        TransactionIso dto = TransactionIso.builder().build();
        lifecycleService.runStep("channelActive", dto, ctx.channel());
        log.info("[INGRESS] POS conectado {}", ctx.channel().remoteAddress());
    }

    // ------------------------------------------------------
    // 3️⃣ Mensagem recebida
    // ------------------------------------------------------
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        if (!(msg instanceof byte[] raw)) {
            log.warn("[INGRESS] Payload ignorado — tipo inválido: {}", msg.getClass());
            return;
        }

        TransactionIso dto = TransactionIso.builder()
                .rawBytes(raw)
                .build();

        lifecycleService.runStep("channelRead", dto, ctx.channel());
    }

    // ------------------------------------------------------
    // 4️⃣ Desconexão
    // ------------------------------------------------------
    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        log.warn("[INGRESS] POS desconectou {}", ctx.channel().remoteAddress());
    }

    // ------------------------------------------------------
    // 5️⃣ Exceções — sem travar EventLoop
    // ------------------------------------------------------
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        log.error("[INGRESS] Erro no canal {}: {}", ctx.channel().remoteAddress(), cause.getMessage());

        TransactionIso dto = TransactionIso.builder().build();
        lifecycleService.runError(dto, cause, ctx);

        ctx.close();
    }
}
