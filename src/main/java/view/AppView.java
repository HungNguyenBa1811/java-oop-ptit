package main.java.view;
import main.java.utils.FXUtils;
import javafx.application.Application;
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
        stage.getIcons().add(icon);
        stage.setWidth(1366);
        stage.setHeight(768);

        NavigationManager navigationManager = new NavigationManager(stage);
        navigationManager.showLoginScreen();
    }
}