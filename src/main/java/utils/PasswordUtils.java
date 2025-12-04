package main.java.utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public final class PasswordUtils {

    private PasswordUtils() {}

    /**
     * Returns the MD5 hash of the given input as a lower-case hexadecimal string.
     * Uses the standard Java MessageDigest implementation.
     */
    public static String md5Hex(String input) {
        if (input == null) return null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digest = md.digest(input.getBytes(StandardCharsets.UTF_8));
            return toHexLower(digest);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("MD5 algorithm not available", e);
        }
    }

    private static String toHexLower(byte[] bytes) {
        StringBuilder sb = new StringBuilder(bytes.length * 2);
        for (byte b : bytes) {
            int v = b & 0xFF;
            if (v < 0x10) sb.append('0');
            sb.append(Integer.toHexString(v));
        }
        return sb.toString();
    }

    public static boolean matchesMd5(String raw, String md5Hex) {
        if (raw == null && md5Hex == null) return true;
        if (raw == null || md5Hex == null) return false;
        return md5Hex.equalsIgnoreCase(md5Hex(raw));
    }
}
