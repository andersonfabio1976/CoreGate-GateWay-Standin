package br.com.coregate.context.server.grpc;

import br.com.coregate.infrastructure.grpc.*;
import br.com.coregate.infrastructure.mapper.TransactionMapper;
import br.com.coregate.infrastructure.saga.SagaEventPublisher;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import javax.annotation.PostConstruct;

@Service
@Slf4j
public class ServerGrpcWaiting extends TransactionProtoServiceGrpc.TransactionProtoServiceImplBase {

    private final SagaEventPublisher publisher;
    private final TransactionMapper mapper;
    private final GrpcServerComponent grpcServer;
    private final TransactionClientServiceGrpc grpcClient;

    public ServerGrpcWaiting(SagaEventPublisher publisher, TransactionMapper mapper, GrpcServerComponent grpcServerComponent, GrpcServerComponent grpcServer, TransactionClientServiceGrpc grpcClient) {
        this.publisher = publisher;
        this.mapper = mapper;
        this.grpcServer = grpcServer;
        this.grpcClient = grpcClient;
    }

    @PostConstruct
    public void init() {
        log.info("🧩 Registering OrchestratorSagaOfApprove in gRPC server...");
        grpcServer.start(this);
    }

    // Recebendo Chamada de Ingress
    @Override
    public void process(TransactionCommandProtoRequest request,
                        StreamObserver<TransactionCommandProtoResponse> responseObserver) {

        TransactionCommandProtoRequest requestToApprove = TransactionCommandProtoRequest
                .newBuilder()
                .setTransaction(request.getTransaction())
                .build();
        TransactionCommandProtoResponse response = null;
        try {
            // Chamando Orquestrator
            response = grpcClient.callGrpc(requestToApprove, 8091);
            log.info("Recebido do Orchestrator Response: {}", response);
        } catch (Exception e) {
            log.error("Erro ao Conectar No Grpc Server Orchestrator... ");
        }

        responseObserver.onNext(response);
        responseObserver.onCompleted();

    }

}
