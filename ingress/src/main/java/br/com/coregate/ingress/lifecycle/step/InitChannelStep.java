package br.com.coregate.ingress.lifecycle.step;

import br.com.coregate.core.contracts.dto.transaction.TransactionIso;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class InitChannelStep {

    public TransactionIso execute(TransactionIso ctx, Channel channel) {
        //log.info("🚀 InitChannelStep - Initializing Netty Channel...");
        return ctx;
    }

    public TransactionIso rollback(TransactionIso ctx, ChannelHandlerContext channel) {
        log.warn("↩️ Rollback InitChannelStep - limpando recursos do canal...");
        return ctx;
    }
}
