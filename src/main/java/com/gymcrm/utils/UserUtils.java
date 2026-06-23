package com.gymcrm.utils;

import com.gymcrm.model.User;
import java.security.SecureRandom;
import java.util.Collection;
import java.util.Map;

public class UserUtils {
    private static final String CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final SecureRandom RANDOM = new SecureRandom();

    // Генерирует случайный 10-значный пароль
    public static String generatePassword() {
        StringBuilder sb = new StringBuilder(10);
        for (int i = 0; i < 10; i++) {
            sb.append(CHARS.charAt(RANDOM.nextInt(CHARS.length())));
        }
        return sb.toString();
    }

    // Создает юзернейм вида "Имя.Фамилия". Если такой уже есть, добавляет цифру на конце
    public static <T extends User, K extends User> String generateUsername(
            String firstName, String lastName, Map<Long, T> s1, Map<Long, K> s2) {
        String base = firstName.trim() + "." + lastName.trim();
        long count = countMatches(base, s1.values()) + countMatches(base, s2.values());
        return count == 0 ? base : base + count;
    }

    private static <T extends User> long countMatches(String base, Collection<T> users) {
        return users.stream()
                .map(User::getUsername)
                .filter(u -> u != null && u.replaceAll("\\d+$", "").equals(base))
                .count();
    }

    // Метод-"хелпер", который сразу собирает и логин, и пароль для нового юзера
    public static <T extends User, K extends User> void setupCredentials(User u, Map<Long, T> s1, Map<Long, K> s2) {
        u.setUsername(generateUsername(u.getFirstName(), u.getLastName(), s1, s2));
        u.setPassword(generatePassword());
    }
}