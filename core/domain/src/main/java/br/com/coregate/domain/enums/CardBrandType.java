package br.com.coregate.domain.enums;

import lombok.Getter;

@Getter
public enum CardBrandType {
    CARD_BRAND_UNKNOWN("CARD_BRAND_UNKNOWN",new String[]{"0"}),
    UNRECOGNIZED("UNRECOGNIZED",new String[]{"-1"}),
    VISA("VISA", new String[]{"4"}),
    MASTERCARD("MASTERCARD", new String[]{"51","52","53","54","55","2221","2720"}),
    ELO("ELO", new String[]{"636368","636297","504175","5067","5090"}),
    AMEX("AMEX", new String[]{"34","37"}),
    HIPERCARD("HIPERCARD", new String[]{"606282","384100","384140"}),
    DISCOVER("DISCOVER", new String[]{"6011","622","64","65"}),
    JCB("JCB", new String[]{"35"});

    private final String name;
    private final String[] prefixes;

    CardBrandType(String name, String[] prefixes) {
        this.name = name;
        this.prefixes = prefixes;
    }

    public static CardBrandType fromPan(String pan) {
        if (pan == null || pan.isEmpty()) return CARD_BRAND_UNKNOWN;
        for (CardBrandType brand : values()) {
            for (String prefix : brand.prefixes) {
                if (pan.startsWith(prefix)) {
                    return brand;
                }
            }
        }
        return CARD_BRAND_UNKNOWN;
    }
}
