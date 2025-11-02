package br.com.coregate.orchestrator.saga.step;

import br.com.coregate.application.dto.finalizer.FinalizerRequestDto;
import br.com.coregate.application.dto.finalizer.FinalizerResponseDto;
import br.com.coregate.application.dto.orquestrator.OrquestratorSagaContext;
import br.com.coregate.application.dto.rules.RulesRequestDto;
import br.com.coregate.application.dto.transaction.AuthorizationResult;
import br.com.coregate.infrastructure.mapper.FinalizerMapper;
import br.com.coregate.infrastructure.mapper.RulesMapper;
import br.com.coregate.infrastructure.mapper.TransactionMapper;
import br.com.coregate.infrastructure.mode.OperationalModeManager;
import br.com.coregate.orchestrator.grpc.client.GrpcFinalizerClientService;
import br.com.coregate.orchestrator.grpc.client.GrpcRulesClientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class Process {

    private static OperationalModeManager modeManager;
    private static TransactionMapper transactionMapper;
    private static GrpcFinalizerClientService grpcFinalizerClientService;
    private static GrpcRulesClientService grpcRulesClientService;
    private static FinalizerMapper finalizerMapper;
    private static RulesMapper rulesMapper;


    public static OrquestratorSagaContext execute(OrquestratorSagaContext tx) {

        var result = AuthorizationResult.builder().build();
        var responseDto = FinalizerResponseDto.builder().build();

        if( modeManager.isStandin()) {

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

    public static OrquestratorSagaContext rollback(OrquestratorSagaContext tx) {
        var processingMode = modeManager.getProcessingMode();
        log.warn("↩️ Rolling back Process data mode {} for {}", processingMode, tx);
        return tx;
    }
}
