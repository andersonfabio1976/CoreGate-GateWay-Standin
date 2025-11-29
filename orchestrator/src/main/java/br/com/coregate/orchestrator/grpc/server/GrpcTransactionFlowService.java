package br.com.coregate.orchestrator.grpc.server;

import br.com.coregate.core.contracts.dto.orquestrator.OrchestratorSagaContext;
import br.com.coregate.core.contracts.dto.transaction.AuthorizationResult;
import br.com.coregate.core.contracts.dto.transaction.ResponseTransactionFlow;
import br.com.coregate.core.contracts.mapper.TransactionFlowMapper;
import br.com.coregate.orchestrator.saga.service.OrchestratorSagaService;
import br.com.coregate.proto.Orchestrator.RequestTransactionFlowProto;
import br.com.coregate.proto.Orchestrator.ResponseTransactionFlowProto;
import br.com.coregate.proto.Orchestrator.TransactionFlowServiceGrpc;
import io.grpc.stub.StreamObserver;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import javax.annotation.PostConstruct;


@Slf4j
@Service
@Async
@AllArgsConstructor
public class GrpcTransactionFlowService extends TransactionFlowServiceGrpc.TransactionFlowServiceImplBase {

    private final OrchestratorSagaService orchestratorSagaService;
    private final GrpcServerComponent grpcServer;
    private final TransactionFlowMapper transactionFlowMapper;

    @Value("${grpc.server.port}")
    private int grpcPort;

    @PostConstruct
    public void init() {
        log.info("🧩 Registering in SagaOfApprove Transaction in gRPC Orquestrator server...");
        grpcServer.start(this, grpcPort);
    }

    @Override
    public void connect(RequestTransactionFlowProto request,
                        StreamObserver<ResponseTransactionFlowProto> responseObserver) {
        var requestTransactionFlow = transactionFlowMapper.toDto(request);
        log.info("📥 [GRPC SERVER] Received transactionCommand: " + requestTransactionFlow.transactionCommand());
        var authorizationResult = AuthorizationResult.builder().build();
        var orchestratorSagaContext = OrchestratorSagaContext.builder()
                .transactionCommand(requestTransactionFlow.getTransactionCommand())
                .authorizationResult(authorizationResult)
                .build();

        // SAGA OF APPROVED /////////////////////
        var orquestratorSagaContextResponse = orchestratorSagaService.start(orchestratorSagaContext);
        /////////////////////////////////////////

        ResponseTransactionFlowProto response = responseProtoFactory(orquestratorSagaContextResponse);

        log.info("📥 [GRPC SERVER] Return Transaction: " + orquestratorSagaContextResponse.getAuthorizationResult());

        // Retorna a resposta ao cliente
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    private ResponseTransactionFlowProto responseProtoFactory(OrchestratorSagaContext contextResponse) {
        var responseDto = ResponseTransactionFlow.builder()
                            .authorizationResult(contextResponse.getAuthorizationResult())
                            .build();
        return transactionFlowMapper.toProto(responseDto);
    }

}
