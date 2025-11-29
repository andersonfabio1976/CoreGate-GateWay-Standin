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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.BitSet;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
@SpringBootApplication(scanBasePackages = "br.com.coregate")
public class MockPosApplication implements CommandLineRunner {

    // ======================================================
    // CONFIGURAÇÕES
    // ======================================================
    @Value("${mockpos.host:localhost}")
    private String host;

    @Value("${mockpos.port:8583}")
    private int port;

    @Value("${mockpos.pos.count:1}")
    private int initialPos;

    @Value("${mockpos.interval.seconds:5}")
    private int intervalSeconds;

    @Value("${mockpos.socket.timeout.ms:3000}")
    private int socketTimeoutMs;

    @Value("${mockpos.evolution.enabled:false}")
    private boolean evolutionEnabled;

    @Value("${mockpos.evolution.increment:10}")
    private int evolutionIncrement;

    @Value("${mockpos.evolution.max:450}")
    private int evolutionMax;

    @Value("${mockpos.evolution.cycle.seconds:50}")
    private int evolutionCycleSeconds;

    // ======================================================
    // MÉTRICAS GERAIS
    // ======================================================
    private final AtomicLong totalSent = new AtomicLong(0);
    private final AtomicLong totalSuccess = new AtomicLong(0);
    private final AtomicLong totalErrors = new AtomicLong(0);

    private final AtomicLong tpsCounter = new AtomicLong(0);
    private final Deque<Long> tpsHistory = new ArrayDeque<>();
    private long tpsMax = 0;

    private int posPeak = 0;
    private volatile int activePosCount = 0;

    // ======================================================
    // MÉTRICA: TEMPO DE APROVAÇÃO MÉDIA
    // ======================================================
    private final AtomicLong approvalTimeTotal = new AtomicLong(0);  // soma total dos tempos
    private final AtomicLong approvalCount = new AtomicLong(0);      // número de transações concluídas

    private static final Random RANDOM = new Random();

    private ExecutorService posWorkers;

    public static void main(String[] args) {
        SpringApplication.run(MockPosApplication.class, args);
    }

    @Override
    public void run(String... args) {

        log.info("""
                =========================================================
                🚀 MOCK POS — LOAD GENERATOR
                Host: {} | Port: {}
                POS Iniciais: {} | Intervalo: {}s
                Evolução: {} | Inc: {} | Máx: {}
                =========================================================
                """,
                host, port, initialPos, intervalSeconds,
                evolutionEnabled, evolutionIncrement, evolutionMax);

        posWorkers = Executors.newFixedThreadPool(evolutionMax);

        ScheduledExecutorService reporter = Executors.newSingleThreadScheduledExecutor();
        ScheduledExecutorService evolution = Executors.newSingleThreadScheduledExecutor();

        for (int i = 0; i < initialPos; i++) {
            startNewPos(i + 1);
        }

        // RESUMO DE 10s
        reporter.scheduleAtFixedRate(this::printSummary, 10, 10, TimeUnit.SECONDS);

        if (evolutionEnabled) {
            evolution.scheduleAtFixedRate(this::evolve,
                    evolutionCycleSeconds, evolutionCycleSeconds, TimeUnit.SECONDS);
        }
    }

    // ======================================================
    // AUMENTO DINÂMICO DE POS
    // ======================================================
    private void evolve() {
        int current = activePosCount;

        if (current >= evolutionMax) {
            log.info("⚠️ Evolução pausada — limite máximo atingido ({}).", evolutionMax);
            return;
        }

        log.info("🔥 Evolução ativada: adicionando {} POS...", evolutionIncrement);

        for (int i = 0; i < evolutionIncrement; i++) {
            int id = activePosCount + 1;
            if (id > evolutionMax) break;
            startNewPos(id);
        }
    }

    // ======================================================
    // INICIA UM NOVO POS
    // ======================================================
    private void startNewPos(int id) {
        activePosCount++;

        String terminal = "T%07d".formatted(id);
        String merchant = "M%014d".formatted(id);

        posWorkers.submit(() -> runPosSession(terminal, merchant));

        if (activePosCount > posPeak) posPeak = activePosCount;

        log.info("🔌 POS {} iniciado (Ativos = {})", terminal, activePosCount);
    }

