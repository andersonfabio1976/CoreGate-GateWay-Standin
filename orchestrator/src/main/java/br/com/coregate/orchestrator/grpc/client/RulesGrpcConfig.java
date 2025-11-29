package br.com.coregate.orchestrator.grpc.client;

import br.com.coregate.core.contracts.RulesProtoServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RulesGrpcConfig {

    @Value("${grpc.rules.host:localhost}")
    private String host;

    @Value("${grpc.rules.port:8093}")
    private int port;

    @Bean(destroyMethod = "shutdownNow")
    public ManagedChannel rulesChannel() {
        return ManagedChannelBuilder
                .forAddress(host, port)
                .usePlaintext()
                .build();
    }

    @Bean
    public RulesProtoServiceGrpc.RulesProtoServiceBlockingStub rulesBlockingStub(
            ManagedChannel rulesChannel
    ) {
        return RulesProtoServiceGrpc.newBlockingStub(rulesChannel);
    }
}
