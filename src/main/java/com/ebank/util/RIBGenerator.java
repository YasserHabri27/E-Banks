package com.ebank.util;

import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.security.SecureRandom;

@Component
public class RIBGenerator {

    // Bank Code (Example: 30003 for SocGen, using 12345 for eBank)
    private static final String CODE_BANQUE = "12345";
    // Guichet Code (Example: 00001)
    private static final String CODE_GUICHET = "00001";

    private final SecureRandom random = new SecureRandom();

    public String generateRib() {
        // Generate random 11-digit account number (alphanumeric allowed, simplified to
        // numeric here)
        StringBuilder numCompte = new StringBuilder();
        for (int i = 0; i < 11; i++) {
            numCompte.append(random.nextInt(10));
        }

        String accountKey = calculateKey(CODE_BANQUE, CODE_GUICHET, numCompte.toString());

        // Format FR76 (IBAN) + Bank + Branch + Account + Key
        // Simplified RIB format: Bank (5) + Guichet (5) + Compte (11) + Key (2)
        return String.format("%s%s%s%s", CODE_BANQUE, CODE_GUICHET, numCompte, accountKey);
    }

    private String calculateKey(String bank, String branch, String account) {
        // RIB Key Algo: 97 - ((89*Bank + 15*Branch + 3*Account) % 97)
        // Adjust for alphanumeric account numbers if needed (replace letters with
        // values)
        // Here assuming numeric account for simplicity

        try {
            BigInteger bBank = new BigInteger(bank);
            BigInteger bBranch = new BigInteger(branch);
            BigInteger bAccount = new BigInteger(account.replaceAll("[^0-9]", "0")); // Simple sanitization

            BigInteger bankPart = bBank.multiply(BigInteger.valueOf(89));
            BigInteger branchPart = bBranch.multiply(BigInteger.valueOf(15));
            BigInteger accountPart = bAccount.multiply(BigInteger.valueOf(3));

            BigInteger sum = bankPart.add(branchPart).add(accountPart);
            BigInteger remainder = sum.remainder(BigInteger.valueOf(97));

            int key = 97 - remainder.intValue();
            return String.format("%02d", key);
        } catch (NumberFormatException e) {
            // Fallback
            return "00";
        }
    }

    public boolean validateRib(String rib) {
        if (rib == null || rib.length() != 23)
            return false;
        // Basic length check for this internal format
        // Real logic would parse parts and re-calculate key
        return true;
    }
}
