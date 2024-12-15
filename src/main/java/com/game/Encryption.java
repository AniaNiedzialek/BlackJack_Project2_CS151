package com.game;

import java.security.SecureRandom;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

public class Encryption {

    // Create key
    private static SecretKey secretKey = null;

    static {
        try{
            // Generate random key
            KeyGenerator keyGen = KeyGenerator.getInstance("AES");
            keyGen.init(256); // Use 256-bit key
            secretKey = keyGen.generateKey();
        } catch (Exception e) {
        }
    }

    public static String encrypt(String data) {
        try {
            // Generate random salt
            byte[] salt = new byte[32];
            SecureRandom random = new SecureRandom();
            random.nextBytes(salt);

            // Initialize AES Cipher
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            
            // Generate random iv
            SecureRandom randomSecureRandom = new SecureRandom();
            byte[] iv = new byte[cipher.getBlockSize()];
            randomSecureRandom.nextBytes(iv);
            IvParameterSpec ivParams = new IvParameterSpec(iv);

            // Complete initialization
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivParams);

            // Encrypt data
            byte[] encryptedData = cipher.doFinal(data.getBytes());
            
            // Combine iv with encrypted data
            byte[] encryptedResult = new byte[iv.length + encryptedData.length];
            System.arraycopy(iv, 0, encryptedResult, 0, iv.length);
            System.arraycopy(encryptedData, 0, encryptedResult, iv.length, encryptedData.length);

            return Base64.getEncoder().encodeToString(encryptedResult);
            
        }
        catch (Exception e) {
            return null;
        }
    }

    public static String decrypt(String encryptedData) {
        try {
            // Decode the encrypted data
            byte[] decodedData = Base64.getDecoder().decode(encryptedData);

            // Extract IV (first 16 bytes )
            byte[] iv = new byte[16];
            System.arraycopy(decodedData, 0, iv, 0, iv.length);

            // Extract encrypted data (everything after iv)
            byte[] encryptedBytes = new byte[decodedData.length - iv.length];
            System.arraycopy(decodedData, iv.length, encryptedBytes, 0, encryptedBytes.length);

            // Generate key
            KeyGenerator keyGen = KeyGenerator.getInstance("AES");
            keyGen.init(256); // 256-bit AES key
            SecretKey secretKey = keyGen.generateKey();

            // Initialize cipher
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            IvParameterSpec ivParams = new IvParameterSpec(iv);
            cipher.init(Cipher.DECRYPT_MODE, secretKey, ivParams);

            // Decrypt the data
            byte[] decryptedData = cipher.doFinal(encryptedBytes);

            return new String(decryptedData);
        }
        catch (Exception e) {
            return null;
        }
    }

}
