package br.com.coregate.infrastructure.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.itextpdf.text.pdf.PdfReader;

@Slf4j
@RestController
@RequestMapping("/api/v1/reports")
@RequiredArgsConstructor
public class ReportVerificationController {

    private static final Pattern HASH_PATTERN =
            Pattern.compile("SHA-256:\\s*([0-9a-fA-F]{32,64})");

    /**
     * ✅ Endpoint: POST /api/v1/reports/verify
     * Recebe um PDF (multipart/form-data) e retorna o resultado JSON.
     */
    @PostMapping(value = "/verify", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String, Object>> verifyReport(@RequestParam("file") MultipartFile file) {
        Map<String, Object> result = new LinkedHashMap<>();
        try {
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "Arquivo PDF não enviado."));
            }

            // Salva temporariamente
            File temp = File.createTempFile("coregate_verify_", ".pdf");
            file.transferTo(temp);

            // Extrai hash embutido
            String embedded = extractEmbeddedHash(temp);
            if (embedded == null) {
                return ResponseEntity.badRequest().body(Map.of("error", "Hash SHA-256 não encontrado no PDF."));
            }

            // Calcula hash atual
            String computed = computeSha256(temp);

            boolean valid = computed.startsWith(embedded.substring(0, 32));
            result.put("filename", file.getOriginalFilename());
            result.put("valid", valid);
            result.put("embeddedHash", embedded);
            result.put("computedHash", computed);
            result.put("message", valid
                    ? "O relatório é autêntico e não foi modificado."
                    : "O relatório foi alterado ou corrompido.");
            result.put("verifiedAt", new Date().toString());

            temp.delete();
            return ResponseEntity.ok(result);

        } catch (Exception e) {
            log.error("💥 Erro ao verificar relatório: {}", e.getMessage(), e);
            result.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
        }
    }

    // =============================================================
    // 🔧 Utilitários internos (mesma lógica da versão CLI)
    // =============================================================

    private String extractEmbeddedHash(File pdfFile) throws IOException {
        PdfReader reader = new PdfReader(pdfFile.getAbsolutePath());
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i <= reader.getNumberOfPages(); i++) {
            byte[] content = reader.getPageContent(i);
            if (content != null)
                sb.append(new String(content, StandardCharsets.ISO_8859_1));
        }
        reader.close();
        Matcher m = HASH_PATTERN.matcher(sb.toString());
        return m.find() ? m.group(1) : null;
    }

    private String computeSha256(File pdfFile) throws Exception {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        try (FileInputStream fis = new FileInputStream(pdfFile)) {
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
