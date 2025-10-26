package br.com.coregate.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import java.util.Properties;

/**
 * ✉️ Configuração global de e-mail do CoreGate
 *
 * Permite envio de relatórios PDF com assinatura eletrônica,
 * alertas NOC e notificações corporativas (SLA, Stand-In, etc.)
 */
@Configuration
public class MailConfig {

    @Bean
    public JavaMailSender javaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

        // ⚙️ Ajuste conforme ambiente
        mailSender.setHost("smtp.gmail.com");
        mailSender.setPort(587);
        mailSender.setUsername("coregate.noc@gmail.com");
        mailSender.setPassword("SUA_SENHA_DE_APLICATIVO_AQUI");

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "false");

        return mailSender;
    }
}
