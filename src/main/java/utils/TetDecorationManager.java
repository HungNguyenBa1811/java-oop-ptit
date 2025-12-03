package main.java.utils;

import javafx.animation.*;
import javafx.scene.Group;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.util.Duration;
import java.util.Random;

/**
 * TetDecorationManager - Quản lý decoration Tết
 * Bao gồm: Pháo hoa, Bánh chưng, Hoa đào/mai
 */
public class TetDecorationManager {
    
    private static final Random random = new Random();
    
    /**
     * Tạo decoration layer với pháo hoa và bánh chưng
     * @param width Chiều rộng container
     * @param height Chiều cao container
     * @return Pane chứa các decoration
     */
    public static Pane createTetDecorationLayer(double width, double height) {
        Pane decorationPane = new Pane();
        decorationPane.setPickOnBounds(false); // Cho phép click xuyên qua
        decorationPane.setMouseTransparent(true); // Không chặn mouse events
        
        // Thêm hiệu ứng pháo hoa
        startFireworksAnimation(decorationPane, width, height);
        
        return decorationPane;
    }
    
    /**
     * Tạo hiệu ứng pháo hoa
     */
    public static void startFireworksAnimation(Pane container, double width, double height) {
        // Timeline để spawn pháo hoa định kỳ
        Timeline fireworksTimeline = new Timeline(
            new KeyFrame(Duration.seconds(3), e -> {
                // Random vị trí
                double x = 100 + random.nextDouble() * (width - 200);
                double y = 100 + random.nextDouble() * (height / 2);
                
                // Random màu
                Color[] colors = {
                    Color.web("#FF0000"), // Đỏ
                    Color.web("#FFD700"), // Vàng
                    Color.web("#FF69B4"), // Hồng
                    Color.web("#00FF00"), // Xanh lá
                    Color.web("#FFA500")  // Cam
                };
                Color color = colors[random.nextInt(colors.length)];
                
                createFirework(container, x, y, color);
            })
        );
        fireworksTimeline.setCycleCount(Animation.INDEFINITE);
        fireworksTimeline.play();
    }
    
    /**
     * Tạo một vụ nổ pháo hoa
     */
    private static void createFirework(Pane container, double x, double y, Color color) {
        Group firework = new Group();
        int particleCount = 96; // 12 * 8 = 96 particles
        
        for (int i = 0; i < particleCount; i++) {
            // Tạo particle
            Circle particle = new Circle(6); // Gấp đôi kích thước particle
            particle.setFill(color);
            particle.setCenterX(x);
            particle.setCenterY(y);
            
            // Tính toán hướng bay - gấp 4 lần khoảng cách
            double angle = (2 * Math.PI / particleCount) * i;
            double distance = 200 + random.nextDouble() * 120; // (50+30) * 4 = 320 max
            double endX = x + Math.cos(angle) * distance;
            double endY = y + Math.sin(angle) * distance;
            
            firework.getChildren().add(particle);
            
            // Animation cho particle
            Timeline particleAnim = new Timeline(
                new KeyFrame(Duration.ZERO,
                    new KeyValue(particle.centerXProperty(), x),
                    new KeyValue(particle.centerYProperty(), y),
                    new KeyValue(particle.opacityProperty(), 1.0),
                    new KeyValue(particle.radiusProperty(), 8) // Gấp đôi radius ban đầu
                ),
                new KeyFrame(Duration.seconds(1.2), // Kéo dài animation
                    new KeyValue(particle.centerXProperty(), endX),
                    new KeyValue(particle.centerYProperty(), endY + 40), // Rơi xuống nhiều hơn
                    new KeyValue(particle.opacityProperty(), 0.0),
                    new KeyValue(particle.radiusProperty(), 2)
                )
            );
            particleAnim.play();
        }
        
        // Thêm tia sáng trung tâm - to gấp 4
        Circle flash = new Circle(32);
        flash.setFill(Color.WHITE);
        flash.setCenterX(x);
        flash.setCenterY(y);
        firework.getChildren().add(flash);
        
        FadeTransition flashFade = new FadeTransition(Duration.millis(300), flash);
        flashFade.setFromValue(1.0);
        flashFade.setToValue(0.0);
        flashFade.play();
        
        container.getChildren().add(firework);
        
        // Xóa firework sau khi animation kết thúc
        Timeline cleanup = new Timeline(new KeyFrame(Duration.seconds(1.5), e -> {
            container.getChildren().remove(firework);
        }));
        cleanup.play();
    }
    
    /**
     * Tạo bao lì xì (bonus decoration)
     */
    public static Group createLiXi(double size) {
        Group lixi = new Group();
        
        // Thân bao lì xì
        Rectangle body = new Rectangle(size * 0.6, size);
        body.setFill(Color.web("#C41E3A"));
        body.setStroke(Color.web("#8B0000"));
        body.setStrokeWidth(2);
        body.setArcWidth(5);
        body.setArcHeight(5);
        
        // Viền vàng
        Rectangle goldBorder = new Rectangle(size * 0.6, size * 0.15);
        goldBorder.setFill(Color.web("#FFD700"));
        goldBorder.setLayoutY(size * 0.4);
        
        // Chữ Phúc đơn giản (hình tròn với pattern)
        Circle symbol = new Circle(size * 0.15);
        symbol.setFill(Color.web("#FFD700"));
        symbol.setCenterX(size * 0.3);
        symbol.setCenterY(size * 0.5);
        
        lixi.getChildren().addAll(body, goldBorder, symbol);
        
        // Animation lắc nhẹ
        RotateTransition rotate = new RotateTransition(Duration.seconds(1.5), lixi);
        rotate.setFromAngle(-3);
        rotate.setToAngle(3);
        rotate.setCycleCount(Animation.INDEFINITE);
        rotate.setAutoReverse(true);
        rotate.play();
        
        return lixi;
    }
    
    /**
     * Wrap một root node với decoration layer
     */
    public static StackPane wrapWithDecoration(javafx.scene.Parent root, double width, double height) {
        StackPane wrapper = new StackPane();
        Pane decorations = createTetDecorationLayer(width, height);
        wrapper.getChildren().addAll(root, decorations);
        return wrapper;
    }
}
