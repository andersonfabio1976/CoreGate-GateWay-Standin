package br.com.coregate.domain.enums;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum ProcessingCodeType {

    PROCESSING_CODE_UNKNOWN("0","PROCESSING_CODE_UNKNOWN","PROCESSING_CODE_UNKNOWN"),
    // --- 💳 Operações financeiras ---
    PURCHASE_DEBIT("000000", "DÉBITO", "Compra no débito (Purchase Debit)"),
    PURCHASE_CREDIT("000001", "CRÉDITO", "Compra no crédito (Purchase Credit)"),
    PURCHASE_INSTALLMENT("610000", "CRÉDITO", "Compra parcelada (Installment Purchase)"),
    PURCHASE_CASHBACK("600000", "DÉBITO", "Compra com saque (Cashback)"),

    // --- 🏧 Saques ---
    CASH_WITHDRAWAL("010000", "SAQUE", "Saque em ATM (Cash Withdrawal)"),
    CASH_ADVANCE("700000", "CRÉDITO", "Adiantamento de crédito (Cash Advance)"),

    // --- 🧾 Pagamentos ---
    BILL_PAYMENT("100000", "PAGAMENTO", "Pagamento de conta / boleto"),
    LOAN_PAYMENT("103000", "PAGAMENTO", "Pagamento de fatura ou empréstimo"),

    // --- 💱 Transferências ---
    FUNDS_TRANSFER("200000", "TRANSFERÊNCIA", "Transferência entre contas"),
    FUNDS_TRANSFER_SAVINGS("202000", "TRANSFERÊNCIA", "Transferência poupança → corrente"),

    // --- 🧮 Consultas ---
    BALANCE_INQUIRY("300000", "CONSULTA", "Consulta de saldo / limite"),
    STATEMENT_INQUIRY("311000", "CONSULTA", "Consulta de extrato"),

    // --- 🧾 Pré-autorização / reserva ---
    PRE_AUTH("000090", "PRÉ-AUTORIZAÇÃO", "Reserva de limite (Pré-autorização)"),
    PRE_AUTH_CAPTURE("000091", "PRÉ-AUTORIZAÇÃO", "Captura da pré-autorização"),
    PRE_AUTH_CANCEL("000092", "PRÉ-AUTORIZAÇÃO", "Cancelamento da pré-autorização"),

    // --- 🔄 Estornos / reversais ---
    REVERSAL("400000", "ESTORNO", "Reversal total da transação"),
    PARTIAL_REVERSAL("400010", "ESTORNO", "Reversal parcial da transação"),
    REVERSAL_PRE_AUTH("430000", "ESTORNO", "Reversal de pré-autorização"),

    // --- ⚙️ Administrativos / testes ---
    CHARGEBACK("500000", "AJUSTE", "Chargeback / ajuste financeiro"),
    RELOAD_PREPAID("060000", "CARGA", "Carga de cartão pré-pago"),
    ECHO_TEST("910000", "TESTE", "Echo test / HealthCheck POS"),
    UNRECOGNIZED("-1","UNRECOGNIZED","UNRECOGNIZED");

    private final String code;
    private final String category;
    private final String description;

    ProcessingCodeType(String code, String category, String description) {
        this.code = code;
        this.category = category;
        this.description = description;
    }

    // 🔍 Busca por código (campo 3 ISO8583)
    public static ProcessingCodeType fromCode(String code) {
        return Arrays.stream(values())
                .filter(p -> p.code.equals(code))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Código de processamento desconhecido: " + code));
    }

    // 🔍 Busca por categoria (ex: "CRÉDITO", "DÉBITO")
    public static ProcessingCodeType[] fromCategory(String category) {
        return Arrays.stream(values())
                .filter(p -> p.category.equalsIgnoreCase(category))
                .toArray(ProcessingCodeType[]::new);
    }

    @Override
    public String toString() {
        return "%s [%s] — %s".formatted(category, code, description);
    }
}
