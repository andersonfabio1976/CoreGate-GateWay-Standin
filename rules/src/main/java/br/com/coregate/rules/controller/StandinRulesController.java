//package br.com.coregate.rules.controller;
//
//import br.com.coregate.core.contracts.dto.rules.StandinDecision;
//import br.com.coregate.core.contracts.dto.rules.TransactionFactDto;
//import br.com.coregate.rules.auth.StandinRulesEngine;
//import lombok.RequiredArgsConstructor;
//
//@RestController
//@RequestMapping("/standin")
//@RequiredArgsConstructor
//public class StandinRulesController {
//
//    private final StandinRulesEngine engine;
//
//    @PostMapping("/authorize")
//    public ResponseEntity<StandinDecision> authorize(
//            @RequestHeader("X-Tenant-Id") String tenantId,
//            @RequestBody TransactionFactDto request
//    ) {
//        StandinDecision decision = engine.evaluate(request);
//        return ResponseEntity.ok(decision);
//    }
//
//}
