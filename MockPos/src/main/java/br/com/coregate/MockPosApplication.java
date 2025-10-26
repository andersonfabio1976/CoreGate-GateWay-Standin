package br.com.coregate;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 🚀 Mock POS Stress Tool
 * Envia mensagens ISO8583 simuladas para o módulo Ingress com métricas de performance e modo evolutivo.
 */
@Slf4j
@SpringBootApplication
public class MockPosApplication implements CommandLineRunner {

    @Value("${mockpos.host:localhost}")
    private String host;

    @Value("${mockpos.port:8583}")
    private int port;

    @Value("${mockpos.pos.count:5}")
    private int posCount;

    @Value("${mockpos.messages.per.pos:100}")
    private int messagesPerPos;

    @Value("${mockpos.interval.seconds:3}")
    private int intervalSeconds;

    @Value("${mockpos.socket.timeout.ms:3000}")
    private int socketTimeoutMs;

    @Value("${mockpos.evolution.enabled:false}")
    private boolean evolutionEnabled;

    @Value("${mockpos.evolution.increment:5}")
    private int evolutionIncrement;

    @Value("${mockpos.evolution.max:100}")
    private int evolutionMax;

    @Value("${mockpos.evolution.cycle.seconds:30}")
    private int evolutionCycleSeconds;

    private static final List<String> MTIS = List.of("0100", "0110", "0200", "0210", "0400", "0410");
    private static final Random RANDOM = new Random();

    // Estatísticas globais
    private final AtomicLong totalSent = new AtomicLong(0);
    private final AtomicLong totalSuccess = new AtomicLong(0);
    private final AtomicLong totalErrors = new AtomicLong(0);
    private final AtomicLong totalLatency = new AtomicLong(0);
    private final AtomicLong totalTimeouts = new AtomicLong(0);

    public static void main(String[] args) {
        SpringApplication.run(MockPosApplication.class, args);
    }

    @Override
    public void run(String... args) {
        log.info("""
                =========================================================
                🧩 Starting MOCK POS LOAD TEST
                Host: {} | Port: {}
                POS: {} | Msg/Pos: {} | Interval: {}s
                Evolution Mode: {} | +{} POS every {}s (max {})
                =========================================================
                """, host, port, posCount, messagesPerPos, intervalSeconds,
                evolutionEnabled, evolutionIncrement, evolutionCycleSeconds, evolutionMax);

        startLoadTest(posCount);
    }

