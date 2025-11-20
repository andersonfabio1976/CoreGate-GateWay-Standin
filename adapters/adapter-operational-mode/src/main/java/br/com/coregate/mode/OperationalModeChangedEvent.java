package br.com.coregate.mode;

import br.com.coregate.domain.enums.OperationalMode;
import lombok.*;
import java.time.LocalDateTime;


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
