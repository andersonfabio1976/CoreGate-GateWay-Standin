package br.com.coregate.orchestrator.grpc.client;

import br.com.coregate.core.contracts.RulesProtoServiceGrpc;
import br.com.coregate.core.contracts.RulesRequestProto;
import br.com.coregate.core.contracts.RulesResponseProto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class GrpcRulesClientService {

    private final RulesProtoServiceGrpc.RulesProtoServiceBlockingStub stub;

    public RulesResponseProto callGrpc(RulesRequestProto request) {
        try {
            RulesResponseProto response = stub.evaluate(request);
            log.info("📡 [ORCHESTRATOR] gRPC Rules OK: {}", response);
            return response;
        } catch (Exception e) {
            log.error("❌ [ORCHESTRATOR] Falha na chamada gRPC para RULES", e);
            throw e;
        }
    }
}
