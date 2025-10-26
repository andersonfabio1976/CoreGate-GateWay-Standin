package br.com.coregate.infrastructure.monitoring;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Range;
import org.springframework.data.redis.connection.stream.*;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class AlertHistoryService {

    private final RedisTemplate<String, String> redis;
    private static final String STREAM_KEY = "coregate:alerts";
    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public void appendAlert(String level, String message, String source) {
        String timestamp = LocalDateTime.now().format(FMT);

        Map<String, String> fields = Map.of(
                "level", level,
                "message", message,
                "source", source,
                "timestamp", timestamp
        );

        redis.opsForStream().add(StreamRecords.newRecord()
                .in(STREAM_KEY)
                .ofMap(fields));

        log.warn("⚠️ [ALERT] {} - {} ({})", level, message, source);
    }

    public Object readRecentAlerts(long limit) {
        var entries = redis.opsForStream().range(STREAM_KEY, Range.closed("-", "+"));
        if (entries == null) return Map.of("message", "No alerts");
        return entries.stream()
                .skip(Math.max(0, entries.size() - limit))
                .map(e -> e.getValue())
                .toList();
    }
}
