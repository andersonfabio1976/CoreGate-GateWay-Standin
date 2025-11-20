package br.com.coregate.core.contracts.iso8583.model;

import br.com.coregate.annotations.iso.annotations.IsoField;
import br.com.coregate.annotations.iso.annotations.IsoMessage;
import br.com.coregate.annotations.iso.enums.IsoVarType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@IsoMessage(mti = "0200")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FinancialRequestIsoModel {

    @IsoField(number = 2, length = 19)
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

    @IsoField(number = 18, length = 4)
    private String mcc;

    @IsoField(number = 22, length = 3)
    private String channelType;

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
