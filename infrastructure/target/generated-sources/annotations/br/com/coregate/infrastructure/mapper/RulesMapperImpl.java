package br.com.coregate.infrastructure.mapper;

import br.com.coregate.application.dto.rules.DecisionOutcome;
import br.com.coregate.application.dto.rules.RulesRequestDto;
import br.com.coregate.application.dto.rules.RulesResponseDto;
import br.com.coregate.application.dto.rules.StandinDecision;
import br.com.coregate.proto.rules.RulesRequestProto;
import br.com.coregate.proto.rules.RulesResponseProto;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.8 (Oracle Corporation)"
)
@Component
public class RulesMapperImpl implements RulesMapper {

    @Override
    public RulesRequestProto toProto(RulesRequestDto dto) {
        if ( dto == null ) {
            return null;
        }

        RulesRequestProto.Builder rulesRequestProto = RulesRequestProto.newBuilder();

        return rulesRequestProto.build();
    }

    @Override
    public RulesRequestDto toDto(RulesRequestProto proto) {
        if ( proto == null ) {
            return null;
        }

        RulesRequestDto.RulesRequestDtoBuilder rulesRequestDto = RulesRequestDto.builder();

        return rulesRequestDto.build();
    }

    @Override
    public RulesResponseProto toProto(RulesResponseDto dto) {
        if ( dto == null ) {
            return null;
        }

        RulesResponseProto.Builder rulesResponseProto = RulesResponseProto.newBuilder();

        rulesResponseProto.setDecision( standinDecisionToStandinDecision( dto.decision() ) );
        if ( dto.evaluatedAtEpochMs() != null ) {
            rulesResponseProto.setEvaluatedAtEpochMs( dto.evaluatedAtEpochMs() );
        }

        return rulesResponseProto.build();
    }

    @Override
    public RulesResponseDto toDto(RulesResponseProto proto) {
        if ( proto == null ) {
            return null;
        }

        RulesResponseDto.RulesResponseDtoBuilder rulesResponseDto = RulesResponseDto.builder();

        if ( proto.hasDecision() ) {
            rulesResponseDto.decision( standinDecisionToStandinDecision1( proto.getDecision() ) );
        }
        rulesResponseDto.evaluatedAtEpochMs( (int) proto.getEvaluatedAtEpochMs() );

        return rulesResponseDto.build();
    }

    protected br.com.coregate.proto.rules.DecisionOutcome decisionOutcomeToDecisionOutcome(DecisionOutcome decisionOutcome) {
        if ( decisionOutcome == null ) {
            return null;
        }

        br.com.coregate.proto.rules.DecisionOutcome decisionOutcome1;

        switch ( decisionOutcome ) {
            case DECISION_OUTCOME_UNSPECIFIED: decisionOutcome1 = br.com.coregate.proto.rules.DecisionOutcome.DECISION_OUTCOME_UNSPECIFIED;
            break;
            case APPROVED: decisionOutcome1 = br.com.coregate.proto.rules.DecisionOutcome.APPROVED;
            break;
            case DECLINED: decisionOutcome1 = br.com.coregate.proto.rules.DecisionOutcome.DECLINED;
            break;
            case REVIEW: decisionOutcome1 = br.com.coregate.proto.rules.DecisionOutcome.REVIEW;
            break;
            case UNRECOGNIZED: decisionOutcome1 = br.com.coregate.proto.rules.DecisionOutcome.UNRECOGNIZED;
            break;
            default: throw new IllegalArgumentException( "Unexpected enum constant: " + decisionOutcome );
        }

        return decisionOutcome1;
    }

    protected br.com.coregate.proto.rules.StandinDecision standinDecisionToStandinDecision(StandinDecision standinDecision) {
        if ( standinDecision == null ) {
            return null;
        }

        br.com.coregate.proto.rules.StandinDecision.Builder standinDecision1 = br.com.coregate.proto.rules.StandinDecision.newBuilder();

        standinDecision1.setOutcome( decisionOutcomeToDecisionOutcome( standinDecision.getOutcome() ) );
        standinDecision1.setReason( standinDecision.getReason() );
        standinDecision1.setAuthCode( standinDecision.getAuthCode() );
        standinDecision1.setRequestId( standinDecision.getRequestId() );

        return standinDecision1.build();
    }

    protected DecisionOutcome decisionOutcomeToDecisionOutcome1(br.com.coregate.proto.rules.DecisionOutcome decisionOutcome) {
        if ( decisionOutcome == null ) {
            return null;
        }

        DecisionOutcome decisionOutcome1;

        switch ( decisionOutcome ) {
            case DECISION_OUTCOME_UNSPECIFIED: decisionOutcome1 = DecisionOutcome.DECISION_OUTCOME_UNSPECIFIED;
            break;
            case APPROVED: decisionOutcome1 = DecisionOutcome.APPROVED;
            break;
            case DECLINED: decisionOutcome1 = DecisionOutcome.DECLINED;
            break;
            case REVIEW: decisionOutcome1 = DecisionOutcome.REVIEW;
            break;
            case UNRECOGNIZED: decisionOutcome1 = DecisionOutcome.UNRECOGNIZED;
            break;
            default: throw new IllegalArgumentException( "Unexpected enum constant: " + decisionOutcome );
        }

        return decisionOutcome1;
    }

    protected StandinDecision standinDecisionToStandinDecision1(br.com.coregate.proto.rules.StandinDecision standinDecision) {
        if ( standinDecision == null ) {
            return null;
        }

        StandinDecision.StandinDecisionBuilder standinDecision1 = StandinDecision.builder();

        standinDecision1.outcome( decisionOutcomeToDecisionOutcome1( standinDecision.getOutcome() ) );
        standinDecision1.reason( standinDecision.getReason() );
        standinDecision1.authCode( standinDecision.getAuthCode() );
        standinDecision1.requestId( standinDecision.getRequestId() );

        return standinDecision1.build();
    }
}
