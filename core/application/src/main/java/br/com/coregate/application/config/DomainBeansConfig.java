package br.com.coregate.application.config;

import br.com.coregate.domain.service.StandInDomainService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DomainBeansConfig {

    @Bean
    public StandInDomainService standInDomainService() {
        return new StandInDomainService();
    }
}
