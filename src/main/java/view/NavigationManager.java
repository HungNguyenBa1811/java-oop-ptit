package main.java.view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import main.java.utils.FXUtils;

import java.io.IOException;

public class NavigationManager {
    private final Stage stage;

    public NavigationManager(Stage stage) {
        this.stage = stage;
    }

    public void showLoginScreen() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(FXUtils.fxml("fxml/login.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Đăng nhập");
        stage.setScene(scene);
        stage.show();
    }

    public void showDashboard() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(FXUtils.fxml("fxml/dashboard.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Bảng điều khiển");
        stage.setScene(scene);
        // You might want to maximize the window for the dashboard
        stage.setMaximized(true);
    }

    // Add more methods here for other views, e.g., showCourseManagement(), showUserSettings(), etc.
}