package br.com.coregate.ingress.saga.service;

import br.com.coregate.infrastructure.saga.FunctionalSaga;
import br.com.coregate.ingress.saga.step.*;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class IngressSagaService {


    public void runStep(String stepName, IngressContext ctx) {
        switch (stepName) {
            case "initChannel" -> FunctionalSaga.start(ctx)
                    .then("initChannel", InitChannelStep::execute, InitChannelStep::rollback)
                    .onEnd(EndStep::execute)
                    .enableSilentRollback(true)
                    .end(ctx);

            case "channelActive" -> FunctionalSaga.start(ctx)
                    .then("channelActive", ChannelActiveStep::execute,ChannelActiveStep::rollback)
                    .onEnd(EndStep::execute)
                    .enableSilentRollback(true)
                    .end(ctx);

            case "channelRead" -> FunctionalSaga.start(ctx)
                    .then("channelRead", ChannelReadStep::execute, ChannelReadStep::rollback)
                    .then("bytesToHex", BytesToHexStep::execute,BytesToHexStep::rollback)
                    // TODO  Levar este STEP decodeiso para Context
                    .then("decodeIso", DecodeIsoStep::execute, DecodeIsoStep::rollback)
                    .then("dispatch", DispatchStep::execute, DispatchStep::rollback)
                    .onEnd(EndStep::execute)
                    .onError(ErrorStep::execute)
                    .enableSilentRollback(true)
                    .end(ctx);

            case "end" -> EndStep.execute(ctx);
        }
    }


    public void runError(ChannelHandlerContext ctx, Throwable cause) {
        log.error("❌ Netty exception: {}", cause.getMessage(), cause);
        ErrorStep.execute(new Exception(cause));
        ctx.close();
    }
}
