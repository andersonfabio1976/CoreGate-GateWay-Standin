package br.com.coregate.orchestrator.saga.step;

import br.com.coregate.core.contracts.dto.finalizer.FinalizerRequestDto;
import br.com.coregate.core.contracts.dto.finalizer.FinalizerResponseDto;
import br.com.coregate.core.contracts.dto.orquestrator.OrquestratorSagaContext;
import br.com.coregate.core.contracts.dto.rules.RulesRequestDto;
import br.com.coregate.core.contracts.dto.transaction.AuthorizationResult;
import br.com.coregate.core.contracts.mapper.FinalizerMapper;
import br.com.coregate.core.contracts.mapper.RulesMapper;
import br.com.coregate.core.contracts.mapper.TransactionMapper;
import br.com.coregate.mode.OperationalModeManager;
import br.com.coregate.orchestrator.grpc.client.GrpcFinalizerClientService;
import br.com.coregate.orchestrator.grpc.client.GrpcRulesClientService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@AllArgsConstructor
public class Process {

    private final OperationalModeManager modeManager;
    private final TransactionMapper transactionMapper;
    private final GrpcFinalizerClientService grpcFinalizerClientService;
    private final GrpcRulesClientService grpcRulesClientService;
    private final FinalizerMapper finalizerMapper;
    private final RulesMapper rulesMapper;


    public OrquestratorSagaContext execute(OrquestratorSagaContext tx) {

        var result = AuthorizationResult.builder().build();
        var responseDto = FinalizerResponseDto.builder().build();

        if( modeManager.isStandIn()) {

            var requestDto = RulesRequestDto.builder()
                            .transactionFactDto(tx.getTransactionFactDto())
                            .build();
            var requestProto = rulesMapper.toProto(requestDto);
            var responseRulesProto = grpcRulesClientService.callGrpc(requestProto,8093);
            var responseRulesDto = rulesMapper.toDto(responseRulesProto);


        } else {
            var transactionCommand = transactionMapper.toDto(tx.getTransaction());
            log.info("🚀 Iniciando orquestração para transação {}", tx.getTransaction().getId());
            var requestDto   = FinalizerRequestDto.builder()
                                    .transactionCommand(transactionCommand)
                                    .build();
            var requestProto = finalizerMapper.toProto(requestDto);
            var responseProto = grpcFinalizerClientService.callGrpc(requestProto, 8092);
            responseDto = finalizerMapper.toDto(responseProto);

        }
        
        tx.setAuthorizationResult(responseDto.authorizationResult());
        return tx;

    }

    public OrquestratorSagaContext rollback(OrquestratorSagaContext tx) {
        var processingMode = modeManager.getMode() ;
        log.warn("↩️ Rolling back Process data mode {} for {}", processingMode, tx);
        return tx;
    }
}
