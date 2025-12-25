package com.ebank.service;

import java.math.BigDecimal;

public interface EmailService {
    void sendClientCredentials(String toEmail, String login, String generatedPassword, String clientName);

    void sendTransferNotification(String toEmail, String fromRIB, String toRIB, BigDecimal amount, String motif);

    void sendPasswordChangeNotification(String toEmail);

    void sendWelcomeEmail(String toEmail, String clientName);

    void testConnection();
}
