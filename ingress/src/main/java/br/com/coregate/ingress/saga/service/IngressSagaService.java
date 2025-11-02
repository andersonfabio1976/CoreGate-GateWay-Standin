package br.com.coregate.ingress.saga.service;

import br.com.coregate.application.dto.context.ContextRequestDto;
import br.com.coregate.infrastructure.saga.FunctionalSaga;
import br.com.coregate.ingress.saga.step.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class IngressSagaService {

    public void runStep(String stepName, ContextRequestDto ctx) {
        try {
            switch (stepName) {

                case "initChannel" -> FunctionalSaga.start(ctx)
                        .then("initChannel", InitChannelStep::execute, InitChannelStep::rollback)
                        .onEnd(EndStep::execute)
                        .enableSilentRollback(true)
                        .end(ctx);

                case "channelActive" -> FunctionalSaga.start(ctx)
                        .then("channelActive", ChannelActiveStep::execute, ChannelActiveStep::rollback)
                        .onEnd(EndStep::execute)
                        .enableSilentRollback(true)
                        .end(ctx);

                case "channelRead" -> FunctionalSaga.start(ctx)
                        .then("channelRead", ChannelReadStep::execute, ChannelReadStep::rollback)
                        .then("bytesToHex", BytesToHexStep::execute, BytesToHexStep::rollback)
                        .then("dispatch", DispatchStep::execute, DispatchStep::rollback)
                        .onError(ErrorStep::execute)
                        .onEnd(EndStep::execute)
                        .enableSilentRollback(true)
                        .end(ctx);

                case "end" -> EndStep.execute(ctx);

                default -> log.warn("⚠️ Step desconhecido: {}", stepName);
            }

        } catch (Exception e) {
            log.error("❌ Erro inesperado na execução do step '{}': {}", stepName, e.getMessage(), e);
            ErrorStep.execute(ctx, e);
        }
    }

    public void runError(ContextRequestDto ctx, Throwable cause) {
        log.error("❌ Netty exception: {}", cause.getMessage(), cause);
        ErrorStep.execute(ctx, new Exception(cause));
        ctx.getContext().getChannel().close();
    }
}
