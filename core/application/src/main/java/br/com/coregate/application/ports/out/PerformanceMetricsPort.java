package br.com.coregate.application.ports.out;

public interface PerformanceMetricsPort {
    void recordRollbackStatic(String tenant, String type, String mode, int steps, long elapsedMs);
    void recordStepStatic(String tenant, String mode, String step, long elapsedMs, boolean success);
    void recordTransaction(String tenant, String type, long elapsedMs, boolean success);
}
