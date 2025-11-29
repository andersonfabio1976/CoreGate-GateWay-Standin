package br.com.coregate.ingress.executor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.*;

/**
 * 🔄 Controle FIFO por conexão/POS.
 * Cada conexão tem sua própria fila e só executa UMA tarefa por vez.
 *
 * Evita que:
 * - o EventLoop do Netty bloqueie
 * - o POS receba respostas fora de ordem
 * - haja explosão de threads
 */
@Slf4j
@Component
public class PerConnectionTaskQueue {

    /**
     * Cada connectionId → fila de tasks
     */
    private final Map<String, Queue<Runnable>> queues = new ConcurrentHashMap<>();

    /**
     * Estados: se o connectionId está processando no momento.
     */
    private final Map<String, Boolean> running = new ConcurrentHashMap<>();

    private final IngressWorkerPool workerPool;

    public PerConnectionTaskQueue(IngressWorkerPool workerPool) {
        this.workerPool = workerPool;
    }

    /**
     * Registra um novo connectionId com fila vazia.
     */
    public void initConnection(String connectionId) {
        queues.putIfAbsent(connectionId, new ConcurrentLinkedQueue<>());
        running.putIfAbsent(connectionId, false);
        log.debug("🔗 [QUEUE] Conexão registrada connectionId={}", connectionId);
    }

    /**
     * Adiciona uma tarefa à fila da conexão.
     * O processamento é garantido FIFO.
     */
    public void submit(String connectionId, Runnable task) {
        Queue<Runnable> queue = queues.computeIfAbsent(connectionId, id -> new ConcurrentLinkedQueue<>());
        queue.offer(task);
        processQueue(connectionId);
    }

    /**
     * Executa tarefas da fila da conexão — sempre 1 por vez.
     */
    private void processQueue(String connectionId) {
        // Se já está processando, ignora
        if (running.getOrDefault(connectionId, false)) {
            return;
        }

        Queue<Runnable> queue = queues.get(connectionId);
        if (queue == null) return;

        Runnable task = queue.poll();
        if (task == null) {
            running.put(connectionId, false);
            return;
        }

        // Marca como rodando
        running.put(connectionId, true);

        // Envia para o pool
        workerPool.submit(() -> {
            try {
                task.run();
            } catch (Exception e) {
                log.error("💥 [QUEUE] Erro ao executar task para connectionId={}: {}", connectionId, e.getMessage(), e);
            } finally {
                running.put(connectionId, false);
                processQueue(connectionId); // processa a próxima
            }
        });
    }

    /**
     * Remove a fila da conexão (usado no channelInactive ou timeout).
     */
    public void removeConnection(String connectionId) {
        queues.remove(connectionId);
        running.remove(connectionId);
        log.debug("🧹 [QUEUE] Conexão removida connectionId={}", connectionId);
    }
}
