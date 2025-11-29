package br.com.coregate.ingress.grpc.client;

import br.com.coregate.proto.ingress.ContextProtoServiceGrpc;
import br.com.coregate.proto.ingress.ContextRequestProto;
import br.com.coregate.proto.ingress.ContextResponseProto;
import io.grpc.ManagedChannel;
import org.springframework.stereotype.Service;

@Service
public class GrpcIngressClientService {

    private final ContextProtoServiceGrpc.ContextProtoServiceBlockingStub stub;

    public GrpcIngressClientService(ManagedChannel contextChannel) {
        this.stub = ContextProtoServiceGrpc.newBlockingStub(contextChannel);
    }

    public ContextResponseProto callGrpc(ContextRequestProto request) {
        return stub.authorize(request);
    }
}
