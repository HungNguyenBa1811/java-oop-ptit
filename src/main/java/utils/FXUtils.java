package main.java.utils;

import java.io.File;
import java.net.URL;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import main.java.view.AppView;

public class FXUtils {
    public static final String APP_ICON_PATH = "/main/resources/assets/images/huzano.png";
    
    public static void setAppIcon(Stage stage) {
        if (stage != null) {
            Image icon = new Image(AppView.class.getResourceAsStream(APP_ICON_PATH));
            stage.getIcons().add(icon);
        }
    }
    
    public static URL fxml(String relativePath) {
        try {
            return new File("src/main/resources/" + relativePath).toURI().toURL();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public static void loadFonts() {
        final String RESET = "\u001B[0m";
        final String GREEN = "\u001B[32m";
        final String RED = "\u001B[31m";

        String[] fonts = {
            "/main/resources/assets/fonts/Roboto-Bold.ttf"
        };
        for (String path : fonts) {
            Font font = Font.loadFont(AppView.class.getResourceAsStream(path), 12);
            System.out.println((font != null ? String.format("%sLoaded: %s | Family: %s%s", GREEN, font.getName(), font.getFamily(), RESET) : String.format("%sFailed: %s%s", RED, path, RESET)));
        }
    }
    public static void closeWindow(Button cancelButton) {
        if (cancelButton != null && cancelButton.getScene() != null) {
            Stage stage = (Stage) cancelButton.getScene().getWindow();
            if (stage != null) stage.close();
        }
    }
    public static void showError(String... msg) {
        showAlert(AlertType.ERROR, "Error", msg);
    }
    public static void showSuccess(String... msg) {
        showAlert(AlertType.INFORMATION, "Success", msg);
    }
    private static void showAlert(AlertType type, String title, String... msg) {
        String message = String.join(" ", msg);
        Alert alert = new Alert(type);
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        setAppIcon(stage);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
