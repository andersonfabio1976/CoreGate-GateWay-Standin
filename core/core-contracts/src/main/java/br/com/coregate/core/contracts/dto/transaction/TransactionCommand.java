package br.com.coregate.core.contracts.dto.transaction;

import br.com.coregate.domain.enums.*;
import br.com.coregate.domain.vo.Pan;
import br.com.coregate.domain.vo.TenantId;
import jakarta.validation.constraints.*;
import lombok.Builder;
import java.math.BigDecimal;

@Builder
public record TransactionCommand(
        @NotBlank String tenantId,
        @NotBlank String merchantId,
        @NotNull BigDecimal amount,
        @NotNull CurrencyCode currency,
        @NotNull CardBrandType brand,
        @NotNull ChannelType channel,
        @NotNull TransactionType type,
        @NotNull TransactionStatus status,
        @NotBlank Pan pan,
        @NotBlank String mti,
        @NotBlank String mcc,
        @NotBlank ProcessingCodeType operation
        ) {}
