package br.com.coregate.domain.enums;

import lombok.Getter;
import java.util.Arrays;

/**
 * 🌐 ChannelType — Identifica o canal de origem da transação.
 * Baseado em padrões ISO8583 (campo 25 - POS Entry Mode, campo 60.1 em algumas redes).
 */
@Getter
public enum ChannelType {
    // --- Para Atender Proto
    CHANNEL_UNKNOWN("0","CHANNEL_UNKNOWN","CHANNEL_UNKNOWN"),
    UNRECOGNIZED("-1", "UNRECOGNIZED","UNRECOGNIZED"),
    // --- 💳 Canais físicos ---
    POS_MAGSTRIPE("01", "POS", "POS com tarja magnética"),
    POS_CHIP("02", "POS", "POS com chip EMV"),
    POS_CONTACTLESS("03", "POS", "POS contactless (NFC)"),
    POS_FALLBACK("04", "POS", "POS fallback - chip falhou, leitura manual"),

    // --- 🏧 ATM / Autoatendimento ---
    ATM_LOCAL("05", "ATM", "ATM local (saque / consulta)"),
    ATM_REMOTE("06", "ATM", "ATM de outra rede / interoperável"),

    // --- 🌐 Canais online ---
    ECOMMERCE("07", "ECOMMERCE", "E-commerce / pagamento online"),
    MOTO("08", "ECOMMERCE", "Mail Order / Telephone Order"),
    MOBILE_APP("09", "MOBILE", "Aplicativo mobile banking / wallet"),
    IN_APP_PAYMENT("10", "MOBILE", "Pagamento in-app (Apple Pay, Google Pay)"),
    QR_CODE("11", "MOBILE", "Pagamento via QR Code / PIX QR"),

    // --- 🧾 Outros / corporativos ---
    API_GATEWAY("12", "API", "Integração direta via API / gRPC / WebSocket"),
    BACKOFFICE("13", "BACKOFFICE", "Operação manual / administrativa"),
    IVR("14", "TELEFONE", "Canal de voz / URA / call center"),
    TERMINAL_UNATTENDED("15", "KIOSK", "Totem ou terminal não assistido");

    private final String code;
    private final String category;
    private final String description;

    ChannelType(String code, String category, String description) {
        this.code = code;
        this.category = category;
        this.description = description;
    }

    // 🔍 Busca por código ISO
    public static ChannelType fromCode(String code) {
        return Arrays.stream(values())
                .filter(c -> c.code.equals(code))
                .findFirst()
                .orElse(CHANNEL_UNKNOWN);
    }

    // 🔍 Busca por categoria (POS, ATM, ECOMMERCE, MOBILE etc.)
    public static ChannelType[] fromCategory(String category) {
        return Arrays.stream(values())
                .filter(c -> c.category.equalsIgnoreCase(category))
                .toArray(ChannelType[]::new);
    }

    @Override
    public String toString() {
        return "%s [%s] — %s".formatted(category, code, description);
    }
}
