package main.java.controller.admin.user;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import main.java.model.User;
import main.java.model.Student;
import main.java.model.Faculty;
import main.java.service.impl.StudentServiceImpl;
import main.java.service.impl.FacultyServiceImpl;
import main.java.service.impl.MajorServiceImpl;

import static main.java.utils.GenericUtils.safeParseString;

public class ReadUserFormController {
	@FXML private Label userIdLabel;
	@FXML private Label usernameLabel;
	@FXML private Label fullNameLabel;
	@FXML private Label emailLabel;
	@FXML private Label roleLabel;
	@FXML private Label facultyNameLabel;
	@FXML private Label facultyIdLabel;
	@FXML private Label majorNameLabel;
	@FXML private Label majorIdLabel;
	@FXML private Label statusLabel;
	@FXML private Button editButton;
	@FXML private Button closeButton;

	private final StudentServiceImpl studentService = new StudentServiceImpl();
	private final FacultyServiceImpl facultyService = new FacultyServiceImpl();
	private final MajorServiceImpl majorService = new MajorServiceImpl();
	@SuppressWarnings("unused")
	private User currentUser;

	public void prefillFrom(User user) {
		this.currentUser = user;
		if (user == null) return;
		if (userIdLabel != null) userIdLabel.setText(safeParseString(user.getUserId()));
		if (usernameLabel != null) usernameLabel.setText(safeParseString(user.getUsername()));
		if (fullNameLabel != null) fullNameLabel.setText(safeParseString(user.getFullName()));
		if (emailLabel != null) emailLabel.setText(safeParseString(user.getEmail()));
		if (roleLabel != null) roleLabel.setText(user.getRole() == 1 ? "Admin" : "Sinh viên");

		if (user.getRole() == 0) {
			try {
				Student st = studentService.getStudentById(user.getUserId());
				String facultyText = "";
				String facultyId = "";
				String majorText = "";
				String majorId = "";
				String status = "";
				if (st != null) {
					facultyId = st.getFacultyId() != null ? st.getFacultyId() : "";
					majorId = st.getMajorId() != null ? st.getMajorId() : "";
					status = st.getStatus() != null ? st.getStatus() : "";
					if (!facultyId.isEmpty()) {
						Faculty f = facultyService.getFacultyById(facultyId);
						facultyText = (f != null && f.getFacultyName() != null) ? f.getFacultyName() : facultyId;
					}
					if (!majorId.isEmpty()) {
						try {
							var mj = majorService.getMajorById(majorId);
							majorText = (mj != null && mj.getMajorName() != null) ? mj.getMajorName() : majorId;
						} catch (Exception ignored) {
							majorText = majorId;
						}
					}
				}
				if (facultyNameLabel != null) facultyNameLabel.setText(facultyText);
				if (facultyIdLabel != null) facultyIdLabel.setText(facultyId);
				if (majorNameLabel != null) majorNameLabel.setText(majorText);
				if (majorIdLabel != null) majorIdLabel.setText(majorId);
				if (statusLabel != null) statusLabel.setText(status);
			} catch (Exception ignored) {
				if (facultyNameLabel != null) facultyNameLabel.setText("");
			}
		} else {
			if (facultyNameLabel != null) facultyNameLabel.setText("");
			if (facultyIdLabel != null) facultyIdLabel.setText("");
			if (majorNameLabel != null) majorNameLabel.setText("");
			if (majorIdLabel != null) majorIdLabel.setText("");
			if (statusLabel != null) statusLabel.setText("");
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
