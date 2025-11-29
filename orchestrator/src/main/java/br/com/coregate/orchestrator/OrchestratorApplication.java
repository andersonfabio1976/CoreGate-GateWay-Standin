package br.com.coregate.orchestrator;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "br.com.coregate")
@Slf4j
public class OrquestratorApplication {

    public static void main(String[] args) {
        org.springframework.boot.SpringApplication.run(OrquestratorApplication.class, args);
    }

}