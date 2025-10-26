package br.com.coregate.infrastructure.mapper;

import br.com.coregate.application.dto.TransactionCommand;
import br.com.coregate.application.dto.TransactionCommandRequest;
import br.com.coregate.application.dto.TransactionCommandResponse;
import br.com.coregate.domain.enums.CardBrand;
import br.com.coregate.domain.enums.TransactionType;
import br.com.coregate.domain.model.Transaction;
import br.com.coregate.infrastructure.grpc.ChannelType;
import br.com.coregate.infrastructure.grpc.CurrencyCode;
import br.com.coregate.infrastructure.grpc.TransactionCommandProto;
import br.com.coregate.infrastructure.grpc.TransactionCommandProtoRequest;
import br.com.coregate.infrastructure.grpc.TransactionCommandProtoResponse;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.8 (Oracle Corporation)"
)
@Component
public class TransactionMapperImpl implements TransactionMapper {

    @Autowired
    private CommonValueObjectMapper commonValueObjectMapper;

    @Override
    public TransactionCommandProto toProto(TransactionCommand dto) {
        if ( dto == null ) {
            return null;
        }

        TransactionCommandProto.Builder transactionCommandProto = TransactionCommandProto.newBuilder();

        transactionCommandProto.setTenantId( dto.tenantId() );
        transactionCommandProto.setMerchantId( dto.merchantId() );
        transactionCommandProto.setAmount( commonValueObjectMapper.map( dto.amount() ) );
        transactionCommandProto.setCurrency( currencyCodeToCurrencyCode( dto.currency() ) );
        transactionCommandProto.setBrand( cardBrandToCardBrand( dto.brand() ) );
        transactionCommandProto.setChannel( channelTypeToChannelType( dto.channel() ) );
        transactionCommandProto.setType( transactionTypeToTransactionType( dto.type() ) );
        transactionCommandProto.setPan( commonValueObjectMapper.toProtoPan( dto.pan() ) );

        return transactionCommandProto.build();
    }

    @Override
    public TransactionCommand fromProto(TransactionCommandProto proto) {
        if ( proto == null ) {
            return null;
        }

        TransactionCommand.TransactionCommandBuilder transactionCommand = TransactionCommand.builder();

        transactionCommand.tenantId( proto.getTenantId() );
        transactionCommand.merchantId( proto.getMerchantId() );
        if ( proto.hasAmount() ) {
            transactionCommand.amount( commonValueObjectMapper.map( proto.getAmount() ) );
        }
        transactionCommand.currency( currencyCodeToCurrencyCode1( proto.getCurrency() ) );
        transactionCommand.brand( cardBrandToCardBrand1( proto.getBrand() ) );
        transactionCommand.channel( channelTypeToChannelType1( proto.getChannel() ) );
        transactionCommand.type( transactionTypeToTransactionType1( proto.getType() ) );
        if ( proto.hasPan() ) {
            transactionCommand.pan( commonValueObjectMapper.toDomainPan( proto.getPan() ) );
        }

        return transactionCommand.build();
    }

    @Override
    public TransactionCommandProtoRequest toProtoRequest(TransactionCommandRequest dto) {
        if ( dto == null ) {
            return null;
        }

        TransactionCommandProtoRequest.Builder transactionCommandProtoRequest = TransactionCommandProtoRequest.newBuilder();

        return transactionCommandProtoRequest.build();
    }

    @Override
    public TransactionCommandProtoResponse toProtoResponse(TransactionCommandResponse dto) {
        if ( dto == null ) {
            return null;
        }

        TransactionCommandProtoResponse.Builder transactionCommandProtoResponse = TransactionCommandProtoResponse.newBuilder();

        return transactionCommandProtoResponse.build();
    }

    @Override
    public TransactionCommandRequest fromProtoRequest(TransactionCommandProtoRequest proto) {
        if ( proto == null ) {
            return null;
        }

        TransactionCommand transactionCommand = null;

        TransactionCommandRequest transactionCommandRequest = new TransactionCommandRequest( transactionCommand );

        return transactionCommandRequest;
    }

