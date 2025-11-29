package br.com.coregate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "br.com.coregate")
public class MockEmissorApplication {
    public static void main(String[] args) {
        SpringApplication.run(MockEmissorApplication.class, args);
    }
}