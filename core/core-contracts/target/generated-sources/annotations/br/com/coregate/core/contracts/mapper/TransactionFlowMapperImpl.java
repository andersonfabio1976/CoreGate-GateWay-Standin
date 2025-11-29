package br.com.coregate.core.contracts.mapper;

import br.com.coregate.core.contracts.ChannelType;
import br.com.coregate.core.contracts.CurrencyCode;
import br.com.coregate.core.contracts.RequestTransactionFlowProto;
import br.com.coregate.core.contracts.ResponseTransactionFlowProto;
import br.com.coregate.core.contracts.TransactionCommand;
import br.com.coregate.core.contracts.TransactionType;
import br.com.coregate.core.contracts.dto.transaction.AuthorizationResult;
import br.com.coregate.core.contracts.dto.transaction.RequestTransactionFlow;
import br.com.coregate.core.contracts.dto.transaction.ResponseTransactionFlow;
import br.com.coregate.domain.enums.CardBrandType;
import br.com.coregate.domain.enums.ProcessingCodeType;
import br.com.coregate.domain.enums.TransactionStatus;
import br.com.coregate.domain.vo.Pan;
import java.math.BigDecimal;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-11-29T08:54:01-0300",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.8 (Oracle Corporation)"
)
@Component
public class TransactionFlowMapperImpl implements TransactionFlowMapper {

    @Autowired
    private DateTimeMapper dateTimeMapper;

    @Override
    public RequestTransactionFlow toDto(RequestTransactionFlowProto proto) {
        if ( proto == null ) {
            return null;
        }

        RequestTransactionFlow.RequestTransactionFlowBuilder requestTransactionFlow = RequestTransactionFlow.builder();

        if ( proto.hasTransactionCommand() ) {
            requestTransactionFlow.transactionCommand( transactionCommandToTransactionCommand( proto.getTransactionCommand() ) );
        }

        return requestTransactionFlow.build();
    }

    @Override
    public RequestTransactionFlowProto toProto(RequestTransactionFlow dto) {
        if ( dto == null ) {
            return null;
        }

        RequestTransactionFlowProto.Builder requestTransactionFlowProto = RequestTransactionFlowProto.newBuilder();

        requestTransactionFlowProto.setTransactionCommand( transactionCommandToTransactionCommand1( dto.getTransactionCommand() ) );

        return requestTransactionFlowProto.build();
    }

    @Override
    public ResponseTransactionFlow toDto(ResponseTransactionFlowProto proto) {
        if ( proto == null ) {
            return null;
        }

        ResponseTransactionFlow.ResponseTransactionFlowBuilder responseTransactionFlow = ResponseTransactionFlow.builder();

        if ( proto.hasTransactionCommand() ) {
            responseTransactionFlow.transactionCommand( transactionCommandToTransactionCommand( proto.getTransactionCommand() ) );
        }
        if ( proto.hasAuthorizationResult() ) {
            responseTransactionFlow.authorizationResult( authorizationResultToAuthorizationResult( proto.getAuthorizationResult() ) );
        }

        return responseTransactionFlow.build();
    }

    @Override
    public ResponseTransactionFlowProto toProto(ResponseTransactionFlow dto) {
        if ( dto == null ) {
            return null;
        }

        ResponseTransactionFlowProto.Builder responseTransactionFlowProto = ResponseTransactionFlowProto.newBuilder();

        responseTransactionFlowProto.setAuthorizationResult( authorizationResultToAuthorizationResult1( dto.getAuthorizationResult() ) );
        responseTransactionFlowProto.setTransactionCommand( transactionCommandToTransactionCommand1( dto.getTransactionCommand() ) );

        return responseTransactionFlowProto.build();
    }