    @Override
    public TransactionCommandResponse fromProtoResponse(TransactionCommandProtoResponse proto) {
        if ( proto == null ) {
            return null;
        }

        TransactionCommandResponse.TransactionCommandResponseBuilder transactionCommandResponse = TransactionCommandResponse.builder();

        return transactionCommandResponse.build();
    }

    @Override
    public Transaction fromProtoToDomain(TransactionCommandProto proto) {
        if ( proto == null ) {
            return null;
        }

        Transaction.TransactionBuilder transaction = Transaction.builder();

        transaction.tenantId( commonValueObjectMapper.toTenantId( proto.getTenantId() ) );
        transaction.merchantId( commonValueObjectMapper.toMerchantId( proto.getMerchantId() ) );
        transaction.type( transactionTypeToTransactionType1( proto.getType() ) );
        if ( proto.hasPan() ) {
            transaction.pan( commonValueObjectMapper.toDomainPan( proto.getPan() ) );
        }
        transaction.brand( cardBrandToCardBrand1( proto.getBrand() ) );
        transaction.channel( channelTypeToChannelType1( proto.getChannel() ) );
        transaction.currency( currencyCodeToCurrencyCode1( proto.getCurrency() ) );

        return transaction.build();
    }

    @Override
    public Transaction fromDtoToDomain(TransactionCommand dto) {
        if ( dto == null ) {
            return null;
        }

        Transaction.TransactionBuilder transaction = Transaction.builder();

        transaction.tenantId( commonValueObjectMapper.toTenantId( dto.tenantId() ) );
        transaction.merchantId( commonValueObjectMapper.toMerchantId( dto.merchantId() ) );
        transaction.type( dto.type() );
        transaction.pan( dto.pan() );
        transaction.brand( dto.brand() );
        transaction.channel( dto.channel() );
        transaction.currency( dto.currency() );
        transaction.responseCode( dto.responseCode() );

        return transaction.build();
    }

    @Override
    public TransactionCommandProto toProtoFromDomain(Transaction domain) {
        if ( domain == null ) {
            return null;
        }

        TransactionCommandProto.Builder transactionCommandProto = TransactionCommandProto.newBuilder();

        transactionCommandProto.setTenantId( commonValueObjectMapper.map( domain.getTenantId() ) );
        transactionCommandProto.setMerchantId( commonValueObjectMapper.map( domain.getMerchantId() ) );
        transactionCommandProto.setCurrency( currencyCodeToCurrencyCode( domain.getCurrency() ) );
        transactionCommandProto.setBrand( cardBrandToCardBrand( domain.getBrand() ) );
        transactionCommandProto.setChannel( channelTypeToChannelType( domain.getChannel() ) );
        transactionCommandProto.setType( transactionTypeToTransactionType( domain.getType() ) );
        transactionCommandProto.setPan( commonValueObjectMapper.toProtoPan( domain.getPan() ) );

        return transactionCommandProto.build();
    }

    @Override
    public TransactionCommand toDtoFromDomain(Transaction domain) {
        if ( domain == null ) {
            return null;
        }

        TransactionCommand.TransactionCommandBuilder transactionCommand = TransactionCommand.builder();

        transactionCommand.tenantId( commonValueObjectMapper.map( domain.getTenantId() ) );
        transactionCommand.merchantId( commonValueObjectMapper.map( domain.getMerchantId() ) );
        transactionCommand.currency( domain.getCurrency() );
        transactionCommand.brand( domain.getBrand() );
        transactionCommand.channel( domain.getChannel() );
        transactionCommand.type( domain.getType() );
        transactionCommand.pan( domain.getPan() );
        transactionCommand.responseCode( domain.getResponseCode() );

        return transactionCommand.build();
    }

