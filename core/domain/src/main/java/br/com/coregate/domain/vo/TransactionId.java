package br.com.coregate.domain.vo;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Value
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class TransactionId {
    @NotBlank
    String value;
    public static TransactionId of(String v){ return new TransactionId(v.trim()); }
}