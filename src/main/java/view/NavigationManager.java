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
        String cssPath = getClass().getResource("/main/resources/css/style.css").toExternalForm();
        scene.getStylesheets().add(cssPath);
        stage.setTitle("Đăng nhập");
        stage.setScene(scene);
        stage.setWidth(1366);
        stage.setHeight(768);
        stage.show();
    }

    public void showStudentDashboard() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(FXUtils.fxml("fxml/studentdashboard.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        String cssPath = getClass().getResource("/main/resources/css/style.css").toExternalForm();
        scene.getStylesheets().add(cssPath);
        stage.setTitle("Bảng điều khiển");
        stage.setScene(scene);
        stage.show();
    }

    public void showAdminDashboard() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(FXUtils.fxml("fxml/admindashboard.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        String cssPath = getClass().getResource("/main/resources/css/style.css").toExternalForm();
        scene.getStylesheets().add(cssPath);
        stage.setTitle("Bảng điều khiển quản trị viên");
        stage.setScene(scene);
        stage.show();
    }

    // Add more methods here for other views, e.g., showCourseManagement(), showUserSettings(), etc.
}