    protected br.com.coregate.domain.enums.CurrencyCode currencyCodeToCurrencyCode(CurrencyCode currencyCode) {
        if ( currencyCode == null ) {
            return null;
        }

        br.com.coregate.domain.enums.CurrencyCode currencyCode1;

        switch ( currencyCode ) {
            case CURRENCY_UNKNOWN: currencyCode1 = br.com.coregate.domain.enums.CurrencyCode.CURRENCY_UNKNOWN;
            break;
            case BRL: currencyCode1 = br.com.coregate.domain.enums.CurrencyCode.BRL;
            break;
            case USD: currencyCode1 = br.com.coregate.domain.enums.CurrencyCode.USD;
            break;
            case EUR: currencyCode1 = br.com.coregate.domain.enums.CurrencyCode.EUR;
            break;
            case GBP: currencyCode1 = br.com.coregate.domain.enums.CurrencyCode.GBP;
            break;
            case JPY: currencyCode1 = br.com.coregate.domain.enums.CurrencyCode.JPY;
            break;
            case CHF: currencyCode1 = br.com.coregate.domain.enums.CurrencyCode.CHF;
            break;
            case CAD: currencyCode1 = br.com.coregate.domain.enums.CurrencyCode.CAD;
            break;
            case AUD: currencyCode1 = br.com.coregate.domain.enums.CurrencyCode.AUD;
            break;
            case CNY: currencyCode1 = br.com.coregate.domain.enums.CurrencyCode.CNY;
            break;
            case ARS: currencyCode1 = br.com.coregate.domain.enums.CurrencyCode.ARS;
            break;
            case CLP: currencyCode1 = br.com.coregate.domain.enums.CurrencyCode.CLP;
            break;
            case MXN: currencyCode1 = br.com.coregate.domain.enums.CurrencyCode.MXN;
            break;
            case PEN: currencyCode1 = br.com.coregate.domain.enums.CurrencyCode.PEN;
            break;
            case COP: currencyCode1 = br.com.coregate.domain.enums.CurrencyCode.COP;
            break;
            case UYU: currencyCode1 = br.com.coregate.domain.enums.CurrencyCode.UYU;
            break;
            case PYG: currencyCode1 = br.com.coregate.domain.enums.CurrencyCode.PYG;
            break;
            case BOB: currencyCode1 = br.com.coregate.domain.enums.CurrencyCode.BOB;
            break;
            case ZAR: currencyCode1 = br.com.coregate.domain.enums.CurrencyCode.ZAR;
            break;
            case INR: currencyCode1 = br.com.coregate.domain.enums.CurrencyCode.INR;
            break;
            case RUB: currencyCode1 = br.com.coregate.domain.enums.CurrencyCode.RUB;
            break;
            case KRW: currencyCode1 = br.com.coregate.domain.enums.CurrencyCode.KRW;
            break;
            case AED: currencyCode1 = br.com.coregate.domain.enums.CurrencyCode.AED;
            break;
            case TRY: currencyCode1 = br.com.coregate.domain.enums.CurrencyCode.TRY;
            break;
            case UNRECOGNIZED: currencyCode1 = br.com.coregate.domain.enums.CurrencyCode.UNRECOGNIZED;
            break;
            default: throw new IllegalArgumentException( "Unexpected enum constant: " + currencyCode );
        }

        return currencyCode1;
    }

    protected CardBrandType cardBrandTypeToCardBrandType(br.com.coregate.core.contracts.CardBrandType cardBrandType) {
        if ( cardBrandType == null ) {
            return null;
        }

        CardBrandType cardBrandType1;

        switch ( cardBrandType ) {
            case CARD_BRAND_UNKNOWN: cardBrandType1 = CardBrandType.CARD_BRAND_UNKNOWN;
            break;
            case VISA: cardBrandType1 = CardBrandType.VISA;
            break;
            case MASTERCARD: cardBrandType1 = CardBrandType.MASTERCARD;
            break;
            case ELO: cardBrandType1 = CardBrandType.ELO;
            break;
            case AMEX: cardBrandType1 = CardBrandType.AMEX;
            break;
            case HIPERCARD: cardBrandType1 = CardBrandType.HIPERCARD;
            break;
            case DISCOVER: cardBrandType1 = CardBrandType.DISCOVER;
            break;
            case JCB: cardBrandType1 = CardBrandType.JCB;
            break;
            case UNRECOGNIZED: cardBrandType1 = CardBrandType.UNRECOGNIZED;
            break;
            default: throw new IllegalArgumentException( "Unexpected enum constant: " + cardBrandType );
        }

        return cardBrandType1;
    }

