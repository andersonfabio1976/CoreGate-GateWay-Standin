package br.com.coregate.orchestrator.grpc.client;

import br.com.coregate.proto.finalizer.FinalizerRequestProto;
import br.com.coregate.proto.finalizer.FinalizerResponseProto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class GrpcFinalizerClientService {

    private final GrpcFinalizerClientFactory grpcFinalizerClientFactory;

    public FinalizerResponseProto callGrpc(FinalizerRequestProto request, int port) {
        var stub = grpcFinalizerClientFactory.createFinalizertStub(port);
        var response = stub.authorize(request);
        System.out.println("Connected Via Grpc Request: " + request);
        return response;
    }

}
