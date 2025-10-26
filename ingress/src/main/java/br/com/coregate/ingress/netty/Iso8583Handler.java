package br.com.coregate.ingress.netty;

import br.com.coregate.ingress.saga.service.IngressContext;
import br.com.coregate.ingress.saga.service.IngressSagaService;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 💾 Handler principal do módulo Ingress.
 * Ele traduz os eventos de baixo nível do Netty (TCP) em steps da SAGA funcional.
 */
@Slf4j
@Component
@ChannelHandler.Sharable
public class Iso8583Handler extends ChannelInboundHandlerAdapter {

    private final IngressSagaService sagaService;

    public Iso8583Handler(IngressSagaService sagaService) {
        this.sagaService = sagaService;
    }

    // ----------------------------------------
    // 🔹 1️⃣ Canal inicializado
    // ----------------------------------------
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) {
        log.debug("🔧 Handler adicionado ao pipeline: {}", ctx.name());
        sagaService.runStep("initChannel", new IngressContext(ctx, null, null, null));
    }

    // ----------------------------------------
    // 🔹 2️⃣ Canal ativo (conexão estabelecida)
    // ----------------------------------------
    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        log.info("🔗 Conexão ativa com {}", ctx.channel().remoteAddress());
        sagaService.runStep("channelActive", new IngressContext(ctx, null, null, null));
    }

    // ----------------------------------------
    // 🔹 3️⃣ Recepção da mensagem ISO8583
    // ----------------------------------------
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        if (!(msg instanceof ByteBuf buf)) {
            log.warn("⚠️ Mensagem recebida não é ByteBuf");
            return;
        }

        byte[] data = new byte[buf.readableBytes()];
        buf.readBytes(data);

        log.debug("📥 Bytes recebidos: {} bytes", data.length);

        IngressContext context =
                new IngressContext(ctx, data, null, null);
        sagaService.runStep("channelRead", context);
    }

    // ----------------------------------------
    // 🔹 4️⃣ Canal inativo (cliente desconectou)
    // ----------------------------------------
    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        log.warn("🔚 Conexão encerrada: {}", ctx.channel().remoteAddress());
        sagaService.runStep("end", new IngressContext(ctx, null, null, null));
    }

    // ----------------------------------------
    // 🔹 5️⃣ Exceções do canal
    // ----------------------------------------
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        log.error("💥 Erro no canal {}: {}", ctx.channel().remoteAddress(), cause.getMessage(), cause);
        sagaService.runError(ctx, cause);
    }
}
