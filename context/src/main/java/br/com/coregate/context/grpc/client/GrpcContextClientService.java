package br.com.coregate.context.grpc.client;

import br.com.coregate.proto.Orquestrator.OrquestratorRequestProto;
import br.com.coregate.proto.Orquestrator.OrquestratorResponseProto;
import org.springframework.stereotype.Service;

@Service
public class GrpcContextClientService {

    private final GrpcContextClientFactory grpcContextFactory;

    public GrpcContextClientService(GrpcContextClientFactory grpcContextFactory) {
        this.grpcContextFactory = grpcContextFactory;
    }

    public OrquestratorResponseProto callGrpc(OrquestratorRequestProto request, int port) {
        var stub = grpcContextFactory.createOrquestratorStub(port);
        var response = stub.orquestrateTransaction(request); // process pode ser authorize ou settle
        System.out.println("Connected Via Grpc Request: " + request);
        return response;
    }
}
