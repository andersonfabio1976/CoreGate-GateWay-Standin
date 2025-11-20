package br.com.coregate.finalizer.grpc.server;

import io.grpc.BindableService;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class GrpcServerComponent {

    private Server server;

    @Value("${grpc.server.port:9090}")
    private int port;

    public <T extends BindableService> void start(T service, int port) {
        try {
            log.info("🚀 Starting gRPC server on port {} with service {}", port, service.getClass().getSimpleName());

            server = ServerBuilder.forPort(port)
                    .addService(service)
                    .build()
                    .start();

            log.info("✅ gRPC server started successfully on port {}", port);

            // Thread bloqueante para manter o app vivo
            new Thread(() -> {
                try {
                    server.awaitTermination();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    log.error("gRPC server interrupted", e);
                }
            }, "grpc-server-thread").start();

        } catch (Exception e) {
            throw new RuntimeException("❌ Failed to start gRPC Server", e);
        }
    }

    @PreDestroy
    public void stop() {
        if (server != null) {
            log.info("🛑 Stopping gRPC server...");
            server.shutdown();
        }
    }
}
