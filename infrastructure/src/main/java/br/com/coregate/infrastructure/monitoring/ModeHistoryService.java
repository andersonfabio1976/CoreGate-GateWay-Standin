package br.com.coregate.infrastructure.monitoring;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Range;
import org.springframework.data.redis.connection.Limit;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.connection.stream.StreamRecords;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StreamOperations;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class ModeHistoryService {

    private final RedisTemplate<String, Object> redisTemplate;

    @Value("${coregate.mode.stream-key:coregate:mode:stream}")
    private String streamKey;

    /** Adiciona um evento no stream (opcional, útil para testes) */
    public void appendEvent(Map<String, Object> fields) {
        StreamOperations<String, Object, Object> ops = redisTemplate.opsForStream();
        ops.add(StreamRecords.mapBacked(fields).withStreamKey(streamKey));
    }

    /** ✅ Casa com o tipo do Controller */
    public List<MapRecord<String, Object, Object>> readRecentEvents(long limit) {
        StreamOperations<String, Object, Object> ops = redisTemplate.opsForStream();

        // Tenta pegar em ordem decrescente (mais recentes primeiro).
        // Se sua versão não tiver reverseRange, use o fallback com range + reverse.
        try {
            return ops.reverseRange(
                    streamKey,
                    Range.unbounded(),
                    Limit.limit().count((int) limit)
            );
        } catch (NoSuchMethodError | UnsupportedOperationException ex) {
            // Fallback: busca em ordem ascendente e inverte
            List<MapRecord<String, Object, Object>> asc = ops.range(
                    streamKey,
                    Range.unbounded(),
                    Limit.limit().count((int) limit)
            );
            if (asc != null) {
                Collections.reverse(asc);
            }
            return asc;
        }
    }

    public void saveModeChange(String mode) {
        redisTemplate.opsForList().leftPush("coregate:mode-history", mode);
        log.info("🧠 Saved mode change in Redis history: {}", mode);
    }
}
