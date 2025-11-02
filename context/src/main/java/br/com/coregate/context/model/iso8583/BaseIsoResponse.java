package br.com.coregate.context.model.iso8583;

/**
 * Interface base para modelos de resposta ISO 8583 (0210, 0410, etc.).
 * Permite reuso genérico no pipeline de decode.
 */
public interface BaseIsoResponse {

    String getCardNumber();
    String getMerchantId();
    String getAmount();
    String getResponseCode();
    String getAuthIdResponse();
    String getCurrencyCode();
    String getMcc();
}
