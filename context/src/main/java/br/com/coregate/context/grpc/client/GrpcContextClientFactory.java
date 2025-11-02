package br.com.coregate.context.grpc.client;

import br.com.coregate.proto.ingress.ContextProtoServiceGrpc;
import br.com.coregate.proto.Orquestrator.OrquestratorProtoServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.stereotype.Component;

@Component
public class GrpcContextClientFactory {

    public OrquestratorProtoServiceGrpc.OrquestratorProtoServiceBlockingStub createOrquestratorStub(int serverPort) {
        ManagedChannel channel = ManagedChannelBuilder
                .forAddress("localhost", serverPort)
                .usePlaintext()
                .build();
        return OrquestratorProtoServiceGrpc.newBlockingStub(channel);
    }
}

