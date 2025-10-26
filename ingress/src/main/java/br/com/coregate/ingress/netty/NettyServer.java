package br.com.coregate.ingress.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.bytes.ByteArrayDecoder;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 🧩 Responsável por subir o servidor TCP do Ingress e
 * gerenciar o ciclo de vida da conexão com POS/adquirente.
 */
@Slf4j
@Component
public class NettyServer {

    @Value("${coregate.ingress.port:8583}")
    private int port;

    private final EventLoopGroup bossGroup = new NioEventLoopGroup(1);
    private final EventLoopGroup workerGroup = new NioEventLoopGroup();
    private ChannelFuture serverChannel;

    private final Iso8583Handler iso8583Handler;

    public NettyServer(Iso8583Handler iso8583Handler) {
        this.iso8583Handler = iso8583Handler;
    }

    /**
     * 🚀 Inicializa o servidor na subida do Spring Boot.
     */
    @PostConstruct
    public void start() throws InterruptedException {
        log.info("🟢 Iniciando servidor Netty na porta {}...", port);

        try {
            ServerBootstrap bootstrap = new ServerBootstrap();

            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) {
                            ChannelPipeline p = ch.pipeline();

                            // 1️⃣ Frame decoder (2 bytes = tamanho da mensagem)
                            p.addLast(new LengthFieldBasedFrameDecoder(
                                    4096, // tamanho máximo da mensagem ISO
                                    0,    // offset do campo de tamanho
                                    2,    // tamanho do campo (2 bytes)
                                    0,    // ajuste
                                    2     // remove os 2 bytes do tamanho no payload
                            ));

                            // 2️⃣ Byte decoder → converte ByteBuf para byte[]
                            p.addLast(new ByteArrayDecoder());

                            // 3️⃣ Handler da sua SAGA ISO8583
                            p.addLast(iso8583Handler);
                        }
                    })
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);

            // ⏳ Bind na porta e bloqueia até subir
            serverChannel = bootstrap.bind(port).sync();

            log.info("✅ Servidor Netty iniciado e escutando na porta {}", port);

        } catch (Exception e) {
            log.error("❌ Erro ao iniciar servidor Netty", e);
            throw e;
        }
    }

    /**
     * 🛑 Finaliza todas as conexões graciosamente.
     */
    @PreDestroy
    public void stop() {
        log.info("🛑 Encerrando servidor Netty...");
        try {
            if (serverChannel != null) serverChannel.channel().close();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
        log.info("✅ Servidor Netty finalizado com sucesso");
    }
}
