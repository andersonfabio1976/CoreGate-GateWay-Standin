package br.com.coregate.finalizer.service;

import br.com.coregate.application.dto.TransactionCommandRequest;
import br.com.coregate.application.dto.TransactionCommandResponse;
import br.com.coregate.finalizer.client.ConnectionToIssuer;
import br.com.coregate.infrastructure.grpc.GrpcServerComponent;
import br.com.coregate.infrastructure.grpc.TransactionCommandProtoRequest;
import br.com.coregate.infrastructure.grpc.TransactionCommandProtoResponse;
import br.com.coregate.infrastructure.grpc.TransactionProtoServiceGrpc;
import br.com.coregate.infrastructure.mapper.TransactionMapper;
import br.com.coregate.infrastructure.saga.SagaEventPublisher;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Slf4j
@Service
public class FinalizerService extends TransactionProtoServiceGrpc.TransactionProtoServiceImplBase {

    private final ConnectionToIssuer connectionToIssuer;
    private final TransactionMapper transactionMapper;
    private final SagaEventPublisher publisher;
    private final GrpcServerComponent grpcServer;

    public FinalizerService(ConnectionToIssuer connectionToIssuer, TransactionMapper transactionMapper, SagaEventPublisher publisher, GrpcServerComponent grpcServer) {
        this.connectionToIssuer = connectionToIssuer;
        this.transactionMapper = transactionMapper;
        this.publisher = publisher;
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
        TransactionCommandRequest txReq = transactionMapper.fromProtoRequest(request);
        log.info("📥 [GRPC SERVER] Received transactionCommand: " + txReq);
        publisher.publishStart("[FINALIZER]", txReq);

        // Pegar Aprovação do ISSUER Emissor (Banco)
        TransactionCommandResponse txResponse = connectionToIssuer.connect(txReq);

        TransactionCommandProtoResponse response = transactionMapper.toProtoResponse(txResponse);

        publisher.publishSuccess("[FINALIZER]", response);
        log.info("📥 [GRPC SERVER] Response: " + response);

        // Retorna a resposta ao cliente
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

}
