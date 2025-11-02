package br.com.coregate.domain.enums;

import lombok.Getter;

import java.util.Arrays;

/**
 * 💱 CurrencyCode — conforme ISO 4217
 * Usado no campo 49 (Transaction Currency Code) da ISO 8583.
 */
@Getter
public enum CurrencyCode {

    // --- 🌎 Principais moedas globais ---
    UNRECOGNIZED("-1","UNRECOGNIZED","UNRECOGNIZED","UNRECOGNIZED","UNRECOGNIZED"),
    BRL("986", "BRL", "R$", "Real", "Brasil"),
    USD("840", "USD", "$", "Dólar Americano", "Estados Unidos"),
    EUR("978", "EUR", "€", "Euro", "União Europeia"),
    GBP("826", "GBP", "£", "Libra Esterlina", "Reino Unido"),
    JPY("392", "JPY", "¥", "Iene", "Japão"),
    CHF("756", "CHF", "Fr", "Franco Suíço", "Suíça"),
    CAD("124", "CAD", "$", "Dólar Canadense", "Canadá"),
    AUD("036", "AUD", "$", "Dólar Australiano", "Austrália"),
    CNY("156", "CNY", "¥", "Yuan", "China"),
    ARS("032", "ARS", "$", "Peso Argentino", "Argentina"),
    CLP("152", "CLP", "$", "Peso Chileno", "Chile"),
    MXN("484", "MXN", "$", "Peso Mexicano", "México"),
    PEN("604", "PEN", "S/", "Sol", "Peru"),
    COP("170", "COP", "$", "Peso Colombiano", "Colômbia"),
    UYU("858", "UYU", "$U", "Peso Uruguaio", "Uruguai"),
    PYG("600", "PYG", "₲", "Guarani", "Paraguai"),
    BOB("068", "BOB", "Bs", "Boliviano", "Bolívia"),
    ZAR("710", "ZAR", "R", "Rand", "África do Sul"),
    INR("356", "INR", "₹", "Rupia", "Índia"),
    RUB("643", "RUB", "₽", "Rublo", "Rússia"),
    KRW("410", "KRW", "₩", "Won", "Coreia do Sul"),
    AED("784", "AED", "د.إ", "Dirham", "Emirados Árabes Unidos"),
    TRY("949", "TRY", "₺", "Lira Turca", "Turquia"),
    CURRENCY_UNKNOWN("000", "UNK", "", "Desconhecida", "N/A");

    private final String numericCode;   // ISO4217 numérico (campo 49)
    private final String alphaCode;     // ISO4217 alfabético
    private final String symbol;        // Símbolo monetário
    private final String name;          // Nome da moeda
    private final String country;       // País principal

    CurrencyCode(String numericCode, String alphaCode, String symbol, String name, String country) {
        this.numericCode = numericCode;
        this.alphaCode = alphaCode;
        this.symbol = symbol;
        this.name = name;
        this.country = country;
    }

    // 🔍 Busca por código numérico ISO 8583 (campo 49)
    public static CurrencyCode fromNumeric(String code) {
        if (code == null) return CURRENCY_UNKNOWN;
        return Arrays.stream(values())
                .filter(c -> c.numericCode.equals(code))
                .findFirst()
                .orElse(CURRENCY_UNKNOWN);
    }

    // 🔍 Busca por código alfabético (ex: "BRL")
    public static CurrencyCode fromAlpha(String alpha) {
        if (alpha == null) return CURRENCY_UNKNOWN;
        return Arrays.stream(values())
                .filter(c -> c.alphaCode.equalsIgnoreCase(alpha))
                .findFirst()
                .orElse(CURRENCY_UNKNOWN);
    }

    // 🔍 Busca por símbolo
    public static CurrencyCode fromSymbol(String symbol) {
        if (symbol == null) return CURRENCY_UNKNOWN;
        return Arrays.stream(values())
                .filter(c -> c.symbol.equals(symbol))
                .findFirst()
                .orElse(CURRENCY_UNKNOWN);
    }

    @Override
    public String toString() {
        return "%s (%s) — %s [%s]".formatted(name, alphaCode, country, numericCode);
    }
}
