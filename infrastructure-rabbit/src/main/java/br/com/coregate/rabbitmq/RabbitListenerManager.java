//package br.com.coregate.rabbitmq;
//
//import br.com.coregate.rabbitmq.config.RabbitProperties;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.amqp.core.Queue;
//import org.springframework.amqp.rabbit.connection.ConnectionFactory;
//import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
//import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
//import org.springframework.beans.factory.InitializingBean;
//import org.springframework.context.annotation.Configuration;
//
//import com.rabbitmq.client.Channel;
//
///**
// * Registra listeners "baixíssimo atrito" para os eventos de modo.
// * - ACK automático (AcknowledgeMode.AUTO)
// * - 1 consumidor / prefetch 1 (evita corrida e garante ordenação)
// * - Sem requeue em erro (não ficar reciclando mensagem de controle)
// *
// * IMPORTANTE: NÃO chamar basicAck/basicNack manualmente em NENHUM lugar.
// */
//@Slf4j
//@Configuration
//@RequiredArgsConstructor
//public class RabbitListenerManager implements InitializingBean {
//
//    private final ConnectionFactory connectionFactory;
//    private final RabbitProperties props;
//   // private final OperationalModeListener operationalModeListener;
//
//    private SimpleMessageListenerContainer standInRequestedContainer;
//    private SimpleMessageListenerContainer gatewayContainer;
//
//    @Override
//    public void afterPropertiesSet() {
//        // Listener para STANDIN_REQUESTED
//        standInRequestedContainer = buildContainer(
//                props.getConsumes().get("standin_requested").getQueue(),
//                (message, channel) -> {
//                    // NENHUM ACK MANUAL AQUI!
//                    String payload = new String(message.getBody());
//                    //operationalModeListener.onStandInRequested(payload);
//                }
//        );
//        standInRequestedContainer.start();
//        log.info("🎯 Listener customizado registrado → [STANDIN_REQUESTED] Queue={}",
//                props.getConsumes().get("standin_requested").getQueue());
//
//        // Listener para GATEWAY
//        gatewayContainer = buildContainer(
//                props.getConsumes().get("gateway").getQueue(),
//                (message, channel) -> {
//                    // NENHUM ACK MANUAL AQUI!
//                    String payload = new String(message.getBody());
//                   // operationalModeListener.onGatewayRequested(payload);
//                }
//        );
//        gatewayContainer.start();
//        log.info("🎯 Listener customizado registrado → [GATEWAY] Queue={}",
//                props.getConsumes().get("gateway").getQueue());
//
//        log.info("✅ Todos os listeners RabbitMQ foram registrados com sucesso.");
//    }
//
//    private SimpleMessageListenerContainer buildContainer(String queueName,
//                                                          ChannelAwareMessageListener listener) {
//        SimpleMessageListenerContainer c = new SimpleMessageListenerContainer(connectionFactory);
//        c.setQueues(new Queue(queueName));
//        c.setAcknowledgeMode(org.springframework.amqp.core.AcknowledgeMode.AUTO); // ACK automático
//        c.setConcurrentConsumers(1);    // 1 consumidor
//        c.setMaxConcurrentConsumers(1); // evita paralelismo
//        c.setPrefetchCount(1);          // 1 por vez
//        c.setDefaultRequeueRejected(false); // não requeue em erro (evita loop)
//        c.setMissingQueuesFatal(false); // não matar a app se a fila não existir na hora
//        c.setExposeListenerChannel(false); // não expor canal para evitar uso indevido
//        c.setMessageListener(listener);
//        return c;
//    }
//}
