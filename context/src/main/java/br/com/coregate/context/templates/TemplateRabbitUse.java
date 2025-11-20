//package br.com.coregate.context.templates;
//
//import br.com.coregate.application.dto.transaction.TransactionCommand;
//import br.com.coregate.infrastructure.mock.TransactionCommandMockBuilder;
//import br.com.coregate.infrastructure.rabbitmq.RabbitConsumer;
//import br.com.coregate.infrastructure.rabbitmq.RabbitFactory;
//import br.com.coregate.rabbitmq.enums.RabbitQueueType;
//import org.springframework.stereotype.Component;
//
//@Component
//public class TemplateRabbitUse {
//
//    private final RabbitFactory rabbitFactory;
//
//    public TemplateRabbitUse(RabbitFactory rabbitFactory) {
//        this.rabbitFactory = rabbitFactory;
//    }
//
//    public void execute() {
//        System.out.println("🚀 [TEST] Iniciando publicações de teste...");
//
//        // Objeto de Teste
//        TransactionCommand tx = TransactionCommandMockBuilder.build();
//
//        //TESTE DE PRODUCER
//        rabbitFactory.publish(RabbitQueueType.NOTIFY, tx);
//        rabbitFactory.publish(RabbitQueueType.REGISTER, tx);
//    }
//
//    // TESTE DE CONSUMER
//    @RabbitConsumer(RabbitQueueType.NOTIFY)
//    public void handleNotify(TransactionCommand payload) {
//        System.out.println("📥 [LISTENER-NOTIFY] Mensagem recebida → " + payload);
//    }
//
//    @RabbitConsumer(RabbitQueueType.REGISTER)
//    public void handleRegister(TransactionCommand payload) {
//        System.out.println("📥 [LISTENER-REGISTER] Mensagem recebida → " + payload);
//    }
//
//}
