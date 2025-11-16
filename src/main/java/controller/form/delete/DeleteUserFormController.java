package main.java.controller.form.delete;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import main.java.model.User;
import main.java.service.impl.UserServiceImpl;
import main.java.utils.FXUtils;

public class DeleteUserFormController {
	@FXML private Label messageLabel;
	@FXML private CheckBox dontAskAgain;
	@FXML private Button cancelButton;
	@FXML private Button continueButton;

	private final UserServiceImpl userService = new UserServiceImpl();
	private User targetUser;

	public void prefillFrom(User user) {
		this.targetUser = user;
		String name = (user != null && user.getFullName() != null && !user.getFullName().isEmpty())
				? user.getFullName() : (user != null ? user.getUsername() : "");
		if (messageLabel != null) {
			messageLabel.setText("Thao tác này sẽ xóa người dùng '" + name + "' khỏi hệ thống. Hành động không thể hoàn tác.");
		}
	}

	@FXML
	private void handleCancel() {
		close();
	}

	@FXML
	private void handleContinue() {
		try {
			if (targetUser == null || targetUser.getUserId() == null || targetUser.getUserId().trim().isEmpty()) {
				FXUtils.showError("Không xác định được người dùng để xóa");
				return;
			}
			boolean ok = userService.deleteUser(targetUser.getUserId());
			if (ok) {
				FXUtils.showSuccess("Đã xóa người dùng thành công");
				close();
			} else {
				FXUtils.showError("Xóa người dùng thất bại");
			}
		} catch (Exception ex) {
			FXUtils.showError("Xóa người dùng thất bại: " + ex.getMessage());
		}
	}

	private void close() {
		if (cancelButton != null && cancelButton.getScene() != null) {
			Stage st = (Stage) cancelButton.getScene().getWindow();
			if (st != null) st.close();
		}
	}
}
