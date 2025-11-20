package br.com.coregate.rules.grpc.server;

import br.com.coregate.core.contracts.dto.rules.RulesResponseDto;
import br.com.coregate.core.contracts.dto.rules.StandinDecision;
import br.com.coregate.core.contracts.mapper.RulesMapper;
import br.com.coregate.proto.rules.RulesProtoServiceGrpc;
import br.com.coregate.proto.rules.RulesRequestProto;
import br.com.coregate.proto.rules.RulesResponseProto;
import br.com.coregate.rules.auth.StandinRulesEngine;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import javax.annotation.PostConstruct;

@Service
@Slf4j
public class GrpcServerService extends RulesProtoServiceGrpc.RulesProtoServiceImplBase {

    private final GrpcServerComponent grpcServerComponent;
    private final StandinRulesEngine engine;
    private final RulesMapper rulesMapper;

    @Value("${grpc.server.port}")
    private int grpcPort;

    public GrpcServerService(GrpcServerComponent grpcServerComponent, StandinRulesEngine engine, RulesMapper rulesMapper) {
        this.grpcServerComponent = grpcServerComponent;
        this.engine = engine;
        this.rulesMapper = rulesMapper;
    }

    @PostConstruct
    public void init() {
        log.info("🧩 [RULES] Start gRPC Server in Port {}", grpcPort);
        grpcServerComponent.start(this, grpcPort);
    }

    @Override
    public void evaluate(RulesRequestProto request, StreamObserver<RulesResponseProto> responseObserver) {

        var requestDto = rulesMapper.toDto(request);
        StandinDecision decision = engine.evaluate(requestDto.transactionFactDto());
        RulesResponseDto rulesResponseDto = RulesResponseDto.builder()
                .decision(decision)
                .build();
        var responseProto = rulesMapper.toProto(rulesResponseDto);

        responseObserver.onNext(responseProto);
        responseObserver.onCompleted();
    }
}
