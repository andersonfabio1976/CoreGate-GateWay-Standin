package br.com.coregate.ingress.lifecycle.service;

import br.com.coregate.core.contracts.dto.context.ContextRequestDto;
import br.com.coregate.ingress.lifecycle.step.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class IngressSagaService {

    // 🔹 Steps injetados como beans Spring
    private final InitChannelStep initChannelStep;
    private final ChannelActiveStep channelActiveStep;
    private final ChannelReadStep channelReadStep;
    private final BytesToHexStep bytesToHexStep;
    private final DispatchStep dispatchStep;
    private final ErrorStep errorStep;
    private final EndStep endStep;

    public void runStep(String stepName, ContextRequestDto ctx) {
        try {
            switch (stepName) {

                case "initChannel" ->
                        endStep.execute(initChannelStep.execute(ctx));

                case "channelActive" ->
                        endStep.execute(channelActiveStep.execute(ctx));

                case "channelRead" -> {
                        ctx = channelReadStep.execute(ctx);
                        ctx = bytesToHexStep.execute(ctx);
                        ctx = dispatchStep.execute(ctx);
                }

                case "end" -> endStep.execute(ctx);

                default -> log.warn("⚠️ Step desconhecido: {}", stepName);
            }

        } catch (Exception e) {
            log.error("❌ Erro inesperado na execução do step '{}': {}", stepName, e.getMessage(), e);
            errorStep.execute(ctx, e);
        }
    }

    public void runError(ContextRequestDto ctx, Throwable cause) {
        log.error("❌ Netty exception: {}", cause.getMessage(), cause);
        errorStep.execute(ctx, new Exception(cause));
        ctx.getContext().getChannel().close();
    }
}
