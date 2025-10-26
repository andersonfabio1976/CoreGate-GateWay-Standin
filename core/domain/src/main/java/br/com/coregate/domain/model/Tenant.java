package br.com.coregate.domain.model;

import br.com.coregate.domain.enums.CardBrand;
import br.com.coregate.domain.vo.Money;
import br.com.coregate.domain.vo.TenantId;
import br.com.coregate.domain.vo.TimeWindow;
import jakarta.validation.constraints.*;
import lombok.*;
import java.math.BigDecimal;
import java.util.Set;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Tenant {

    @NotNull private TenantId id;
    @NotBlank private String name;
    private boolean active;

    @NotNull private StandInPolicy standInPolicy;
    @NotNull private RoutingPolicy routingPolicy;
    @NotNull private RiskPolicy riskPolicy;

    public void activate(){ this.active = true; }
    public void deactivate(){ this.active = false; }

    @Getter
    @Builder
    @AllArgsConstructor @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class StandInPolicy {
        @NotNull private TimeWindow window;
        @NotNull private BigDecimal maxAmountPerTxn;
        @NotNull private BigDecimal maxDailyAmount;
        @NotNull private Set<CardBrand> allowedBrands;
        private boolean enabled;

        public boolean canStandIn(Money money, CardBrand brand, java.time.LocalTime now,
                                  BigDecimal tenantDailyUsed){
            if(!enabled) return false;
            if(!window.contains(now)) return false;
            if(!allowedBrands.contains(brand)) return false;
            if(money.getAmount().compareTo(maxAmountPerTxn) > 0) return false;
            if(tenantDailyUsed.add(money.getAmount()).compareTo(maxDailyAmount) > 0) return false;
            return true;
        }
    }

    @Getter
    @Builder
    @AllArgsConstructor @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class RoutingPolicy {
        /** exemplo simples: roteamento preferencial por brand */
        @NotNull private java.util.Map<CardBrand, String> preferredAcquirerByBrand; // brand -> acquirerId
        public String resolveAcquirer(CardBrand brand){
            return preferredAcquirerByBrand.getOrDefault(brand, preferredAcquirerByBrand.get(CardBrand.OTHER));
        }
    }

    @Getter
    @Builder
    @AllArgsConstructor @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class RiskPolicy {
        @NotNull private BigDecimal maxAmountPerMerchant;
        private boolean blockMCCHighRisk;
        public void checkMerchantLimit(Money money){
            if(money.getAmount().compareTo(maxAmountPerMerchant) > 0)
                throw new IllegalStateException("amount exceeds tenant merchant limit");
        }
    }
}
