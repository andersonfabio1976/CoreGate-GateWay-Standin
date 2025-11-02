package br.com.coregate.context.grpc.client;

import br.com.coregate.application.dto.context.ContextResponseDto;
import br.com.coregate.application.dto.orquestrator.OrquestratorRequestDto;
import br.com.coregate.context.iso8583.ParserIso8583;
import br.com.coregate.infrastructure.grpc.server.GrpcServerComponent;
import br.com.coregate.infrastructure.mapper.ContextMapper;
import br.com.coregate.infrastructure.mapper.OrquestratorMapper;
import br.com.coregate.proto.ingress.ContextProtoServiceGrpc;
import br.com.coregate.proto.ingress.ContextRequestProto;
import br.com.coregate.proto.ingress.ContextResponseProto;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import javax.annotation.PostConstruct;

@Service
@Slf4j
public class GrpcContextServerConnect extends ContextProtoServiceGrpc.ContextProtoServiceImplBase {

    private final ContextMapper contextMapper;
    private final GrpcServerComponent grpcServer;
    private final GrpcContextClientService grpcContextClientService;
    private final OrquestratorMapper orquestratorMapper;

    public GrpcContextServerConnect(ContextMapper contextMapper, GrpcServerComponent grpcServer, GrpcContextClientService grpcContextClientService, OrquestratorMapper orquestratorMapper) {
        this.contextMapper = contextMapper;
        this.grpcServer = grpcServer;
        this.grpcContextClientService = grpcContextClientService;
        this.orquestratorMapper = orquestratorMapper;
    }

    @PostConstruct
    public void init() {
        log.info("🧩 Parser decode and encode iso8583 to dto and consume orquestrator...");
        grpcServer.start(this);
    }

    // Recebendo Chamada de Ingress
    @Override
    public void authorize(ContextRequestProto request,
                                       StreamObserver<ContextResponseProto> responseObserver) {

        // Decode ISO8583 to TransactionCommand //
        var requestIngressDto = contextMapper.toDto(request);
        var tx = ParserIso8583.decode(requestIngressDto.getRawBytes());
        var requestOrquestratorDto = OrquestratorRequestDto.builder()
                .transactionCommand(tx)
                .build();
        var requestOrquestratorProto = orquestratorMapper.toProto(requestOrquestratorDto);
        try {
            // Call Orquestrator
            var responseOrchestrateProto = grpcContextClientService.callGrpc(requestOrquestratorProto, 8091);
            var responseOrchestrateDto   = orquestratorMapper.toDto(responseOrchestrateProto);

            // ReEncode TransactionCommand to Iso8583 and Return do Ingress
            var rowBytes = ParserIso8583.encode(requestOrquestratorDto.transactionCommand(), responseOrchestrateDto.authorizationResult());
            var ingressResponseDto = ContextResponseDto.builder()
                                                    .rawBytes(rowBytes)
                                                    .context(requestIngressDto.getContext())
                                                    .build();
            var ingressResponseProto = contextMapper.toProto(ingressResponseDto);
            responseObserver.onNext(ingressResponseProto);
            responseObserver.onCompleted();

            log.info("Recebido do Orquestrator Response: {}", responseOrchestrateDto);
        } catch (Exception e) {
            log.error("Erro ao Conectar No Grpc Server Orquestrator... ");
        }
    }

}
