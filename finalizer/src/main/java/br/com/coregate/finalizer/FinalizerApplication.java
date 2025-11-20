package br.com.coregate.finalizer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {
        "br.com.coregate"
})
public class FinalizerApplication {
    public static void main(String[] args) {
        SpringApplication.run(FinalizerApplication.class, args);
    }
}
