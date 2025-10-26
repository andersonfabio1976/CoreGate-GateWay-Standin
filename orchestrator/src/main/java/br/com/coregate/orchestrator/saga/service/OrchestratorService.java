package br.com.coregate.orchestrator.saga.service;

import br.com.coregate.application.dto.TransactionCommand;
import br.com.coregate.application.dto.TransactionCommandResponse;
import br.com.coregate.domain.model.Transaction;
import br.com.coregate.infrastructure.grpc.*;
import br.com.coregate.infrastructure.mapper.TransactionMapper;
import br.com.coregate.infrastructure.saga.FunctionalSaga;
import br.com.coregate.infrastructure.saga.SagaEventPublisher;
import br.com.coregate.orchestrator.saga.step.*;
import br.com.coregate.orchestrator.saga.step.Error;
import br.com.coregate.orchestrator.saga.step.Process;
import com.google.protobuf.util.Timestamps;
import io.grpc.stub.StreamObserver;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Slf4j
@Service
public class OrchestratorService  {

    @SneakyThrows
    public Transaction orchestrate(Transaction tx) {
        log.info("🧭 Starting orchestration for transaction {}", tx);

            FunctionalSaga
                    .start(tx)
                    .then("start", Start::execute)
                    .then("fetchData", FetchData::execute, FetchData::rollback)
                    .then("register", Register::execute, Register::rollback)
                    .then("validate", Validate::execute, Validate::rollback)
                    .then("process", Process::execute, Process::rollback)
                    .then("notify", Notify::execute, Notify::rollback)
                    .onError(Error::execute)
                    .onEnd(End::execute)
                    .enableSilentRollback(true)
                    .end(tx);
        log.info("✅ Transaction {} completed orchestration flow", tx);
        return tx;
        
    }
























}
