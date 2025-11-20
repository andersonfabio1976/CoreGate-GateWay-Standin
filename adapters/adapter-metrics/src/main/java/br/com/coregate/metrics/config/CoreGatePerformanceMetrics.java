package br.com.coregate.metrics.config;

import br.com.coregate.application.ports.out.PerformanceMetricsPort;
import io.micrometer.core.instrument.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 📊 CoreGate Performance Metrics
 * - TPS, duração, sucesso/falha, rollback e lifecycle dos steps.
 */
@Slf4j
@Component
public class CoreGatePerformanceMetrics implements PerformanceMetricsPort {

    private static MeterRegistry staticRegistry;
    private final MeterRegistry registry;

    private final Map<String, AtomicInteger> tpsMap = new ConcurrentHashMap<>();

    public CoreGatePerformanceMetrics(MeterRegistry registry) {
        this.registry = registry;
        staticRegistry = registry;
    }

    // ===========================================================
    // ✅ Transações principais
    // ===========================================================
    public void recordTransaction(String tenant, String type, long elapsedMs, boolean success) {
        Tags tags = Tags.of("tenant", tenant, "type", type);

        Timer.builder("coregate_transaction_duration_ms")
                .description("Transaction duration per tenant/type")
                .publishPercentileHistogram()
                .sla(Duration.ofMillis(50), Duration.ofMillis(100), Duration.ofMillis(200), Duration.ofMillis(500))
                .tags(tags)
                .register(registry)
                .record(Duration.ofMillis(elapsedMs));

        Counter.builder(success ? "coregate_transaction_success_total" : "coregate_transaction_failure_total")
                .description("Total successful or failed transactions")
                .tags(tags)
                .register(registry)
                .increment();

        recordTps(tenant);
    }

    public void recordTps(String tenant) {
        tpsMap.computeIfAbsent(tenant,
                        t -> registry.gauge("coregate_tps_per_tenant", Tags.of("tenant", tenant), new AtomicInteger(0)))
                .incrementAndGet();
    }

    // ===========================================================
    // ↩️ Rollback Metrics
    // ===========================================================
    public void recordRollback(String tenant, String type, String mode, int steps, long elapsedMs) {
        Tags tags = Tags.of("tenant", tenant, "type", type, "mode", mode);

        Counter.builder("coregate_saga_rollback_total")
                .description("Total Saga rollbacks executed")
                .tags(tags)
                .register(registry)
                .increment();

        Counter.builder("coregate_saga_rollback_step_total")
                .description("Total Saga steps reverted")
                .tags(tags)
                .register(registry)
                .increment(steps);

        Timer.builder("coregate_saga_rollback_duration_ms")
                .description("Total rollback duration per tenant/mode/type")
                .publishPercentileHistogram()
                .sla(Duration.ofMillis(50), Duration.ofMillis(200), Duration.ofMillis(500))
                .tags(tags)
                .register(registry)
                .record(Duration.ofMillis(elapsedMs));
    }

    // ===========================================================
    // 🔄 Lifecycle Metrics (por step)
    // ===========================================================
    public void recordStep(String tenant, String mode, String step, long elapsedMs, boolean success) {
        Tags tags = Tags.of("tenant", tenant, "mode", mode, "step", step, "status", success ? "SUCCESS" : "FAILURE");

        Counter.builder(success ? "coregate_saga_step_executed_total" : "coregate_saga_step_failed_total")
                .description("Saga step execution count")
                .tags(tags)
                .register(registry)
                .increment();

        Timer.builder("coregate_saga_step_duration_ms")
                .description("Saga step duration per tenant/mode/step")
                .publishPercentileHistogram()
                .sla(Duration.ofMillis(10), Duration.ofMillis(50), Duration.ofMillis(100), Duration.ofMillis(500))
                .tags(tags)
                .register(registry)
                .record(Duration.ofMillis(elapsedMs));
    }

    // ===========================================================
    // Métodos estáticos auxiliares
    // ===========================================================
    public void recordRollbackStatic(String tenant, String type, String mode, int steps, long elapsedMs) {
        if (staticRegistry == null) return;
        new CoreGatePerformanceMetrics(staticRegistry)
                .recordRollback(tenant, type, mode, steps, elapsedMs);
    }

    public void recordStepStatic(String tenant, String mode, String step, long elapsedMs, boolean success) {
        if (staticRegistry == null) return;
        new CoreGatePerformanceMetrics(staticRegistry)
                .recordStep(tenant, mode, step, elapsedMs, success);
    }
}
