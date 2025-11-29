package br.com.coregate.ingress.grpc.client;

import br.com.coregate.core.contracts.TransactionIsoServiceProtoGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.stereotype.Component;

@Component
public class TransactionIsoClientFactory {

    public TransactionIsoServiceProtoGrpc.TransactionIsoServiceProtoBlockingStub stub(int serverPort) {
        ManagedChannel channel = ManagedChannelBuilder
                .forAddress("localhost", serverPort)
                .usePlaintext()
                .build();
        return TransactionIsoServiceProtoGrpc.newBlockingStub(channel);
    }
}