    // ======================================================
    // LOOP DO POS (CONEXÃO CONTÍNUA)
    // ======================================================
    private void runPosSession(String terminalId, String merchantId) {

        while (true) {
            try (Socket socket = new Socket(host, port)) {
                socket.setSoTimeout(socketTimeoutMs);

                OutputStream out = socket.getOutputStream();
                InputStream in = socket.getInputStream();

                while (true) {
                    long seq = totalSent.incrementAndGet();
                    String iso = generateRealIso8583(seq, terminalId, merchantId);
                    byte[] isoBytes = iso.getBytes(StandardCharsets.ISO_8859_1);

                    int length = isoBytes.length;
                    byte[] header = {(byte) (length >> 8), (byte) length};

                    long start = System.currentTimeMillis();

                    // logIsoSend(...) — DESATIVADO
                    // logIsoSend(terminalId, seq, iso, length);

                    out.write(header);
                    out.write(isoBytes);
                    out.flush();

                    tpsCounter.incrementAndGet();

                    byte[] buf = new byte[512];
                    int read;

                    try {
                        read = in.read(buf);
                    } catch (Exception e) {
                        long elapsed = System.currentTimeMillis() - start;
                        totalErrors.incrementAndGet();
                        // logIsoNoData(...) — DESATIVADO
                        throw e;
                    }

                    long elapsed = System.currentTimeMillis() - start;

                    if (read > 0) {
                        totalSuccess.incrementAndGet();

                        // REGISTRA TEMPO PARA "TEMPO DE APROVAÇÃO MÉDIA"
                        approvalTimeTotal.addAndGet(elapsed);
                        approvalCount.incrementAndGet();

                        // logIsoReceive(...) — DESATIVADO
                        // logIsoReceive(...)
                    } else {
                        totalErrors.incrementAndGet();
                    }

                    Thread.sleep(intervalSeconds * 1000L);
                }

            } catch (Exception e) {
                totalErrors.incrementAndGet();
                try { Thread.sleep(800); } catch (InterruptedException ignored) {}
            }
        }
    }

    // ======================================================
    // RESUMO A CADA 10s
    // ======================================================
    private void printSummary() {

        long tps = tpsCounter.getAndSet(0);
        if (tps > tpsMax) tpsMax = tps;

        tpsHistory.addLast(tps);
        if (tpsHistory.size() > 20) tpsHistory.removeFirst();

        long tpsAvg = Math.round(tpsHistory.stream().mapToLong(x -> x).average().orElse(0));
        String trend = detectTrend(tpsHistory);

        long total = totalSent.get();
        long ok = totalSuccess.get();
        long err = totalErrors.get();

        double okPct = total == 0 ? 0 : (ok * 100.0 / total);
        double errPct = total == 0 ? 0 : (err * 100.0 / total);

        // Tempo de aprovação média
        long approvalAvg = approvalCount.get() == 0
                ? 0
                : approvalTimeTotal.get() / approvalCount.get();

        log.info("""
                =========================================================
                📊 LOAD SUMMARY — Últimos 10s
                POS Ativos: {}
                TPS Atual: {}     (REAL)
                TPS Médio: {}     (REAL)
                TPS Máximo: {}    (REAL)
                Tendência: {}
                ---------------------------------------------------------
                ⏱️ Tempo de Aprovação Média: {} ms
                ---------------------------------------------------------
                Total Enviadas: {}
                Sucesso: {} ({}%)
                Erros: {} ({}%)
                ---------------------------------------------------------
                Pico Máximo de POS: {}
                Histórico TPS: {}
                =========================================================
                """,
                activePosCount,
                tps,
                tpsAvg,
                tpsMax,
                trend,

                approvalAvg,

                total,
                ok, String.format("%.2f", okPct),
                err, String.format("%.2f", errPct),

                posPeak,
                tpsHistory
        );
    }

    // ======================================================
    // TENDÊNCIA DO TPS
    // ======================================================
    private String detectTrend(Deque<Long> hist) {
        if (hist.size() < 4) return "➡ Estável";

        long first = hist.getFirst();
        long last = hist.getLast();

        if (last > first * 1.15) return "📈 Subindo";
        if (last < first * 0.85) return "📉 Caindo";
        return "➡ Estável";
    }

    // ======================================================
    // LOGS (DESATIVADOS)
    // ======================================================
    private void logIsoSend(String terminalId, long seq, String iso, int length) {}
    private void logIsoReceive(String terminalId, long seq, int readBytes, long elapsedMs) {}
    private void logIsoNoData(String terminalId, long seq, int readBytes, long elapsedMs) {}

    // ======================================================
    // ISO8583 + HELPERS (SEM MUDANÇAS)
    // ======================================================

    private String generateRealIso8583(long seq, String terminalId, String merchantId) {
        return switch (pickRandomMti()) {
            case "0100" -> generate0100(seq, terminalId, merchantId);
            case "0200" -> generate0200(seq, terminalId, merchantId);
            case "0400" -> generate0400(seq, terminalId, merchantId);
            default -> throw new IllegalStateException("MTI inesperado");
        };
    }

    private String pickRandomMti() {
        return switch (RANDOM.nextInt(3)) {
            case 0 -> "0100";
            case 1 -> "0200";
            default -> "0400";
        };
    }

