package br.com.coregate.application.dto;

import br.com.coregate.domain.enums.CardBrand;
import br.com.coregate.domain.enums.ChannelType;
import br.com.coregate.domain.enums.CurrencyCode;
import br.com.coregate.domain.enums.TransactionType;
import br.com.coregate.domain.vo.Pan;
import jakarta.validation.constraints.*;
import lombok.Builder;
import java.math.BigDecimal;

@Builder
public record TransactionCommand(
        @NotBlank String tenantId,
        @NotBlank String merchantId,
        @NotNull BigDecimal amount,
        @NotNull CurrencyCode currency,
        @NotNull CardBrand brand,
        @NotNull ChannelType channel,
        @NotNull TransactionType type,
        @NotBlank Pan pan,
        String authorizeCode,
        String responseCode

) {}