    protected br.com.coregate.domain.enums.ChannelType channelTypeToChannelType(ChannelType channelType) {
        if ( channelType == null ) {
            return null;
        }

        br.com.coregate.domain.enums.ChannelType channelType1;

        switch ( channelType ) {
            case CHANNEL_UNKNOWN: channelType1 = br.com.coregate.domain.enums.ChannelType.CHANNEL_UNKNOWN;
            break;
            case POS_MAGSTRIPE: channelType1 = br.com.coregate.domain.enums.ChannelType.POS_MAGSTRIPE;
            break;
            case POS_CHIP: channelType1 = br.com.coregate.domain.enums.ChannelType.POS_CHIP;
            break;
            case POS_CONTACTLESS: channelType1 = br.com.coregate.domain.enums.ChannelType.POS_CONTACTLESS;
            break;
            case POS_FALLBACK: channelType1 = br.com.coregate.domain.enums.ChannelType.POS_FALLBACK;
            break;
            case ATM_LOCAL: channelType1 = br.com.coregate.domain.enums.ChannelType.ATM_LOCAL;
            break;
            case ATM_REMOTE: channelType1 = br.com.coregate.domain.enums.ChannelType.ATM_REMOTE;
            break;
            case ECOMMERCE: channelType1 = br.com.coregate.domain.enums.ChannelType.ECOMMERCE;
            break;
            case MOTO: channelType1 = br.com.coregate.domain.enums.ChannelType.MOTO;
            break;
            case MOBILE_APP: channelType1 = br.com.coregate.domain.enums.ChannelType.MOBILE_APP;
            break;
            case IN_APP_PAYMENT: channelType1 = br.com.coregate.domain.enums.ChannelType.IN_APP_PAYMENT;
            break;
            case QR_CODE: channelType1 = br.com.coregate.domain.enums.ChannelType.QR_CODE;
            break;
            case API_GATEWAY: channelType1 = br.com.coregate.domain.enums.ChannelType.API_GATEWAY;
            break;
            case BACKOFFICE: channelType1 = br.com.coregate.domain.enums.ChannelType.BACKOFFICE;
            break;
            case IVR: channelType1 = br.com.coregate.domain.enums.ChannelType.IVR;
            break;
            case TERMINAL_UNATTENDED: channelType1 = br.com.coregate.domain.enums.ChannelType.TERMINAL_UNATTENDED;
            break;
            case UNRECOGNIZED: channelType1 = br.com.coregate.domain.enums.ChannelType.UNRECOGNIZED;
            break;
            default: throw new IllegalArgumentException( "Unexpected enum constant: " + channelType );
        }

        return channelType1;
    }

    protected br.com.coregate.domain.enums.TransactionType transactionTypeToTransactionType(TransactionType transactionType) {
        if ( transactionType == null ) {
            return null;
        }

        br.com.coregate.domain.enums.TransactionType transactionType1;

        switch ( transactionType ) {
            case TRANSACTION_TYPE_UNSPECIFIED: transactionType1 = br.com.coregate.domain.enums.TransactionType.TRANSACTION_TYPE_UNSPECIFIED;
            break;
            case TRANSACTION_TYPE_PURCHASE: transactionType1 = br.com.coregate.domain.enums.TransactionType.TRANSACTION_TYPE_PURCHASE;
            break;
            case TRANSACTION_TYPE_REVERSAL: transactionType1 = br.com.coregate.domain.enums.TransactionType.TRANSACTION_TYPE_REVERSAL;
            break;
            case TRANSACTION_TYPE_REFUND: transactionType1 = br.com.coregate.domain.enums.TransactionType.TRANSACTION_TYPE_REFUND;
            break;
            case TRANSACTION_TYPE_BALANCE_INQUIRY: transactionType1 = br.com.coregate.domain.enums.TransactionType.TRANSACTION_TYPE_BALANCE_INQUIRY;
            break;
            case TRANSACTION_TYPE_WITHDRAWAL: transactionType1 = br.com.coregate.domain.enums.TransactionType.TRANSACTION_TYPE_WITHDRAWAL;
            break;
            case TRANSACTION_TYPE_AUTHORIZATION: transactionType1 = br.com.coregate.domain.enums.TransactionType.TRANSACTION_TYPE_AUTHORIZATION;
            break;
            case UNRECOGNIZED: transactionType1 = br.com.coregate.domain.enums.TransactionType.UNRECOGNIZED;
            break;
            default: throw new IllegalArgumentException( "Unexpected enum constant: " + transactionType );
        }

        return transactionType1;
    }

    protected Pan panToPan(br.com.coregate.core.contracts.Pan pan) {
        if ( pan == null ) {
            return null;
        }

        String value = null;

        value = pan.getValue();

        Pan pan1 = new Pan( value );

        return pan1;
    }

