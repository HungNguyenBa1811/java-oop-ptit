package main.java.utils;

import javafx.scene.control.Button;
import javafx.stage.Stage;

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

    public static Stage getStageFromSource(Object src) {
        if (src instanceof Button b && b.getScene() != null && b.getScene().getWindow() instanceof Stage stage) {
            return stage;
        }
        throw new IllegalStateException("Không thể xác định cửa sổ cha để mở dialog");
    }

    // Generic fallback
    public static <T> T safeOr(T value, T fallback) {
        return value != null ? value : fallback;
    }

    // String fallback
    public static String safeOr(String s, String fallback) {
        if (s == null || s.trim().isEmpty()) return fallback;
        return s;
    }
}
