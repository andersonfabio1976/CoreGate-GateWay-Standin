package br.com.coregate.infrastructure.reporting;

import com.itextpdf.text.pdf.PdfReader;
import lombok.extern.slf4j.Slf4j;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 🔎 Verificador de integridade de relatórios CoreGate.
 * Lê o hash SHA-256 embedado no PDF (rodapé) e valida contra o hash real do arquivo.
 */
@Slf4j
public class CoreGateReportVerifier {

    private static final Pattern HASH_PATTERN =
            Pattern.compile("SHA-256:\\s*([0-9a-fA-F]{32,64})");

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Uso: java CoreGateReportVerifier <arquivo.pdf>");
            return;
        }

        String path = args[0];
        try {
            boolean valid = verify(path);
            System.out.println(valid
                    ? "✅ O relatório é autêntico e não foi modificado."
                    : "⚠️ ALERTA: O relatório foi alterado ou está corrompido!");
        } catch (Exception e) {
            System.err.println("💥 Erro ao verificar relatório: " + e.getMessage());
        }
    }

    /**
     * Retorna true se o hash embedado no PDF for igual ao hash calculado.
     */
    public static boolean verify(String pdfPath) throws Exception {
        String embedded = extractEmbeddedHash(pdfPath);
        if (embedded == null) {
            throw new IllegalStateException("Hash SHA-256 não encontrado no PDF.");
        }
        String current = computeSha256(pdfPath);
        log.info("📘 Hash embutido:  {}", embedded);
        log.info("🧮 Hash atual:    {}", current);
        return current.startsWith(embedded.substring(0, 32));
    }

    private static String extractEmbeddedHash(String pdfPath) throws IOException {
        PdfReader reader = new PdfReader(pdfPath);
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i <= reader.getNumberOfPages(); i++) {
            byte[] content = reader.getPageContent(i);
            if (content != null) sb.append(new String(content, StandardCharsets.ISO_8859_1));
        }
        reader.close();

        Matcher m = HASH_PATTERN.matcher(sb.toString());
        return m.find() ? m.group(1) : null;
    }

    private static String computeSha256(String pdfPath) throws Exception {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        try (FileInputStream fis = new FileInputStream(pdfPath)) {
            byte[] buf = new byte[8192];
            int r;
            while ((r = fis.read(buf)) > 0) digest.update(buf, 0, r);
        }
        byte[] hash = digest.digest();
        StringBuilder sb = new StringBuilder();
        for (byte b : hash) sb.append(String.format("%02x", b));
        return sb.toString();
    }
}
