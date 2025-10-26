package br.com.coregate.context;

import br.com.coregate.context.templates.TemplateGrpcClientUse;
import br.com.coregate.context.templates.TemplateIsoAnnotationsUse;
import br.com.coregate.context.templates.TemplateRabbitUse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@Slf4j
@SpringBootApplication(scanBasePackages = "br.com.coregate")
@EnableRabbit
public class ContextApplication {
    public static void main(String[] args) {
        org.springframework.boot.SpringApplication.run(ContextApplication.class, args);
    }

    @Bean
    ApplicationRunner run(TemplateIsoAnnotationsUse templateIsoAnnotationsUse,
                          TemplateGrpcClientUse templateGrpcClientUse,
                          TemplateRabbitUse templateRabbitUse) {
        return args -> {

            //templateGrpcClientUse.execute(); // Rodar Orchestrator SERVER
            //templateIsoAnnotationsUse.execute();
            //templateRabbitUse.execute(); // Rodar docker-compose (RABBIT)

        };
    }
}

