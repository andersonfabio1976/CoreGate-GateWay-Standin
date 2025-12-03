package br.com.coregate.context.grpc.client;

import br.com.coregate.core.contracts.TransactionFlowServiceProtoGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.stereotype.Component;

@Component
public class TransactionFlowClientFactory {

    public TransactionFlowServiceProtoGrpc.TransactionFlowServiceProtoBlockingStub stub(String host, int serverPort) {
        ManagedChannel channel = ManagedChannelBuilder
                .forAddress(host, serverPort)
                .usePlaintext()
                .build();
        return TransactionFlowServiceProtoGrpc.newBlockingStub(channel);
    }
}
