package com.ebank.service.impl;

import com.ebank.service.EmailService;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;

@Service
public class EmailServiceImpl implements EmailService {

    private static final Logger log = LoggerFactory.getLogger(EmailServiceImpl.class);

    private final JavaMailSender mailSender;

    @Value("${app.email.mode}")
    private String emailMode;

    @Value("${spring.mail.username}")
    private String fromEmail;

    public EmailServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void sendClientCredentials(String toEmail, String login, String generatedPassword, String clientName) {
        String subject = "Bienvenue chez Habri & Allali Bank - Vos identifiants";
        String template = loadTemplate("welcome-email.html");

        String content = template
                .replace("%CLIENT_NAME%", clientName)
                .replace("%LOGIN%", login)
                .replace("%PASSWORD%", generatedPassword);

        send(subject, toEmail, content);
    }

    @Override
    public void sendTransferNotification(String toEmail, String fromRIB, String toRIB, BigDecimal amount,
            String motif) {
        String subject = "Notification de virement - Habri & Allali Bank";
        String template = loadTemplate("transfer-notification.html");

        String content = template
                .replace("%FROM_RIB%", formatRIB(fromRIB))
                .replace("%TO_RIB%", formatRIB(toRIB))
                .replace("%AMOUNT%", String.format("%.2f €", amount))
                .replace("%MOTIF%", motif != null ? motif : "Non spécifié");

        send(subject, toEmail, content);
    }

    @Override
    public void sendPasswordChangeNotification(String toEmail) {
        String subject = "Changement de mot de passe confirmé - Habri & Allali Bank";
        String template = loadTemplate("password-change.html");

        String content = template.replace("%EMAIL%", toEmail);
        send(subject, toEmail, content);
    }

    @Override
    public void sendWelcomeEmail(String toEmail, String clientName) {
        String subject = "Bienvenue dans notre communauté bancaire";
        String template = loadTemplate("welcome-email.html");

        String content = template
                .replace("%CLIENT_NAME%", clientName)
                .replace("%LOGIN%", toEmail)
                .replace("%PASSWORD%", "[Déjà défini]");

        send(subject, toEmail, content);
    }

    @Override
    public void testConnection() {
        log.info("Testing email connection in mode: {}", emailMode);
        if ("REAL".equalsIgnoreCase(emailMode)) {
            try {
                // mailSender.testConnection() doesn't exist on JavaMailSender interface
                // directly typically
                // usually we do checks or send a test email.
                // However, I will implement a safe check if possible or just log.
                // The user provided code uses `mailSender.testConnection()` which might be
                // valid in their version or a wrapper?
                // JavaMailSenderImpl has testConnection() but the interface JavaMailSender does
                // not.
                if (mailSender instanceof org.springframework.mail.javamail.JavaMailSenderImpl) {
                    ((org.springframework.mail.javamail.JavaMailSenderImpl) mailSender).testConnection();
                    log.info("✅ Email connection test successful");
                } else {
                    log.info("✅ Email connection test skipped (Sender type: " + mailSender.getClass() + ")");
                }
            } catch (Exception e) {
                log.error("❌ Email connection test failed: {}", e.getMessage());
            }
        } else {
            log.info("✅ Email service in SIMULATION mode - ready");
        }
    }

    private void send(String subject, String to, String content) {
        if ("REAL".equalsIgnoreCase(emailMode)) {
            try {
                MimeMessage message = mailSender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
                helper.setFrom(fromEmail, "Habri & Allali Bank");
                helper.setTo(to);
                helper.setSubject(subject);
                helper.setText(content, true);

                mailSender.send(message);
                log.info("📧 Email envoyé (SMTP) à : {}", to);
            } catch (Exception e) {
                log.error("❌ Erreur SMTP vers {}. Bascule en simulation.", to, e);
                logSimulation(subject, to, content);
            }
        } else {
            logSimulation(subject, to, content);
        }
    }

    private void logSimulation(String subject, String to, String content) {
        String plainText = content.replaceAll("<[^>]*>", "").replaceAll("\\s+", " ").trim();

        System.out.println("\n" + "═".repeat(60));
        System.out.println("🏦 HABRI & ALLALI BANK - NOTIFICATION EMAIL (SIMULATION)");
        System.out.println("═".repeat(60));
        System.out.println("📧 À : " + to);
        System.out.println("📌 Sujet : " + subject);
        System.out.println("─".repeat(60));
        System.out.println("\n--- CONTENU ---\n");
        System.out.println(plainText.length() > 300 ? plainText.substring(0, 300) + "..." : plainText);
        System.out.println("\n" + "─".repeat(60));
        System.out.println("📍 Service client : contact@habriallalibank.ma");
        System.out.println("📞 Tél : +212 522 123 456");
        System.out.println("═".repeat(60) + "\n");

        log.info("Email simulé pour : {} - Sujet : {}", to, subject);
    }

    private String loadTemplate(String templateName) {
        try {
            ClassPathResource resource = new ClassPathResource("templates/email/" + templateName);
            return StreamUtils.copyToString(resource.getInputStream(), StandardCharsets.UTF_8);
        } catch (Exception e) {
            log.error("Erreur de chargement du template: {}", templateName, e);
            // Template par défaut
            return """
                    <!DOCTYPE html>
                    <html>
                    <head>
                        <meta charset="UTF-8">
                        <style>
                            body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; }
                            .container { max-width: 600px; margin: 0 auto; padding: 20px; }
                            .header { background-color: #1a237e; color: white; padding: 20px; text-align: center; }
                            .content { padding: 30px; background-color: #f9f9f9; }
                            .footer { text-align: center; margin-top: 30px; color: #666; font-size: 0.9em; }
                        </style>
                    </head>
                    <body>
                        <div class="container">
                            <div class="header">
                                <h1>Habri & Allali Bank</h1>
                            </div>
                            <div class="content">
                                %CONTENT%
                            </div>
                            <div class="footer">
                                <p>© 2024 Habri & Allali Bank. Tous droits réservés.</p>
                            </div>
                        </div>
                    </body>
                    </html>
                    """.replace("%CONTENT%", "Notification de la banque");
        }
    }

    private String formatRIB(String rib) {
        if (rib == null || rib.length() < 8)
            return rib;
        // Format: FR76 XXXX XXXX XXXX XXXX XXXX XXX
        return rib.replaceAll("(.{4})(?!$)", "$1 ");
    }
}