    protected CurrencyCode currencyCodeToCurrencyCode(br.com.coregate.domain.enums.CurrencyCode currencyCode) {
        if ( currencyCode == null ) {
            return null;
        }

        CurrencyCode currencyCode1;

        switch ( currencyCode ) {
            case BRL: currencyCode1 = CurrencyCode.BRL;
            break;
            case USD: currencyCode1 = CurrencyCode.USD;
            break;
            case EUR: currencyCode1 = CurrencyCode.EUR;
            break;
            case UNRECOGNIZED: currencyCode1 = CurrencyCode.UNRECOGNIZED;
            break;
            default: throw new IllegalArgumentException( "Unexpected enum constant: " + currencyCode );
        }

        return currencyCode1;
    }

    protected br.com.coregate.infrastructure.grpc.CardBrand cardBrandToCardBrand(CardBrand cardBrand) {
        if ( cardBrand == null ) {
            return null;
        }

        br.com.coregate.infrastructure.grpc.CardBrand cardBrand1;

        switch ( cardBrand ) {
            case VISA: cardBrand1 = br.com.coregate.infrastructure.grpc.CardBrand.VISA;
            break;
            case MASTERCARD: cardBrand1 = br.com.coregate.infrastructure.grpc.CardBrand.MASTERCARD;
            break;
            case ELO: cardBrand1 = br.com.coregate.infrastructure.grpc.CardBrand.ELO;
            break;
            case AMEX: cardBrand1 = br.com.coregate.infrastructure.grpc.CardBrand.AMEX;
            break;
            case HIPER: cardBrand1 = br.com.coregate.infrastructure.grpc.CardBrand.HIPER;
            break;
            case OTHER: cardBrand1 = br.com.coregate.infrastructure.grpc.CardBrand.OTHER;
            break;
            case UNRECOGNIZED: cardBrand1 = br.com.coregate.infrastructure.grpc.CardBrand.UNRECOGNIZED;
            break;
            default: throw new IllegalArgumentException( "Unexpected enum constant: " + cardBrand );
        }

        return cardBrand1;
    }

    protected ChannelType channelTypeToChannelType(br.com.coregate.domain.enums.ChannelType channelType) {
        if ( channelType == null ) {
            return null;
        }

        ChannelType channelType1;

        switch ( channelType ) {
            case POS: channelType1 = ChannelType.POS;
            break;
            case ECOMMERCE: channelType1 = ChannelType.ECOMMERCE;
            break;
            case MOBILE: channelType1 = ChannelType.MOBILE;
            break;
            case MOTO: channelType1 = ChannelType.MOTO;
            break;
            case UNRECOGNIZED: channelType1 = ChannelType.UNRECOGNIZED;
            break;
            default: throw new IllegalArgumentException( "Unexpected enum constant: " + channelType );
        }

        return channelType1;
    }

    protected br.com.coregate.infrastructure.grpc.TransactionType transactionTypeToTransactionType(TransactionType transactionType) {
        if ( transactionType == null ) {
            return null;
        }

        br.com.coregate.infrastructure.grpc.TransactionType transactionType1;

        switch ( transactionType ) {
            case PURCHASE: transactionType1 = br.com.coregate.infrastructure.grpc.TransactionType.PURCHASE;
            break;
            case REVERSAL: transactionType1 = br.com.coregate.infrastructure.grpc.TransactionType.REVERSAL;
            break;
            case REFUND: transactionType1 = br.com.coregate.infrastructure.grpc.TransactionType.REFUND;
            break;
            case BALANCE_INQUIRY: transactionType1 = br.com.coregate.infrastructure.grpc.TransactionType.BALANCE_INQUIRY;
            break;
            case WITHDRAWAL: transactionType1 = br.com.coregate.infrastructure.grpc.TransactionType.WITHDRAWAL;
            break;
            case AUTHORIZATION: transactionType1 = br.com.coregate.infrastructure.grpc.TransactionType.AUTHORIZATION;
            break;
            case UNRECOGNIZED: transactionType1 = br.com.coregate.infrastructure.grpc.TransactionType.UNRECOGNIZED;
            break;
            default: throw new IllegalArgumentException( "Unexpected enum constant: " + transactionType );
        }

        return transactionType1;
    }

