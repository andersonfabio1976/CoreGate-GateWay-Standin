package br.com.coregate.orchestrator.saga.service;

import br.com.coregate.domain.model.Transaction;
import br.com.coregate.infrastructure.grpc.*;
import br.com.coregate.infrastructure.mapper.TransactionMapper;
import br.com.coregate.infrastructure.saga.SagaEventPublisher;
import br.com.coregate.orchestrator.saga.step.*;
import com.google.protobuf.util.Timestamps;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.UUID;

@Slf4j
@Service
public class OrchestratorSagaOfApprove extends TransactionProtoServiceGrpc.TransactionProtoServiceImplBase {

    private final SagaEventPublisher publisher;
    private final TransactionMapper transactionMapper;
    private final OrchestratorService orchestratorService;
    private final GrpcServerComponent grpcServer;

    public OrchestratorSagaOfApprove(SagaEventPublisher publisher, TransactionMapper transactionMapper, OrchestratorService orchestratorService, GrpcServerComponent grpcServerComponent, GrpcServerComponent grpcServer) {
        this.publisher = publisher;
        this.transactionMapper = transactionMapper;
        this.orchestratorService = orchestratorService;
        this.grpcServer = grpcServer;
    }

    @PostConstruct
    public void init() {
        log.info("🧩 Registering OrchestratorSagaOfApprove in gRPC server...");
        grpcServer.start(this);
    }

    @Override
    public void process(TransactionCommandProtoRequest request,
                        StreamObserver<TransactionCommandProtoResponse> responseObserver) {

        Transaction tx = transactionMapper.fromProtoToDomain(request.getTransaction());
        log.info("📥 [GRPC SERVER] Received transactionCommand: " + tx);
        publisher.publishStart("[ORCHESTRATOR]", tx);

        // SAGA OF APPROVED /////////////////////
        tx = orchestratorService.orchestrate(tx);
        /////////////////////////////////////////

        TransactionCommandProtoResponse response = responseFactory(tx);

        publisher.publishSuccess("[ORCHESTRATOR]", tx);
        log.info("📥 [GRPC SERVER] Response: " + response);

        // Retorna a resposta ao cliente
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    private TransactionCommandProtoResponse responseFactory(Transaction tx) {
        return TransactionCommandProtoResponse.newBuilder()
                .setTransactionId(tx.getTenantId().getValue() + UUID.randomUUID().toString())
                .setAuthorizationCode(tx.getAuthorizationCode())
                .setResponseCode(tx.getResponseCode())
                .setStatus(tx.getResponseCode()
                            .equals("00")
                            ? TransactionStatus.AUTHORIZED
                            : TransactionStatus.REJECTED)
                .setTimestamp(Timestamps.fromMillis(System.currentTimeMillis()))
                .build();
    }
}
