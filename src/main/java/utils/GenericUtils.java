package main.java.utils;

public class GenericUtils {    
    public static int safeParseInt(String s, int fallback) {
        try {
            return s == null ? fallback : Integer.parseInt(s.trim());
        } catch (NumberFormatException ex) {
            return fallback;
        }
    }
    public static String safeParseString(String s) {
        return s == null ? "" : s;
    }
}
