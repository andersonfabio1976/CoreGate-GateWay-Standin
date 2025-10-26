package br.com.coregate.finalizer.controller;

import br.com.coregate.infrastructure.enums.ModeChangeEventType;
import br.com.coregate.infrastructure.enums.RabbitQueueType;
import br.com.coregate.infrastructure.mode.OperationalModeManager;
import br.com.coregate.infrastructure.enums.OperationalMode;
import br.com.coregate.infrastructure.rabbitmq.RabbitFactory;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 🧭 Controller para verificar o modo atual do CoreGate.
 * - Retorna se o sistema está operando em modo GATEWAY ou STANDIN.
 * - Endpoint simples para monitoramento ou NOC.
 */
@Slf4j
@RestController
public class ModeStatusController {

    private final OperationalModeManager modeManager;
    private final RabbitFactory rabbitFactory;

    public ModeStatusController(OperationalModeManager modeManager, RabbitFactory rabbitFactory) {
        this.modeManager = modeManager;
        this.rabbitFactory = rabbitFactory;
    }

    @GetMapping("/mode/status")
    public ResponseEntity<ModeStatusResponse> getModeStatus() {
        OperationalMode current = modeManager.getMode();
        log.info("🔍 [STATUS] Modo atual do sistema: {}", current);

        return ResponseEntity.ok(new ModeStatusResponse(current.name()));
    }

    @PostMapping("/mode/standin")
    public ResponseEntity<Void> requestStandIn() {
        rabbitFactory.publish(RabbitQueueType.STANDIN_REQUESTED, ModeChangeEventType.CHANGE_MODE_STANDIN_REQUEST_ON);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/mode/gateway") public ResponseEntity<Void> unlockGateway() {
        rabbitFactory.publish(RabbitQueueType.GATEWAY, ModeChangeEventType.RESTORE_MODE_GATEWAY_REQUEST_ON);
        return ResponseEntity.ok().build();
    }

    /** DTO simples para resposta JSON */
    public record ModeStatusResponse(String mode) { }
}
