package br.com.coregate.infrastructure.mode;

import lombok.*;

import java.time.LocalDateTime;
import br.com.coregate.infrastructure.enums.OperationalMode;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OperationalModeChangedEvent {
    private OperationalMode newMode;
    private String reason;
    private LocalDateTime timestamp;
}