    protected ProcessingCodeType processingCodeTypeToProcessingCodeType(br.com.coregate.core.contracts.ProcessingCodeType processingCodeType) {
        if ( processingCodeType == null ) {
            return null;
        }

        ProcessingCodeType processingCodeType1;

        switch ( processingCodeType ) {
            case PROCESSING_CODE_UNKNOWN: processingCodeType1 = ProcessingCodeType.PROCESSING_CODE_UNKNOWN;
            break;
            case PURCHASE_DEBIT: processingCodeType1 = ProcessingCodeType.PURCHASE_DEBIT;
            break;
            case PURCHASE_CREDIT: processingCodeType1 = ProcessingCodeType.PURCHASE_CREDIT;
            break;
            case PURCHASE_INSTALLMENT: processingCodeType1 = ProcessingCodeType.PURCHASE_INSTALLMENT;
            break;
            case PURCHASE_CASHBACK: processingCodeType1 = ProcessingCodeType.PURCHASE_CASHBACK;
            break;
            case CASH_WITHDRAWAL: processingCodeType1 = ProcessingCodeType.CASH_WITHDRAWAL;
            break;
            case CASH_ADVANCE: processingCodeType1 = ProcessingCodeType.CASH_ADVANCE;
            break;
            case BILL_PAYMENT: processingCodeType1 = ProcessingCodeType.BILL_PAYMENT;
            break;
            case FUNDS_TRANSFER: processingCodeType1 = ProcessingCodeType.FUNDS_TRANSFER;
            break;
            case BALANCE_INQUIRY: processingCodeType1 = ProcessingCodeType.BALANCE_INQUIRY;
            break;
            case PRE_AUTH: processingCodeType1 = ProcessingCodeType.PRE_AUTH;
            break;
            case PRE_AUTH_CAPTURE: processingCodeType1 = ProcessingCodeType.PRE_AUTH_CAPTURE;
            break;
            case PRE_AUTH_CANCEL: processingCodeType1 = ProcessingCodeType.PRE_AUTH_CANCEL;
            break;
            case REVERSAL: processingCodeType1 = ProcessingCodeType.REVERSAL;
            break;
            case PARTIAL_REVERSAL: processingCodeType1 = ProcessingCodeType.PARTIAL_REVERSAL;
            break;
            case CHARGEBACK: processingCodeType1 = ProcessingCodeType.CHARGEBACK;
            break;
            case RELOAD_PREPAID: processingCodeType1 = ProcessingCodeType.RELOAD_PREPAID;
            break;
            case ECHO_TEST: processingCodeType1 = ProcessingCodeType.ECHO_TEST;
            break;
            case LOAN_PAYMENT: processingCodeType1 = ProcessingCodeType.LOAN_PAYMENT;
            break;
            case FUNDS_TRANSFER_SAVINGS: processingCodeType1 = ProcessingCodeType.FUNDS_TRANSFER_SAVINGS;
            break;
            case STATEMENT_INQUIRY: processingCodeType1 = ProcessingCodeType.STATEMENT_INQUIRY;
            break;
            case REVERSAL_PRE_AUTH: processingCodeType1 = ProcessingCodeType.REVERSAL_PRE_AUTH;
            break;
            case UNRECOGNIZED: processingCodeType1 = ProcessingCodeType.UNRECOGNIZED;
            break;
            default: throw new IllegalArgumentException( "Unexpected enum constant: " + processingCodeType );
        }

        return processingCodeType1;
    }

    protected br.com.coregate.core.contracts.dto.transaction.TransactionCommand transactionCommandToTransactionCommand(TransactionCommand transactionCommand) {
        if ( transactionCommand == null ) {
            return null;
        }

        br.com.coregate.core.contracts.dto.transaction.TransactionCommand.TransactionCommandBuilder transactionCommand1 = br.com.coregate.core.contracts.dto.transaction.TransactionCommand.builder();

        transactionCommand1.transactionId( transactionCommand.getTransactionId() );
        transactionCommand1.tenantId( transactionCommand.getTenantId() );
        transactionCommand1.merchantId( transactionCommand.getMerchantId() );
        transactionCommand1.amount( BigDecimal.valueOf( transactionCommand.getAmount() ) );
        transactionCommand1.currency( currencyCodeToCurrencyCode( transactionCommand.getCurrency() ) );
        transactionCommand1.brand( cardBrandTypeToCardBrandType( transactionCommand.getBrand() ) );
        transactionCommand1.channel( channelTypeToChannelType( transactionCommand.getChannel() ) );
        transactionCommand1.type( transactionTypeToTransactionType( transactionCommand.getType() ) );
        if ( transactionCommand.hasPan() ) {
            transactionCommand1.pan( panToPan( transactionCommand.getPan() ) );
        }
        transactionCommand1.mti( transactionCommand.getMti() );
        transactionCommand1.mcc( transactionCommand.getMcc() );
        transactionCommand1.operation( processingCodeTypeToProcessingCodeType( transactionCommand.getOperation() ) );

        return transactionCommand1.build();
    }

