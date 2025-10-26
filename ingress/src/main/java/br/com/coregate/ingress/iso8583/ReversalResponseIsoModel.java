package br.com.coregate.ingress.iso8583;

import br.com.coregate.infrastructure.iso8583.annotations.iso.IsoField;
import br.com.coregate.infrastructure.iso8583.annotations.iso.IsoMessage;
import lombok.Data;

/**
 * Representa o modelo ISO 0410 (Reversal Response).
 *
 * É utilizado para mapear a resposta de reversão (anulação de transação financeira)
 * vinda do adquirente, conforme padrão ISO 8583.
 */
@IsoMessage(mti = "0410")
@Data
public class ReversalResponseIsoModel implements BaseIsoResponse {

    /** Campo 2 - Primary Account Number (PAN / Card Number) */
    @IsoField(number = 2, length = 19)
    private String cardNumber;

    /** Campo 3 - Processing Code */
    @IsoField(number = 3, length = 6)
    private String processingCode;

    /** Campo 4 - Amount, Transaction */
    @IsoField(number = 4, length = 12)
    private String amount;

    /** Campo 11 - Systems Trace Audit Number (STAN) */
    @IsoField(number = 11, length = 6)
    private String stan;

    /** Campo 37 - Retrieval Reference Number */
    @IsoField(number = 37, length = 12)
    private String retrievalReferenceNumber;

    /** Campo 38 - Authorization Identification Response (Auth ID) */
    @IsoField(number = 38, length = 6)
    private String authIdResponse;

    /** Campo 39 - Response Code */
    @IsoField(number = 39, length = 2)
    private String responseCode;

    /** Campo 41 - Card Acceptor Terminal Identification */
    @IsoField(number = 41, length = 8)
    private String terminalId;

    /** Campo 42 - Card Acceptor Identification Code (Merchant ID) */
    @IsoField(number = 42, length = 15)
    private String merchantId;

    /** Campo 49 - Currency Code, Transaction */
    @IsoField(number = 49, length = 3)
    private String currencyCode;
}
