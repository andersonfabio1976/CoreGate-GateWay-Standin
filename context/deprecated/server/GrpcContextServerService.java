package br.com.coregate.context.grpc.server;

import br.com.coregate.context.grpc.client.GrpcContextClientService;
import br.com.coregate.core.contracts.dto.context.ContextResponseDto;
import br.com.coregate.core.contracts.dto.orquestrator.OrquestratorRequestDto;
import br.com.coregate.core.contracts.iso8583.parsing.ParserIso8583;
import br.com.coregate.core.contracts.mapper.OrquestratorMapper;
import br.com.coregate.core.contracts.mapper.ContextMapper;
import br.com.coregate.proto.ingress.ContextProtoServiceGrpc;
import br.com.coregate.proto.ingress.ContextRequestProto;
import br.com.coregate.proto.ingress.ContextResponseProto;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import javax.annotation.PostConstruct;
import java.time.Instant;

@Service
@Slf4j
public class GrpcContextServerService extends ContextProtoServiceGrpc.ContextProtoServiceImplBase {

    private final ContextMapper contextMapper;
    private final GrpcContextClientService grpcContextClientService;
    private final OrquestratorMapper orquestratorMapper;
    private final ParserIso8583 parserIso8583;
    private final GrpcServerComponent grpcServer;

    public GrpcContextServerService(ContextMapper contextMapper, GrpcServerComponent grpcServer, GrpcContextClientService grpcContextClientService, OrquestratorMapper orquestratorMapper, ParserIso8583 parserIso8583) {
        this.contextMapper = contextMapper;
        this.grpcServer = grpcServer;
        this.grpcContextClientService = grpcContextClientService;
        this.orquestratorMapper = orquestratorMapper;
        this.parserIso8583 = parserIso8583;
    }

    @Value("${grpc.server.port}")
    private int grpcPort;

    @PostConstruct
    public void init() {
        log.info("🧩 Parser decode and encode iso8583 to dto and consume orquestrator...");
        grpcServer.start(this, grpcPort);
    }

    // Recebendo Chamada de Ingress
    @Override
    public void authorize(ContextRequestProto request,
                                       StreamObserver<ContextResponseProto> responseObserver) {

        // Decode ISO8583 to TransactionCommand //
        var requestIngressDto = contextMapper.toDto(request);
        var tx = parserIso8583.decode(requestIngressDto.getRawBytes());
        var requestOrquestratorDto = OrquestratorRequestDto.builder()
                .transactionCommand(tx)
                .enrichedAtEpochMs(Instant.now().getEpochSecond())
                .build();
        log.info("SEND TRANSACTION {} TO ORQUESTRATOR ", requestOrquestratorDto);
        var requestOrquestratorProto = orquestratorMapper.toProto(requestOrquestratorDto);
        try {
            // Call Orquestrator
            var responseOrchestrateProto = grpcContextClientService.callGrpc(requestOrquestratorProto);
            var responseOrchestrateDto   = orquestratorMapper.toDto(responseOrchestrateProto);

            // ReEncode TransactionCommand to Iso8583 and Return do Ingress
            var rowBytes = parserIso8583.encode(requestOrquestratorDto.transactionCommand(), responseOrchestrateDto.authorizationResult());
            var ingressResponseDto = ContextResponseDto.builder()
                                                    .rawBytes(rowBytes)
                                                    .hexString(toHexString(rowBytes))
                                                    .build();
            var ingressResponseProto = contextMapper.toProto(ingressResponseDto);
            responseObserver.onNext(ingressResponseProto);
            responseObserver.onCompleted();

            log.info("Recebido do Orquestrator Response: {}", responseOrchestrateDto);
        } catch (Exception e) {
            log.error("Erro ao Conectar No Grpc Server Orquestrator... ");
        }
    }

    public static String toHexString(byte[] bytes) {
        StringBuilder hex = new StringBuilder(bytes.length * 2);
        for (byte b : bytes) {
            hex.append(String.format("%02X", b));
        }
        return hex.toString();
    }

}
