package br.com.coregate.ingress.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBufAllocator;
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
 * NettyServer (Optimized)
 * -----------------------
 * 🔥 Alta performance para POS real e adquirentes:
 * - TCP_NODELAY
 * - SO_REUSEADDR
 * - backlog aumentado
 * - write watermark para evitar congestionamento
 * - EventLoop dimensionado automaticamente
 *
 * Sem qualquer mudança na lógica do Ingress.
 */
@Slf4j
@Component
public class NettyServer {

    @Value("${coregate.ingress.port:8583}")
    private int port;

    // ========================================================================
    // EventLoopGroup tuning: boss=1, workers=CPU*2  → recomendado para IO-bound
    // ========================================================================
    private final EventLoopGroup bossGroup = new NioEventLoopGroup(1);
    private final EventLoopGroup workerGroup =
            new NioEventLoopGroup(Math.max(4, Runtime.getRuntime().availableProcessors() * 2));

    private ChannelFuture serverChannel;
    private final Iso8583Handler iso8583Handler;

    public NettyServer(Iso8583Handler iso8583Handler) {
        this.iso8583Handler = iso8583Handler;
    }

    @PostConstruct
    public void start() throws InterruptedException {
        log.info("🟢 Iniciando servidor Netty otimizado na porta {}...", port);

        try {
            ServerBootstrap bootstrap = new ServerBootstrap();

            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)

                    // ========================================================
                    // Global Tuning de baixo nível TCP
                    // ========================================================
                    .option(ChannelOption.SO_REUSEADDR, true)
                    .option(ChannelOption.SO_BACKLOG, 512)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)

                    // ========================================================
                    // WriteBuffer otimizado (evita travamento)
                    // ========================================================
                    .childOption(ChannelOption.WRITE_BUFFER_WATER_MARK,
                            new WriteBufferWaterMark(32 * 1024, 64 * 1024)) // low/high

                    // ========================================================
                    // Alocador rápido (PooledByteBuf) → menor GC
                    // ========================================================
                    .childOption(ChannelOption.ALLOCATOR, ByteBufAllocator.DEFAULT)

                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) {
                            ChannelPipeline p = ch.pipeline();

                            // 1️⃣ Frame decoder
                            p.addLast(new LengthFieldBasedFrameDecoder(
                                    4096,
                                    0,
                                    2,
                                    0,
                                    2
                            ));

                            // 2️⃣ Convert ByteBuf → byte[]
                            p.addLast(new ByteArrayDecoder());

                            // 3️⃣ Seu handler ISO8583
                            p.addLast(iso8583Handler);
                        }
                    });

            // ========================================================
            // Bind sem bloqueio do EventLoop (mas espera subir)
            // ========================================================
            serverChannel = bootstrap.bind(port).sync();

            log.info("✅ Servidor Netty otimizado iniciado na porta {}", port);

        } catch (Exception e) {
            log.error("❌ Falha ao iniciar Netty Ingress", e);
            throw e;
        }
    }

    @PreDestroy
    public void stop() {
        log.info("🛑 Encerrando Netty...");
        try {
            if (serverChannel != null) {
                serverChannel.channel().close();
            }
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
        log.info("✅ Netty finalizado com sucesso.");
    }
}
