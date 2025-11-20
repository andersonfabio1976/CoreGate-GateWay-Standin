package br.com.coregate.metrics.controller;

import br.com.coregate.metrics.dto.NocStatus;
import br.com.coregate.metrics.monitoring.AlertHistoryService;
import br.com.coregate.metrics.monitoring.ModeHistoryService;
import br.com.coregate.metrics.monitoring.ModeMetricsService;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/metrics")
public class NocStatusController {

    private final ModeHistoryService modeHistoryService;
    private final ModeMetricsService modeMetricsService;
    private final AlertHistoryService alertHistoryService;

    public NocStatusController(
            ModeHistoryService modeHistoryService,
            ModeMetricsService modeMetricsService,
            AlertHistoryService alertHistoryService
    ) {
        this.modeHistoryService = modeHistoryService;
        this.modeMetricsService = modeMetricsService;
        this.alertHistoryService = alertHistoryService;
    }

    @GetMapping("/noc-status")
    public NocStatus getStatus() {
        // ⚙️ Mock temporário — depois pode vir do Redis ou Prometheus
        // TODO OBSERVABILITY
        return NocStatus.builder()
                .mode("Gateway")
                .tps(1632.5)
                .sla(99.982)
                .updatedAt(LocalDateTime.now()
                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .build();
    }

    @GetMapping("/mode-history")
    public List<MapRecord<String, Object, Object>> getModeHistory(
            @RequestParam(defaultValue = "20") long limit) {
        return modeHistoryService.readRecentEvents(limit);
    }

    @GetMapping("/mode-metrics")
    public Map<String, Object> getModeMetrics() {
        return modeMetricsService.calculateMetrics();
    }

    @GetMapping("/sla-timeline")
    public List<Map<String, Object>> getSlaTimeline(
            @RequestParam(defaultValue = "24") int hours) {
        return modeMetricsService.buildSlaTimeline(hours);
    }

    @GetMapping("/alerts-history")
    public Object getAlertHistory(@RequestParam(defaultValue = "20") long limit) {
        return alertHistoryService.readRecentAlerts(limit);
    }

}
