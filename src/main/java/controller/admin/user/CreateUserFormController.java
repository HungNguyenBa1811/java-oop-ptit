package main.java.controller.admin.user;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import main.java.model.Admin;
import main.java.model.Student;
import main.java.model.Major;
import main.java.service.impl.AdminServiceImpl;
import main.java.service.impl.MajorServiceImpl;
import main.java.utils.FXUtils;
import main.java.dto.user.UserFormData;

public class CreateUserFormController {
    // FXML fields (both add and edit forms)
    @FXML private Text formTitle;
    @FXML private TextField userIdField;
    @FXML private TextField usernameField;
    @FXML private TextField fullNameField;
    @FXML private PasswordField passwordField;
    @FXML private TextField emailField;
    @FXML private ComboBox<String> roleComboBox;
    @FXML private TextField classField;          // add-form
    @FXML private ComboBox<String> majorComboBox;
    @FXML private ComboBox<String> statusComboBox;
    @FXML private Button cancelButton;
    @FXML private Button saveButton;
    @FXML private Label classLabel;   // optional (present in userForm.fxml)
    @FXML private Label majorLabel;   // present in both forms
    @FXML private Label statusLabel;  // present in both forms

    // Data model
    

    private final UserFormData formData = new UserFormData();
    private final AdminServiceImpl adminService = new AdminServiceImpl();
    private final MajorServiceImpl majorService = new MajorServiceImpl();

    @FXML
    public void initialize() {
        bindFields();
        loadRoleOptions();
        loadStatusOptions();
        loadMajors();
        if (roleComboBox != null) {
            roleComboBox.valueProperty().addListener((obs, o, n) -> updateStudentFieldsVisibility());
        }
        updateStudentFieldsVisibility();
        if (saveButton != null) saveButton.setOnAction(e -> handleSave());
        if (cancelButton != null) cancelButton.setOnAction(e -> handleCancel());
    }

    private void bindFields() {
        if (userIdField != null) userIdField.textProperty().bindBidirectional(formData.userIdProperty());
        if (usernameField != null) usernameField.textProperty().bindBidirectional(formData.usernameProperty());
        if (passwordField != null) passwordField.textProperty().bindBidirectional(formData.passwordProperty());
        if (fullNameField != null) fullNameField.textProperty().bindBidirectional(formData.fullNameProperty());
        if (emailField != null) emailField.textProperty().bindBidirectional(formData.emailProperty());
        if (roleComboBox != null) roleComboBox.valueProperty().bindBidirectional(formData.roleProperty());
        if (classField != null) classField.textProperty().bindBidirectional(formData.studentClassProperty());
        if (majorComboBox != null) majorComboBox.valueProperty().bindBidirectional(formData.majorIdProperty());
        if (statusComboBox != null) statusComboBox.valueProperty().bindBidirectional(formData.statusProperty());
    }

    private void loadRoleOptions() {
        if (roleComboBox != null) {
            roleComboBox.setItems(FXCollections.observableArrayList("Admin", "Sinh viên"));
        }
    }

    private void loadStatusOptions() {
        if (statusComboBox != null) {
            statusComboBox.setItems(FXCollections.observableArrayList("Đang học", "Nghỉ học"));
        }
    }

    private void loadMajors() {
        try {
            var majors = majorService.getAllMajors();
            if (majorComboBox != null) {
                majorComboBox.setItems(FXCollections.observableArrayList(
                    majors.stream().map(Major::getMajorId).toList()
                ));
            }
        } catch (Exception e) {
            if (majorComboBox != null) majorComboBox.setItems(FXCollections.observableArrayList());
        }
    }

