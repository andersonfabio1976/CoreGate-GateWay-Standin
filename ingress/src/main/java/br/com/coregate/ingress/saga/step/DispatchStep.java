package br.com.coregate.ingress.saga.step;

import br.com.coregate.application.dto.context.ContextRequestDto;
import br.com.coregate.application.dto.context.ContextResponseDto;
import br.com.coregate.infrastructure.mapper.ContextMapper;
import br.com.coregate.ingress.grpc.GrpcIngressClientService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.extern.slf4j.Slf4j;

import java.io.FileWriter;
import java.io.IOException;

@Slf4j
public class DispatchStep {

    private static GrpcIngressClientService grpcContextClientService;
    private static ContextMapper contextMapper;

    @Retry(name = "connectGrpcIngress", fallbackMethod = "grpcIngressFallBack")
    @CircuitBreaker(name = "connectGrpcIngress", fallbackMethod = "grpcIngressFallBack")
    public static ContextRequestDto execute(ContextRequestDto request) {
        log.info("📤 DispatchStep - Encaminhando TransactionCommand para processamento...");
        try {
            if (request.getRawBytes() == null)
                throw new IllegalStateException("TransactionCommand nulo - nada a despachar.");

            try {

                var requestProto  = contextMapper.toProto(request);
                var responseDto = contextMapper.toDto(
                                        grpcContextClientService
                                                .callGrpc(
                                                        requestProto
                                                        , 8090));
                log.info("Recebido do Context Response: {}", responseDto.getRawBytes());

            } catch (Exception e) {
                log.error("Erro ao Conectar No Grpc Server Context... ");
            }

            log.info("✅ DispatchStep - Mensagem enviada com sucesso: {}", request.getRawBytes());
            return request;

        } catch (Exception e) {
            log.error("❌ Falha no DispatchStep: {}", e.getMessage(), e);
            throw new RuntimeException("Erro ao despachar comando", e);
        }
    }

    public static ContextRequestDto rollback(ContextRequestDto ctx) {
        log.warn("↩️ Rollback DispatchStep - desfazendo envio RabbitMQ (noop)");
        return ctx;
    }

    public void grpcIngressFallBack(ContextResponseDto ctx, Throwable ex) {
        System.err.printf("💥 [FALLBACK] Falha ao publicar em %s → %s%n", ctx.getRawBytes(), ex.getMessage());
        try (FileWriter fw = new FileWriter("rabbit-fallback.log", true)) {
            fw.write("[%s]".formatted(ctx));
        } catch (IOException ignored) {}
    }
}
