package br.com.coregate.context.grpc.server;

import br.com.coregate.context.grpc.client.TransactionFlowClientService;
import br.com.coregate.core.contracts.RequestTransactionIsoProto;
import br.com.coregate.core.contracts.ResponseTransactionIsoProto;
import br.com.coregate.core.contracts.TransactionIsoServiceProtoGrpc;
import br.com.coregate.core.contracts.dto.transaction.RequestTransactionFlow;
import br.com.coregate.core.contracts.dto.transaction.ResponseTransactionIso;
import br.com.coregate.core.contracts.dto.transaction.TransactionIso;
import br.com.coregate.core.contracts.iso8583.parsing.ParserIso8583;
import br.com.coregate.core.contracts.mapper.TransactionFlowMapper;
import br.com.coregate.core.contracts.mapper.TransactionIsoMapper;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Slf4j
@Service
@RequiredArgsConstructor
public class GrpcTransactionIsoService
        extends TransactionIsoServiceProtoGrpc.TransactionIsoServiceProtoImplBase {

    private final TransactionIsoMapper transactionIsoMapper;
    private final TransactionFlowMapper transactionFlowMapper;
    private final TransactionFlowClientService transactionFlowClientService;
    private final ParserIso8583 parserIso8583;
    private final GrpcServerComponent grpcServer;

    @Value("${grpc.orchestrator.host}")
    private String grpcHost;

    @Value("${grpc.orchestrator.port}")
    private int grpcOrchestratorPort;

    @Value("${grpc.context.port}")
    private int grpcContextPort;

    @PostConstruct
    public void init() {
        log.info("🧩 Parser decode and encode iso8583 to dto and consume orquestrator...");
        grpcServer.start(this, grpcContextPort);
    }

    @Override
    public void connect(RequestTransactionIsoProto request,
                        StreamObserver<ResponseTransactionIsoProto> responseObserver) {

        var isoReq = transactionIsoMapper.toDto(request);

        String transactionId = isoReq.getTransactionId();
        byte[] rawBytes = isoReq.getRawBytes();
        long t0 = System.currentTimeMillis();

        log.info("📥 [CONTEXT-GRPC] Received transactionIso={} bytes={}",
                transactionId, rawBytes != null ? rawBytes.length : 0);

        if (rawBytes == null) {
            log.error("❌ [CONTEXT] rawBytes nulo para transactionId={}", transactionId);
            responseObserver.onError(
                    new IllegalArgumentException("rawBytes is null for tx " + transactionId));
            return;
        }

        try {
            // ------------------------------
            // 1) DECODE ISO8583
            // ------------------------------
            long tDecodeStart = System.currentTimeMillis();
            var tx = parserIso8583.decode(rawBytes, transactionId);
            long tDecode = System.currentTimeMillis() - tDecodeStart;

            log.info("🧩 [CONTEXT] Decoded ISO8583 transactionId={} tx={}", transactionId, tx);

            var requestFlow = RequestTransactionFlow.builder()
                    .transactionCommand(tx)
                    .build();

            var requestFlowProto = transactionFlowMapper.toProto(requestFlow);

            // ------------------------------
            // 2) GRPC ORCHESTRATOR
            // ------------------------------
            long tGrpcStart = System.currentTimeMillis();
            var responseFlowProto = transactionFlowClientService.callGrpc(requestFlowProto, grpcHost, grpcOrchestratorPort);
            long tGrpc = System.currentTimeMillis() - tGrpcStart;

            var responseFlow = transactionFlowMapper.toDto(responseFlowProto);

            // ------------------------------
            // 3) RE-ENCODE ISO8583
            // ------------------------------
            long tEncodeStart = System.currentTimeMillis();
            var encoded = parserIso8583.encode(responseFlow);
            long tEncode = System.currentTimeMillis() - tEncodeStart;

            var txIso = TransactionIso.builder()
                    .transactionId(transactionId)
                    .rawBytes(encoded)
                    .hexString(toHexString(encoded))
                    .build();

            var responseIso = ResponseTransactionIso.builder()
                    .transactionId(responseFlow.getAuthorizationResult().transactionId())
                    .rawBytes(txIso.getRawBytes())
                    .hexString(txIso.getHexString())
                    .build();

            var protoResponse = transactionIsoMapper.toProto(responseIso);

            long total = System.currentTimeMillis() - t0;

            log.info("""
                    ✅ [CONTEXT] Fluxo concluído transactionId={}
                        - decode ISO8583        : {} ms
                        - gRPC Orchestrator     : {} ms
                        - encode ISO8583        : {} ms
                        - TOTAL                 : {} ms
                    """,
                    transactionId, tDecode, tGrpc, tEncode, total
            );

            log.info("📤 [CONTEXT] Returning ISO8583 to Ingress tx={} bytes={}",
                    transactionId, encoded.length);

            responseObserver.onNext(protoResponse);
            responseObserver.onCompleted();

        } catch (Exception e) {
            long total = System.currentTimeMillis() - t0;
            log.error("💥 [CONTEXT] Erro ao processar tx={} após {} ms: {}",
                    transactionId, total, e.getMessage(), e);

            responseObserver.onError(e);
        }
    }

    private String toHexString(byte[] bytes) {
        StringBuilder hex = new StringBuilder(bytes.length * 2);
        for (byte b : bytes) hex.append(String.format("%02X", b));
        return hex.toString();
    }
}
