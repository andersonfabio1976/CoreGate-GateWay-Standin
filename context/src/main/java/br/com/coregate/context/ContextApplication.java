package br.com.coregate.context;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@Slf4j
@SpringBootApplication(scanBasePackages = "br.com.coregate")
public class ContextApplication {
    public static void main(String[] args) {
        org.springframework.boot.SpringApplication.run(ContextApplication.class, args);
    }

    @Bean
    ApplicationRunner run() {
        return args -> {

            //templateGrpcClientUse.execute(); // Rodar Orquestrator SERVER
            //templateIsoAnnotationsUse.execute();
            //templateRabbitUse.execute(); // Rodar docker-compose (RABBIT)

        };
    }
}