    protected CurrencyCode currencyCodeToCurrencyCode1(br.com.coregate.domain.enums.CurrencyCode currencyCode) {
        if ( currencyCode == null ) {
            return null;
        }

        CurrencyCode currencyCode1;

        switch ( currencyCode ) {
            case UNRECOGNIZED: currencyCode1 = CurrencyCode.UNRECOGNIZED;
            break;
            case BRL: currencyCode1 = CurrencyCode.BRL;
            break;
            case USD: currencyCode1 = CurrencyCode.USD;
            break;
            case EUR: currencyCode1 = CurrencyCode.EUR;
            break;
            case GBP: currencyCode1 = CurrencyCode.GBP;
            break;
            case JPY: currencyCode1 = CurrencyCode.JPY;
            break;
            case CHF: currencyCode1 = CurrencyCode.CHF;
            break;
            case CAD: currencyCode1 = CurrencyCode.CAD;
            break;
            case AUD: currencyCode1 = CurrencyCode.AUD;
            break;
            case CNY: currencyCode1 = CurrencyCode.CNY;
            break;
            case ARS: currencyCode1 = CurrencyCode.ARS;
            break;
            case CLP: currencyCode1 = CurrencyCode.CLP;
            break;
            case MXN: currencyCode1 = CurrencyCode.MXN;
            break;
            case PEN: currencyCode1 = CurrencyCode.PEN;
            break;
            case COP: currencyCode1 = CurrencyCode.COP;
            break;
            case UYU: currencyCode1 = CurrencyCode.UYU;
            break;
            case PYG: currencyCode1 = CurrencyCode.PYG;
            break;
            case BOB: currencyCode1 = CurrencyCode.BOB;
            break;
            case ZAR: currencyCode1 = CurrencyCode.ZAR;
            break;
            case INR: currencyCode1 = CurrencyCode.INR;
            break;
            case RUB: currencyCode1 = CurrencyCode.RUB;
            break;
            case KRW: currencyCode1 = CurrencyCode.KRW;
            break;
            case AED: currencyCode1 = CurrencyCode.AED;
            break;
            case TRY: currencyCode1 = CurrencyCode.TRY;
            break;
            case CURRENCY_UNKNOWN: currencyCode1 = CurrencyCode.CURRENCY_UNKNOWN;
            break;
            default: throw new IllegalArgumentException( "Unexpected enum constant: " + currencyCode );
        }

        return currencyCode1;
    }

    protected br.com.coregate.core.contracts.CardBrandType cardBrandTypeToCardBrandType1(CardBrandType cardBrandType) {
        if ( cardBrandType == null ) {
            return null;
        }

        br.com.coregate.core.contracts.CardBrandType cardBrandType1;

        switch ( cardBrandType ) {
            case CARD_BRAND_UNKNOWN: cardBrandType1 = br.com.coregate.core.contracts.CardBrandType.CARD_BRAND_UNKNOWN;
            break;
            case UNRECOGNIZED: cardBrandType1 = br.com.coregate.core.contracts.CardBrandType.UNRECOGNIZED;
            break;
            case VISA: cardBrandType1 = br.com.coregate.core.contracts.CardBrandType.VISA;
            break;
            case MASTERCARD: cardBrandType1 = br.com.coregate.core.contracts.CardBrandType.MASTERCARD;
            break;
            case ELO: cardBrandType1 = br.com.coregate.core.contracts.CardBrandType.ELO;
            break;
            case AMEX: cardBrandType1 = br.com.coregate.core.contracts.CardBrandType.AMEX;
            break;
            case HIPERCARD: cardBrandType1 = br.com.coregate.core.contracts.CardBrandType.HIPERCARD;
            break;
            case DISCOVER: cardBrandType1 = br.com.coregate.core.contracts.CardBrandType.DISCOVER;
            break;
            case JCB: cardBrandType1 = br.com.coregate.core.contracts.CardBrandType.JCB;
            break;
            default: throw new IllegalArgumentException( "Unexpected enum constant: " + cardBrandType );
        }

        return cardBrandType1;
    }

