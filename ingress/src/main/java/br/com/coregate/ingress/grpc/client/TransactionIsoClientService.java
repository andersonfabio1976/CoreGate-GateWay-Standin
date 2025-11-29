package br.com.coregate.ingress.grpc.client;

import br.com.coregate.core.contracts.RequestTransactionIsoProto;
import br.com.coregate.core.contracts.ResponseTransactionIsoProto;
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
public class TransactionIsoClientService {

    private final TransactionIsoClientFactory transactionIsoClientFactory;

    @Value("${grpc.finalizer.deadline.ms:2500}")
    private long deadlineMs;

    public ResponseTransactionIsoProto callGrpc(RequestTransactionIsoProto request, int port) {
        String txId = request.getTransactionId();
        var stub = transactionIsoClientFactory.stub(port);

        try {
            long start = System.nanoTime();
            ResponseTransactionIsoProto response = stub.connect(request);
            long elapsed = System.nanoTime() - start;

            log.info("gRPC Context OK, latency={} ms, transactionId={}",
                    TimeUnit.NANOSECONDS.toMillis(elapsed),
                    txId);
            return response;

        } catch (StatusRuntimeException e) {
            Status.Code code = e.getStatus().getCode();
            log.error("Erro gRPC ao chamar Context: code={} message={} transactionId={}",
                    code, e.getStatus().getDescription(), txId);
            throw e;
        }
    }
}
