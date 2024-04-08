package com.example.loginscreen;

import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class UtilityCommunication {

    // Fix password length to 16 characters
    private static String fixKey(String key) {
        // Pad and truncate the key
        key += "0000000000000000";
        key = key.substring(0, 16);
        return key;
    }

    // Encrypt text using AES
    public static String encrypt(String text, String key) throws Exception {
        key = fixKey(key);
        SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, keySpec);
        byte[] encryptedBytes = cipher.doFinal(text.getBytes());
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    // Decrypt text using AES
    public static String decrypt(String text, String key) throws Exception {
        key = fixKey(key);
        SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, keySpec);
        byte[] decodedBytes = Base64.getDecoder().decode(text);
        byte[] decryptedBytes = cipher.doFinal(decodedBytes);
        return new String(decryptedBytes);
    }

    // Encrypt an array of strings together
    public static String encryptTicket(String[] texts, String key) throws Exception {
        // Replace delimiter characters
        for (int i = 0; i< texts.length; i++) {
            texts[i] = texts[i].replace("╡", "|");
        }

        // Join and encrypt
        String text = texts.length + "╡" + String.join("╡", texts);
        return encrypt(text, key);
    }

    // Decrypt an array of strings together
    public static String[] decryptTicket(String text, String key) throws Exception {
        // Decrypt
        text = decrypt(text, key);

        // Get number of parts
        String[] parts = text.split("╡", 2);
        int count = Integer.parseInt(parts[0]);
        String data = parts[1];

        // Split data
        String[] texts = data.split("╡", count);
        return texts;
    }

}