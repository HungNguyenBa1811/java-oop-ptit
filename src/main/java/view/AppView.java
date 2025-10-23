package main.java.view;
import main.java.utils.FXUtils;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class AppView extends Application {
    public static void main(String[] args) {
        launch(args);
    }
    @Override
    public void start(Stage stage) throws Exception {
        FXUtils.loadFonts();
        Image icon = new Image(AppView.class.getResourceAsStream("/main/resources/assets/images/huzano.png"));
        FXMLLoader fxmlLoader = new FXMLLoader(FXUtils.fxml("fxml/login.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        stage.setTitle("Đăng nhập");
        stage.setScene(scene);
        stage.getIcons().add(icon);

        stage.show();
    }
}