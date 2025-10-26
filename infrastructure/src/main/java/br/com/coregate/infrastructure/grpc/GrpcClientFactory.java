package br.com.coregate.infrastructure.grpc;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.stereotype.Component;

@Component
public class GrpcClientFactory {

    public TransactionProtoServiceGrpc.TransactionProtoServiceBlockingStub createTransactionStub(int serverPort) {
        ManagedChannel channel = ManagedChannelBuilder
                .forAddress("localhost", serverPort)
                .usePlaintext()
                .build();

        return TransactionProtoServiceGrpc.newBlockingStub(channel);
    }
}
