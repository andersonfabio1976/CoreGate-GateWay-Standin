package br.com.coregate.context.grpc.client;

import br.com.coregate.core.contracts.RequestTransactionFlowProto;
import br.com.coregate.core.contracts.ResponseTransactionFlowProto;
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
public class TransactionFlowClientService {

    private final TransactionFlowClientFactory transactionFlowClientFactory;

    @Value("${grpc.finalizer.deadline.ms:2500}")
    private long deadlineMs;

    public ResponseTransactionFlowProto callGrpc(RequestTransactionFlowProto request, int port) {
        String txId = request.getTransactionCommand().getTransactionId();

        var stub = transactionFlowClientFactory.stub(port);

        try {
            long start = System.nanoTime();
            ResponseTransactionFlowProto response = stub.connect(request);
            long elapsed = System.nanoTime() - start;

            log.info("gRPC Orchestrator OK, latency={} ms, transactionId={}",
                    TimeUnit.NANOSECONDS.toMillis(elapsed),
                    request.getTransactionCommand().getTransactionId());
            return response;

        } catch (StatusRuntimeException e) {
            Status.Code code = e.getStatus().getCode();
            log.error("Erro gRPC ao chamar Orchestrator: code={} message={} transactionId={}",
                    code, e.getStatus().getDescription(), request.getTransactionCommand().getTransactionId());
            throw e;
        }
    }
}
