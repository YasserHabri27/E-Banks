package com.ebank.util;

import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
public class ValidationUtil {

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");
    private static final Pattern PHONE_PATTERN = Pattern.compile("^\\+?[0-9]{10,13}$");

    public boolean isValidEmail(String email) {
        return email != null && EMAIL_PATTERN.matcher(email).matches();
    }

    public boolean isValidPhone(String phone) {
        // Phone is optional, so null/empty is considered "valid" (handled by @NotNull
        // elsewhere if needed)
        // If provided, must match pattern
        return phone == null || phone.isEmpty() || PHONE_PATTERN.matcher(phone).matches();
    }
}
