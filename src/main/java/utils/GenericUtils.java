package main.java.utils;

import javafx.scene.Node;
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
    
    public static String safeOr(String value, String fallback) {
        return (value == null || value.trim().isEmpty()) ? fallback : value;
    }
    
    public static Stage getStageFromSource(Object source) {
        if (source instanceof Node) {
            Node node = (Node) source;
            return node.getScene() != null ? (Stage) node.getScene().getWindow() : null;
        }
        return null;
    }
}
