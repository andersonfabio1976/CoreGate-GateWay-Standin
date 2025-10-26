package br.com.coregate.orchestrator.saga.step;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Error {

    public static void execute(Exception e) {
        log.error("❌ Saga failed: {}", e.getMessage(), e);
        log.warn("⚠️ Saga completed with rollback (context restored to safe state).");
    }

}

