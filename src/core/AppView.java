package core;
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
        // Stage stage = new Stage();
        Group root = new Group();
        Scene scene = new Scene(root, 800, 600, Color.BLACK);

        Text text = new Text();
        text.setText("Huzano 2.0 App");
        text.setY(100);
        text.setFont(Font.font("Comic Sans MS", 50));
        text.setFill(Color.WHITE);

        // Auto Alignment
        scene.widthProperty().addListener((_, _, newVal) -> {
            text.setLayoutX((newVal.doubleValue() - text.getLayoutBounds().getWidth()) / 2);
        });
        text.layoutBoundsProperty().addListener((_, _, newVal) -> {
            text.setLayoutX((scene.getWidth() - newVal.getWidth()) / 2);
        });

        root.getChildren().add(text);

        Image icon = new Image(AppView.class.getResourceAsStream("/assets/images/huzano.png"));
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
}