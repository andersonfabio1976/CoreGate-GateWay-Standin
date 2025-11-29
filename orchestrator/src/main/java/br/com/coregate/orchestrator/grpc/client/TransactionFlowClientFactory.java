package br.com.coregate.orchestrator.grpc.client;

import br.com.coregate.core.contracts.TransactionFlowServiceProtoGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.concurrent.TimeUnit;

@Configuration
public class TransactionFlowGrpcConfig {

    @Value("${grpc.finalizer.host:localhost}")
    private String host;

    @Value("${grpc.finalizer.port:8092}")
    private int port;

    @Bean(destroyMethod = "shutdownNow")
    public ManagedChannel finalizerChannel() {
        return ManagedChannelBuilder
                .forAddress(host, port)
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

    @Bean
    public TransactionFlowServiceProtoGrpc.TransactionFlowServiceProtoBlockingStub stub(
            ManagedChannel finalizerChannel
    ) {
        return TransactionFlowServiceProtoGrpc.newBlockingStub(finalizerChannel);
    }
}
