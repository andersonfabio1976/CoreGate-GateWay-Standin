package br.com.coregate.finalizer.controller;

import br.com.coregate.infrastructure.enums.OperationalMode;
import br.com.coregate.infrastructure.enums.RabbitQueueType;
import br.com.coregate.infrastructure.mode.OperationalModeManager;
import br.com.coregate.infrastructure.rabbitmq.RabbitFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 🧭 Controller para consultar e alterar o modo operacional do sistema.
 *
 * - GET /mode/status → retorna o modo atual
 * - POST /mode/standin → solicita entrada manual em modo STAND-IN
 * - POST /mode/gateway → solicita retorno manual ao modo GATEWAY
 *
 * A mudança efetiva é feita via Listener (idempotente).
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class ModeStatusController {

    private final OperationalModeManager modeManager;
    private final RabbitFactory rabbitFactory;

    /**
     * Retorna o modo atual carregado pelo sistema.
     */
    @GetMapping("/mode/status")
    public ResponseEntity<ModeStatusResponse> getModeStatus() {
        OperationalMode current = modeManager.getMode();
        log.info("🔍 [STATUS] Modo atual do sistema: {}", current);
        return ResponseEntity.ok(new ModeStatusResponse(current));
    }

    // TODO NAKA
    // não esta trocando de standin solicitado e voltando pra gateway
    // endpoint abaixo
    /**
     * Solicita entrada manual em modo STAND-IN.
     * Publica evento RabbitMQ e o listener fará a transição real.
     */
    @PostMapping("/mode/standin")
    public ResponseEntity<Void> requestStandIn() {
        OperationalMode current = modeManager.getMode();
        OperationalMode next = OperationalMode.STANDIN_REQUESTED;

        rabbitFactory.publish(RabbitQueueType.STANDIN_REQUESTED, next.name());
        log.info("📤 [PUBLISH] STANDIN_REQUESTED → ");

        return ResponseEntity.ok().build();
    }

    /**
     * Solicita retorno manual ao modo GATEWAY.
     * Publica evento RabbitMQ e o listener fará a transição real.
     */
    @PostMapping("/mode/gateway")
    public ResponseEntity<Void> requestGateway() {
        OperationalMode current = modeManager.getMode();
        OperationalMode next = OperationalMode.GATEWAY;

        rabbitFactory.publish(RabbitQueueType.GATEWAY, next.name());
        log.info("📤 [PUBLISH] GATEWAY → ");

        return ResponseEntity.ok().build();
    }

    /* ======= DTOs ======= */

    public record ModeStatusResponse(OperationalMode mode) {}

    public record ModeToggleResponse(OperationalMode current,
                                     OperationalMode requested,
                                     String status) {}
}
