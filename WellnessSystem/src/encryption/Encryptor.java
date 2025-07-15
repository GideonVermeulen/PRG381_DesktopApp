package encryption;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class Encryptor {
    // --- Password Hashing (SHA-256) ---
    public static String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hash) sb.append(String.format("%02x", b));
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 not available", e);
        }
    }

    // --- AES Encryption for Feedback Comments ---
    private static final String AES_KEY = "MySuperSecretKey"; // 16 chars for AES-128
    private static final String AES_ALGO = "AES";

    public static String encrypt(String plainText) {
        try {
            Cipher cipher = Cipher.getInstance(AES_ALGO);
            SecretKeySpec key = new SecretKeySpec(AES_KEY.getBytes(), AES_ALGO);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] encrypted = cipher.doFinal(plainText.getBytes());
            return Base64.getEncoder().encodeToString(encrypted);
        } catch (Exception e) {
            throw new RuntimeException("Encryption failed", e);
        }
    }

    public static String decrypt(String cipherText) {
        try {
            Cipher cipher = Cipher.getInstance(AES_ALGO);
            SecretKeySpec key = new SecretKeySpec(AES_KEY.getBytes(), AES_ALGO);
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] decoded = Base64.getDecoder().decode(cipherText);
            byte[] decrypted = cipher.doFinal(decoded);
            return new String(decrypted);
        } catch (Exception e) {
            // If decryption fails, assume it's plain text and return as is
            return cipherText;
        }
    }
}
