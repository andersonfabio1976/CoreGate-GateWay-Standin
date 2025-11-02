package br.com.coregate.infrastructure.mock;

import br.com.coregate.application.dto.transaction.TransactionCommand;
import br.com.coregate.domain.enums.CardBrandType;
import br.com.coregate.domain.enums.ChannelType;
import br.com.coregate.domain.enums.CurrencyCode;
import br.com.coregate.domain.enums.TransactionType;
import br.com.coregate.domain.vo.Money;
import br.com.coregate.domain.vo.Pan;
import br.com.coregate.domain.vo.TenantId;
import br.com.coregate.domain.vo.TransactionId;
import java.math.BigDecimal;

public class TransactionCommandMockBuilder {
    public static TransactionCommand build() {
        TransactionId id = TransactionId.builder()
                .value("1000")
                .build();

        Money money = Money.builder()
                .currency(CurrencyCode.BRL)
                .amount(new BigDecimal("1500"))
                .build();
        TenantId tenantId = TenantId.of("Tenant-001");


        TransactionCommand transaction = TransactionCommand.builder()
                .brand(CardBrandType.AMEX)
                .currency(CurrencyCode.USD)
                .channel(ChannelType.API_GATEWAY)
                .tenantId("tenantId")
                .amount(new BigDecimal("1500"))
                .merchantId("111111")
                .pan(new Pan("111111111122"))
                .brand(CardBrandType.AMEX)
                .type(TransactionType.TRANSACTION_TYPE_AUTHORIZATION)
                .build();
        return transaction;
    }
}
