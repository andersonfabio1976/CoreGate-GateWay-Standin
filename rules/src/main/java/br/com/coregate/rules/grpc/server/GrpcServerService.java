package br.com.coregate.rules.grpc.server;

import br.com.coregate.application.dto.rules.RulesResponseDto;
import br.com.coregate.application.dto.rules.StandinDecision;
import br.com.coregate.infrastructure.grpc.server.GrpcServerComponent;
import br.com.coregate.infrastructure.mapper.RulesMapper;
import br.com.coregate.proto.rules.RulesProtoServiceGrpc;
import br.com.coregate.proto.rules.RulesRequestProto;
import br.com.coregate.proto.rules.RulesResponseProto;
import br.com.coregate.rules.auth.StandinRulesEngine;
import io.grpc.stub.StreamObserver;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import javax.annotation.PostConstruct;

@Service
@AllArgsConstructor
@Slf4j
public class GrpcServerService extends RulesProtoServiceGrpc.RulesProtoServiceImplBase {

    private final GrpcServerComponent grpcServerComponent;
    private final StandinRulesEngine engine;
    private final RulesMapper rulesMapper;

    @PostConstruct
    public void init() {
        log.info("🧩 [RULES] Approve Transaction By Rules ...");
        grpcServerComponent.start(this);
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
