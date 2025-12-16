package br.com.coregate.orchestrator.saga.step;

import br.com.coregate.core.contracts.dto.orquestrator.OrchestratorSagaContext;
import br.com.coregate.core.contracts.dto.rules.RulesRequestDto;
import br.com.coregate.core.contracts.dto.transaction.AuthorizationResult;
import br.com.coregate.core.contracts.dto.transaction.RequestTransactionFlow;
import br.com.coregate.core.contracts.dto.transaction.ResponseTransactionFlow;
import br.com.coregate.core.contracts.mapper.RulesMapper;
import br.com.coregate.core.contracts.mapper.TransactionFlowMapper;
import br.com.coregate.mode.OperationalModeManager;
import br.com.coregate.orchestrator.grpc.client.TransactionFlowClientService;
import br.com.coregate.orchestrator.grpc.client.GrpcRulesClientService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class Process {

    private final OperationalModeManager modeManager;
    private final TransactionFlowMapper transactionFlowMapper;
    private final TransactionFlowClientService grpcTransactionFlowClientService;
    private final GrpcRulesClientService grpcRulesClientService;
    private final RulesMapper rulesMapper;

    @Value("${grpc.finalizer.port}")
    private int grpcFinalizerPort;

    @Value("${grpc.finalizer.host}")
    private String grpcFinalizerHost;

    public OrchestratorSagaContext execute(OrchestratorSagaContext tx) {

        var result = AuthorizationResult.builder().build();
        var responseDto = ResponseTransactionFlow.builder().build();

        if( modeManager.isStandIn()) {

            var requestDto = RulesRequestDto.builder()
                            .transactionFactDto(tx.getTransactionFactDto())
                            .build();
            var requestProto = rulesMapper.toProto(requestDto);
            var responseRulesProto = grpcRulesClientService.callGrpc(requestProto);
            var responseRulesDto = rulesMapper.toDto(responseRulesProto);
            // TODO Implementar Aprovação via Rules

        } else {
            log.info("🚀 Iniciando orquestração para transação {}", tx.getTransactionCommand());
            var requestDto   = RequestTransactionFlow.builder()
                                    .transactionCommand(tx.getTransactionCommand())
                                    .build();
            var requestProto = transactionFlowMapper.toProto(requestDto);
            log.info("REQUEST TO FINALIZER {}",requestDto);
            var responseProto = grpcTransactionFlowClientService.callGrpc(requestProto, grpcFinalizerHost,  grpcFinalizerPort);
            responseDto = transactionFlowMapper.toDto(responseProto);

        }
        
        tx.setAuthorizationResult(responseDto.getAuthorizationResult());
        return tx;

    }

    public OrchestratorSagaContext rollback(OrchestratorSagaContext tx) {
        var processingMode = modeManager.getMode() ;
        log.warn("↩️ Rolling back Process data mode {} for {}", processingMode, tx);
        return tx;
    }
}
