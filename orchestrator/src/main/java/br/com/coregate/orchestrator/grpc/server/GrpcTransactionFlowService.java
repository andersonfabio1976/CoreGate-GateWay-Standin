package br.com.coregate.orchestrator.grpc.server;

import br.com.coregate.context.grpc.server.GrpcServerComponent;
import br.com.coregate.core.contracts.RequestTransactionFlowProto;
import br.com.coregate.core.contracts.ResponseTransactionFlowProto;
import br.com.coregate.core.contracts.TransactionFlowServiceProtoGrpc;
import br.com.coregate.core.contracts.dto.orquestrator.OrchestratorSagaContext;
import br.com.coregate.core.contracts.dto.transaction.AuthorizationResult;
import br.com.coregate.core.contracts.dto.transaction.ResponseTransactionFlow;
import br.com.coregate.core.contracts.mapper.TransactionFlowMapper;
import br.com.coregate.orchestrator.saga.service.OrchestratorSagaService;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import javax.annotation.PostConstruct;

@Slf4j
@Service
@RequiredArgsConstructor
public class GrpcTransactionFlowService
        extends TransactionFlowServiceProtoGrpc.TransactionFlowServiceProtoImplBase {

    private final OrchestratorSagaService orchestratorSagaService;
    private final TransactionFlowMapper mapper;
    private final GrpcServerComponent grpcServer;

    @Value("${grpc.server.port}")
    private int grpcPort;

    @PostConstruct
    public void init() {
        log.info("🧩 Parser decode and encode iso8583 to dto and consume orquestrator...");
        grpcServer.start(this, grpcPort);
    }

    @Override
    public void connect(RequestTransactionFlowProto request,
                        StreamObserver<ResponseTransactionFlowProto> responseObserver) {

        var requestDto = mapper.toDto(request);

        log.info("📥 [ORCHESTRATOR-GRPC] Received transactionCommand={}",
                requestDto.getTransactionCommand());

        // Monta o contexto da SAGA
        var context = OrchestratorSagaContext.builder()
                .transactionCommand(requestDto.getTransactionCommand())
                .authorizationResult(AuthorizationResult.builder().build())
                .build();

        // Execução da SAGA
        var resultContext = orchestratorSagaService.start(context);

        // Monta o response final
        ResponseTransactionFlowProto response = toResponseProto(resultContext);

        log.info("📤 [ORCHESTRATOR-GRPC] Returning AuthorizationResult={}",
                resultContext.getAuthorizationResult());

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    /**
     * Converte o contexto final da SAGA em ResponseTransactionFlowProto.
     * Mantém a sua regra de correção de transactionId.
     */
    private ResponseTransactionFlowProto toResponseProto(OrchestratorSagaContext ctx) {

        AuthorizationResult ar = ctx.getAuthorizationResult();

        // Em alguns casos, o Saga retorna AuthorizationResult sem transactionId preenchido.
        if (ar.transactionId() == null) {
            ar = AuthorizationResult.builder()
                    .transactionId(ctx.getTransactionCommand().transactionId())
                    .mti(ctx.getTransactionCommand().mti())
                    .responseCode(ctx.getAuthorizationResult().responseCode())
                    .status(ctx.getAuthorizationResult().status())
                    .date(ctx.getAuthorizationResult().date())
                    .build();
        }

        var responseDto = ResponseTransactionFlow.builder()
                .transactionCommand(ctx.getTransactionCommand())
                .authorizationResult(ar)
                .build();

        return mapper.toProto(responseDto);
    }

}
