package com.mycompany.myapp.service.captcha;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Random;
import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.lang3.StringUtils;

public class CommonUtil {

    private static final String SECRET = "NrEwv2uU3/vYTmfxQbH8dfiRhMG60SfF0IzJnrGNZvw=";
    private static final String IV_PARAM = "DcSX9Nl4QcnpD2lv";

    public static String generateCaptchaTextMethod2(int captchaLength) {
        String saltChars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder captchaStrBuffer = new StringBuilder();
        Random rnd = new Random();

        // build a random captchaLength chars salt
        while (captchaStrBuffer.length() < captchaLength) {
            int index = (int) (rnd.nextFloat() * saltChars.length());
            captchaStrBuffer.append(saltChars.substring(index, index + 1));
        }

        return captchaStrBuffer.toString();
    }

    public static String encrypt(String plaintext) {
        try {
            // Get Cipher Instance
            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");

            // Create SecretKeySpec
            SecretKeySpec keySpec = new SecretKeySpec(Base64.getDecoder().decode(SECRET), "AES");
            byte[] IV = Base64.getDecoder().decode(IV_PARAM);

            // Create GCMParameterSpec
            GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(16 * 8, IV);

            // Initialize Cipher for ENCRYPT_MODE
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, gcmParameterSpec);

            // Perform Encryption
            byte[] cipherText = cipher.doFinal(plaintext.getBytes(StandardCharsets.UTF_8));

            return Base64.getEncoder().encodeToString(cipherText);
        } catch (Exception e) {}
        return null;
    }

    public static String decrypt(String cipherText) {
        if (StringUtils.isEmpty(cipherText)) {
            return null;
        }
        try {
            // Get Cipher Instance
            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");

            // Create SecretKeySpec
            SecretKeySpec keySpec = new SecretKeySpec(Base64.getDecoder().decode(SECRET), "AES");
            byte[] IV = Base64.getDecoder().decode(IV_PARAM);

            // Create GCMParameterSpec
            GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(16 * 8, IV);

            // Initialize Cipher for DECRYPT_MODE
            cipher.init(Cipher.DECRYPT_MODE, keySpec, gcmParameterSpec);

            // Perform Decryption
            byte[] decryptedText = cipher.doFinal(Base64.getDecoder().decode(cipherText));

            return new String(decryptedText, StandardCharsets.UTF_8);
        } catch (Exception e) {}
        return null;
    }
}
