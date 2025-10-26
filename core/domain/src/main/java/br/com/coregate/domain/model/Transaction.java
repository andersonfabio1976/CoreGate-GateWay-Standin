package br.com.coregate.domain.model;

import br.com.coregate.domain.enums.*;
import br.com.coregate.domain.vo.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Transaction {

    @NotNull private String id;
    @NotNull private TenantId tenantId;
    @NotNull private MerchantId merchantId;
    @NotNull private TransactionType type;
    @NotNull private TransactionStatus status;
    @NotNull private Money money;
    @NotNull private Pan pan;
    @NotNull private CardBrand brand;
    @NotNull private ChannelType channel;
    @NotNull private CurrencyCode currency;
    @NotNull private LocalDateTime createdAt;
    private String authorizationCode;
    private String responseCode;
    private LocalDateTime authorizedAt;
    private LocalDateTime settledAt;

    private String acquirerId; // resolvido via RoutingPolicy
    private String issuerId;


    private boolean standInApplied;

    // Fábrica segura
    public static Transaction createNew(TenantId tenant, Merchant merchant, Money money, Pan pan,
                                        CardBrand brand, ChannelType channel, TransactionType type) {
        merchant.ensureSameTenant(tenant);
        if(!merchant.isActive()) throw new IllegalStateException("merchant inactive");
        return Transaction.builder()
                .tenantId(tenant)
                .id(UUID.randomUUID().toString())
                .merchantId(merchant.getId())
                .type(type)
                .status(TransactionStatus.PENDING)
                .money(money)
                .pan(pan)
                .brand(brand)
                .channel(channel)
                .currency(money.getCurrency())
                .createdAt(LocalDateTime.now())
                .build();
    }

    public boolean isPending(){ return status == TransactionStatus.PENDING; }

    public void authorize(String authCode){
        if(!isPending()) throw new IllegalStateException("only PENDING can be authorized");
        this.authorizationCode = authCode;
        this.status = TransactionStatus.AUTHORIZED;
        this.authorizedAt = LocalDateTime.now();
    }

    public void reject(String resp){
        if(!isPending()) throw new IllegalStateException("only PENDING can be rejected");
        this.responseCode = resp;
        this.status = TransactionStatus.REJECTED;
    }

    public void settle(){
        if(status != TransactionStatus.AUTHORIZED)
            throw new IllegalStateException("only AUTHORIZED can be settled");
        this.status = TransactionStatus.SETTLED;
        this.settledAt = LocalDateTime.now();
    }

    public void applyStandInDecision(boolean approved, String code){
        if(!isPending()) throw new IllegalStateException("stand-in on non-pending");
        this.standInApplied = true;
        if(approved) authorize(code); else reject(code);
    }

    public void assertSameTenant(TenantId tenant){
        if(!tenantId.equals(tenant)) throw new IllegalStateException("cross-tenant transaction access");
    }
}
