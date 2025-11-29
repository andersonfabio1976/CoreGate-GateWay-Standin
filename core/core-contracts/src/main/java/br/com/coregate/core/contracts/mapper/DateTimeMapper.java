package br.com.coregate.core.contracts.mapper;

import com.google.type.DateTime;
import org.mapstruct.Mapper;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Mapper(componentModel = "spring",
        uses = { DateTimeMapper.class }
        )
public interface DateTimeMapper {

    // LocalDateTime -> google.type.DateTime
    default DateTime toProto(LocalDateTime ldt) {
        if (ldt == null) return null;

        Instant instant = ldt.toInstant(ZoneOffset.UTC);

        return DateTime.newBuilder()
                .setYear(ldt.getYear())
                .setMonth(ldt.getMonthValue())
                .setDay(ldt.getDayOfMonth())
                .setHours(ldt.getHour())
                .setMinutes(ldt.getMinute())
                .setSeconds(ldt.getSecond())
                .setNanos(ldt.getNano())
                .build();
    }

    // google.type.DateTime -> LocalDateTime
    default LocalDateTime fromProto(DateTime dt) {
        if (dt == null) return null;

        return LocalDateTime.of(
                dt.getYear(),
                dt.getMonth(),
                dt.getDay(),
                dt.getHours(),
                dt.getMinutes(),
                dt.getSeconds(),
                dt.getNanos()
        );
    }
}
