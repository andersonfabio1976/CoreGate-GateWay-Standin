package br.com.coregate.finalizer.grpc.server;

import br.com.coregate.core.contracts.dto.finalizer.FinalizerResponseDto;
import br.com.coregate.core.contracts.dto.transaction.AuthorizationResult;
import br.com.coregate.core.contracts.mapper.FinalizerMapper;
import br.com.coregate.finalizer.client.IssuerClient;
import br.com.coregate.proto.finalizer.FinalizerProtoServiceGrpc;
import br.com.coregate.proto.finalizer.FinalizerRequestProto;
import br.com.coregate.proto.finalizer.FinalizerResponseProto;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import javax.annotation.PostConstruct;

@Service
@Slf4j
public class GrpcServerFinalizerService extends FinalizerProtoServiceGrpc.FinalizerProtoServiceImplBase {

    private final GrpcServerComponent grpcServerComponent;
    private final FinalizerMapper finalizerMapper;
    private final IssuerClient issuerClient;

    public GrpcServerFinalizerService(GrpcServerComponent grpcServerComponent, FinalizerMapper finalizerMapper, IssuerClient issuerClient) {
        this.grpcServerComponent = grpcServerComponent;
        this.finalizerMapper = finalizerMapper;
        this.issuerClient = issuerClient;
    }

    @Value("${grpc.server.port}")
    private int grpcPort;

    @PostConstruct
    public void init() {
        log.info("🧩 Initializing Server to Approve Transaction in Institution ...");
        grpcServerComponent.start(this, grpcPort);
    }

    @Override
    public void authorize(FinalizerRequestProto request, StreamObserver<FinalizerResponseProto> responseObserver) {

        var requestDto = finalizerMapper.toDto(request);
        log.info("🚀 Iniciando orquestração para transação {}", request);
        AuthorizationResult result = issuerClient.authorize(requestDto.transactionCommand());
        var responseDto = FinalizerResponseDto.builder()
                        .authorizationResult(result)
                        .build();
        var responseProto = finalizerMapper.toProto(responseDto);
        responseObserver.onNext(responseProto);
        responseObserver.onCompleted();

    }

}
