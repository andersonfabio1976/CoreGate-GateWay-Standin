package br.com.coregate.infrastructure.metrics;

import br.com.coregate.infrastructure.mode.OperationalModeManager;
import io.micrometer.core.instrument.*;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 📊 CoreGate Multi-Tenant Performance Metrics
 * Registra TPS, latência e sucesso por Tenant + Tipo de Transação + Modo Operacional.
 * Ideal para clusters multi-tenant e dashboards detalhados no Grafana.
 */
@Slf4j
@Component
public class CoreGatePerformanceMetrics {

    private final MeterRegistry registry;
    private final OperationalModeManager modeManager;

    // Estrutura por tenant+type
    private final Map<String, PerfBucket> buckets = new ConcurrentHashMap<>();

    public CoreGatePerformanceMetrics(MeterRegistry registry, OperationalModeManager modeManager) {
        this.registry = registry;
        this.modeManager = modeManager;
    }

    @PostConstruct
    public void init() {
        log.info("📊 CoreGate Multi-Tenant Performance Metrics registradas no Prometheus");
    }

    // ==========================================================
    // 🔹 Registro de eventos transacionais
    // ==========================================================

    public void recordTransaction(String tenant, String type, long latencyMs, boolean success) {
        getBucket(tenant, type).recordTransaction(latencyMs, success);
    }

    public void recordTimeout(String tenant, String type) {
        getBucket(tenant, type).recordTimeout();
    }

    public void recordError(String tenant, String type) {
        getBucket(tenant, type).recordError();
    }

    // ==========================================================
    // 🔹 Recupera bucket ou cria sob demanda
    // ==========================================================

    private PerfBucket getBucket(String tenant, String type) {
        return buckets.computeIfAbsent(tenant + "|" + type, key -> {
            PerfBucket bucket = new PerfBucket(tenant, type);
            bucket.registerMetrics();
            return bucket;
        });
    }

    // ==========================================================
    // 🔸 Classe interna por Tenant + Tipo
    // ==========================================================

    private class PerfBucket {
        private final String tenant;
        private final String type;
        private final AtomicInteger totalCount = new AtomicInteger(0);
        private final AtomicInteger successCount = new AtomicInteger(0);
        private final AtomicLong totalLatency = new AtomicLong(0);
        private final AtomicInteger tps = new AtomicInteger(0);

        private final Counter timeoutCounter;
        private final Counter errorCounter;
        private Instant lastTpsUpdate = Instant.now();

        PerfBucket(String tenant, String type) {
            this.tenant = tenant;
            this.type = type;
            this.timeoutCounter = Counter.builder("coregate_timeout_count")
                    .description("Timeouts por Tenant e Tipo")
                    .tags("tenant", tenant, "type", type)
                    .register(registry);

            this.errorCounter = Counter.builder("coregate_error_count")
                    .description("Erros por Tenant e Tipo")
                    .tags("tenant", tenant, "type", type)
                    .register(registry);
        }

        void registerMetrics() {
            // Gauge TPS
            Gauge.builder("coregate_tps_rate", tps, AtomicInteger::get)
                    .description("TPS por Tenant e Tipo")
                    .tags("tenant", tenant, "type", type, "mode", modeManager.getMode().name())
                    .baseUnit("tps")
                    .register(registry);

            // Latência média
            Gauge.builder("coregate_latency_avg_ms", this, b -> b.getAverageLatency())
                    .description("Latência média por Tenant e Tipo")
                    .tags("tenant", tenant, "type", type, "mode", modeManager.getMode().name())
                    .baseUnit("milliseconds")
                    .register(registry);

            // Taxa de sucesso
            Gauge.builder("coregate_success_rate", this, b -> b.getSuccessRate())
                    .description("Taxa de sucesso (%) por Tenant e Tipo")
                    .tags("tenant", tenant, "type", type, "mode", modeManager.getMode().name())
                    .baseUnit("percent")
                    .register(registry);
        }

        synchronized void recordTransaction(long latencyMs, boolean success) {
            totalCount.incrementAndGet();
            totalLatency.addAndGet(latencyMs);
            if (success) successCount.incrementAndGet();
            updateTps();
        }

        void recordTimeout() { timeoutCounter.increment(); }

        void recordError() { errorCounter.increment(); }

        double getAverageLatency() {
            int count = totalCount.get();
            return count == 0 ? 0.0 : (double) totalLatency.get() / count;
        }

        double getSuccessRate() {
            int total = totalCount.get();
            return total == 0 ? 0.0 : ((double) successCount.get() / total) * 100.0;
        }

        private void updateTps() {
            Instant now = Instant.now();
            long elapsedMs = now.toEpochMilli() - lastTpsUpdate.toEpochMilli();

            if (elapsedMs >= 1000) {
                tps.set(totalCount.getAndSet(0));
                successCount.set(0);
                totalLatency.set(0);
                lastTpsUpdate = now;
            }
        }
    }
}




//2️⃣ Integração no MockPOS ou no Ingress
//
//Em qualquer ponto onde você processa as mensagens (no MockPOS ou no IngressSagaService), injete o componente e registre os eventos:
//
//@Autowired
//private CoreGatePerformanceMetrics metrics;
//
//// Ao receber resposta ISO8583
//metrics.recordTransaction(latencyMs, true);
//
//// Ao detectar timeout
//metrics.recordTimeout();
//
//// Ao pegar erro de socket
//metrics.recordError();


//🧩 3️⃣ Exemplo de uso no MockPOS ou Orchestrator
//@Autowired
//private CoreGatePerformanceMetrics metrics;
//
//public void handleTransaction(Transaction tx, long latencyMs, boolean success) {
//    String tenant = tx.getTenantId().toString();
//    String type = tx.getType().name();
//    metrics.recordTransaction(tenant, type, latencyMs, success);
//}

