package main.java.controller.auth;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import main.java.model.User;
import main.java.service.AuthService;
import main.java.service.impl.AuthServiceImpl;
import main.java.utils.FXUtils;
import main.java.view.NavigationManager;

public class LoginController {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Button loginButton;
    
    private final AuthService auth = AuthServiceImpl.getInstance();
    @FXML
    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();

        if(username.isEmpty() || password.isEmpty()) {
            FXUtils.showError("Vui lòng điền đầy đủ thông tin đăng nhập!");
            return;
        }

        try {
            User user = auth.login(username, password);
            if(user == null) {
                FXUtils.showError("Sai thông tin đăng nhập!");
                return;
            }
            FXUtils.showSuccess("Đăng nhập thành công!");
            Stage currentStage = (Stage) loginButton.getScene().getWindow();
            NavigationManager navigationManager = new NavigationManager(currentStage);
            if(user.getRole() == 1) {
                navigationManager.showAdminDashboard(); 
            } else {
                navigationManager.showStudentDashboard();
            }
        } catch (IllegalArgumentException e) {
            FXUtils.showError("Lỗi đăng nhập:", e.getMessage());
        } catch (Exception e) {
            FXUtils.showError("Lỗi ứng dụng, vui lòng thử lại sau!");
            e.printStackTrace();
        } 
    }
}
