package br.com.coregate.application.port.in;

public interface AdviceGenerationUseCase {
    void generateAdvice(String tenantId, String transactionId);
}