    protected ChannelType channelTypeToChannelType1(br.com.coregate.domain.enums.ChannelType channelType) {
        if ( channelType == null ) {
            return null;
        }

        ChannelType channelType1;

        switch ( channelType ) {
            case CHANNEL_UNKNOWN: channelType1 = ChannelType.CHANNEL_UNKNOWN;
            break;
            case UNRECOGNIZED: channelType1 = ChannelType.UNRECOGNIZED;
            break;
            case POS_MAGSTRIPE: channelType1 = ChannelType.POS_MAGSTRIPE;
            break;
            case POS_CHIP: channelType1 = ChannelType.POS_CHIP;
            break;
            case POS_CONTACTLESS: channelType1 = ChannelType.POS_CONTACTLESS;
            break;
            case POS_FALLBACK: channelType1 = ChannelType.POS_FALLBACK;
            break;
            case ATM_LOCAL: channelType1 = ChannelType.ATM_LOCAL;
            break;
            case ATM_REMOTE: channelType1 = ChannelType.ATM_REMOTE;
            break;
            case ECOMMERCE: channelType1 = ChannelType.ECOMMERCE;
            break;
            case MOTO: channelType1 = ChannelType.MOTO;
            break;
            case MOBILE_APP: channelType1 = ChannelType.MOBILE_APP;
            break;
            case IN_APP_PAYMENT: channelType1 = ChannelType.IN_APP_PAYMENT;
            break;
            case QR_CODE: channelType1 = ChannelType.QR_CODE;
            break;
            case API_GATEWAY: channelType1 = ChannelType.API_GATEWAY;
            break;
            case BACKOFFICE: channelType1 = ChannelType.BACKOFFICE;
            break;
            case IVR: channelType1 = ChannelType.IVR;
            break;
            case TERMINAL_UNATTENDED: channelType1 = ChannelType.TERMINAL_UNATTENDED;
            break;
            default: throw new IllegalArgumentException( "Unexpected enum constant: " + channelType );
        }

        return channelType1;
    }

    protected TransactionType transactionTypeToTransactionType1(br.com.coregate.domain.enums.TransactionType transactionType) {
        if ( transactionType == null ) {
            return null;
        }

        TransactionType transactionType1;

        switch ( transactionType ) {
            case TRANSACTION_TYPE_UNSPECIFIED: transactionType1 = TransactionType.TRANSACTION_TYPE_UNSPECIFIED;
            break;
            case TRANSACTION_TYPE_PURCHASE: transactionType1 = TransactionType.TRANSACTION_TYPE_PURCHASE;
            break;
            case TRANSACTION_TYPE_REVERSAL: transactionType1 = TransactionType.TRANSACTION_TYPE_REVERSAL;
            break;
            case TRANSACTION_TYPE_REFUND: transactionType1 = TransactionType.TRANSACTION_TYPE_REFUND;
            break;
            case TRANSACTION_TYPE_BALANCE_INQUIRY: transactionType1 = TransactionType.TRANSACTION_TYPE_BALANCE_INQUIRY;
            break;
            case TRANSACTION_TYPE_WITHDRAWAL: transactionType1 = TransactionType.TRANSACTION_TYPE_WITHDRAWAL;
            break;
            case TRANSACTION_TYPE_AUTHORIZATION: transactionType1 = TransactionType.TRANSACTION_TYPE_AUTHORIZATION;
            break;
            case UNRECOGNIZED: transactionType1 = TransactionType.UNRECOGNIZED;
            break;
            default: throw new IllegalArgumentException( "Unexpected enum constant: " + transactionType );
        }

        return transactionType1;
    }

    protected br.com.coregate.core.contracts.Pan panToPan1(Pan pan) {
        if ( pan == null ) {
            return null;
        }

        br.com.coregate.core.contracts.Pan.Builder pan1 = br.com.coregate.core.contracts.Pan.newBuilder();

        pan1.setValue( pan.getValue() );

        return pan1.build();
    }

