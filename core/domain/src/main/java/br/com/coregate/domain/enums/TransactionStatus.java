package br.com.coregate.domain.enums;

public enum TransactionStatus {
    TRANSACTION_STATUS_UNSPECIFIED,  // ✅ igual ao proto
    PENDING,
    AUTHORIZED,
    REJECTED,
    SETTLED,
    CANCELED,
    END,
    ERROR,
    UNRECOGNIZED                   // ✅ mantém para compatibilidade
}
