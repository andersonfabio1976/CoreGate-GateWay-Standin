package br.com.coregate.core.contracts.mapper;

import br.com.coregate.core.contracts.dto.transaction.TransactionCommand;
import br.com.coregate.domain.model.Transaction;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-11-29T08:54:01-0300",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.8 (Oracle Corporation)"
)
@Component
public class TransactionMapperImpl implements TransactionMapper {

    @Override
    public TransactionCommand toDto(Transaction domain) {
        if ( domain == null ) {
            return null;
        }

        TransactionCommand.TransactionCommandBuilder transactionCommand = TransactionCommand.builder();

        transactionCommand.tenantId( domain.getTenantId() );
        transactionCommand.merchantId( domain.getMerchantId() );
        transactionCommand.currency( domain.getCurrency() );
        transactionCommand.brand( domain.getBrand() );
        transactionCommand.channel( domain.getChannel() );
        transactionCommand.type( domain.getType() );
        transactionCommand.status( domain.getStatus() );
        transactionCommand.pan( domain.getPan() );
        transactionCommand.mti( domain.getMti() );
        transactionCommand.mcc( domain.getMcc() );

        return transactionCommand.build();
    }

    @Override
    public Transaction toDomain(TransactionCommand dto) {
        if ( dto == null ) {
            return null;
        }

        Transaction.TransactionBuilder transaction = Transaction.builder();

        transaction.tenantId( dto.tenantId() );
        transaction.merchantId( dto.merchantId() );
        transaction.type( dto.type() );
        transaction.status( dto.status() );
        transaction.pan( dto.pan() );
        transaction.brand( dto.brand() );
        transaction.channel( dto.channel() );
        transaction.currency( dto.currency() );
        transaction.mcc( dto.mcc() );
        transaction.mti( dto.mti() );

        return transaction.build();
    }
}
