package com.gymcrm.utils;

import com.gymcrm.model.User;
import java.security.SecureRandom;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

public class UserUtils {
    private static final String CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final SecureRandom RANDOM = new SecureRandom();

    public static String generatePassword() {
        StringBuilder sb = new StringBuilder(10);
        for (int i = 0; i < 10; i++) {
            sb.append(CHARS.charAt(RANDOM.nextInt(CHARS.length())));
        }
        return sb.toString();
    }

    public static String generateUsername(String firstName, String lastName, Set<String> existingUsernames) {
        String base = firstName.trim() + "." + lastName.trim();
        long count = existingUsernames.stream()
                .filter(u -> u.replaceAll("\\d+$", "").equals(base))
                .count();
        return count == 0 ? base : base + count;
    }

    public static void setupCredentials(User u, Set<String> existingUsernames) {
        u.setUsername(generateUsername(u.getFirstName(), u.getLastName(), existingUsernames));
        u.setPassword(generatePassword());
    }
}