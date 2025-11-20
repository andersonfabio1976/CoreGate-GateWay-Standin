package br.com.coregate;

import br.com.coregate.core.contracts.dto.transaction.TransactionCommand;
import br.com.coregate.core.contracts.iso8583.parsing.ParserIso8583;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;

@Slf4j
@SpringBootApplication(scanBasePackages = "br.com.coregate")
public class MockPosApplication implements CommandLineRunner {

    @Value("${mockpos.host:localhost}")
    private String host;

    @Value("${mockpos.port:8583}")
    private int port;

    @Value("${mockpos.pos.count:1}")
    private int posCount;

    @Value("${mockpos.interval.ms:250}")
    private int intervalMs;

    @Value("${mockpos.socket.timeout.ms:3000}")
    private int socketTimeoutMs;

    @Autowired
    private ParserIso8583 parserIso8583;

    private final AtomicLong totalSent = new AtomicLong(0);
    private final AtomicLong totalSuccess = new AtomicLong(0);
    private final AtomicLong totalErrors = new AtomicLong(0);
    private final AtomicLong tpsCounter = new AtomicLong(0);
    private final AtomicLong idempotentHits = new AtomicLong(0);

    private final Map<String, Boolean> recentPan = new LinkedHashMap<>(10_000, 0.75f, true) {
        @Override protected boolean removeEldestEntry(Map.Entry<String, Boolean> eldest) {
            return this.size() > 10_000;
        }
    };

    private static final Random RANDOM = new Random();

    public static void main(String[] args) {
        SpringApplication.run(MockPosApplication.class, args);
    }

    @Override
    public void run(String... args) {

        log.info("""
                ===========================================
                🚀 MOCK POS (ISO8583 Real Generator)
                Host: {} | Port: {}
                POS Workers: {} | Interval: {}ms
                ===========================================
                """, host, port, posCount, intervalMs);

        ScheduledExecutorService workers = Executors.newScheduledThreadPool(posCount);
        ScheduledExecutorService reporter = Executors.newSingleThreadScheduledExecutor();

        for (int i = 0; i < posCount; i++) {
            String terminal = "T%07d".formatted(i + 1);
            String merchant = "M%014d".formatted(i + 1);

            workers.scheduleAtFixedRate(
                    () -> sendTransaction(terminal, merchant),
                    0,
                    intervalMs,
                    TimeUnit.MILLISECONDS
            );
        }

        reporter.scheduleAtFixedRate(this::printSummary, 7, 7, TimeUnit.SECONDS);
    }

    private void sendTransaction(String terminalId, String merchantId) {
        long seq = totalSent.incrementAndGet();

        String iso = generateRealIso8583(seq, terminalId, merchantId);
        byte[] isoBytes = iso.getBytes(StandardCharsets.ISO_8859_1);

        String pan = extractPan(iso);
        synchronized (recentPan) {
            if (recentPan.containsKey(pan)) idempotentHits.incrementAndGet();
            recentPan.put(pan, true);
        }

        tpsCounter.incrementAndGet();

        try (Socket socket = new Socket(host, port)) {
            socket.setSoTimeout(socketTimeoutMs);
            OutputStream out = socket.getOutputStream();
            InputStream in = socket.getInputStream();

            int length = isoBytes.length;
            byte[] header = {(byte) (length >> 8), (byte) length};

            out.write(header);
            out.write(isoBytes);
            out.flush();

            byte[] buf = new byte[512];
            int read = in.read(buf);

            if (read > 0) totalSuccess.incrementAndGet();
            else totalErrors.incrementAndGet();

        } catch (Exception e) {
            totalErrors.incrementAndGet();
        }
    }

    private void printSummary() {
        long tps = tpsCounter.getAndSet(0);

        log.info("""
                ===========================================
                📊 MOCK POS SUMMARY (7s)
                TPS: {}
                Total Enviadas: {}
                Total OK: {}
                Total Erros: {}
                Idempotentes: {}
                ===========================================
                """, tps,
                totalSent.get(),
                totalSuccess.get(),
                totalErrors.get(),
                idempotentHits.get());
    }

    // ---------------------------------------------------------
    // 🔥 GERAÇÃO REALISTA DE ISO8583 (TESTE / ADQUIRENTE)
    // ---------------------------------------------------------

    private String generateRealIso8583(long seq, String terminalId, String merchantId) {

        String mti = pickRandomMti();
        String pan = randomPan();
        String procCode = procCodeForMti(mti);
        String amount = "%012d".formatted(10000 + RANDOM.nextInt(50000));
        String stan = "%06d".formatted(seq % 999999);

        LocalDateTime now = LocalDateTime.now();
        String timeLocal = now.format(DateTimeFormatter.ofPattern("HHmmss"));
        String dateLocal = now.format(DateTimeFormatter.ofPattern("MMdd"));

        String mcc = randomMcc();
        String posEntry = randomEntryMode();
        String currency = "986";

        BitSet bitmap = new BitSet(64);
        activate(bitmap, 2, 3, 4, 7, 11, 12, 13, 18, 22, 41, 42, 49);

        StringBuilder iso = new StringBuilder();
        iso.append(mti);
        iso.append(bitSetToHex(bitmap));

        iso.append(llvar(pan));       // f2
        iso.append(procCode);         // f3
        iso.append(amount);           // f4
        iso.append(stan);             // f11
        iso.append(timeLocal);        // f12
        iso.append(dateLocal);        // f13
        iso.append(mcc);              // f18
        iso.append(posEntry);         // f22
        iso.append(fixLen(terminalId, 8));   // f41
        iso.append(fixLen(merchantId, 15));  // f42
        iso.append(currency);         // f49

        return iso.toString();
    }

    private static String pickRandomMti() {
        return switch (RANDOM.nextInt(3)) {
            case 0 -> "0100"; // auth
            case 1 -> "0200"; // financial
            default -> "0400"; // reversal
        };
    }

    private static String procCodeForMti(String mti) {
        return switch (mti) {
            case "0100" -> "000000";
            case "0200" -> "000001";
            case "0400" -> "200000";
            default -> "999999";
        };
    }

    private static String randomPan() {
        int suffix = 100000 + RANDOM.nextInt(900000);
        return "400000" + suffix;
    }

    private static String randomMcc() {
        String[] mccs = {"4111", "4789", "5311", "5411", "5999", "6011", "7011", "7999"};
        return mccs[RANDOM.nextInt(mccs.length)];
    }

    private static String randomEntryMode() {
        return switch (RANDOM.nextInt(3)) {
            case 0 -> "051"; // chip
            case 1 -> "071"; // contactless
            default -> "901"; // e-commerce
        };
    }

    private static void activate(BitSet b, int... fields) {
        for (int f : fields) b.set(f - 1);
    }

    private static String bitSetToHex(BitSet bitSet) {
        byte[] bytes = new byte[8];
        for (int i = 0; i < 64; i++)
            if (bitSet.get(i)) bytes[i / 8] |= 1 << (7 - (i % 8));

        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) sb.append("%02X".formatted(b));
        return sb.toString();
    }

    private static String fixLen(String s, int len) {
        return String.format("%-" + len + "s", s);
    }

    private static String llvar(String s) {
        return "%02d%s".formatted(s.length(), s);
    }

    private static String extractPan(String iso) {
        int bitmapEnd = 4 + 16;
        int ll = Integer.parseInt(iso.substring(bitmapEnd, bitmapEnd + 2));
        return iso.substring(bitmapEnd + 2, bitmapEnd + 2 + ll);
    }
}
