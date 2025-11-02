package br.com.coregate.ingress.grpc;

import br.com.coregate.proto.ingress.ContextRequestProto;
import br.com.coregate.proto.ingress.ContextResponseProto;
import org.springframework.stereotype.Service;

@Service
public class GrpcIngressClientService {

    private final GrpcIngressClientFactory grpcContextClientFactory;

    public GrpcIngressClientService(GrpcIngressClientFactory grpcContextClientFactory) {
        this.grpcContextClientFactory = grpcContextClientFactory;
    }

    public ContextResponseProto callGrpc(ContextRequestProto request, int port) {
        var stub = grpcContextClientFactory.createContextStub(port);
        var response = stub.authorize(request);
        System.out.println("Connected Via Grpc Request: " + request);
        return response;
    }
}