    private String generate0100(long seq, String terminalId, String merchantId) {
        String mti = "0100";
        String pan = randomPanDigits();
        String procCode = "000000";
        String amount = randomAmount();
        String stan = randomStan(seq);
        String mcc = randomMcc();
        String channel = randomEntryMode();
        String currency = "986";

        BitSet bitmap = new BitSet(64);
        activate(bitmap, 2, 3, 4, 11, 18, 22, 41, 42, 49);

        return new StringBuilder()
                .append(mti)
                .append(bitSetToHex(bitmap))
                .append(fixLen(pan, 19))
                .append(procCode)
                .append(amount)
                .append(stan)
                .append(mcc)
                .append(channel)
                .append(fixLen(terminalId, 8))
                .append(fixLen(merchantId, 15))
                .append(currency)
                .toString();
    }

    private String generate0200(long seq, String terminalId, String merchantId) {
        String mti = "0200";
        String pan = randomPanDigits();
        String procCode = "000001";
        String amount = randomAmount();
        LocalDateTime now = LocalDateTime.now();

        String f7 = now.format(DateTimeFormatter.ofPattern("MMddHHmmss"));
        String stan = randomStan(seq);
        String f12 = now.format(DateTimeFormatter.ofPattern("HHmmss"));
        String f13 = now.format(DateTimeFormatter.ofPattern("MMdd"));
        String mcc = randomMcc();
        String channel = randomEntryMode();
        String acquiringId = randomAcquiringId();
        String rrn = randomRrn(seq);
        String currency = "986";

        BitSet bitmap = new BitSet(64);
        activate(bitmap, 2, 3, 4, 7, 11, 12, 13, 18, 22, 32, 37, 41, 42, 49);

        return new StringBuilder()
                .append(mti)
                .append(bitSetToHex(bitmap))
                .append(fixLen(pan, 19))
                .append(procCode)
                .append(amount)
                .append(f7)
                .append(stan)
                .append(f12)
                .append(f13)
                .append(mcc)
                .append(channel)
                .append(llvar(acquiringId))
                .append(rrn)
                .append(fixLen(terminalId, 8))
                .append(fixLen(merchantId, 15))
                .append(currency)
                .toString();
    }

    private String generate0400(long seq, String terminalId, String merchantId) {
        String mti = "0400";
        String pan = randomPanDigits();
        String procCode = "200000";
        String amount = randomAmount();
        String stan = randomStan(seq);
        String mcc = randomMcc();
        String channel = randomEntryMode();
        String currency = "986";

        BitSet bitmap = new BitSet(64);
        activate(bitmap, 2, 3, 4, 11, 18, 22, 41, 42, 49);

        return new StringBuilder()
                .append(mti)
                .append(bitSetToHex(bitmap))
                .append(fixLen(pan, 19))
                .append(procCode)
                .append(amount)
                .append(stan)
                .append(mcc)
                .append(channel)
                .append(fixLen(terminalId, 8))
                .append(fixLen(merchantId, 15))
                .append(currency)
                .toString();
    }

    private static void activate(BitSet b, int... fields) {
        for (int f : fields) b.set(f - 1);
    }

    private static String randomPanDigits() {
        int len = 16 + RANDOM.nextInt(4);
        StringBuilder sb = new StringBuilder("400000");
        while (sb.length() < len) sb.append(RANDOM.nextInt(10));
        return sb.toString();
    }

    private static String randomAmount() {
        int base = 100;
        int extra = RANDOM.nextInt(500000);
        long cents = base + extra;
        return String.format("%012d", cents);
    }

    private static String randomStan(long seq) {
        return String.format("%06d", seq % 1_000_000L);
    }

    private static String randomMcc() {
        String[] mccs = {"4111", "4789", "5311", "5411", "5999", "6011", "7011", "7999"};
        return mccs[RANDOM.nextInt(mccs.length)];
    }

    private static String randomEntryMode() {
        return switch (RANDOM.nextInt(3)) {
            case 0 -> "051";
            case 1 -> "071";
            default -> "901";
        };
    }

    private static String randomAcquiringId() {
        int len = 6 + RANDOM.nextInt(6);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < len; i++) sb.append(RANDOM.nextInt(10));
        return sb.toString();
    }

    private static String randomRrn(long seq) {
        long base = seq % 1_000_000_000_000L;
        return String.format("%012d", base);
    }

    private static String bitSetToHex(BitSet bitSet) {
        byte[] bytes = new byte[8];
        for (int i = 0; i < 64; i++) {
            if (bitSet.get(i)) {
                bytes[i / 8] |= 1 << (7 - (i % 8));
            }
        }
        StringBuilder sb = new StringBuilder();
        for (byte aByte : bytes) {
            sb.append(String.format("%02X", aByte));
        }
        return sb.toString();
    }

    private static String fixLen(String s, int len) {
        if (s == null) s = "";
        if (s.length() > len) return s.substring(0, len);
        return String.format("%-" + len + "s", s);
    }

    private static String llvar(String s) {
        if (s == null) s = "";
        return String.format("%02d%s", s.length(), s);
    }
}
