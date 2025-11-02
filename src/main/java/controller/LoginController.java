package main.java.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import main.java.view.NavigationManager;

import java.io.IOException;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button loginButton;

    @FXML
    private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        System.out.println("Username: " + username);
        System.out.println("Password: " + password);
        // --- Your authentication logic here ---
        boolean isAuthenticated = true; // Replace with real authentication logic

        if (isAuthenticated) {
            try {
                // Get the stage from any component in the current scene
                Stage currentStage = (Stage) loginButton.getScene().getWindow();

                // Use the NavigationManager to switch to the dashboard
                NavigationManager navigationManager = new NavigationManager(currentStage);
                navigationManager.showDashboard();
            } catch (IOException e) {
                e.printStackTrace();
                // Handle navigation error (e.g., show an alert)
            }
        } else {
            // Show login error message
        }
    }
}
