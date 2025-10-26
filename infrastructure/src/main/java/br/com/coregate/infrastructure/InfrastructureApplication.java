package br.com.coregate.infrastructure;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"br.com.coregate"})
public class InfrastructureApplication {
    public static void main(String[] args) {
        SpringApplication.run(InfrastructureApplication.class, args);
    }
}