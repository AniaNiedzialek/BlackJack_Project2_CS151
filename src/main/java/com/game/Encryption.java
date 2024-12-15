package com.game;

import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class Encryption {

    // Use a fixed key (16 bytes for AES-128, 24 for AES-192, 32 for AES-256)
    private static final String FIXED_KEY = "MASMASMASMASMASM"; // 16-byte key for AES-128

    public static String encrypt(String data) {
        try {
            // Create AES SecretKey
            SecretKeySpec secretKey = new SecretKeySpec(FIXED_KEY.getBytes(), "AES");

            // Generate a random IV
            byte[] iv = new byte[16];
            System.arraycopy(FIXED_KEY.getBytes(), 0, iv, 0, iv.length); // Use part of key as IV
            IvParameterSpec ivParams = new IvParameterSpec(iv);

            // Initialize AES Cipher
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivParams);

            // Encrypt data
            byte[] encryptedData = cipher.doFinal(data.getBytes());

            // Encode to Base64
            return Base64.getEncoder().encodeToString(encryptedData);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String decrypt(String encryptedData) {
        try {
            // Create AES SecretKey
            SecretKeySpec secretKey = new SecretKeySpec(FIXED_KEY.getBytes(), "AES");

            // Use the same fixed IV
            byte[] iv = new byte[16];
            System.arraycopy(FIXED_KEY.getBytes(), 0, iv, 0, iv.length); // Use part of key as IV
            IvParameterSpec ivParams = new IvParameterSpec(iv);

            // Initialize AES Cipher
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, secretKey, ivParams);

            // Decode Base64
            byte[] decodedData = Base64.getDecoder().decode(encryptedData);

            // Decrypt data
            byte[] decryptedData = cipher.doFinal(decodedData);

            return new String(decryptedData);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
