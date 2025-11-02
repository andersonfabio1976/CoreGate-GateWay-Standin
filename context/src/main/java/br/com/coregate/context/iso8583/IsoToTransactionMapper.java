package br.com.coregate.context.iso8583;

import br.com.coregate.application.dto.transaction.AuthorizationResult;
import br.com.coregate.application.dto.transaction.TransactionCommand;
import br.com.coregate.context.model.iso8583.*;
import br.com.coregate.domain.enums.*;
import br.com.coregate.domain.vo.Pan;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;

@Component
public class IsoToTransactionMapper {

    // ======================================
    // 🔽 ISO → TransactionCommand (Decode)
    // ======================================

    public TransactionCommand fromFinancialRequest(FinancialRequestIsoModel model) {
        return baseCommand(model.getMerchantId(), model.getAmount(), model.getCardNumber(), TransactionType.TRANSACTION_TYPE_PURCHASE, "0200", model.getMcc(), model.getProcessingCode(),model.getChannelType(),model.getCurrencyCode());
    }

    public TransactionCommand fromFinancialResponse(FinancialResponseIsoModel model) {
        return baseCommand(model.getMerchantId(), model.getAmount(), model.getCardNumber(), TransactionType.TRANSACTION_TYPE_PURCHASE, "0210", model.getMcc(), model.getProcessingCode(),model.getChannelType(),model.getCurrencyCode());
    }

    public TransactionCommand fromReversalRequest(ReversalRequestIsoModel model) {
        return baseCommand(model.getMerchantId(), model.getAmount(), model.getCardNumber(), TransactionType.TRANSACTION_TYPE_REVERSAL, "0400", model.getMcc(), model.getProcessingCode(),model.getChannelType(),model.getCurrencyCode());
    }

    public TransactionCommand fromReversalResponse(ReversalResponseIsoModel model) {
        return baseCommand(model.getMerchantId(), model.getAmount(), model.getCardNumber(), TransactionType.TRANSACTION_TYPE_REVERSAL, "0410", model.getMcc(), model.getProcessingCode(),model.getChannelType(),model.getCurrencyCode());
    }

    public TransactionCommand fromAuthorizationRequest(AuthorizationRequestIsoModel model) {
        return baseCommand(model.getMerchantId(), model.getAmount(), model.getCardNumber(), TransactionType.TRANSACTION_TYPE_AUTHORIZATION, "0100", model.getMcc(), model.getProcessingCode(),model.getChannelType(),model.getCurrencyCode());
    }

    public TransactionCommand fromAuthorizationResponse(AuthorizationResponseIsoModel model) {
        return baseCommand(model.getMerchantId(), model.getAmount(), model.getCardNumber(), TransactionType.TRANSACTION_TYPE_AUTHORIZATION, "0110", model.getMcc(), model.getProcessingCode(),model.getChannelType(),model.getCurrencyCode());
    }

    // ======================================
    // 🔼 TransactionCommand → ISO (Encode)
    // ======================================

    public FinancialRequestIsoModel toFinancialRequest(TransactionCommand cmd, AuthorizationResult result) {
        return FinancialRequestIsoModel.builder()
                .merchantId(cmd.merchantId())
                .amount(formatAmount(cmd.amount()))
                .cardNumber(cmd.pan().getValue())
                .mcc(cmd.mcc())
                .processingCode(cmd.operation().getCode())
                .channelType(cmd.channel().getCode())
                .currencyCode(cmd.currency().getNumericCode())
                .build();
    }

    public FinancialResponseIsoModel toFinancialResponse(TransactionCommand cmd, AuthorizationResult result) {
        return FinancialResponseIsoModel.builder()
                .merchantId(cmd.merchantId())
                .amount(formatAmount(cmd.amount()))
                .cardNumber(cmd.pan().getValue())
                .responseCode(result.responseCode())
                .mcc(cmd.mcc())
                .processingCode(cmd.operation().getCode())
                .channelType(cmd.channel().getCode())
                .currencyCode(cmd.currency().getNumericCode())
                .build();
    }

    public ReversalRequestIsoModel toReversalRequest(TransactionCommand cmd, AuthorizationResult result) {
        return ReversalRequestIsoModel.builder()
                .merchantId(cmd.merchantId())
                .amount(formatAmount(cmd.amount()))
                .cardNumber(cmd.pan().getValue())
                .mcc(cmd.mcc())
                .processingCode(cmd.operation().getCode())
                .channelType(cmd.channel().getCode())
                .currencyCode(cmd.currency().getNumericCode())
                .build();
    }

    public ReversalResponseIsoModel toReversalResponse(TransactionCommand cmd, AuthorizationResult result) {
        return ReversalResponseIsoModel.builder()
                .merchantId(cmd.merchantId())
                .amount(formatAmount(cmd.amount()))
                .cardNumber(cmd.pan().getValue())
                .mcc(cmd.mcc())
                .processingCode(cmd.operation().getCode())
                .channelType(cmd.channel().getCode())
                .currencyCode(cmd.currency().getNumericCode())
                .responseCode(result.responseCode())
                .build();
    }

    public AuthorizationRequestIsoModel toAuthorizationRequest(TransactionCommand cmd, AuthorizationResult result) {
        return AuthorizationRequestIsoModel.builder()
                .merchantId(cmd.merchantId())
                .amount(formatAmount(cmd.amount()))
                .cardNumber(cmd.pan().getValue())
                .mcc(cmd.mcc())
                .processingCode(cmd.operation().getCode())
                .channelType(cmd.channel().getCode())
                .currencyCode(cmd.currency().getNumericCode())
                .build();
    }

    public AuthorizationResponseIsoModel toAuthorizationResponse(TransactionCommand cmd, AuthorizationResult result) {
        return AuthorizationResponseIsoModel.builder()
                .merchantId(cmd.merchantId())
                .amount(formatAmount(cmd.amount()))
                .cardNumber(cmd.pan().getValue())
                .mcc(cmd.mcc())
                .processingCode(cmd.operation().getCode())
                .channelType(cmd.channel().getCode())
                .currencyCode(cmd.currency().getNumericCode())
                .responseCode(result.responseCode())
                .build();
    }

    // ======================================
    // 🧩 Helpers comuns
    // ======================================

    private TransactionCommand baseCommand(String merchantId, String amount, String card, TransactionType type, String mti, String mcc, String processingCode, String channelType, String currencyCode) {
        return TransactionCommand.builder()
                .tenantId("COREGATE-TENANT")
                .merchantId(merchantId)
                .amount(parseAmount(amount))
                .currency(CurrencyCode.fromNumeric(currencyCode))
                .brand(CardBrandType.fromPan(card))
                .channel(ChannelType.fromCode(channelType))
                .type(type)
                .pan(Pan.of(card))
                .mti(mti)
                .mcc(mcc)
                .operation(ProcessingCodeType.fromCode(processingCode))
                .build();
    }

    private BigDecimal parseAmount(String amount) {
        try {
            return new BigDecimal(amount).movePointLeft(2);
        } catch (Exception e) {
            return BigDecimal.ZERO;
        }
    }

    private String formatAmount(BigDecimal amount) {
        if (amount == null) return "000000000000";
        return String.format("%012d", amount.movePointRight(2).longValue());
    }
}
