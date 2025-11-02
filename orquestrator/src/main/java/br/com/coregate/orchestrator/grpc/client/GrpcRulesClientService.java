package br.com.coregate.orchestrator.grpc.client;

import br.com.coregate.proto.finalizer.FinalizerRequestProto;
import br.com.coregate.proto.finalizer.FinalizerResponseProto;
import br.com.coregate.proto.rules.RulesRequestProto;
import br.com.coregate.proto.rules.RulesResponseProto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class GrpcRulesClientService {

    private final GrpcRulesClientFactory grpcRulesClientFactory;

    public RulesResponseProto callGrpc(RulesRequestProto request, int port) {
        var stub = grpcRulesClientFactory.createRulestStub(port);
        var response = stub.evaluate(request);
        System.out.println("Connected Via Grpc Request: " + request);
        return response;
    }

}
