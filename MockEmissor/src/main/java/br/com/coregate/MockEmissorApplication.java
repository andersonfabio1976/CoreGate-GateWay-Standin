package br.com.coregate;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication(scanBasePackages = "br.com.coregate")
public class MockEmissorApplication {
    public static void main(String[] args) {
        SpringApplication.run(MockEmissorApplication.class, args);
    }
}