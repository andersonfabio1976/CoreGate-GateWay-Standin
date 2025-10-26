package br.com.coregate.domain.vo;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor
public class TenantId {
    @NotBlank
    String value;
    public static TenantId of(String v){ return new TenantId(v.trim()); }
}