package br.com.coregate.domain.vo;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;
import java.time.LocalTime;

@Value
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class TimeWindow {
    @NotNull
    LocalTime start;
    @NotNull LocalTime end;
    public static TimeWindow of(LocalTime s, LocalTime e){
        if(s == null || e == null) throw new IllegalArgumentException("invalid window");
        // aceita janelas que cruzam meia-noite
        return new TimeWindow(s, e);
    }
    public boolean contains(LocalTime t){
        if(start.equals(end)) return true;
        return start.isBefore(end) ? !t.isBefore(start) && !t.isAfter(end)
                : ( !t.isBefore(start) || !t.isAfter(end) );
    }
}