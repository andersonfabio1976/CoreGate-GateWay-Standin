package br.com.coregate.domain.vo;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor
public class AdviceId {
    @NotBlank
    String value;
    public static AdviceId of(String v){ return new AdviceId(v.trim()); }
}