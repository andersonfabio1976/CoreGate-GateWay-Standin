package br.com.coregate.context.iso8583;

import br.com.coregate.infrastructure.iso8583.annotations.iso.IsoField;
import br.com.coregate.infrastructure.iso8583.annotations.iso.IsoMessage;
import br.com.coregate.infrastructure.iso8583.annotations.iso.IsoVarType;
import br.com.coregate.infrastructure.iso8583.annotations.iso.VarType;

@IsoMessage(mti = "0200") // Verificar os Mtis que vamos precisar e formatos transacionais
public class TransactionIsoModel {

    @IsoField(number = 2, varType = IsoVarType.LLVAR)   // PAN variável
    private String cardNumber;

    @IsoField(number = 4, length = 12, varType = IsoVarType.FIXED, padLeft = true, padChar = '0') // 12 N
    private String amount;

    @IsoField(number = 7, length = 10, varType = IsoVarType.FIXED) // MMddHHmmss
    private String transmissionDateTime;

    // getters/setters
    public String getCardNumber() { return cardNumber; }
    public void setCardNumber(String cardNumber) { this.cardNumber = cardNumber; }
    public String getAmount() { return amount; }
    public void setAmount(String amount) { this.amount = amount; }
    public String getTransmissionDateTime() { return transmissionDateTime; }
    public void setTransmissionDateTime(String transmissionDateTime) { this.transmissionDateTime = transmissionDateTime; }
}
