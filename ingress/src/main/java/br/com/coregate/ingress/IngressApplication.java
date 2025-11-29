package br.com.coregate.ingress;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication(scanBasePackages = {
        "br.com.coregate.ingress",
        "br.com.coregate",
        "br.com.coregate.core.contracts"
})
public class IngressApplication {
    public static void main(String[] args) {
        SpringApplication.run(IngressApplication.class, args);
    }
}


