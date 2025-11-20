package br.com.coregate.orchestrator.saga.component;

import br.com.coregate.application.ports.out.PerformanceMetricsPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Factory responsável por criar instâncias de FunctionalSaga
 * por transação, mantendo PerformanceMetricsPort injetado via Spring.
 *
 * Isso evita static/singleton stateful e mantém a criação de objetos
 * barata e segura para alta concorrência.
 */
@Component
@RequiredArgsConstructor
public class FunctionalSagaFactory {

    private final PerformanceMetricsPort performanceMetrics;

    /**
     * Cria uma nova FunctionalSaga para o tipo T.
     *
     * Exemplo:
     *   FunctionalSaga<OrquestratorSagaContext> saga =
     *       sagaFactory.create("orchestrator-saga");
     */
    public <T> FunctionalSaga<T> create(String sagaName) {
        return new FunctionalSaga<>(sagaName, performanceMetrics);
    }
}
