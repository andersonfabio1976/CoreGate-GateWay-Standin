package br.com.coregate.domain.model;

import br.com.coregate.domain.vo.MerchantId;
import br.com.coregate.domain.vo.TenantId;
import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Merchant {
    @NotNull private TenantId tenantId;
    @NotNull private MerchantId id;
    @NotBlank private String name;
    @NotBlank private String mcc;
    private boolean active;

    public void ensureSameTenant(TenantId t){
        if(!this.tenantId.equals(t)) throw new IllegalStateException("cross-tenant operation");
    }
    public void activate(){ this.active = true; }
    public void deactivate(){ this.active = false; }
}
