package br.com.coregate.metrics.monitoring;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Range;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class ModeMetricsService {

    private final RedisTemplate<String, String> redis;
    private static final String STREAM_KEY = "coregate:mode-history";
    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public Map<String, Object> calculateMetrics() {
        var entries = redis.opsForStream().range(STREAM_KEY, Range.closed("-", "+"));
        if (entries == null || entries.isEmpty()) return Map.of("message", "Sem histórico disponível");

        Duration totalManual = Duration.ZERO;
        Duration totalAuto = Duration.ZERO;
        Duration totalGateway = Duration.ZERO;

        LocalDateTime lastTimestamp = null;
        String lastMode = null;

        for (MapRecord<String, Object, Object> entry : entries) {
            String mode = (String) entry.getValue().get("mode");
            String timestamp = (String) entry.getValue().get("timestamp");
            if (mode == null || timestamp == null) continue;

            LocalDateTime currentTime = LocalDateTime.parse(timestamp, FMT);
            if (lastTimestamp != null) {
                Duration delta = Duration.between(lastTimestamp, currentTime);
                if (lastMode != null) {
                    switch (lastMode) {
                        case "Stand-In Manual" -> totalManual = totalManual.plus(delta);
                        case "Stand-In Automático" -> totalAuto = totalAuto.plus(delta);
                        case "Gateway" -> totalGateway = totalGateway.plus(delta);
                    }
                }
            }
            lastTimestamp = currentTime;
            lastMode = mode;
        }

        long totalSeconds = totalManual.plus(totalAuto).plus(totalGateway).getSeconds();
        double percManual = totalSeconds > 0 ? totalManual.getSeconds() * 100.0 / totalSeconds : 0;
        double percAuto = totalSeconds > 0 ? totalAuto.getSeconds() * 100.0 / totalSeconds : 0;
        double percGateway = totalSeconds > 0 ? totalGateway.getSeconds() * 100.0 / totalSeconds : 0;

        Duration totalDowntime = totalManual.plus(totalAuto);
        Duration totalUptime = totalGateway;
        double sla = totalSeconds > 0 ? (totalUptime.getSeconds() * 100.0 / totalSeconds) : 0;

        return Map.of(
                "totalRecords", entries.size(),
                "totalManualSeconds", totalManual.getSeconds(),
                "totalAutoSeconds", totalAuto.getSeconds(),
                "totalGatewaySeconds", totalGateway.getSeconds(),
                "percentageManual", percManual,
                "percentageAuto", percAuto,
                "percentageGateway", percGateway,
                "totalDowntime", totalDowntime.toMinutes(),
                "meanTimeToRecovery", calculateMTTR(entries),
                "slaPercent", sla
        );
    }

    private double calculateMTTR(List<MapRecord<String, Object, Object>> entries) {
        List<Long> recoverTimes = new ArrayList<>();
        LocalDateTime startDown = null;
        for (MapRecord<String, Object, Object> entry : entries) {
            String mode = (String) entry.getValue().get("mode");
            String timestamp = (String) entry.getValue().get("timestamp");
            if (mode == null || timestamp == null) continue;
            LocalDateTime time = LocalDateTime.parse(timestamp, FMT);

            if (mode.startsWith("Stand-In") && startDown == null) startDown = time;
            if ("Gateway".equals(mode) && startDown != null) {
                recoverTimes.add(Duration.between(startDown, time).toMinutes());
                startDown = null;
            }
        }
        return recoverTimes.isEmpty()
                ? 0.0
                : recoverTimes.stream().mapToDouble(Long::doubleValue).average().orElse(0);
    }

    public List<Map<String, Object>> buildSlaTimeline(int hours) {
        var entries = redis.opsForStream().range(STREAM_KEY, Range.closed("-", "+"));
        if (entries == null || entries.isEmpty()) return List.of();

        List<Map<String, Object>> timeline = new ArrayList<>();
        LocalDateTime start = LocalDateTime.now().minusHours(hours);
        LocalDateTime last = start;
        String lastMode = "Gateway";

        for (MapRecord<String, Object, Object> entry : entries) {
            String mode = (String) entry.getValue().get("mode");
            String ts = (String) entry.getValue().get("timestamp");
            if (mode == null || ts == null) continue;

            LocalDateTime current = LocalDateTime.parse(ts, FMT);
            if (current.isBefore(start)) continue;

            Duration delta = Duration.between(last, current);
            double downtime = (lastMode.startsWith("Stand-In")) ? delta.toSeconds() : 0;

            timeline.add(Map.of(
                    "timestamp", ts,
                    "mode", mode,
                    "sla", 100.0 - (downtime / 864.0) // simplificado
            ));

            last = current;
            lastMode = mode;
        }
        return timeline;
    }

}
