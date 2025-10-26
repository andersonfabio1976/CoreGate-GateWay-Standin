package br.com.coregate.domain.vo;

import br.com.coregate.domain.enums.CurrencyCode;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import java.math.BigDecimal;

@Value
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class Money {
    @NotNull
    BigDecimal amount;
    @NotNull
    CurrencyCode currency;
    public static Money of(BigDecimal a, CurrencyCode c){
        if(a == null || a.signum() <= 0) throw new IllegalArgumentException("amount must be > 0");
        return new Money(a, c);
    }
}