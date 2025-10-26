package br.com.coregate;


import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "br.com.coregate")
@Slf4j
public class AdapterApplication {
    public static void main(String[] args) {
        org.springframework.boot.SpringApplication.run(AdapterApplication.class, args);
    }
}