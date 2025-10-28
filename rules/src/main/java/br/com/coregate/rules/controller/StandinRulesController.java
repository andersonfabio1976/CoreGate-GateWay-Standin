package br.com.coregate.rules.controller;

import br.com.coregate.rules.auth.StandinRulesEngine;
import br.com.coregate.rules.model.StandinDecision;
import br.com.coregate.rules.model.TransactionFact;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/standin")
@RequiredArgsConstructor
public class StandinRulesController {

    private final StandinRulesEngine engine;

    @PostMapping("/authorize")
    public ResponseEntity<StandinDecision> authorize(
            @RequestHeader("X-Tenant-Id") String tenantId,
            @RequestBody TransactionFact request
    ) {
        StandinDecision decision = engine.evaluate(request, tenantId);
        return ResponseEntity.ok(decision);
    }

}
