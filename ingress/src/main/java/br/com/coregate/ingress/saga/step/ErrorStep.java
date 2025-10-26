package br.com.coregate.ingress.saga.step;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ErrorStep {

    public static void execute(Exception e) {
        log.error("💥 Erro na Saga Netty: {}", e.getMessage(), e);
        log.warn("⚠️ Saga finalizada com rollback completo.");
    }
}
