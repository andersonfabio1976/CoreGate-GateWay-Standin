package br.com.coregate.orchestrator.saga.service;

import br.com.coregate.core.contracts.dto.orquestrator.OrchestratorSagaContext;
import br.com.coregate.mode.OperationalModeManager;
import br.com.coregate.orchestrator.saga.component.CoreGateSagaMetricsListener;
import br.com.coregate.orchestrator.saga.component.FunctionalSaga;
import br.com.coregate.orchestrator.saga.component.FunctionalSagaFactory;
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

    // 🔹 Steps injetados (agora são beans)
    private final Start start;
    private final FetchData fetchData;
    private final Register register;
    private final Validate validate;
    private final Process process;
    private final Notify notify;
    private final Error error;
    private final End end;

    private final FunctionalSagaFactory sagaFactory;

    @SneakyThrows
    public OrchestratorSagaContext start(OrchestratorSagaContext tx) {
        log.info("🧭 Starting orchestration for transaction {}", tx);

        FunctionalSaga<OrchestratorSagaContext> saga =
                sagaFactory.create("coregate-orchestrator-saga");

        OrchestratorSagaContext out = saga
                .start(tx)
                .then("start", start::execute)
                .then("fetchData", fetchData::execute, fetchData::rollback)
                .then("register", register::execute, register::rollback)
                .then("validate", validate::execute, validate::rollback)
                .then("process", process::execute, process::rollback)
                .then("notify", notify::execute, notify::rollback)
                .enableSilentRollback(true)
                .withListener(new CoreGateSagaMetricsListener(registry, modeManager))
                .onError(error::execute)
                .end(tx);

        out = end.execute(out);

        log.info("✅ Transaction {} completed orchestration flow", out);
        return out;
    }
}
