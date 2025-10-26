package br.com.coregate.finalizer;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(scanBasePackages = {
        "br.com.coregate.finalizer",       // Módulo atual
        "br.com.coregate.infrastructure",  // 👈 Inclui todos os beans da infraestrutura
        "br.com.coregate.application",     // Se usa DTOs e serviços mapeados
        "br.com.coregate.domain"           // Se precisa de enums e objetos de domínio
})
@EnableFeignClients(basePackages = "br.com.coregate")
@EnableRabbit
public class FinalizerApplication {

    public static void main(String[] args) {
        SpringApplication.run(FinalizerApplication.class, args);
    }
}
