package br.com.coregate.orchestrator.grpc.server;

import br.com.coregate.core.contracts.dto.orquestrator.OrchestratorSagaContext;
import br.com.coregate.core.contracts.dto.transaction.AuthorizationResult;
import br.com.coregate.core.contracts.dto.transaction.ResponseTransactionFlow;
import br.com.coregate.core.contracts.mapper.TransactionFlowMapper;
import br.com.coregate.orchestrator.saga.service.OrchestratorSagaService;
import br.com.coregate.proto.transaction.flow.RequestTransactionFlowProto;
import br.com.coregate.proto.transaction.flow.ResponseTransactionFlowProto;
import br.com.coregate.proto.transaction.flow.TransactionFlowServiceProtoGrpc;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import javax.annotation.PostConstruct;


@Slf4j
@Service
@Async
@RequiredArgsConstructor
public class GrpcTransactionFlowService extends TransactionFlowServiceProtoGrpc.TransactionFlowServiceProtoImplBase {

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
        log.info("📥 [GRPC SERVER] Received transactionCommand: " + requestTransactionFlow.getTransactionCommand());
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
        AuthorizationResult authorizationResult = null;
        if(contextResponse.getAuthorizationResult().transactionId() == null) {
            authorizationResult = AuthorizationResult.builder()
                    .transactionId(contextResponse.getTransactionCommand().transactionId())
                    .mti(contextResponse.getTransactionCommand().mti())
                    .responseCode(contextResponse.getAuthorizationResult().responseCode())
                    .status(contextResponse.getAuthorizationResult().status())
                    .date(contextResponse.getAuthorizationResult().date())
                    .build();
        } else {
            authorizationResult = contextResponse.getAuthorizationResult();
        }
        var responseDto = ResponseTransactionFlow.builder()
                            .transactionCommand(contextResponse.getTransactionCommand())
                            .authorizationResult(authorizationResult)
                            .build();
        return transactionFlowMapper.toProto(responseDto);
    }

}
