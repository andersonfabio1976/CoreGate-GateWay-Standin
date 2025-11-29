package br.com.coregate.context.grpc.client;

import io.grpc.ManagedChannel;
import org.springframework.stereotype.Service;

@Service
public class GrpcOrchestratorClientService {

    private final TransactionFlow OrchestratorProtoServiceGrpc.OrchestratorProtoServiceBlockingStub stub;

    public GrpcOrchestratorClientService(ManagedChannel contextChannel) {
        this.stub = OrchestratorProtoServiceGrpc.newBlockingStub(contextChannel);
    }

    public OrchestratorResponseProto callGrpc(OrchestratorRequestProto request) {
        return stub.orchestrateTransaction(request);
    }
}
