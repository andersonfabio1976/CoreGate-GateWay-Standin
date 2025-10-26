package br.com.coregate.ingress.saga.service;

import br.com.coregate.application.dto.TransactionCommand;
import io.netty.channel.ChannelHandlerContext;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Contexto compartilhado entre todos os steps da SAGA Netty.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class IngressContext { // TODO Renomear TransactionWrapper
    private ChannelHandlerContext ctx;
    private byte[] rawBytes;
    private String hexString;
    private TransactionCommand transactionCommand;
}
