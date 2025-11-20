package br.com.coregate.orchestrator.grpc.server;

import br.com.coregate.core.contracts.dto.orquestrator.OrquestratorResponseDto;
import br.com.coregate.core.contracts.dto.orquestrator.OrquestratorSagaContext;
import br.com.coregate.core.contracts.dto.transaction.AuthorizationResult;
import br.com.coregate.core.contracts.mapper.OrquestratorMapper;
import br.com.coregate.core.contracts.mapper.TransactionMapper;
import br.com.coregate.orchestrator.saga.component.SagaEventPublisher;
import br.com.coregate.orchestrator.saga.service.OrquestratorSagaService;
import br.com.coregate.proto.Orquestrator.OrquestratorProtoServiceGrpc;
import br.com.coregate.proto.Orquestrator.OrquestratorRequestProto;
import br.com.coregate.proto.Orquestrator.OrquestratorResponseProto;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import javax.annotation.PostConstruct;


@Slf4j
@Service
@Async
public class GrpcOrquestratorServerService extends OrquestratorProtoServiceGrpc.OrquestratorProtoServiceImplBase {

    private final SagaEventPublisher publisher;
    private final OrquestratorSagaService orchestratorSagaService;
    private final GrpcServerComponent grpcServer;
    private final TransactionMapper transactionMapper;
    private final OrquestratorMapper orquestratorMapper;

    public GrpcOrquestratorServerService(SagaEventPublisher publisher, OrquestratorSagaService orchestratorSagaService, GrpcServerComponent grpcServer, TransactionMapper transactionMapper, OrquestratorMapper orquestratorMapper) {
        this.publisher = publisher;
        this.orchestratorSagaService = orchestratorSagaService;
        this.grpcServer = grpcServer;
        this.transactionMapper = transactionMapper;
        this.orquestratorMapper = orquestratorMapper;
    }

    @Value("${grpc.server.port}")
    private int grpcPort;

    @PostConstruct
    public void init() {
        log.info("🧩 Registering in SagaOfApprove Transaction in gRPC Orquestrator server...");
        grpcServer.start(this, grpcPort);
    }

    @Override
    public void orquestrateTransaction(OrquestratorRequestProto request,
                                       StreamObserver<OrquestratorResponseProto> responseObserver) {

        var transactionModel = transactionMapper.toModel(request);
        var transactionDto = transactionMapper.toDto(transactionModel);
        log.info("📥 [GRPC SERVER] Received transactionCommand: " + transactionDto.transactionCommand());
        publisher.publishStart("[ORCHESTRATOR]", request);
        var authorizationResult = AuthorizationResult.builder().build();
        var orquestratorSagaContext = OrquestratorSagaContext.builder()
                .transaction(transactionModel.transaction())
                .authorizationResult(authorizationResult)
                .build();

        // SAGA OF APPROVED /////////////////////
        var orquestratorSagaContextResponse = orchestratorSagaService.orchestrate(orquestratorSagaContext);
        /////////////////////////////////////////

        OrquestratorResponseProto response = responseProtoFactory(orquestratorSagaContextResponse);

        publisher.publishSuccess("[ORCHESTRATOR]", orquestratorSagaContextResponse.getAuthorizationResult());
        log.info("📥 [GRPC SERVER] Response: " + response);

        // Retorna a resposta ao cliente
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    private OrquestratorResponseProto responseProtoFactory(OrquestratorSagaContext contextResponse) {
        var responseDto = OrquestratorResponseDto.builder()
                            .authorizationResult(contextResponse.getAuthorizationResult())
                            .build();
        return orquestratorMapper.toProto(responseDto);
    }

}
