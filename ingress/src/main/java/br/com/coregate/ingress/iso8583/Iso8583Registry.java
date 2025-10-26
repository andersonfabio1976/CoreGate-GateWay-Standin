package br.com.coregate.ingress.iso8583;

import br.com.coregate.ingress.iso8583.*;
import lombok.extern.slf4j.Slf4j;

/**
 * 🧠 Iso8583Registry
 * Roteador genérico para identificar o MTI e usar o mapper correto gerado via JSR-269.
 */
@Slf4j
public final class Iso8583Registry {

    private Iso8583Registry() {}

    /**
     * Decodifica uma string ISO8583 bruta (MTI + bitmap + campos)
     * e retorna a instância do modelo Java correto.
     *
     * @param raw Mensagem ISO8583 completa (em ASCII)
     * @return instância do modelo correspondente ao MTI
     */
    public static Object decode(String raw) {
        if (raw == null || raw.length() < 4) {
            throw new IllegalArgumentException("Mensagem ISO inválida ou vazia");
        }

        String mti = raw.substring(0, 4);
        log.info("🧾 Identificado MTI: {}", mti);

        return switch (mti) {
            case "0100" -> AuthorizationRequestIsoModelMapper.decode(raw);
            case "0110" -> AuthorizationResponseIsoModelMapper.decode(raw);
            case "0200" -> FinancialRequestIsoModelMapper.decode(raw);
            case "0210" -> FinancialResponseIsoModelMapper.decode(raw);
            case "0400" -> ReversalRequestIsoModelMapper.decode(raw);
            case "0410" -> ReversalResponseIsoModelMapper.decode(raw);
            default -> throw new UnsupportedOperationException("MTI não suportado: " + mti);
        };
    }

    /**
     * Codifica um modelo Java para string ISO8583 (MTI + bitmap + campos)
     *
     * @param isoModel Instância de um modelo anotado com @IsoMessage
     * @return String ISO8583 codificada pronta para envio
     */
    public static String encode(Object isoModel) {
        if (isoModel == null) {
            throw new IllegalArgumentException("Modelo ISO nulo.");
        }

        return switch (isoModel) {
            case AuthorizationRequestIsoModel m -> AuthorizationRequestIsoModelMapper.encode(m);
            case AuthorizationResponseIsoModel m -> AuthorizationResponseIsoModelMapper.encode(m);
            case FinancialRequestIsoModel m -> FinancialRequestIsoModelMapper.encode(m);
            case FinancialResponseIsoModel m -> FinancialResponseIsoModelMapper.encode(m);
            case ReversalRequestIsoModel m -> ReversalRequestIsoModelMapper.encode(m);
            case ReversalResponseIsoModel m -> ReversalResponseIsoModelMapper.encode(m);
            default -> throw new UnsupportedOperationException("Modelo não suportado para encode: " + isoModel.getClass().getSimpleName());
        };
    }
}
