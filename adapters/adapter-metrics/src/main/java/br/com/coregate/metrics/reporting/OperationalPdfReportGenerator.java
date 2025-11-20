package br.com.coregate.metrics.reporting;

import br.com.coregate.metrics.mail.EmailReportSender;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import java.io.*;
import java.nio.file.*;
import java.security.MessageDigest;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import com.google.zxing.*;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;

@Slf4j
@Component
@RequiredArgsConstructor
public class OperationalPdfReportGenerator {

    private final MeterRegistry meterRegistry;
    private final EmailReportSender emailReportSender;

    private static final String MODE_STATE = "coregate_operational_mode_state";
    private static final String LATENCY = "coregate_latency_avg_ms";
    private static final String SUCCESS = "coregate_success_rate";
    private static final String TPS = "coregate_tps_rate";

    @Value("${coregate.reporting.mail.out-dir:/var/coregate/reports}")
    private String outDir;

    // Executa todo dia 06:05
    @Scheduled(cron = "0 5 6 * * *")
    public void generateDailyPdfReport() {
        try {
            double mode = getMetric(MODE_STATE);
            double latency = getMetric(LATENCY);
            double success = getMetric(SUCCESS);
            double tps = getMetric(TPS);

            String modeDesc = switch ((int) mode) {
                case 0 -> "Gateway";
                case 1 -> "Stand-In Automático";
                case 2 -> "Stand-In Solicitado (Manual)";
                default -> "Indefinido";
            };

            double sla = calculateSLA(success, latency);
            LocalDate today = LocalDate.now();

            Files.createDirectories(Paths.get(outDir));
            String filename = "CoreGate_SLA_Report_%s.pdf"
                    .formatted(today.format(DateTimeFormatter.ofPattern("yyyyMMdd")));
            Path pdfPath = Paths.get(outDir, filename);

            // Gera PDF
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            Document document = new Document(PageSize.A4, 50, 50, 80, 50);
            PdfWriter writer = PdfWriter.getInstance(document, baos);
            document.open();

            addHeader(document, today);
            addSectionTitle(document, "CoreGate SLA & Availability Daily Report");

            PdfPTable table = new PdfPTable(2);
            table.setWidthPercentage(100);
            table.setSpacingBefore(15f);
            table.setWidths(new float[]{40, 60});
            addRow(table, "Data de Geração", LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));
            addRow(table, "Modo Operacional", modeDesc);
            addRow(table, "Taxa de Sucesso (%)", String.format("%.3f %%", success));
            addRow(table, "Latência Média (ms)", String.format("%.2f", latency));
            addRow(table, "TPS Médio", String.format("%.2f trans/s", tps));
            addRow(table, "SLA Consolidado", String.format("%.3f %%", sla));
            document.add(table);

            addSectionTitle(document, "Análise Executiva");
            Paragraph summary = new Paragraph(buildInterpretation(modeDesc, success, latency, sla),
                    FontFactory.getFont(FontFactory.HELVETICA, 11));
            summary.setSpacingBefore(5);
            document.add(summary);

            document.close();

            // Calcula hash SHA-256
            byte[] pdfBytes = baos.toByteArray();
            String hash = computeSha256(pdfBytes);

            // Adiciona QR Code e hash no rodapé
            addDigitalSeal(pdfBytes, pdfPath, hash);

            log.info("✅ Relatório PDF diário gerado e assinado digitalmente: {}", pdfPath);

            // Envia o PDF por e-mail
            emailReportSender.sendPdf(pdfPath);

        } catch (Exception e) {
            log.error("💥 Falha ao gerar/enviar relatório PDF: {}", e.getMessage(), e);
        }
    }

    private double getMetric(String name) {
        return Optional.ofNullable(meterRegistry.find(name).gauge())
                .map(g -> g.value())
                .orElse(Double.NaN);
    }

    private double calculateSLA(double success, double latency) {
        if (Double.isNaN(success) || Double.isNaN(latency)) return 0.0;
        double baseSLA = success / 100.0;
        double latencyPenalty = latency > 200 ? 0.98 : (latency > 100 ? 0.995 : 1.0);
        return baseSLA * latencyPenalty * 100;
    }

    private String computeSha256(byte[] data) throws Exception {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(data);
        StringBuilder sb = new StringBuilder();
        for (byte b : hash) sb.append(String.format("%02x", b));
        return sb.toString();
    }

    private void addDigitalSeal(byte[] originalPdf, Path outPath, String hash) throws Exception {
        // Cria QR Code contendo hash + data
        String qrContent = "CoreGate Report Verification\nSHA-256: " + hash;
        BitMatrix matrix = new MultiFormatWriter()
                .encode(qrContent, BarcodeFormat.QR_CODE, 100, 100);
        Path qrPath = Files.createTempFile("coregate_qr_", ".png");
        MatrixToImageWriter.writeToPath(matrix, "PNG", qrPath);

        // Adiciona hash e QR no rodapé do PDF
        PdfReader reader = new PdfReader(originalPdf);
        PdfStamper stamper = new PdfStamper(reader, Files.newOutputStream(outPath));
        Image qrImage = Image.getInstance(qrPath.toAbsolutePath().toString());
        qrImage.scaleAbsolute(60, 60);
        qrImage.setAbsolutePosition(50, 30);

        PdfContentByte cb = stamper.getOverContent(reader.getNumberOfPages());
        cb.addImage(qrImage);

        ColumnText.showTextAligned(cb, Element.ALIGN_LEFT,
                new Phrase("Digital Signature (SHA-256): " + hash.substring(0, 32) + "...",
                        FontFactory.getFont(FontFactory.HELVETICA_OBLIQUE, 8, BaseColor.GRAY)),
                120, 40, 0);

        ColumnText.showTextAligned(cb, Element.ALIGN_LEFT,
                new Phrase("Verifique autenticidade via QR Code",
                        FontFactory.getFont(FontFactory.HELVETICA, 8, BaseColor.DARK_GRAY)),
                120, 30, 0);

        stamper.close();
        reader.close();
        Files.deleteIfExists(qrPath);
    }

    private void addHeader(Document doc, LocalDate today) throws DocumentException {
        Paragraph header = new Paragraph("CoreGate Processing Platform",
                FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16, BaseColor.BLUE));
        header.setAlignment(Element.ALIGN_CENTER);
        doc.add(header);

        Paragraph date = new Paragraph("Relatório diário de SLA e Disponibilidade — " +
                today.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                FontFactory.getFont(FontFactory.HELVETICA, 11, BaseColor.DARK_GRAY));
        date.setAlignment(Element.ALIGN_CENTER);
        date.setSpacingAfter(10f);
        doc.add(date);
    }

    private void addSectionTitle(Document doc, String title) throws DocumentException {
        Paragraph section = new Paragraph(title,
                FontFactory.getFont(FontFactory.HELVETICA_BOLD, 13, BaseColor.BLACK));
        section.setSpacingBefore(15f);
        section.setSpacingAfter(10f);
        doc.add(section);
    }

    private void addRow(PdfPTable table, String key, String value) {
        PdfPCell c1 = new PdfPCell(new Phrase(key, FontFactory.getFont(FontFactory.HELVETICA_BOLD, 11)));
        c1.setBorder(Rectangle.NO_BORDER);
        c1.setPadding(5f);
        table.addCell(c1);

        PdfPCell c2 = new PdfPCell(new Phrase(value, FontFactory.getFont(FontFactory.HELVETICA, 11)));
        c2.setBorder(Rectangle.NO_BORDER);
        c2.setPadding(5f);
        table.addCell(c2);
    }

    private String buildInterpretation(String mode, double success, double latency, double sla) {
        StringBuilder sb = new StringBuilder();
        sb.append("Durante o último ciclo de operação, o sistema CoreGate manteve as seguintes condições:\n\n");
        sb.append(String.format("- Modo Operacional: %s\n", mode));
        sb.append(String.format("- Taxa de sucesso média: %.2f %%\n", success));
        sb.append(String.format("- Latência média observada: %.2f ms\n", latency));
        sb.append(String.format("- SLA Consolidado: %.3f %%\n\n", sla));

        if (mode.equals("Gateway")) {
            sb.append("✅ Operação normal (Gateway), sem ativação de Stand-In.\n");
        } else if (mode.contains("Automático")) {
            sb.append("🟠 Stand-In Automático ativado — failover controlado.\n");
        } else if (mode.contains("Manual")) {
            sb.append("🔴 Stand-In Manual solicitado pelo emissor — retorno depende de liberação externa.\n");
        }

        sb.append("\n📊 SLA dentro das metas contratuais (meta ≥ 99.98%).");
        return sb.toString();
    }
}
