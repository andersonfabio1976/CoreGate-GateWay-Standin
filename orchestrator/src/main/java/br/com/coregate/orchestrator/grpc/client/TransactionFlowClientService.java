package br.com.coregate.orchestrator.grpc.client;

import br.com.coregate.proto.Orchestrator.RequestTransactionFlowProto;
import br.com.coregate.proto.Orchestrator.ResponseTransactionFlowProto;
import br.com.coregate.proto.Orchestrator.TransactionFlowServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class GrpcTransactionFlowClientService {

    private final ManagedChannel orchestratorChannel;

   // private final TransactionFlowServiceGrpc.TransactionFlowServiceBlockingStub stub;

    @Value("${grpc.finalizer.deadline.ms:2500}")
    private long deadlineMs;

    public ResponseTransactionFlowProto callGrpc(RequestTransactionFlowProto request) {
        String txId = request.getTransactionCommand().getTransactionId();
        //long start = System.nanoTime();
        TransactionFlowServiceGrpc.TransactionFlowServiceBlockingStub stub =
                TransactionFlowServiceGrpc.newBlockingStub(orchestratorChannel)
                        .withDeadlineAfter(300, TimeUnit.MILLISECONDS);

        try {
            long start = System.nanoTime();
//            ResponseTransactionFlowProto response = stub
//                    .withDeadlineAfter(deadlineMs, TimeUnit.MILLISECONDS)
//                    .connect(request);

            ResponseTransactionFlowProto response = stub.connect(request);

            long elapsed = System.nanoTime() - start;

            //long elapsedMs = (System.nanoTime() - start) / 1_000_000;

            log.info("gRPC Orchestrator OK, latency={} ms, transactionId={}",
                    TimeUnit.NANOSECONDS.toMillis(elapsed),
                    request.getTransactionCommand().getTransactionId());

//            log.info("⏱ [ORCHESTRATOR] gRPC FINALIZER transactionId={} concluído em {}ms (deadline={}ms)",
//                    txId, elapsedMs, deadlineMs);

            return response;

        } catch (StatusRuntimeException e) {
            Status.Code code = e.getStatus().getCode();
            log.error("Erro gRPC ao chamar Orchestrator: code={} message={} transactionId={}",
                    code, e.getStatus().getDescription(), request.getTransactionCommand().getTransactionId());
//            long elapsedMs = (System.nanoTime() - start) / 1_000_000;
//            log.error("💥 [ORCHESTRATOR] Erro gRPC FINALIZER transactionId={} após {}ms (deadline={}ms): {} - {}",
//                    txId, elapsedMs, deadlineMs, e.getStatus().getCode(), e.getStatus().getDescription());
            throw e;
        }
    }
}
