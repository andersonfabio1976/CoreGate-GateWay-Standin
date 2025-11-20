package br.com.coregate.metrics.monitoring;

import br.com.coregate.metrics.dto.NocStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class NocStatusPublisher {

    private final SimpMessagingTemplate template;

    @Scheduled(fixedRate = 5000)
    public void publishStatus() {
        var dto = NocStatus.builder()
                .mode(Math.random() > 0.8 ? "Stand-In Automático" : "Gateway")
                .tps(1500 + Math.random() * 200)
                .sla(99.9 + Math.random() * 0.1)
                .updatedAt(LocalDateTime.now()
                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .build();

        template.convertAndSend("/topic/noc-status", dto);
    }
}
