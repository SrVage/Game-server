package org.example.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

@Service
public class PasswordHashing {
    private static final int HASH_ITERATIONS = 65536;
    private static final int KEY_LENGTH = 256;
    @Value("app.authorization.salt")
    private String salt;

    public String generatePasswordHash(String password) {
        try {
            return generatePasswordHash(password, salt);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean checkPasswords(String password, String hashedPassword) {
        var hashNewPassword = generatePasswordHash(password);
        return hashNewPassword.equals(hashedPassword);
    }

    private static String generatePasswordHash(String password, String salt)
            throws NoSuchAlgorithmException, InvalidKeySpecException {
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt.getBytes(), HASH_ITERATIONS, KEY_LENGTH);
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        byte[] hash = factory.generateSecret(spec).getEncoded();
        return Base64.getEncoder().encodeToString(hash);
    }
}