package br.com.coregate.ingress.executor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.concurrent.*;

/**
 * 🧵 Executor global do Ingress.
 * - Processa Steps fora do EventLoop do Netty.
 * - Tamanho fixo, evita explosão de threads.
 * - Fila limitada para impedir backpressure infinito.
 *
 * Mantém performance realista de gateway.
 */
@Slf4j
@Component
public class IngressWorkerPool {

    /**
     * Executor com pool fixo:
     * - 8 threads é um bom ponto para iniciar.
     * - Podemos ajustar para 16 se necessário.
     */
    private final ExecutorService workers;

    /**
     * Fila limitada para evitar memória ilimitada em cenários de carga.
     */
    private static final int QUEUE_CAPACITY = 5000;

    public IngressWorkerPool() {
        this.workers = new ThreadPoolExecutor(
                8,                     // core threads
                8,                     // max threads
                60L, TimeUnit.SECONDS, // keep alive
                new ArrayBlockingQueue<>(QUEUE_CAPACITY),
                runnable -> {
                    Thread t = new Thread(runnable);
                    t.setName("ingress-worker-" + t.getId());
                    t.setDaemon(true);
                    return t;
                },
                (r, executor) -> {
                    log.error("❌ [WORKER-POOL] Task rejeitada — fila cheia.");
                }
        );

        log.info("🧵 IngressWorkerPool inicializado com 8 threads e fila {}", QUEUE_CAPACITY);
    }

    /**
     * Submete tarefa ao pool.
     * Sempre retorna imediatamente ao Netty, sem bloquear o EventLoop.
     */
    public void submit(Runnable task) {
        this.workers.submit(task);
    }

    /**
     * Finalização graciosa.
     */
    public void shutdown() {
        try {
            workers.shutdown();
            if (!workers.awaitTermination(5, TimeUnit.SECONDS)) {
                workers.shutdownNow();
            }
        } catch (InterruptedException ignored) {
            workers.shutdownNow();
        }
    }
}
