package br.com.coregate.ingress.mapper;

import br.com.coregate.application.dto.TransactionCommand;
import br.com.coregate.domain.enums.*;
import br.com.coregate.domain.vo.Pan;
import br.com.coregate.ingress.iso8583.*;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class IsoToTransactionMapper {

    public TransactionCommand fromFinancialRequest(FinancialRequestIsoModel model) {
        return TransactionCommand.builder()
                .tenantId("COREGATE")
                .merchantId(model.getMerchantId())
                .amount(parseAmount(model.getAmount()))
                .currency(CurrencyCode.BRL)
                .brand(CardBrand.VISA)
                .channel(ChannelType.POS)
                .type(TransactionType.PURCHASE)
                .pan(new Pan(model.getCardNumber()))
                .build();
    }

    public TransactionCommand fromFinancialResponse(FinancialResponseIsoModel model) {
        return TransactionCommand.builder()
                .tenantId("COREGATE")
                .merchantId(model.getMerchantId())
                .amount(parseAmount(model.getAmount()))
                .currency(CurrencyCode.BRL)
                .brand(CardBrand.VISA)
                .channel(ChannelType.POS)
                .type(TransactionType.PURCHASE)
                .pan(new Pan(model.getCardNumber()))
                .build();
    }

    public TransactionCommand fromReversalRequest(ReversalRequestIsoModel model) {
        return TransactionCommand.builder()
                .tenantId("COREGATE")
                .merchantId(model.getMerchantId())
                .amount(parseAmount(model.getAmount()))
                .currency(CurrencyCode.BRL)
                .brand(CardBrand.VISA)
                .channel(ChannelType.POS)
                .type(TransactionType.REVERSAL)
                .pan(new Pan(model.getCardNumber()))
                .build();
    }

    public TransactionCommand fromReversalResponse(ReversalResponseIsoModel model) {
        return TransactionCommand.builder()
                .tenantId("COREGATE")
                .merchantId(model.getMerchantId())
                .amount(parseAmount(model.getAmount()))
                .currency(CurrencyCode.BRL)
                .brand(CardBrand.VISA)
                .channel(ChannelType.POS)
                .type(TransactionType.REVERSAL)
                .pan(new Pan(model.getCardNumber()))
                .build();
    }

    public TransactionCommand fromAuthorizationRequest(AuthorizationRequestIsoModel model) {
        return TransactionCommand.builder()
                .tenantId("COREGATE")
                .merchantId(model.getMerchantId())
                .amount(parseAmount(model.getAmount()))
                .currency(CurrencyCode.BRL)
                .brand(CardBrand.VISA)
                .channel(ChannelType.POS)
                .type(TransactionType.AUTHORIZATION)
                .pan(new Pan(model.getCardNumber()))
                .build();
    }

    public TransactionCommand fromAuthorizationResponse(AuthorizationResponseIsoModel model) {
        return TransactionCommand.builder()
                .tenantId("COREGATE")
                .merchantId(model.getMerchantId())
                .amount(parseAmount(model.getAmount()))
                .currency(CurrencyCode.BRL)
                .brand(CardBrand.VISA)
                .channel(ChannelType.POS)
                .type(TransactionType.AUTHORIZATION)
                .pan(new Pan(model.getCardNumber()))
                .build();
    }

    private BigDecimal parseAmount(String amount) {
        try {
            return new BigDecimal(amount).movePointLeft(2);
        } catch (Exception e) {
            return BigDecimal.ZERO;
        }
    }
}
