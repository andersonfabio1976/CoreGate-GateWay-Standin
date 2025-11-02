package br.com.coregate.orchestrator.saga.service;

import br.com.coregate.infrastructure.mode.OperationalModeManager;
import br.com.coregate.infrastructure.saga.FunctionalSaga;
import br.com.coregate.infrastructure.saga.CoreGateSagaMetricsListener;
import br.com.coregate.application.dto.orquestrator.OrquestratorSagaContext;
import br.com.coregate.orchestrator.saga.step.*;
import br.com.coregate.orchestrator.saga.step.Error;
import br.com.coregate.orchestrator.saga.step.Process;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrquestratorSagaService {

    private final MeterRegistry registry;
    private final OperationalModeManager modeManager;

    @SneakyThrows
    public OrquestratorSagaContext orchestrate(OrquestratorSagaContext tx) {
        log.info("🧭 Starting orchestration for transaction {}", tx);

        OrquestratorSagaContext out = FunctionalSaga
                .start(tx)
                .then("start", Start::execute)
                .then("fetchData", FetchData::execute, FetchData::rollback)
                .then("register", Register::execute, Register::rollback)
                .then("validate", Validate::execute, Validate::rollback)
                .then("process", Process::execute, Process::rollback)
                .then("notify", Notify::execute, Notify::rollback)
                .enableSilentRollback(true)
                .withListener(new CoreGateSagaMetricsListener(registry, modeManager))
                .onError(Error::execute)
                .enableSilentRollback(true)
                .end(tx);

        out = End.execute(out);

        log.info("✅ Transaction {} completed orchestration flow", out);
        return out;
    }
}
