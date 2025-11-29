package br.com.coregate.ingress.grpc.client;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
public class GrpcContextConfig {

    @Value("${grpc.context.host}")
    private String host;

    @Value("${grpc.context.port}")
    private int port;

    @Bean(destroyMethod = "shutdown")
    public ManagedChannel contextChannel() {
        return ManagedChannelBuilder.forAddress(host, port)
                .usePlaintext()
                .keepAliveWithoutCalls(true)
                .keepAliveTime(30, TimeUnit.SECONDS)
                .keepAliveTimeout(10, TimeUnit.SECONDS)
                .build();
    }

}
