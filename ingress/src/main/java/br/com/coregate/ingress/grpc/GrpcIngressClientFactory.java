package br.com.coregate.ingress.grpc;

import br.com.coregate.proto.ingress.ContextProtoServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.stereotype.Component;

@Component
public class GrpcIngressClientFactory {

    public ContextProtoServiceGrpc.ContextProtoServiceBlockingStub createContextStub(int serverPort) {
        ManagedChannel channel = ManagedChannelBuilder
                .forAddress("localhost", serverPort)
                .usePlaintext()
                .build();
        return ContextProtoServiceGrpc.newBlockingStub(channel);
    }
}

