package main.java.controller.form.edit;

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
import main.java.service.impl.StudentServiceImpl;
import main.java.service.impl.MajorServiceImpl;
import main.java.utils.FXUtils;

public class EditUserFormController {
    // FXML fields (both add and edit forms)
    @FXML private Text formTitle;
    @FXML private TextField userIdField;
    @FXML private TextField usernameField;
    @FXML private TextField fullNameField;
    @FXML private PasswordField passwordField;
    @FXML private TextField emailField;
    @FXML private ComboBox<String> roleComboBox;
    @FXML private ComboBox<String> majorComboBox;
    @FXML private ComboBox<String> statusComboBox;
    @FXML private Button cancelButton;
    @FXML private Button saveButton;
    @FXML private Label majorLabel;
    @FXML private Label statusLabel;

    // Data model
    public static class UserFormData {
        private final StringProperty userId = new SimpleStringProperty();
        private final StringProperty username = new SimpleStringProperty();
        private final StringProperty password = new SimpleStringProperty();
        private final StringProperty fullName = new SimpleStringProperty();
        private final StringProperty email = new SimpleStringProperty();
        private final StringProperty role = new SimpleStringProperty(); // "Admin" | "Sinh viên"
        private final StringProperty studentClass = new SimpleStringProperty();
        private final StringProperty majorId = new SimpleStringProperty();
        private final StringProperty status = new SimpleStringProperty(); // "Đang học" | "Nghỉ học"
        public StringProperty userIdProperty() { return userId; }
        public StringProperty usernameProperty() { return username; }
        public StringProperty passwordProperty() { return password; }
        public StringProperty fullNameProperty() { return fullName; }
        public StringProperty emailProperty() { return email; }
        public StringProperty roleProperty() { return role; }
        public StringProperty studentClassProperty() { return studentClass; }
        public StringProperty majorIdProperty() { return majorId; }
        public StringProperty statusProperty() { return status; }
        public String getUserId() { return userId.get(); }
        public String getUsername() { return username.get(); }
        public String getPassword() { return password.get(); }
        public String getFullName() { return fullName.get(); }
        public String getEmail() { return email.get(); }
        public String getRole() { return role.get(); }
        public String getStudentClass() { return studentClass.get(); }
        public String getMajorId() { return majorId.get(); }
        public String getStatus() { return status.get(); }
    }

    private final UserFormData formData = new UserFormData();
    private final AdminServiceImpl adminService = new AdminServiceImpl();
    private final StudentServiceImpl studentService = new StudentServiceImpl();
    private final MajorServiceImpl majorService = new MajorServiceImpl();

    @FXML
    public void initialize() {
        bindFields();
        loadRoleOptions();
        loadStatusOptions();
        loadMajors();
        updateStudentFieldsVisibility();
    }

    private void bindFields() {
        if (userIdField != null) userIdField.textProperty().bindBidirectional(formData.userIdProperty());
        if (usernameField != null) usernameField.textProperty().bindBidirectional(formData.usernameProperty());
        if (passwordField != null) passwordField.textProperty().bindBidirectional(formData.passwordProperty());
        if (fullNameField != null) fullNameField.textProperty().bindBidirectional(formData.fullNameProperty());
        if (emailField != null) emailField.textProperty().bindBidirectional(formData.emailProperty());
        if (roleComboBox != null) roleComboBox.valueProperty().bindBidirectional(formData.roleProperty());
        if (majorComboBox != null) majorComboBox.valueProperty().bindBidirectional(formData.majorIdProperty());
        if (statusComboBox != null) statusComboBox.valueProperty().bindBidirectional(formData.statusProperty());
        
        if (roleComboBox != null) {
            roleComboBox.valueProperty().addListener((obs, o, n) -> updateStudentFieldsVisibility());
        }
        if (saveButton != null) saveButton.setOnAction(e -> handleSave());
        if (cancelButton != null) cancelButton.setOnAction(e -> handleCancel());
    }

    // Prefill data from a selected user row
    public void prefillFrom(main.java.model.User user) {
        if (user == null) return;
        formData.userIdProperty().set(user.getUserId());
        formData.usernameProperty().set(user.getUsername());
        formData.fullNameProperty().set(user.getFullName());
        formData.emailProperty().set(user.getEmail());
        String roleText = (user.getRole() == 1) ? "Admin" : "Sinh viên";
        formData.roleProperty().set(roleText);
        // Load student-specific info if role is student
        if (user.getRole() == 0) {
            try {
                var st = studentService.getStudentById(user.getUserId());
                if (st != null) {
                    formData.majorIdProperty().set(st.getMajorId());
                    formData.statusProperty().set(st.getStatus());
                }
            } catch (Exception ignored) {}
        }
        updateStudentFieldsVisibility();
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
            if (isBlank(formData.getMajorId())) sb.append("- Ngành (chỉ SV) chưa chọn\n");
            if (isBlank(formData.getStatus())) sb.append("- Trạng thái (chỉ SV) chưa chọn\n");
        }

        if (sb.length() > 0) throw new IllegalArgumentException(sb.toString().trim());
    }

    @FXML
    private void handleSave() {
        try {
            validateForm();
            boolean isStudent = "Sinh viên".equalsIgnoreCase(formData.getRole());

            if (isStudent) {
                // Preserve existing class value if present
                String uid = formData.getUserId();
                String existingClass = null;
                try {
                    Student existing = studentService.getStudentById(uid);
                    if (existing != null) existingClass = existing.getStudentClass();
                } catch (Exception ignored) {}

                Student s = new Student();
                s.setStudentId(uid);
                s.setUsername(formData.getUsername());
                s.setFullName(formData.getFullName());
                s.setEmail(formData.getEmail());
                s.setRole(0);
                s.setStudentClass(existingClass); // keep unchanged; no class field on edit form
                s.setMajorId(formData.getMajorId());
                s.setStatus(formData.getStatus());

                boolean updated = adminService.updateStudent(s);
                if (updated) {
                    // Update password only if provided
                    if (!isBlank(formData.getPassword())) {
                        adminService.resetPassword(uid, formData.getPassword());
                    }
                    FXUtils.showSuccess("Cập nhật sinh viên thành công");
                    closeWindow();
                } else {
                    FXUtils.showError("Không thể cập nhật sinh viên");
                }
            } else {
                Admin a = new Admin();
                a.setUserId(formData.getUserId());
                a.setUsername(formData.getUsername());
                a.setFullName(formData.getFullName());
                a.setEmail(formData.getEmail());
                a.setRole(1);
                boolean updated = adminService.updateUser(a);
                if (updated) {
                    if (!isBlank(formData.getPassword())) {
                        adminService.resetPassword(formData.getUserId(), formData.getPassword());
                    }
                    FXUtils.showSuccess("Cập nhật admin thành công");
                    closeWindow();
                } else {
                    FXUtils.showError("Không thể cập nhật admin");
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