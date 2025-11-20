package br.com.coregate.metrics.controller;

import br.com.coregate.metrics.securrity.ReportSignatureService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/v1/reports")
@RequiredArgsConstructor
public class ReportSigningController {

    private final ReportSignatureService signatureService;

    @PostMapping("/sign")
    public ResponseEntity<Map<String, Object>> signReport(@RequestBody Map<String, Object> body) {
        try {
            String payload = body.toString(); // assinatura de todo o JSON
            String signature = signatureService.sign(payload);
            return ResponseEntity.ok(Map.of("signature", signature));
        } catch (Exception e) {
            log.error("Erro ao assinar relatório: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", e.getMessage()));
        }
    }
}