    protected br.com.coregate.core.contracts.ProcessingCodeType processingCodeTypeToProcessingCodeType1(ProcessingCodeType processingCodeType) {
        if ( processingCodeType == null ) {
            return null;
        }

        br.com.coregate.core.contracts.ProcessingCodeType processingCodeType1;

        switch ( processingCodeType ) {
            case PROCESSING_CODE_UNKNOWN: processingCodeType1 = br.com.coregate.core.contracts.ProcessingCodeType.PROCESSING_CODE_UNKNOWN;
            break;
            case PURCHASE_DEBIT: processingCodeType1 = br.com.coregate.core.contracts.ProcessingCodeType.PURCHASE_DEBIT;
            break;
            case PURCHASE_CREDIT: processingCodeType1 = br.com.coregate.core.contracts.ProcessingCodeType.PURCHASE_CREDIT;
            break;
            case PURCHASE_INSTALLMENT: processingCodeType1 = br.com.coregate.core.contracts.ProcessingCodeType.PURCHASE_INSTALLMENT;
            break;
            case PURCHASE_CASHBACK: processingCodeType1 = br.com.coregate.core.contracts.ProcessingCodeType.PURCHASE_CASHBACK;
            break;
            case CASH_WITHDRAWAL: processingCodeType1 = br.com.coregate.core.contracts.ProcessingCodeType.CASH_WITHDRAWAL;
            break;
            case CASH_ADVANCE: processingCodeType1 = br.com.coregate.core.contracts.ProcessingCodeType.CASH_ADVANCE;
            break;
            case BILL_PAYMENT: processingCodeType1 = br.com.coregate.core.contracts.ProcessingCodeType.BILL_PAYMENT;
            break;
            case LOAN_PAYMENT: processingCodeType1 = br.com.coregate.core.contracts.ProcessingCodeType.LOAN_PAYMENT;
            break;
            case FUNDS_TRANSFER: processingCodeType1 = br.com.coregate.core.contracts.ProcessingCodeType.FUNDS_TRANSFER;
            break;
            case FUNDS_TRANSFER_SAVINGS: processingCodeType1 = br.com.coregate.core.contracts.ProcessingCodeType.FUNDS_TRANSFER_SAVINGS;
            break;
            case BALANCE_INQUIRY: processingCodeType1 = br.com.coregate.core.contracts.ProcessingCodeType.BALANCE_INQUIRY;
            break;
            case STATEMENT_INQUIRY: processingCodeType1 = br.com.coregate.core.contracts.ProcessingCodeType.STATEMENT_INQUIRY;
            break;
            case PRE_AUTH: processingCodeType1 = br.com.coregate.core.contracts.ProcessingCodeType.PRE_AUTH;
            break;
            case PRE_AUTH_CAPTURE: processingCodeType1 = br.com.coregate.core.contracts.ProcessingCodeType.PRE_AUTH_CAPTURE;
            break;
            case PRE_AUTH_CANCEL: processingCodeType1 = br.com.coregate.core.contracts.ProcessingCodeType.PRE_AUTH_CANCEL;
            break;
            case REVERSAL: processingCodeType1 = br.com.coregate.core.contracts.ProcessingCodeType.REVERSAL;
            break;
            case PARTIAL_REVERSAL: processingCodeType1 = br.com.coregate.core.contracts.ProcessingCodeType.PARTIAL_REVERSAL;
            break;
            case REVERSAL_PRE_AUTH: processingCodeType1 = br.com.coregate.core.contracts.ProcessingCodeType.REVERSAL_PRE_AUTH;
            break;
            case CHARGEBACK: processingCodeType1 = br.com.coregate.core.contracts.ProcessingCodeType.CHARGEBACK;
            break;
            case RELOAD_PREPAID: processingCodeType1 = br.com.coregate.core.contracts.ProcessingCodeType.RELOAD_PREPAID;
            break;
            case ECHO_TEST: processingCodeType1 = br.com.coregate.core.contracts.ProcessingCodeType.ECHO_TEST;
            break;
            case UNRECOGNIZED: processingCodeType1 = br.com.coregate.core.contracts.ProcessingCodeType.UNRECOGNIZED;
            break;
            default: throw new IllegalArgumentException( "Unexpected enum constant: " + processingCodeType );
        }

        return processingCodeType1;
    }

    protected TransactionCommand transactionCommandToTransactionCommand1(br.com.coregate.core.contracts.dto.transaction.TransactionCommand transactionCommand) {
        if ( transactionCommand == null ) {
            return null;
        }

        TransactionCommand.Builder transactionCommand1 = TransactionCommand.newBuilder();

        transactionCommand1.setTransactionId( transactionCommand.transactionId() );
        transactionCommand1.setTenantId( transactionCommand.tenantId() );
        transactionCommand1.setMerchantId( transactionCommand.merchantId() );
        if ( transactionCommand.amount() != null ) {
            transactionCommand1.setAmount( transactionCommand.amount().doubleValue() );
        }
        transactionCommand1.setCurrency( currencyCodeToCurrencyCode1( transactionCommand.currency() ) );
        transactionCommand1.setBrand( cardBrandTypeToCardBrandType1( transactionCommand.brand() ) );
        transactionCommand1.setChannel( channelTypeToChannelType1( transactionCommand.channel() ) );
        transactionCommand1.setType( transactionTypeToTransactionType1( transactionCommand.type() ) );
        transactionCommand1.setPan( panToPan1( transactionCommand.pan() ) );
        transactionCommand1.setMti( transactionCommand.mti() );
        transactionCommand1.setMcc( transactionCommand.mcc() );
        transactionCommand1.setOperation( processingCodeTypeToProcessingCodeType1( transactionCommand.operation() ) );

        return transactionCommand1.build();
    }

