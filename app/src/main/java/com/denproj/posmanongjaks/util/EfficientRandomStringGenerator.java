package com.denproj.posmanongjaks.util;

import java.security.SecureRandom;

public class EfficientRandomStringGenerator {

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final int STRING_LENGTH = 10;
    private static final SecureRandom RANDOM = new SecureRandom(); // SecureRandom for better randomness

    public static String generateRandomTenDigitString() {
        char[] result = new char[STRING_LENGTH]; // Pre-allocate the char array for better performance

        for (int i = 0; i < STRING_LENGTH; i++) {
            result[i] = CHARACTERS.charAt(RANDOM.nextInt(CHARACTERS.length()));
        }

        return new String(result); // Convert the char array into a string
    }

}
