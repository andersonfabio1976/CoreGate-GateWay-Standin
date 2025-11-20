package br.com.coregate.application.ports.in;

public interface AdviceGenerationUseCase {
    void generateAdvice(String tenantId, String transactionId);
}
