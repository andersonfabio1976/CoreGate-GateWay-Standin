package br.com.coregate.finalizer.grpc.server.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "grpc.server")
public class GrpcServerProperties {

    /**
     * Porta que este módulo irá expor via gRPC.
     */
    private int port = 9000;

    /**
     * Quantas threads o servidor gRPC pode usar.
     */
    private int threads = 64;
}
