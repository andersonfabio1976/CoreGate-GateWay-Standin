package br.com.coregate.infrastructure.grpc;

import org.springframework.stereotype.Service;

@Service
public class TransactionClientServiceGrpc {

    private final GrpcClientFactory grpcClientFactory;

    public TransactionClientServiceGrpc(GrpcClientFactory grpcClientFactory) {
        this.grpcClientFactory = grpcClientFactory;
    }

    public TransactionCommandProtoResponse callGrpc(TransactionCommandProtoRequest request, int port) {
        var stub = grpcClientFactory.createTransactionStub(port);
        var response = stub.process(request); // process pode ser authorize ou settle
        System.out.println("Connected Via Grpc Request: " + request);
        return response;
    }
}
