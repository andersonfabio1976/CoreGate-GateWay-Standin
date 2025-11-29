package br.com.coregate.context.grpc.server;

import io.grpc.BindableService;
import io.grpc.Server;
import io.grpc.netty.NettyServerBuilder;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class GrpcServerComponent {

    private Server server;

    private final EventLoopGroup bossGroup = new NioEventLoopGroup(1);
    private final EventLoopGroup workerGroup = new NioEventLoopGroup();

    @Value("${grpc.server.port:9090}")
    private int port;

    public <T extends BindableService> void start(T service, int port) {
        try {
            log.info("🚀 Starting gRPC server on port {}", port);

            server = NettyServerBuilder.forPort(port)
                    .bossEventLoopGroup(bossGroup)
                    .workerEventLoopGroup(workerGroup)
                    .channelType(NioServerSocketChannel.class) // ← OBRIGATÓRIO
                    .addService(service)
                    .maxInboundMessageSize(16 * 1024 * 1024)
                    .permitKeepAliveWithoutCalls(true)
                    .permitKeepAliveTime(1, TimeUnit.SECONDS)
                    .keepAliveTime(60, TimeUnit.SECONDS)
                    .keepAliveTimeout(20, TimeUnit.SECONDS)
                    .build()
                    .start();

            log.info("✅ gRPC server started on port {}", port);

            new Thread(() -> {
                try {
                    server.awaitTermination();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }, "grpc-server-thread").start();

        } catch (Exception e) {
            throw new RuntimeException("❌ Failed to start gRPC Server", e);
        }
    }

    @PreDestroy
    public void stop() {
        try {
            if (server != null) {
                log.info("🛑 Stopping gRPC server...");
                server.shutdown();
            }
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
