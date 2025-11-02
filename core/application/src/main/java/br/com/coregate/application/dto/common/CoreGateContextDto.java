package br.com.coregate.application.dto.common;

import io.netty.channel.ChannelHandlerContext;
import lombok.Builder;
import lombok.Data;

/**
 * 🎯 Base abstrata para todos os DTOs CoreGate que trafegam entre módulos.
 * Mantém o contexto operacional mínimo: rastreabilidade, canal e tenant.
 */
@Builder
@Data
public class CoreGateContextDto {
    private String traceId;                     // ID de correlação (end-to-end)
    private ChannelHandlerContext channel;      // canal Netty / origem lógica
    private String tenantId;                    // Tenant responsável
}
