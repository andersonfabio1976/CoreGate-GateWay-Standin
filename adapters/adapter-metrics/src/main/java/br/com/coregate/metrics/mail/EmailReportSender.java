package br.com.coregate.metrics.mail;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import jakarta.mail.internet.MimeMessage;
import java.io.File;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class EmailReportSender {

    private final JavaMailSender mailSender;

    @Value("${coregate.reporting.mail.from}")
    private String from;

    @Value("${coregate.reporting.mail.to}")
    private String toList;

    @Value("${coregate.reporting.mail.subject}")
    private String subject;

    public void sendPdf(Path pdfPath) {
        File file = pdfPath.toFile();
        if (!file.exists()) {
            log.warn("⚠️ PDF não encontrado para envio: {}", pdfPath);
            return;
        }

        List<String> recipients = Arrays.stream(toList.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .toList();

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(from);
            helper.setTo(recipients.toArray(String[]::new));
            helper.setSubject(subject);

            String body = """
                    Prezados,

                    Segue em anexo o Relatório Diário de SLA & Disponibilidade do CoreGate.

                    Qualquer anomalia, por favor acionar o time NOC/OPS.

                    Atenciosamente,
                    CoreGate Platform
                    """;
            helper.setText(body, false);

            helper.addAttachment(file.getName(), new FileSystemResource(file));

            mailSender.send(message);
            log.info("📧 Relatório enviado com sucesso para: {}", recipients);
        } catch (Exception e) {
            log.error("💥 Falha ao enviar e-mail com relatório {}: {}", pdfPath, e.getMessage(), e);
        }
    }
}
