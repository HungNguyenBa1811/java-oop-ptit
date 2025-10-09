package main.java.view;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCombination;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;


public class AppView extends Application {
    public static void main(String[] args) {
        launch(args);
    }
    @Override
    public void start(Stage primaryStage) throws Exception {
        loadFonts();
        // Stage stage = new Stage();

        Group root = new Group();
        Scene scene = new Scene(root, 800, 600, Color.BLACK);

        Text text = new Text();
        text.setText("Huzano 2.0 App");
        text.setY(100);
        text.setFont(Font.font("Comic Sans Pro Regular", 50));
        text.setFill(Color.WHITE);

        // Auto Alignment
        scene.widthProperty().addListener((_, _, newVal) -> {
            text.setLayoutX((newVal.doubleValue() - text.getLayoutBounds().getWidth()) / 2);
        });
        text.layoutBoundsProperty().addListener((_, _, newVal) -> {
            text.setLayoutX((scene.getWidth() - newVal.getWidth()) / 2);
        });

        root.getChildren().add(text);

        Image icon = new Image(AppView.class.getResourceAsStream("/resources/assets/images/huzano.png"));
        primaryStage.getIcons().add(icon);
        primaryStage.setTitle("Dung Huzano 2.0");
        // primaryStage.setWidth(800);
        // primaryStage.setHeight(600);
        primaryStage.setResizable(false);
        // primaryStage.setX(50);
        // primaryStage.setX(50);
        primaryStage.setFullScreen(true);
        primaryStage.setFullScreenExitHint("Dung Huzano hacked your computer :)");
        primaryStage.setFullScreenExitKeyCombination(KeyCombination.valueOf("Q"));

        primaryStage.setScene(scene);
        primaryStage.show();
    }
    public static void loadFonts() {
        final String RESET = "\u001B[0m";
        final String GREEN = "\u001B[32m";
        final String RED = "\u001B[31m";

        String[] fonts = {
            "/resources/assets/fonts/Comic Sans Pro W99 Bold Italic.ttf",
            "/resources/assets/fonts/Comic Sans Pro W99 Bold.ttf",
            "/resources/assets/fonts/Comic Sans Pro W99 Italic.ttf",
            "/resources/assets/fonts/Comic Sans Pro W99 Regular.ttf",
        };

        for (String path : fonts) {
            Font font = Font.loadFont(AppView.class.getResourceAsStream(path), 12);
            System.out.println((font != null ? String.format("%sLoaded: %s | Family: %s%s", GREEN, font.getName(), font.getFamily(), RESET) : String.format("%sFailed: %s%s", RED, path, RESET)));
        }
    }
}