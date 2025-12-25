package com.ebank.config;

import com.ebank.service.EmailService;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EmailTester {

    private static final Logger log = LoggerFactory.getLogger(EmailTester.class);

    private final EmailService emailService;

    public EmailTester(EmailService emailService) {
        this.emailService = emailService;
    }

    @PostConstruct
    public void init() {
        log.info("=".repeat(60));
        log.info("INITIALISATION SERVICE EMAIL");
        log.info("=".repeat(60));
        emailService.testConnection();
        log.info("Service email initialisé avec succès");
        log.info("Mode : {}", System.getProperty("app.email.mode", "SIMULATION"));
        log.info("Pour tester : GET http://localhost:8080/api/test/email");
        log.info("=".repeat(60));
    }
}
