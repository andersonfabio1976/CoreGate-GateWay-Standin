package br.com.coregate.orchestrator.saga.step;

import br.com.coregate.domain.enums.TransactionStatus;
import br.com.coregate.domain.model.Transaction;
import br.com.coregate.infrastructure.enums.RabbitQueueType;
import br.com.coregate.infrastructure.grpc.GrpcClientFactory;
import br.com.coregate.infrastructure.grpc.TransactionClientServiceGrpc;
import br.com.coregate.infrastructure.grpc.TransactionCommandProtoRequest;
import br.com.coregate.infrastructure.mapper.TransactionMapper;
import br.com.coregate.infrastructure.mock.TransactionCommandMockBuilder;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.FileWriter;
import java.io.IOException;
import java.util.UUID;

@Slf4j
public class Process {

    private static TransactionMapper transactionMapper;
    private static TransactionClientServiceGrpc grpcClient;


    @Autowired
    public Process(TransactionMapper transactionMapper,
                   TransactionClientServiceGrpc grpcClient) {
        Process.transactionMapper = transactionMapper;
        Process.grpcClient = grpcClient;
    }

    @Retry(name = "connectGrpcFinalize", fallbackMethod = "grpcFinalizerFallBack")
    @CircuitBreaker(name = "connectGrpcFinalize", fallbackMethod = "grpcFinalizerFallBack")
    public static Transaction execute(Transaction tx) {
        log.info("⚙️ Deciding processing mode: {}", "STAND-IN/GATEWAY");
//        if (tx.isStandIn()) {
//            return rules(tx);
//        } else {
//            return finalize(tx);
//        }

        TransactionCommandProtoRequest request = TransactionCommandProtoRequest
                .newBuilder()
                .setTransaction(transactionMapper.toProtoFromDomain(tx)).build();
        try {
            var response = grpcClient.callGrpc(request, 8092);
            log.info("Recebido do Finalizer Response: {}", response);
        } catch (Exception e) {
            log.error("Erro ao Conectar No Grpc Server Finalizer... ");
        }


        tx.setId(UUID.randomUUID().toString());
        tx.setResponseCode("00");
        tx.setAuthorizationCode(UUID.randomUUID().toString());
        tx.setStatus(TransactionStatus.AUTHORIZED);

        return tx;
    }



    public static Transaction rollback(Transaction tx) {
        log.warn("↩️ Rolling back Process data for {}", tx);
        // Aqui você desfaz o que foi carregado (limpa o contexto, por exemplo)
        //tx.setMerchantData(null);
        return tx;
    }

    public void grpcFinalizerFallBack(Transaction tx, Throwable ex) {
        System.err.printf("💥 [FALLBACK] Falha ao publicar em %s → %s%n", tx.getId(), ex.getMessage());
        try (FileWriter fw = new FileWriter("rabbit-fallback.log", true)) {
            fw.write("[%s]".formatted(tx));
        } catch (IOException ignored) {}
    }

}
