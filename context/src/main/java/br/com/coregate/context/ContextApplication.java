package br.com.coregate.context;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "br.com.coregate")
public class ContextApplication {
    public static void main(String[] args) {
        SpringApplication.run(ContextApplication.class, args);
    }
}

