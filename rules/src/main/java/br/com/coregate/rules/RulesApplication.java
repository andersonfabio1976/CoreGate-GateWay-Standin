package br.com.coregate.rules;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.Async;

@SpringBootApplication(scanBasePackages = "br.com.coregate")
@Slf4j
@Async // para não travar server do grpc
public class RulesApplication {
    public static void main(String[] args) {
        org.springframework.boot.SpringApplication.run(RulesApplication.class, args);
    }
}