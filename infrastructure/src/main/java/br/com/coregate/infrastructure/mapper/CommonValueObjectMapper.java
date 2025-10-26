package br.com.coregate.infrastructure.mapper;

import br.com.coregate.domain.vo.*;
import com.google.type.Decimal;
import org.mapstruct.Mapper;
import java.math.BigDecimal;

@Mapper(componentModel = "spring")
public interface CommonValueObjectMapper {

    // ==============================
    // 🧩 Tenant / Transaction / Merchant
    // ==============================
    default String map(TenantId value) {
        return value != null ? value.getValue() : null;
    }

    default String map(TransactionId value) {
        return value != null ? value.getValue() : null;
    }

    default String map(MerchantId value) {
        return value != null ? value.getValue() : null;
    }

    default TenantId toTenantId(String id) {
        return id != null ? TenantId.of(id) : null;
    }

    default TransactionId toTransactionId(String id) {
        return id != null ? TransactionId.of(id) : null;
    }

    default MerchantId toMerchantId(String id) {
        return id != null ? MerchantId.of(id) : null;
    }

    // ==============================
    // 💰 Money (Decimal)
    // ==============================
    default BigDecimal map(Decimal value) {
        if (value == null || value.getValue().isEmpty()) {
            return null;
        }
        return new BigDecimal(value.getValue());
    }

    default Decimal map(BigDecimal value) {
        if (value == null) {
            return Decimal.newBuilder().setValue("0").build();
        }
        return Decimal.newBuilder().setValue(value.toPlainString()).build();
    }

    // ==============================
    // 💳 Pan (Value Object ↔ Proto)
    // ==============================
    default br.com.coregate.domain.vo.Pan toDomainPan(br.com.coregate.infrastructure.grpc.Pan proto) {
        if (proto == null || proto.getValue().isEmpty()) {
            return null;
        }
        return br.com.coregate.domain.vo.Pan.of(proto.getValue());
    }

    default br.com.coregate.infrastructure.grpc.Pan toProtoPan(br.com.coregate.domain.vo.Pan domain) {
        if (domain == null || domain.getValue() == null) {
            return br.com.coregate.infrastructure.grpc.Pan.newBuilder().setValue("").build();
        }
        return br.com.coregate.infrastructure.grpc.Pan.newBuilder()
                .setValue(domain.getValue())
                .build();
    }

    // ==============================
    // 💬 Pan (opcional String fallback)
    // ==============================
    default String toStringPan(br.com.coregate.domain.vo.Pan pan) {
        return pan != null ? pan.getValue() : null;
    }

    default br.com.coregate.domain.vo.Pan fromStringPan(String value) {
        return value != null ? br.com.coregate.domain.vo.Pan.of(value) : null;
    }
}
