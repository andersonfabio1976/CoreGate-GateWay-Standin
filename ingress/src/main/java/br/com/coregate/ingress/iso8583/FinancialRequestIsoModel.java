package br.com.coregate.ingress.iso8583;

import br.com.coregate.infrastructure.iso8583.annotations.iso.IsoField;
import br.com.coregate.infrastructure.iso8583.annotations.iso.IsoMessage;
import br.com.coregate.infrastructure.iso8583.annotations.iso.IsoVarType;
import lombok.Data;

@IsoMessage(mti = "0200")
@Data
public class FinancialRequestIsoModel {

    @IsoField(number = 2, varType = IsoVarType.LLVAR)
    private String cardNumber;

    @IsoField(number = 3, length = 6)
    private String processingCode;

    @IsoField(number = 4, length = 12)
    private String amount;

    @IsoField(number = 7, length = 10)
    private String transmissionDateTime;

    @IsoField(number = 11, length = 6)
    private String stan;

    @IsoField(number = 12, length = 6)
    private String localTime;

    @IsoField(number = 13, length = 4)
    private String localDate;

    @IsoField(number = 32, varType = IsoVarType.LLVAR) // Acquiring institution ID
    private String acquiringId;

    @IsoField(number = 37, length = 12)
    private String retrievalReferenceNumber;

    @IsoField(number = 41, length = 8)
    private String terminalId;

    @IsoField(number = 42, length = 15)
    private String merchantId;

    @IsoField(number = 49, length = 3)
    private String currencyCode;

}