    protected TransactionStatus transactionStatusToTransactionStatus(br.com.coregate.core.contracts.TransactionStatus transactionStatus) {
        if ( transactionStatus == null ) {
            return null;
        }

        TransactionStatus transactionStatus1;

        switch ( transactionStatus ) {
            case TRANSACTION_STATUS_UNSPECIFIED: transactionStatus1 = TransactionStatus.TRANSACTION_STATUS_UNSPECIFIED;
            break;
            case PENDING: transactionStatus1 = TransactionStatus.PENDING;
            break;
            case AUTHORIZED: transactionStatus1 = TransactionStatus.AUTHORIZED;
            break;
            case REJECTED: transactionStatus1 = TransactionStatus.REJECTED;
            break;
            case SETTLED: transactionStatus1 = TransactionStatus.SETTLED;
            break;
            case CANCELED: transactionStatus1 = TransactionStatus.CANCELED;
            break;
            case END: transactionStatus1 = TransactionStatus.END;
            break;
            case ERROR: transactionStatus1 = TransactionStatus.ERROR;
            break;
            case UNRECOGNIZED: transactionStatus1 = TransactionStatus.UNRECOGNIZED;
            break;
            default: throw new IllegalArgumentException( "Unexpected enum constant: " + transactionStatus );
        }

        return transactionStatus1;
    }

    protected AuthorizationResult authorizationResultToAuthorizationResult(br.com.coregate.core.contracts.AuthorizationResult authorizationResult) {
        if ( authorizationResult == null ) {
            return null;
        }

        AuthorizationResult.AuthorizationResultBuilder authorizationResult1 = AuthorizationResult.builder();

        authorizationResult1.transactionId( authorizationResult.getTransactionId() );
        authorizationResult1.status( transactionStatusToTransactionStatus( authorizationResult.getStatus() ) );
        authorizationResult1.authorizationCode( authorizationResult.getAuthorizationCode() );
        authorizationResult1.responseCode( authorizationResult.getResponseCode() );
        if ( authorizationResult.hasDate() ) {
            authorizationResult1.date( dateTimeMapper.fromProto( authorizationResult.getDate() ) );
        }
        authorizationResult1.mti( authorizationResult.getMti() );

        return authorizationResult1.build();
    }

    protected br.com.coregate.core.contracts.TransactionStatus transactionStatusToTransactionStatus1(TransactionStatus transactionStatus) {
        if ( transactionStatus == null ) {
            return null;
        }

        br.com.coregate.core.contracts.TransactionStatus transactionStatus1;

        switch ( transactionStatus ) {
            case TRANSACTION_STATUS_UNSPECIFIED: transactionStatus1 = br.com.coregate.core.contracts.TransactionStatus.TRANSACTION_STATUS_UNSPECIFIED;
            break;
            case PENDING: transactionStatus1 = br.com.coregate.core.contracts.TransactionStatus.PENDING;
            break;
            case AUTHORIZED: transactionStatus1 = br.com.coregate.core.contracts.TransactionStatus.AUTHORIZED;
            break;
            case REJECTED: transactionStatus1 = br.com.coregate.core.contracts.TransactionStatus.REJECTED;
            break;
            case SETTLED: transactionStatus1 = br.com.coregate.core.contracts.TransactionStatus.SETTLED;
            break;
            case CANCELED: transactionStatus1 = br.com.coregate.core.contracts.TransactionStatus.CANCELED;
            break;
            case END: transactionStatus1 = br.com.coregate.core.contracts.TransactionStatus.END;
            break;
            case ERROR: transactionStatus1 = br.com.coregate.core.contracts.TransactionStatus.ERROR;
            break;
            case UNRECOGNIZED: transactionStatus1 = br.com.coregate.core.contracts.TransactionStatus.UNRECOGNIZED;
            break;
            default: throw new IllegalArgumentException( "Unexpected enum constant: " + transactionStatus );
        }

        return transactionStatus1;
    }

    protected br.com.coregate.core.contracts.AuthorizationResult authorizationResultToAuthorizationResult1(AuthorizationResult authorizationResult) {
        if ( authorizationResult == null ) {
            return null;
        }

        br.com.coregate.core.contracts.AuthorizationResult.Builder authorizationResult1 = br.com.coregate.core.contracts.AuthorizationResult.newBuilder();

        authorizationResult1.setTransactionId( authorizationResult.transactionId() );
        authorizationResult1.setStatus( transactionStatusToTransactionStatus1( authorizationResult.status() ) );
        authorizationResult1.setAuthorizationCode( authorizationResult.authorizationCode() );
        authorizationResult1.setResponseCode( authorizationResult.responseCode() );
        authorizationResult1.setDate( dateTimeMapper.toProto( authorizationResult.date() ) );
        authorizationResult1.setMti( authorizationResult.mti() );

        return authorizationResult1.build();
    }
}
