package main.java.controller.admin.user;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import main.java.model.User;
import main.java.model.Student;
import main.java.model.Major;
import main.java.service.impl.StudentServiceImpl;
import main.java.service.impl.MajorServiceImpl;

import static main.java.utils.GenericUtils.safeParseString;


public class ReadUserFormController {
	@FXML private Label usernameLabel;
	@FXML private Label fullNameLabel;
	@FXML private Label emailLabel;
	@FXML private Label roleLabel;
	@FXML private Label extraLabel;
	@FXML private Button editButton;
	@FXML private Button closeButton;

	private final StudentServiceImpl studentService = new StudentServiceImpl();
	private final MajorServiceImpl majorService = new MajorServiceImpl();
	@SuppressWarnings("unused")
	private User currentUser;

	public void prefillFrom(User user) {
		this.currentUser = user;
		if (user == null) return;
		if (usernameLabel != null) usernameLabel.setText(safeParseString(user.getUsername()));
		if (fullNameLabel != null) fullNameLabel.setText(safeParseString(user.getFullName()));
		if (emailLabel != null) emailLabel.setText(safeParseString(user.getEmail()));
		if (roleLabel != null) roleLabel.setText(user.getRole() == 1 ? "Admin" : "Sinh viên");

		if (user.getRole() == 0) {
			try {
				Student st = studentService.getStudentById(user.getUserId());
				String majorText = "";
				if (st != null && st.getMajorId() != null) {
					Major m = majorService.getMajorById(st.getMajorId());
					majorText = (m != null && m.getMajorName() != null) ? m.getMajorName() : st.getMajorId();
				}
				String status = st != null && st.getStatus() != null ? st.getStatus() : "";
				if (extraLabel != null) extraLabel.setText((majorText + (status.isEmpty() ? "" : " - " + status)).trim());
			} catch (Exception ignored) {
				if (extraLabel != null) extraLabel.setText("");
			}
		} else {
			if (extraLabel != null) extraLabel.setText("");
		}
	}

	@FXML
	private void handleClose() {
		if (closeButton != null && closeButton.getScene() != null) {
			Stage st = (Stage) closeButton.getScene().getWindow();
			if (st != null) st.close();
		}
	}
}