    protected br.com.coregate.domain.enums.CurrencyCode currencyCodeToCurrencyCode1(CurrencyCode currencyCode) {
        if ( currencyCode == null ) {
            return null;
        }

        br.com.coregate.domain.enums.CurrencyCode currencyCode1;

        switch ( currencyCode ) {
            case BRL: currencyCode1 = br.com.coregate.domain.enums.CurrencyCode.BRL;
            break;
            case USD: currencyCode1 = br.com.coregate.domain.enums.CurrencyCode.USD;
            break;
            case EUR: currencyCode1 = br.com.coregate.domain.enums.CurrencyCode.EUR;
            break;
            case UNRECOGNIZED: currencyCode1 = br.com.coregate.domain.enums.CurrencyCode.UNRECOGNIZED;
            break;
            default: throw new IllegalArgumentException( "Unexpected enum constant: " + currencyCode );
        }

        return currencyCode1;
    }

    protected CardBrand cardBrandToCardBrand1(br.com.coregate.infrastructure.grpc.CardBrand cardBrand) {
        if ( cardBrand == null ) {
            return null;
        }

        CardBrand cardBrand1;

        switch ( cardBrand ) {
            case VISA: cardBrand1 = CardBrand.VISA;
            break;
            case MASTERCARD: cardBrand1 = CardBrand.MASTERCARD;
            break;
            case ELO: cardBrand1 = CardBrand.ELO;
            break;
            case AMEX: cardBrand1 = CardBrand.AMEX;
            break;
            case HIPER: cardBrand1 = CardBrand.HIPER;
            break;
            case OTHER: cardBrand1 = CardBrand.OTHER;
            break;
            case UNRECOGNIZED: cardBrand1 = CardBrand.UNRECOGNIZED;
            break;
            default: throw new IllegalArgumentException( "Unexpected enum constant: " + cardBrand );
        }

        return cardBrand1;
    }

    protected br.com.coregate.domain.enums.ChannelType channelTypeToChannelType1(ChannelType channelType) {
        if ( channelType == null ) {
            return null;
        }

        br.com.coregate.domain.enums.ChannelType channelType1;

        switch ( channelType ) {
            case POS: channelType1 = br.com.coregate.domain.enums.ChannelType.POS;
            break;
            case ECOMMERCE: channelType1 = br.com.coregate.domain.enums.ChannelType.ECOMMERCE;
            break;
            case MOBILE: channelType1 = br.com.coregate.domain.enums.ChannelType.MOBILE;
            break;
            case MOTO: channelType1 = br.com.coregate.domain.enums.ChannelType.MOTO;
            break;
            case UNRECOGNIZED: channelType1 = br.com.coregate.domain.enums.ChannelType.UNRECOGNIZED;
            break;
            default: throw new IllegalArgumentException( "Unexpected enum constant: " + channelType );
        }

        return channelType1;
    }

    protected TransactionType transactionTypeToTransactionType1(br.com.coregate.infrastructure.grpc.TransactionType transactionType) {
        if ( transactionType == null ) {
            return null;
        }

        TransactionType transactionType1;

        switch ( transactionType ) {
            case PURCHASE: transactionType1 = TransactionType.PURCHASE;
            break;
            case REVERSAL: transactionType1 = TransactionType.REVERSAL;
            break;
            case REFUND: transactionType1 = TransactionType.REFUND;
            break;
            case BALANCE_INQUIRY: transactionType1 = TransactionType.BALANCE_INQUIRY;
            break;
            case AUTHORIZATION: transactionType1 = TransactionType.AUTHORIZATION;
            break;
            case WITHDRAWAL: transactionType1 = TransactionType.WITHDRAWAL;
            break;
            case UNRECOGNIZED: transactionType1 = TransactionType.UNRECOGNIZED;
            break;
            default: throw new IllegalArgumentException( "Unexpected enum constant: " + transactionType );
        }

        return transactionType1;
    }
}
