package br.com.coregate.infrastructure.reporting;

import io.micrometer.core.instrument.MeterRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

/**
 * 🧾 Gera automaticamente um resumo textual executivo de SLA e disponibilidade,
 *    com base nas métricas expostas via Prometheus/Micrometer.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class OperationalReportGenerator {

    private final MeterRegistry meterRegistry;

    // Mapeia nomes das métricas
    private static final String MODE_STATE = "coregate_operational_mode_state";
    private static final String LATENCY = "coregate_latency_avg_ms";
    private static final String SUCCESS = "coregate_success_rate";
    private static final String TPS = "coregate_tps_rate";

    // 🕒 Executa diariamente (pode ajustar para semanal ou mensal)
    @Scheduled(cron = "0 0 6 * * *") // 06h00 todo dia
    public void generateExecutiveSummary() {
        double mode = getValue(MODE_STATE);
        double latency = getValue(LATENCY);
        double success = getValue(SUCCESS);
        double tps = getValue(TPS);

        String modeDesc = switch ((int) mode) {
            case 0 -> "Gateway";
            case 1 -> "Stand-In Automático";
            case 2 -> "Stand-In Solicitado (Manual)";
            default -> "Indefinido";
        };

        String report = buildSummary(modeDesc, latency, success, tps);

        log.info("""
                ==========================================================
                🧾 CoreGate Executive Summary Report
                Generated at: {}
                {}
                ==========================================================
                """, LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                report
        );
    }

    private double getValue(String metricName) {
        return Optional.ofNullable(meterRegistry.find(metricName).gauge())
                .map(g -> g.value())
                .orElse(Double.NaN);
    }

    private String buildSummary(String mode, double latency, double success, double tps) {
        StringBuilder sb = new StringBuilder();
        sb.append("🧩 Modo operacional atual: ").append(mode).append("\n");

        sb.append(String.format("📈 Taxa média de sucesso: %.2f %%\n", success));
        sb.append(String.format("⚡ Latência média: %.2f ms\n", latency));
        sb.append(String.format("💳 TPS médio: %.2f transações/segundo\n", tps));

        if (mode.equals("Gateway")) {
            sb.append("\n✅ O sistema operou em modo normal com alta disponibilidade.");
        } else if (mode.contains("Automático")) {
            sb.append("\n🟠 O modo Stand-In automático foi acionado — failover ativo, desempenho controlado.");
        } else if (mode.contains("Manual")) {
            sb.append("\n🔴 O modo Stand-In manual foi solicitado pelo emissor — operação contingenciada.");
        }

        sb.append("\n📅 SLA consolidado: ").append(calculateSLA(success, latency));

        return sb.toString();
    }

    private String calculateSLA(double success, double latency) {
        if (Double.isNaN(success) || Double.isNaN(latency))
            return "indisponível (métricas incompletas)";

        double baseSLA = success / 100.0;
        double latencyPenalty = latency > 200 ? 0.98 : (latency > 100 ? 0.995 : 1.0);
        double result = baseSLA * latencyPenalty * 100;
        return String.format("%.3f %%", result);
    }
}


//Perfeito, Anderson ⚙️🔥 — agora vem a última peça do módulo de observabilidade executiva CoreGate:
//o “Executive Summary Generator”, que cria o resumo textual automático de SLA e disponibilidade a partir das métricas Prometheus.
//
//Esse texto é gerado de forma natural, auditável e compatível com relatórios PDF ou logs executivos (como a Fiserv, Stone e Adyen usam nos relatórios mensais de performance).