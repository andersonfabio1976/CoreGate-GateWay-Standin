package br.com.coregate.rules.metrics;

import br.com.coregate.core.contracts.dto.rules.DecisionOutcome;
import io.micrometer.core.instrument.*;
import org.springframework.stereotype.Component;

@Component
public class StandinMetrics {
    private final MeterRegistry registry;
    private final DistributionSummary amountSummary;

    public StandinMetrics(MeterRegistry registry) {
        this.registry = registry;
        this.amountSummary = DistributionSummary.builder("coregate_standin_amount_cents")
                .description("Distribuição de valores autorizados/negados em stand-in")
                .register(registry);
    }

    public void countDecision(DecisionOutcome outcome, String reason, long amountCents, String mcc) {
        Counter.builder("coregate_standin_decision_total")
                .description("Decisões de stand-in")
                .tag("outcome", outcome.name())
                .tag("reason", reason)
                .tag("mcc", mcc == null ? "UNKNOWN" : mcc)
                .register(registry)
                .increment();
        amountSummary.record(amountCents);
    }
}
