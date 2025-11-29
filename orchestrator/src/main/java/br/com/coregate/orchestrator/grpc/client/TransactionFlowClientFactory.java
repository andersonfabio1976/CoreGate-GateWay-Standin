package br.com.coregate.orchestrator.grpc.client;

import br.com.coregate.core.contracts.TransactionFlowServiceProtoGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.stereotype.Component;

@Component
public class TransactionFlowClientFactory {

    public TransactionFlowServiceProtoGrpc.TransactionFlowServiceProtoBlockingStub stub(int serverPort) {
        ManagedChannel channel = ManagedChannelBuilder
                .forAddress("localhost", serverPort)
                .usePlaintext()
                .build();
        return TransactionFlowServiceProtoGrpc.newBlockingStub(channel);
    }
}
