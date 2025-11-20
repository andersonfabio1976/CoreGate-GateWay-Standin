package br.com.coregate.core.contracts.iso8583.model;

import br.com.coregate.annotations.iso.annotations.IsoField;
import br.com.coregate.annotations.iso.annotations.IsoMessage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import br.com.coregate.core.contracts.iso8583.model.BaseIsoResponse;

/**
 * Representa o modelo ISO 0400 (Reversal Request)
 * ⚙️ Layout alinhado ao Authorization/Financial para uso em parsers fixos.
 */
@IsoMessage(mti = "0400")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReversalRequestIsoModel implements BaseIsoResponse {

    /** Campo 2 – Primary Account Number (PAN / Card Number) */
    @IsoField(number = 2, length = 19)
    private String cardNumber;

    /** Campo 3 – Processing Code */
    @IsoField(number = 3, length = 6)
    private String processingCode;

    /** Campo 4 – Amount, Transaction */
    @IsoField(number = 4, length = 12)
    private String amount;

    /** Campo 11 – Systems Trace Audit Number (STAN) */
    @IsoField(number = 11, length = 6)
    private String stan;

    /** Campo 18 – Merchant Category Code */
    @IsoField(number = 18, length = 4)
    private String mcc;

    /** Campo 22 – Point of Service Entry Mode / Channel Type */
    @IsoField(number = 22, length = 3)
    private String channelType;

    /** Campo 41 – Card Acceptor Terminal Identification */
    @IsoField(number = 41, length = 8)
    private String terminalId;

    /** Campo 42 – Card Acceptor Identification Code (Merchant ID) */
    @IsoField(number = 42, length = 15)
    private String merchantId;

    /** Campo 49 – Currency Code, Transaction */
    @IsoField(number = 49, length = 3)
    private String currencyCode;

    @Override
    public String getResponseCode() {
        return "";
    }

    @Override
    public String getAuthIdResponse() {
        return "";
    }
}
