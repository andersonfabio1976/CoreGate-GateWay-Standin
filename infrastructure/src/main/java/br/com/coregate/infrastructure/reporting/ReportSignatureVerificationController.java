package br.com.coregate.infrastructure.reporting;

import br.com.coregate.infrastructure.security.ReportSignatureService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * 🌐 Endpoint público para validação de assinaturas digitais
 * dos relatórios JSON exportados via CoreGate.
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/reports")
@RequiredArgsConstructor
public class ReportSignatureVerificationController {

    private final ReportSignatureService signatureService;

    /**
     * ✅ Endpoint: POST /api/v1/reports/verify-signature
     * Recebe um JSON exportado (com campos 'signature' e 'algorithm') e valida contra a chave pública.
     *
     * Exemplo:
     * {
     *   "filename": "...",
     *   "valid": true,
     *   "signature": "MIIEpAIBAAKCAQEA..."
     * }
     */
    @PostMapping("/verify-signature")
    public ResponseEntity<Map<String, Object>> verifySignature(@RequestBody Map<String, Object> body) {
        try {
            if (!body.containsKey("signature")) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Campo 'signature' ausente no JSON."));
            }

            String signature = (String) body.remove("signature"); // remove para verificar o resto do conteúdo
            String payload = body.toString();

            boolean valid = signatureService.verify(payload, signature);
            String issuer = Optional.ofNullable((String) body.get("issuer"))
                    .orElse("CoreGate Secure Authority");

            return ResponseEntity.ok(Map.of(
                    "valid", valid,
                    "issuer", issuer,
                    "algorithm", "RSA-SHA256",
                    "verifiedAt", new Date().toString(),
                    "message", valid
                            ? "✅ Assinatura válida — relatório autêntico e não alterado."
                            : "⚠️ Assinatura inválida — o arquivo pode ter sido modificado."
            ));

        } catch (Exception e) {
            log.error("Erro ao verificar assinatura digital: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", e.getMessage()));
        }
    }
}
