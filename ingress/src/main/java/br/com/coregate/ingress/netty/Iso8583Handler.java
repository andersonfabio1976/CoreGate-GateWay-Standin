package br.com.coregate.ingress.netty;

import br.com.coregate.application.dto.context.ContextRequestDto;
import br.com.coregate.application.dto.common.CoreGateContextDto;
import br.com.coregate.ingress.saga.service.IngressSagaService;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import java.util.UUID;

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
    public void handlerAdded(ChannelHandlerContext channel) {
        log.debug("🔧 Handler adicionado ao pipeline: {}", channel.name());

        CoreGateContextDto contextChannel = CoreGateContextDto.builder()
                        .tenantId("coregate")
                        .channel(channel)
                        .build();
        ContextRequestDto contextRequestDto = ContextRequestDto.builder()
                        .context(contextChannel)
                        .build();

        sagaService.runStep("initChannel", contextRequestDto);
    }

    // ----------------------------------------
    // 🔹 2️⃣ Canal ativo (conexão estabelecida)
    // ----------------------------------------
    @Override
    public void channelActive(ChannelHandlerContext channel) {
        log.info("🔗 Conexão ativa com {}", channel.channel().remoteAddress());
        CoreGateContextDto contextChannel = CoreGateContextDto.builder()
                .tenantId("coregate")
                .channel(channel)
                .build();
        ContextRequestDto contextRequestDto = ContextRequestDto.builder()
                .context(contextChannel)
                .build();
        sagaService.runStep("channelActive", contextRequestDto);
    }

    // ----------------------------------------
    // 🔹 3️⃣ Recepção da mensagem ISO8583
    // ----------------------------------------
    @Override
    public void channelRead(ChannelHandlerContext channel, Object msg) {
        if (!(msg instanceof ByteBuf buf)) {
            log.warn("⚠️ Mensagem recebida não é ByteBuf");
            return;
        }

        byte[] data = new byte[buf.readableBytes()];
        buf.readBytes(data);

        log.debug("📥 Bytes recebidos: {} bytes", data.length);
        CoreGateContextDto contextChannel = CoreGateContextDto.builder()
                .tenantId("coregate")
                .channel(channel)
                .traceId(UUID.randomUUID().toString())
                .build();
        ContextRequestDto contextRequestDto = ContextRequestDto.builder()
                .context(contextChannel)
                .rawBytes(data)
                .build();
        sagaService.runStep("channelRead", contextRequestDto);
    }

    // ----------------------------------------
    // 🔹 4️⃣ Canal inativo (cliente desconectou)
    // ----------------------------------------
    @Override
    public void channelInactive(ChannelHandlerContext channel) {
        log.warn("🔚 Conexão encerrada: {}", channel.channel().remoteAddress());
        CoreGateContextDto contextChannel = CoreGateContextDto.builder()
                .tenantId("coregate")
                .channel(channel)
                .build();
        ContextRequestDto contextRequestDto = ContextRequestDto.builder()
                .context(contextChannel)
                .build();
        sagaService.runStep("end", contextRequestDto);
    }

    // ----------------------------------------
    // 🔹 5️⃣ Exceções do canal
    // ----------------------------------------
    @Override
    public void exceptionCaught(ChannelHandlerContext channel, Throwable cause) {
        log.error("💥 Erro no canal {}: {}", channel.channel().remoteAddress(), cause.getMessage(), cause);
        CoreGateContextDto contextChannel = CoreGateContextDto.builder()
                .tenantId("coregate")
                .channel(channel)
                .build();
        ContextRequestDto contextRequestDto = ContextRequestDto.builder()
                .context(contextChannel)
                .build();
        sagaService.runError(contextRequestDto, cause);
    }
}
