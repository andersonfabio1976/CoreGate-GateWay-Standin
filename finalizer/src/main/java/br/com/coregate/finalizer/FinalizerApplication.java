package br.com.coregate.finalizer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {
        "br.com.coregate.finalizer",       // Módulo atual
        "br.com.coregate.infrastructure",  // 👈 Inclui todos os beans da infraestrutura
        "br.com.coregate.application",     // Se usa DTOs e serviços mapeados
        "br.com.coregate.domain"           // Se precisa de enums e objetos de domínio
})
public class FinalizerApplication {
    public static void main(String[] args) {
        SpringApplication.run(FinalizerApplication.class, args);
    }
}
