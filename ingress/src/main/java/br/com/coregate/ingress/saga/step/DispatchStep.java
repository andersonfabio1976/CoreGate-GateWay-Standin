package br.com.coregate.ingress.saga.step;

import br.com.coregate.infrastructure.grpc.TransactionClientServiceGrpc;
import br.com.coregate.infrastructure.grpc.TransactionCommandProtoRequest;
import br.com.coregate.infrastructure.mapper.TransactionMapper;
import br.com.coregate.ingress.saga.service.IngressContext;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import java.io.FileWriter;
import java.io.IOException;

@Slf4j
public class DispatchStep {

    private static TransactionMapper transactionMapper;
    private static TransactionClientServiceGrpc grpcClient;

    @Autowired
    public DispatchStep(TransactionMapper transactionMapper,
                   TransactionClientServiceGrpc grpcClient) {
        DispatchStep.transactionMapper = transactionMapper;
        DispatchStep.grpcClient = grpcClient;
    }

    @Retry(name = "connectGrpcIngress", fallbackMethod = "grpcIngressFallBack")
    @CircuitBreaker(name = "connectGrpcIngress", fallbackMethod = "grpcIngressFallBack")
    public static IngressContext execute(IngressContext ctx) {
        log.info("📤 DispatchStep - Encaminhando TransactionCommand para processamento...");
        try {
            if (ctx.getTransactionCommand() == null)
                throw new IllegalStateException("TransactionCommand nulo - nada a despachar.");

            ///CLIENT GRPC /// TODO JUNIOR
            //Aqui tx.getTransactionCommand() esta vindo decodificado
            //Deve criar uma saga para decodificar ele aqui, deve vir bruto
            //do INGRESS
            TransactionCommandProtoRequest request = TransactionCommandProtoRequest.newBuilder()
                            .setTransaction(transactionMapper
                                    .toProto(ctx.getTransactionCommand()))
                            .build();
            try {
                var response = grpcClient.callGrpc(request, 8090);
                log.info("Recebido do Context Response: {}", response);
            } catch (Exception e) {
                log.error("Erro ao Conectar No Grpc Server Context... ");
            }
            /// //////////////

            log.info("✅ DispatchStep - Mensagem enviada com sucesso: {}", ctx.getTransactionCommand());
            return ctx;

        } catch (Exception e) {
            log.error("❌ Falha no DispatchStep: {}", e.getMessage(), e);
            throw new RuntimeException("Erro ao despachar comando", e);
        }
    }

    public static IngressContext rollback(IngressContext ctx) {
        log.warn("↩️ Rollback DispatchStep - desfazendo envio RabbitMQ (noop)");
        return ctx;
    }

    public void grpcIngressFallBack(IngressContext ctx, Throwable ex) {
        System.err.printf("💥 [FALLBACK] Falha ao publicar em %s → %s%n", ctx.getRawBytes(), ex.getMessage());
        try (FileWriter fw = new FileWriter("rabbit-fallback.log", true)) {
            fw.write("[%s]".formatted(ctx));
        } catch (IOException ignored) {}
    }
}