    private void startLoadTest(int initialPosCount) {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(200);
        ExecutorService monitorExecutor = Executors.newSingleThreadExecutor();

        Instant start = Instant.now();
        AtomicInteger activePos = new AtomicInteger(initialPosCount);

        // ✅ Monitor periódico de métricas
//        monitorExecutor.submit(() -> {
//            while (true) {
//                try {
//                    long sent = totalSent.get();
//                    long ok = totalSuccess.get();
//                    long err = totalErrors.get();
//                    double avgLatency = ok > 0 ? (double) totalLatency.get() / ok : 0;
//                    double successRate = sent > 0 ? (ok * 100.0 / sent) : 0;
//                    double tps = sent / (Duration.between(start, Instant.now()).toMillis() / 1000.0);
//
//                    log.info("""
//                            📊 Performance:
//                              ➤ Sent: {}
//                              ➤ Success: {}
//                              ➤ Errors: {}
//                              ➤ Avg Latency: {} ms
//                              ➤ TPS: {}/s
//                              ➤ Success Rate: {}%
//                            """, sent, ok, err, avgLatency, tps, successRate);
//
//                    Thread.sleep(10_000);
//                } catch (Exception e) {
//                    log.error("Monitor error: {}", e.getMessage());
//                }
//            }
//        });

        monitorExecutor.submit(() -> {
            long lastSent = 0;
            long lastSuccess = 0;
            long lastErrors = 0;
            long lastTimeouts = 0;
            long lastLatency = 0;
            int ciclo = 1;


            while (true) {
                try {
                    Thread.sleep(10_000); // período de exibição do ciclo

                    long sentNow = totalSent.get();
                    long okNow = totalSuccess.get();
                    long errNow = totalErrors.get();
                    long toNow = totalTimeouts.get();
                    long latNow = totalLatency.get();

                    long sentDiff = sentNow - lastSent;
                    long okDiff = okNow - lastSuccess;
                    long errDiff = errNow - lastErrors;
                    long toDiff = toNow - lastTimeouts;
                    long latDiff = latNow - lastLatency;

                    double successRate = sentDiff > 0 ? (okDiff * 100.0 / sentDiff) : 0.0;
                    double avgLatency = okDiff > 0 ? (double) latDiff / okDiff : 0.0;
                    double tps = sentDiff / 10.0; // 10 segundos fixos aqui

                    log.info("""
                    📊 Ciclo {} (últimos 10s)
                      ➤ Enviadas: {}
                      ➤ Sucesso: {} ({}%)
                      ➤ Timeout: {}
                      ➤ Erros: {}
                      ➤ Latência média: {} ms
                      ➤ TPS (médio): {}/s
                    """, ciclo++, sentDiff, okDiff, successRate, toDiff, errDiff, avgLatency, tps);

                    lastSent = sentNow;
                    lastSuccess = okNow;
                    lastErrors = errNow;
                    lastTimeouts = toNow;
                    lastLatency = latNow;

                } catch (Exception e) {
                    log.error("Erro no monitor: {}", e.getMessage());
                }
            }
        });


        // 🔸 Lança carga inicial
        launchPosThreads(scheduler, activePos.get());

        // 🔹 Modo evolutivo
        if (evolutionEnabled) {
            Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> {
                int next = activePos.addAndGet(evolutionIncrement);
                if (next <= evolutionMax) {
                    log.info("📈 Increasing POS count to {}", next);
                    launchPosThreads(scheduler, evolutionIncrement);
                } else {
                    log.info("🏁 Max capacity reached ({} POS)", evolutionMax);
                }
            }, evolutionCycleSeconds, evolutionCycleSeconds, TimeUnit.SECONDS);
        }
    }

    private void launchPosThreads(ScheduledExecutorService scheduler, int count) {
        for (int i = 0; i < count; i++) {
            String terminalId = String.format("TERM%03d", i + 1);
            String merchantId = String.format("MERCH%03d", i + 1);

            scheduler.scheduleAtFixedRate(() -> sendTransaction(terminalId, merchantId),
                    0, intervalSeconds, TimeUnit.MILLISECONDS);
        }
    }

    private void sendTransaction(String terminalId, String merchantId) {
        int seq = (int) totalSent.incrementAndGet();
        String mti = MTIS.get(RANDOM.nextInt(MTIS.size()));
        String iso = generateIsoMessage(seq, mti, merchantId, terminalId);
        byte[] isoBytes = iso.getBytes(StandardCharsets.ISO_8859_1);

        int length = isoBytes.length;
        byte[] header = new byte[]{(byte) ((length >> 8) & 0xFF), (byte) (length & 0xFF)};

        long start = System.nanoTime();

        try (Socket socket = new Socket(host, port);
             OutputStream out = socket.getOutputStream();
             InputStream in = socket.getInputStream()) {

            socket.setSoTimeout(socketTimeoutMs);

            out.write(header);
            out.write(isoBytes);
            out.flush();

            byte[] buffer = new byte[256];
            int read = in.read(buffer);

            long duration = (System.nanoTime() - start) / 1_000_000; // ms

            if (read > 0) {
                totalSuccess.incrementAndGet();
                totalLatency.addAndGet(duration);
                String resp = new String(buffer, 2, read - 2, StandardCharsets.ISO_8859_1);
                //log.info("✅ [{}] MTI={} | Resp='{}' | {}ms", terminalId, mti, resp, duration);
            } else {
                totalErrors.incrementAndGet();
                log.warn("⚠️ [{}] Timeout sem resposta", terminalId);
            }

            Thread.sleep(100); // evita RST TCP (tempo para flush)
        } catch (Exception e) {
            totalErrors.incrementAndGet();
            log.error("💥 [{}] Falha: {}", terminalId, e.getMessage());
        }
    }

    private static String generateIsoMessage(int seq, String mti, String merchantId, String terminalId) {
        String pan = String.format("400000000000%04d", seq % 10000);
        String processingCode = switch (mti) {
            case "0100", "0110" -> "000000";
            case "0200", "0210" -> "003000";
            case "0400", "0410" -> "200000";
            default -> "999999";
        };
        String amount = String.format("%012d", 10000 + (seq % 100));
        String trace = String.format("%06d", seq);
        String time = "121212";
        String date = "1026";
        String currency = "986";

        return mti + pan + processingCode + amount + trace + time + date + merchantId + terminalId + currency;
    }
}
