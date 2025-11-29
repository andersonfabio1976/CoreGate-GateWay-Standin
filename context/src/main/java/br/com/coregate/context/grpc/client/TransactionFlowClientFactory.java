package br.com.coregate.context.grpc.client;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.concurrent.TimeUnit;

@Configuration
public class TransactionFlowServiceConfig {

    @Value("${grpc.orchestrator.host}")
    private String host;

    @Value("${grpc.orchestrator.port}")
    private int port;

    @Bean(destroyMethod = "shutdown")
    public ManagedChannel orchestratorChannel() {
        return ManagedChannelBuilder.forAddress(host, port)
                .usePlaintext()
                .keepAliveWithoutCalls(true)
                .keepAliveTime(30, TimeUnit.SECONDS)
                .keepAliveTimeout(10, TimeUnit.SECONDS)
                .idleTimeout(5, TimeUnit.MINUTES)
                .maxInboundMessageSize(4 * 1024 * 1024)
                .enableRetry()
                .maxRetryAttempts(3)
                .build();
    }

}
