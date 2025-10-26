package br.com.coregate.domain.vo;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;

@Value
public class MerchantId {
    @NotBlank
    String value;
    public static MerchantId of(String v){ return new MerchantId(v.trim()); }
}