    private void updateStudentFieldsVisibility() {
        boolean isStudent = roleComboBox != null
                && roleComboBox.getValue() != null
                && roleComboBox.getValue().equalsIgnoreCase("Sinh viên");

        if (classField != null) {
            classField.setDisable(!isStudent);
            classField.setManaged(isStudent);
            classField.setVisible(isStudent);
        }
        if (majorComboBox != null) {
            majorComboBox.setDisable(!isStudent);
            majorComboBox.setManaged(isStudent);
            majorComboBox.setVisible(isStudent);
        }
        if (statusComboBox != null) {
            statusComboBox.setDisable(!isStudent);
            statusComboBox.setManaged(isStudent);
            statusComboBox.setVisible(isStudent);
        }
        if (classLabel != null) {
            classLabel.setManaged(isStudent);
            classLabel.setVisible(isStudent);
        }
        if (majorLabel != null) {
            majorLabel.setManaged(isStudent);
            majorLabel.setVisible(isStudent);
        }
        if (statusLabel != null) {
            statusLabel.setManaged(isStudent);
            statusLabel.setVisible(isStudent);
        }
    }

    private void validateForm() {
        StringBuilder sb = new StringBuilder();
        if (isBlank(formData.getUserId())) sb.append("- User ID trống\n");
        if (isBlank(formData.getUsername())) sb.append("- Tên đăng nhập trống\n");
        if (isBlank(formData.getFullName())) sb.append("- Họ và tên trống\n");
        if (isBlank(formData.getEmail())) sb.append("- Email trống\n");
        if (isBlank(formData.getRole())) sb.append("- Chưa chọn vai trò\n");

        boolean isStudent = "Sinh viên".equalsIgnoreCase(formData.getRole());
        if (isStudent) {
            if (isBlank(formData.getStudentClass())) sb.append("- Lớp (chỉ SV) trống\n");
            if (isBlank(formData.getMajorId())) sb.append("- Ngành (chỉ SV) chưa chọn\n");
            if (isBlank(formData.getStatus())) sb.append("- Trạng thái (chỉ SV) chưa chọn\n");
            if (isBlank(formData.getPassword())) sb.append("- Mật khẩu trống\n");
        } else { // Admin
            if (isBlank(formData.getPassword())) sb.append("- Mật khẩu trống\n");
        }

        if (sb.length() > 0) throw new IllegalArgumentException(sb.toString().trim());
    }

    @FXML
    private void handleSave() {
        try {
            validateForm();
            boolean isStudent = "Sinh viên".equalsIgnoreCase(formData.getRole());

            if (isStudent) {
                Student s = new Student();
                s.setStudentId(formData.getUserId());
                s.setUsername(formData.getUsername());
                s.setFullName(formData.getFullName());
                s.setEmail(formData.getEmail());
                s.setRole(0);
                s.setStudentClass(formData.getStudentClass());
                s.setMajorId(formData.getMajorId());
                s.setStatus(formData.getStatus());
                var created = adminService.registerStudent(s, formData.getPassword(), formData.getMajorId());
                if (created != null) {
                    FXUtils.showSuccess("Tạo sinh viên thành công");
                    closeWindow();
                } else {
                    FXUtils.showError("Không thể tạo sinh viên");
                }
            } else {
                Admin a = new Admin();
                a.setUserId(formData.getUserId());
                a.setUsername(formData.getUsername());
                a.setFullName(formData.getFullName());
                a.setEmail(formData.getEmail());
                a.setRole(1);
                var created = adminService.registerAdmin(a, formData.getPassword());
                if (created != null) {
                    FXUtils.showSuccess("Tạo admin thành công");
                    closeWindow();
                } else {
                    FXUtils.showError("Không thể tạo admin");
                }
            }
        } catch (Exception ex) {
            FXUtils.showError("Lưu thất bại: " + ex.getMessage());
        }
    }

    @FXML
    private void handleCancel() {
        closeWindow();
    }

    private void closeWindow() {
        if (cancelButton != null && cancelButton.getScene() != null) {
            Stage stage = (Stage) cancelButton.getScene().getWindow();
            if (stage != null) stage.close();
        }
    }

    private boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }
}
