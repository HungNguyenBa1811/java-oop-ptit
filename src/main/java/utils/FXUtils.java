package main.java.utils;

import java.io.File;
import java.net.URL;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import main.java.view.AppView;

public class FXUtils {
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
            "/main/resources/assets/fonts/Comic Sans Pro W99 Bold Italic.ttf",
            "/main/resources/assets/fonts/Comic Sans Pro W99 Bold.ttf",
            "/main/resources/assets/fonts/Comic Sans Pro W99 Italic.ttf",
            "/main/resources/assets/fonts/Comic Sans Pro W99 Regular.ttf",
            "/main/resources/assets/fonts/Roboto-Bold.ttf",
        };

        for (String path : fonts) {
            Font font = Font.loadFont(AppView.class.getResourceAsStream(path), 12);
            System.out.println((font != null ? String.format("%sLoaded: %s | Family: %s%s", GREEN, font.getName(), font.getFamily(), RESET) : String.format("%sFailed: %s%s", RED, path, RESET)));
        }
    }

    public static void showError(String... msg) {
        String message = String.join(" ", msg);
        Alert alert = new Alert(AlertType.ERROR);
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(AppView.class.getResourceAsStream("/main/resources/assets/images/huzano.png")));
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void showSuccess(String... msg) {
        String message = String.join(" ", msg);
        Alert alert = new Alert(AlertType.INFORMATION);
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(AppView.class.getResourceAsStream("/main/resources/assets/images/huzano.png")));
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
