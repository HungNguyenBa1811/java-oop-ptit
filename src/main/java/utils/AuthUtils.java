package main.java.utils;

import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import main.java.model.User;
import main.java.service.AuthService;
import main.java.view.NavigationManager;

public class AuthUtils {
    public static void appLogout(AuthService auth, Button logoutBtn) {
        try {
            User current = auth.getCurrentUser();
            if (current != null) {
                auth.logout(current.getUserId());
            } else {
                auth.clearSession();
            }

            // 🎵 Dừng nhạc Tết khi đăng xuất
            TetAudioManager.getInstance().stop();

            FXUtils.showSuccess("Đăng xuất thành công");

            NavigationManager nav = new NavigationManager(
                ((Stage) logoutBtn.getScene().getWindow())
            );
            nav.showLoginScreen();

        } catch (Exception ex) {
            FXUtils.showError("Đăng xuất thất bại: " + ex.getMessage());
        }
    }

    /**
     * Set the provided Text node to show current user's display name with a prefix.
     * Returns the current user's id (or null if not available).
     */
    public static String setUserLabel(Text label, String prefix, AuthService auth) {
        try {
            User current = auth.getCurrentUser();
            String displayName = (current != null && current.getFullName() != null && !current.getFullName().trim().isEmpty())
                ? current.getFullName().trim()
                : "Không xác định";
            if (label != null) {
                label.setText((prefix != null ? prefix : "") + displayName);
            }
            return current != null ? current.getUserId() : null;
        } catch (Exception ex) {
            if (label != null) label.setText((prefix != null ? prefix : "") + "Không xác định");
            return null;
        }
    }
}
