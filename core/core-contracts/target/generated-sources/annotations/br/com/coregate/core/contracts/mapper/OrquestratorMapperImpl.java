package br.com.coregate.core.contracts.mapper;

import br.com.coregate.core.contracts.dto.orquestrator.OrquestratorRequestDto;
import br.com.coregate.core.contracts.dto.orquestrator.OrquestratorResponseDto;
import br.com.coregate.proto.Orquestrator.OrquestratorRequestProto;
import br.com.coregate.proto.Orquestrator.OrquestratorResponseProto;
import br.com.coregate.proto.common.AuthorizationResult;
import br.com.coregate.proto.common.TransactionStatus;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-11-19T21:41:46-0300",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.8 (Oracle Corporation)"
)
@Component
public class OrquestratorMapperImpl implements OrquestratorMapper {

    @Override
    public OrquestratorResponseProto toProto(OrquestratorResponseDto dto) {
        if ( dto == null ) {
            return null;
        }

        OrquestratorResponseProto.Builder orquestratorResponseProto = OrquestratorResponseProto.newBuilder();

        orquestratorResponseProto.setAuthorizationResult( authorizationResultToAuthorizationResult( dto.authorizationResult() ) );

        return orquestratorResponseProto.build();
    }

    @Override
    public OrquestratorRequestProto toProto(OrquestratorRequestDto dto) {
        if ( dto == null ) {
            return null;
        }

        OrquestratorRequestProto.Builder orquestratorRequestProto = OrquestratorRequestProto.newBuilder();

        return orquestratorRequestProto.build();
    }

    @Override
    public OrquestratorResponseDto toDto(OrquestratorResponseProto proto) {
        if ( proto == null ) {
            return null;
        }

        OrquestratorResponseDto.OrquestratorResponseDtoBuilder orquestratorResponseDto = OrquestratorResponseDto.builder();

        if ( proto.hasAuthorizationResult() ) {
            orquestratorResponseDto.authorizationResult( authorizationResultToAuthorizationResult1( proto.getAuthorizationResult() ) );
        }

        return orquestratorResponseDto.build();
    }

    @Override
    public OrquestratorRequestDto toDto(OrquestratorRequestProto proto) {
        if ( proto == null ) {
            return null;
        }

        OrquestratorRequestDto.OrquestratorRequestDtoBuilder orquestratorRequestDto = OrquestratorRequestDto.builder();

        return orquestratorRequestDto.build();
    }

    protected TransactionStatus transactionStatusToTransactionStatus(br.com.coregate.domain.enums.TransactionStatus transactionStatus) {
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

    protected AuthorizationResult authorizationResultToAuthorizationResult(br.com.coregate.core.contracts.dto.transaction.AuthorizationResult authorizationResult) {
        if ( authorizationResult == null ) {
            return null;
        }

        AuthorizationResult.Builder authorizationResult1 = AuthorizationResult.newBuilder();

        authorizationResult1.setTransactionId( authorizationResult.transactionId() );
        authorizationResult1.setStatus( transactionStatusToTransactionStatus( authorizationResult.status() ) );
        authorizationResult1.setAuthorizationCode( authorizationResult.authorizationCode() );
        authorizationResult1.setResponseCode( authorizationResult.responseCode() );
        authorizationResult1.setMti( authorizationResult.mti() );

        return authorizationResult1.build();
    }

    protected br.com.coregate.domain.enums.TransactionStatus transactionStatusToTransactionStatus1(TransactionStatus transactionStatus) {
        if ( transactionStatus == null ) {
            return null;
        }

        br.com.coregate.domain.enums.TransactionStatus transactionStatus1;

        switch ( transactionStatus ) {
            case TRANSACTION_STATUS_UNSPECIFIED: transactionStatus1 = br.com.coregate.domain.enums.TransactionStatus.TRANSACTION_STATUS_UNSPECIFIED;
            break;
            case PENDING: transactionStatus1 = br.com.coregate.domain.enums.TransactionStatus.PENDING;
            break;
            case AUTHORIZED: transactionStatus1 = br.com.coregate.domain.enums.TransactionStatus.AUTHORIZED;
            break;
            case REJECTED: transactionStatus1 = br.com.coregate.domain.enums.TransactionStatus.REJECTED;
            break;
            case SETTLED: transactionStatus1 = br.com.coregate.domain.enums.TransactionStatus.SETTLED;
            break;
            case CANCELED: transactionStatus1 = br.com.coregate.domain.enums.TransactionStatus.CANCELED;
            break;
            case END: transactionStatus1 = br.com.coregate.domain.enums.TransactionStatus.END;
            break;
            case ERROR: transactionStatus1 = br.com.coregate.domain.enums.TransactionStatus.ERROR;
            break;
            case UNRECOGNIZED: transactionStatus1 = br.com.coregate.domain.enums.TransactionStatus.UNRECOGNIZED;
            break;
            default: throw new IllegalArgumentException( "Unexpected enum constant: " + transactionStatus );
        }

        return transactionStatus1;
    }

    protected br.com.coregate.core.contracts.dto.transaction.AuthorizationResult authorizationResultToAuthorizationResult1(AuthorizationResult authorizationResult) {
        if ( authorizationResult == null ) {
            return null;
        }

        br.com.coregate.core.contracts.dto.transaction.AuthorizationResult.AuthorizationResultBuilder authorizationResult1 = br.com.coregate.core.contracts.dto.transaction.AuthorizationResult.builder();

        authorizationResult1.transactionId( authorizationResult.getTransactionId() );
        authorizationResult1.status( transactionStatusToTransactionStatus1( authorizationResult.getStatus() ) );
        authorizationResult1.authorizationCode( authorizationResult.getAuthorizationCode() );
        authorizationResult1.responseCode( authorizationResult.getResponseCode() );
        authorizationResult1.mti( authorizationResult.getMti() );

        return authorizationResult1.build();
    }
}
