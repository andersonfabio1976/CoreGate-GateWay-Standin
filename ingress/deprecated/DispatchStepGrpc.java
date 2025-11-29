package br.com.coregate.ingress.lifecycle.step;

import br.com.coregate.core.contracts.dto.context.ContextRequestDto;
import br.com.coregate.core.contracts.dto.context.ContextResponseDto;
import br.com.coregate.core.contracts.mapper.ContextMapper;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.netty.channel.ChannelHandlerContext;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.FileWriter;
import java.io.IOException;

@Slf4j
@Deprecated
@AllArgsConstructor
public class DispatchStepGrpc {

    private final GrpcIngressClientService grpcContextClientService;
    private final ContextMapper contextMapper;

    @Retry(name = "connectGrpcIngress", fallbackMethod = "grpcIngressFallBack")
    @CircuitBreaker(name = "connectGrpcIngress", fallbackMethod = "grpcIngressFallBack")
    public ContextRequestDto execute(ContextRequestDto request, ChannelHandlerContext channel) {
        log.info("📤 DispatchStep - Encaminhando TransactionCommand para Context...");

        try {
            if (request.getRawBytes() == null)
                throw new IllegalStateException("TransactionCommand nulo - nada a despachar.");

            var requestProto = contextMapper.toProto(request);

            log.info("✅ [INGRESS] DispatchStep - Connect to gRPC...{}",request);
            var responseProto = grpcContextClientService.callGrpc(requestProto);
            var responseDto = contextMapper.toDto(responseProto);

            if (responseDto == null || responseDto.getRawBytes() == null) {
                log.warn("⚠️ [INGRESS] DispatchStep - Nenhuma resposta ISO retornada pelo Context.");
                return request;
            }

            // ✅ Atualiza o contexto com os dados retornados
            request.setRawBytes(responseDto.getRawBytes());
            request.setHexString(responseDto.getHexString());

            log.info("✅ [INGRESS] DispatchStep - Resposta recebida do Context ({} bytes)",
                    responseDto.getRawBytes() != null ? responseDto.getRawBytes().length : 0);

            return request;

        } catch (Exception e) {
            log.error("❌ [INGRESS] Falha no DispatchStep connection gRPC: {}", e.getMessage(), e);
            throw new RuntimeException("Erro ao despachar comando", e);
        }
    }

    public ContextRequestDto rollback(ContextRequestDto ctx, ChannelHandlerContext channel) {
        log.warn("↩️ Rollback DispatchStep - Ignorando envio ao Context.");
        return ctx;
    }

    public void grpcIngressFallBack(ContextResponseDto ctx, Throwable ex) {
        log.error("💥 [FALLBACK] Falha ao chamar Context gRPC: {}", ex.getMessage());
        try (FileWriter fw = new FileWriter("grpc-fallback.log", true)) {
            fw.write("[%s]%n".formatted(ctx));
        } catch (IOException ignored) {}
    }
}
