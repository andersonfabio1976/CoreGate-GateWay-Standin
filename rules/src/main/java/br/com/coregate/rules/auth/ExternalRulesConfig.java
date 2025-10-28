package br.com.coregate.rules.auth;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class ExternalRulesConfig {
    // thresholds
    @Builder.Default private long   autoApproveMaxAmountCents = 10_000; // R$ 100,00
    @Builder.Default private long   panDailyLimitCents        = 200_000; // R$ 2.000,00
    @Builder.Default private double maxRiskScoreAutoApprove   = 50.0;

    // lists
    @Builder.Default private List<String> mccBlacklist = List.of("4829","6012","7995");
    @Builder.Default private List<String> mccWhitelist = List.of("5411","5311","5732");

    // velocity
    @Builder.Default private int velocityWindowSeconds = 60;
    @Builder.Default private int velocityMaxCount      = 3;

    // flags
    @Builder.Default private boolean gamblingAllowed   = false;

    public static ExternalRulesConfig defaults() { return ExternalRulesConfig.builder().build(); }
}
