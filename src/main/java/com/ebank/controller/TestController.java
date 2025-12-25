package com.ebank.controller;

import com.ebank.service.EmailService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/test")
public class TestController {

    private final EmailService emailService;

    public TestController(EmailService emailService) {
        this.emailService = emailService;
    }

    @GetMapping("/email")
    public ResponseEntity<String> testEmail() {
        try {
            // Test 1: Test de connexion
            emailService.testConnection();

            // Test 2: Envoi d'email de test
            emailService.sendWelcomeEmail(
                    "test@habriallalibank.ma",
                    "Client Test");

            // Test 3: Notification de virement
            emailService.sendTransferNotification(
                    "test@habriallalibank.ma",
                    "FR76 3000 1007 9412 3456 7890 185",
                    "FR76 3000 1007 9411 1122 2233 334",
                    new BigDecimal("500.00"),
                    "Test de virement");

            return ResponseEntity.ok("""
                    ✅ Tests email lancés avec succès !

                    Vérifiez :
                    1. La console pour les logs
                    2. Votre boîte email (si en mode REAL)
                    3. Les logs Spring Boot

                    Mode actuel : Vérifiez application.properties
                    """);

        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("❌ Erreur lors du test: " + e.getMessage());
        }
    }

    @GetMapping("/email/connection")
    public ResponseEntity<String> testConnection() {
        emailService.testConnection();
        return ResponseEntity.ok("Test de connexion terminé - Vérifiez les logs");
    }
}
