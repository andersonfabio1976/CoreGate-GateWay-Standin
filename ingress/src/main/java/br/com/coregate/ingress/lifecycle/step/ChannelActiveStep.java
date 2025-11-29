package br.com.coregate.ingress.lifecycle.step;

import br.com.coregate.core.contracts.dto.transaction.TransactionIso;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ChannelActiveStep {

    public TransactionIso execute(TransactionIso ctx, Channel channel) {
        //log.info("[INGRESS] Channel Active Step");
        return ctx;
    }

    public TransactionIso rollback(TransactionIso ctx, ChannelHandlerContext channel) {
        log.warn("↩️ Rollback ChannelActiveStep - Clear Context...");
        ctx.setRawBytes(null);
        return ctx;
    }
}
