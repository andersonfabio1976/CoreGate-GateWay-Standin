package br.com.coregate.finalizer.grpc.server;

import br.com.coregate.core.contracts.RequestTransactionFlowProto;
import br.com.coregate.core.contracts.ResponseTransactionFlowProto;
import br.com.coregate.core.contracts.TransactionFlowServiceProtoGrpc;
import br.com.coregate.core.contracts.dto.transaction.AuthorizationResult;
import br.com.coregate.core.contracts.dto.transaction.ResponseTransactionFlow;
import br.com.coregate.core.contracts.mapper.TransactionFlowMapper;
import br.com.coregate.finalizer.client.IssuerClient;
import br.com.coregate.finalizer.grpc.server.support.GrpcServiceRegistry;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import javax.annotation.PostConstruct;

@Service
@Slf4j
@RequiredArgsConstructor
public class GrpcServerFinalizerService extends TransactionFlowServiceProtoGrpc.TransactionFlowServiceProtoImplBase {

    private final GrpcServiceRegistry registry;
    private final TransactionFlowMapper mapper;
    private final IssuerClient issuerClient;
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
        log.info("🚀 Finalizer received: {}", requestDto);

        AuthorizationResult result = issuerClient.authorize(requestDto.getTransactionCommand());

        var response = ResponseTransactionFlow.builder()
                .transactionCommand(requestDto.getTransactionCommand())
                .authorizationResult(result)
                .build();

        responseObserver.onNext(mapper.toProto(response));
        responseObserver.onCompleted();
    }

}
