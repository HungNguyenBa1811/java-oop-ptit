package main.java.controller.admin.user;
import static main.java.utils.FXUtils.closeWindow;
import static main.java.utils.GenericUtils.isBlank;

import java.util.List;
import java.util.ArrayList;
import java.io.File;
import main.java.utils.CsvUtils;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import main.java.dto.user.UserFormData;
import main.java.model.Admin;
import main.java.model.Faculty;
import main.java.model.Major;
import main.java.model.Student;
import main.java.service.impl.AdminServiceImpl;
import main.java.service.impl.FacultyServiceImpl;
import main.java.service.impl.MajorServiceImpl;
import main.java.service.impl.StudentServiceImpl;
import main.java.utils.FXUtils;

public class CreateUserFormController {
    @FXML private Text formTitle;
    @FXML private TextField userIdField;
    @FXML private TextField usernameField;
    @FXML private TextField fullNameField;
    @FXML private PasswordField passwordField;
    @FXML private TextField emailField;
    @FXML private ComboBox<String> roleComboBox;
    @FXML private TextField classField;
    @FXML private ComboBox<String> facultyComboBox;
    @FXML private ComboBox<String> majorComboBox;
    @FXML private ComboBox<String> statusComboBox;
    @FXML private Button cancelButton;
    @FXML private Button saveButton;
    @FXML private Button importButton;
    @FXML private Button exportButton;
    @FXML private Label classLabel;
    @FXML private Label majorLabel;
    @FXML private Label statusLabel;

    private final UserFormData formData = new UserFormData();
    private final AdminServiceImpl adminService = new AdminServiceImpl();
    private final FacultyServiceImpl facultyService = new FacultyServiceImpl();
    private final StudentServiceImpl studentService = new StudentServiceImpl();
    private final MajorServiceImpl majorService = new MajorServiceImpl();

    @FXML
    public void initialize() {
        bindFields();
        loadRoleOptions();
        loadStatusOptions();
        loadFaculties();
        loadMajors();
        if (roleComboBox != null) {
            roleComboBox.valueProperty().addListener((obs, o, n) -> {
                updateStudentFieldsVisibility();
                try {
                    if (n != null && n.equalsIgnoreCase("Sinh viên")) {
                        if (userIdField != null && (userIdField.getText() == null || userIdField.getText().isBlank())) {
                            userIdField.setText("U");
                            formData.userIdProperty().set("U");
                        }
                    }
                } catch (Exception ignored) {}
            });
        }
        updateStudentFieldsVisibility();
        if (saveButton != null) saveButton.setOnAction(e -> handleSave());
        if (cancelButton != null) cancelButton.setOnAction(e -> handleCancel());
        if (importButton != null) importButton.setOnAction(e -> handleImportCsv());
        if (exportButton != null) exportButton.setOnAction(e -> handleExportCsv());
    }

    private void bindFields() {
        if (userIdField != null) userIdField.textProperty().bindBidirectional(formData.userIdProperty());
        if (usernameField != null) usernameField.textProperty().bindBidirectional(formData.usernameProperty());
        if (passwordField != null) passwordField.textProperty().bindBidirectional(formData.passwordProperty());
        if (fullNameField != null) fullNameField.textProperty().bindBidirectional(formData.fullNameProperty());
        if (emailField != null) emailField.textProperty().bindBidirectional(formData.emailProperty());
        if (roleComboBox != null) roleComboBox.valueProperty().bindBidirectional(formData.roleProperty());
        if (classField != null) classField.textProperty().bindBidirectional(formData.studentClassProperty());
        if (facultyComboBox != null) facultyComboBox.valueProperty().bindBidirectional(formData.facultyIdProperty());
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
            List<Major> majors = majorService.getAllMajors();
            if (majorComboBox != null) {
                majorComboBox.setItems(FXCollections.observableArrayList(
                    majors.stream().map(m -> m.getMajorId() + " - " + m.getMajorName()).toList()
                ));
            }
        } catch (Exception e) {
            if (majorComboBox != null) majorComboBox.setItems(FXCollections.observableArrayList());
        }
    }

    private void loadFaculties() {
        try {
            List<Faculty> faculties = facultyService.getAllFaculties();
            if (facultyComboBox != null) {
                facultyComboBox.setItems(FXCollections.observableArrayList(
                    faculties.stream().map(f -> f.getFacultyId() + " - " + f.getFacultyName()).toList()
                ));
            }
        } catch (Exception e) {
            if (facultyComboBox != null) facultyComboBox.setItems(FXCollections.observableArrayList());
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
        if (facultyComboBox != null) {
            facultyComboBox.setDisable(!isStudent);
            facultyComboBox.setManaged(isStudent);
            facultyComboBox.setVisible(isStudent);
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
        if (importButton != null) {
            importButton.setDisable(!isStudent);
            importButton.setManaged(isStudent);
            importButton.setVisible(isStudent);
        }
        if (exportButton != null) {
            exportButton.setDisable(!isStudent);
            exportButton.setManaged(isStudent);
            exportButton.setVisible(isStudent);
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
            if (isBlank(formData.getFacultyId())) sb.append("- Khoa (chỉ SV) chưa chọn\n");
            if (isBlank(formData.getStatus())) sb.append("- Trạng thái (chỉ SV) chưa chọn\n");
            if (isBlank(formData.getPassword())) sb.append("- Mật khẩu trống\n");
        } else { 
            // Admin
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
                // Extract majorId from display "ID - Name" if present
                String majorDisplay = formData.getMajorId();
                String majorId = null;
                if (majorDisplay != null) {
                    int idxm = majorDisplay.indexOf(" - ");
                    majorId = idxm > 0 ? majorDisplay.substring(0, idxm).trim() : majorDisplay.trim();
                }
                s.setMajorId(majorId);
                // Extract facultyId if displayed as "ID - Name"
                String facultyDisplay = formData.getFacultyId();
                String facultyId = null;
                if (facultyDisplay != null) {
                    int idx = facultyDisplay.indexOf(" - ");
                    facultyId = idx > 0 ? facultyDisplay.substring(0, idx).trim() : facultyDisplay.trim();
                }
                s.setFacultyId(facultyId);
                s.setStatus(formData.getStatus());
                var created = adminService.registerStudent(s, formData.getPassword(), facultyId);
                if (created != null) {
                    FXUtils.showSuccess("Tạo sinh viên thành công");
                    if(cancelButton != null) closeWindow(cancelButton);
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
                    if(cancelButton != null) closeWindow(cancelButton);
                } else {
                    FXUtils.showError("Không thể tạo admin");
                }
            }
        } catch (Exception ex) {
            FXUtils.showError("Lưu thất bại: " + ex.getMessage());
        }
    }

    @FXML
    private void handleImportCsv() {
        try {
            File f = CsvUtils.chooseOpenCsv(saveButton, "Chọn file CSV sinh viên (.csv)");
            if (f == null) return;

            List<String> lines = CsvUtils.readLines(f);
            if (lines.isEmpty()) {
                FXUtils.showError("File rỗng");
                return;
            }

            int success = 0;
            List<String> errors = new ArrayList<>();
            int rowNo = 0;

            boolean firstIsHeader = lines.get(0).toLowerCase().contains("userid") || lines.get(0).toLowerCase().contains("username");
            for (String line : lines) {
                rowNo++;
                if (firstIsHeader && rowNo == 1) continue;
                if (line == null || line.trim().isEmpty()) continue;

                String[] cols = line.split(";", -1);
                // Expected: userId;username;fullName;email;studentClass;majorId;facultyId;status;password
                if (cols.length < 9) {
                    errors.add("Dòng " + rowNo + ": Số cột không hợp lệ (cần 9). Detected:" + cols.length);
                    continue;
                }

                UserFormData row = new UserFormData();
                row.userIdProperty().set(cols[0].trim());
                row.usernameProperty().set(cols[1].trim());
                row.fullNameProperty().set(cols[2].trim());
                row.emailProperty().set(cols[3].trim());
                row.studentClassProperty().set(cols[4].trim());
                row.majorIdProperty().set(cols[5].trim());
                row.facultyIdProperty().set(cols[6].trim());
                row.statusProperty().set(cols[7].trim());
                row.passwordProperty().set(cols[8].trim());

                StringBuilder sb = new StringBuilder();
                if (isBlank(row.getUserId())) sb.append("userId trống; ");
                if (isBlank(row.getUsername())) sb.append("username trống; ");
                if (isBlank(row.getFullName())) sb.append("fullName trống; ");
                if (isBlank(row.getEmail())) sb.append("email trống; ");
                if (isBlank(row.getStudentClass())) sb.append("class trống; ");
                if (isBlank(row.getMajorId())) sb.append("major trống; ");
                if (isBlank(row.getFacultyId())) sb.append("faculty trống; ");
                if (isBlank(row.getStatus())) sb.append("status trống; ");
                if (isBlank(row.getPassword())) sb.append("password trống; ");

                if (sb.length() > 0) {
                    errors.add("Dòng " + rowNo + ": " + sb.toString());
                    continue;
                }

                Student s = new Student();
                s.setStudentId(row.getUserId());
                s.setUsername(row.getUsername());
                s.setFullName(row.getFullName());
                s.setEmail(row.getEmail());
                s.setRole(0);
                s.setStudentClass(row.getStudentClass());
                s.setMajorId(row.getMajorId());
                s.setFacultyId(row.getFacultyId());
                s.setStatus(row.getStatus());

                try {
                    var created = adminService.registerStudent(s, row.getPassword(), row.getFacultyId());
                    if (created != null) success++;
                    else errors.add("Dòng " + rowNo + ": Đăng ký thất bại.");
                } catch (Exception ex) {
                    errors.add("Dòng " + rowNo + ": Lỗi khi tạo - " + ex.getMessage());
                }
            }

            StringBuilder result = new StringBuilder();
            result.append("Import hoàn tất. Thành công: ").append(success).append(", Lỗi: ").append(errors.size());
            FXUtils.showSuccess(result.toString());
            if (!errors.isEmpty()) {
                String errText = String.join("\n", errors);
                FXUtils.showError("Chi tiết lỗi:\n" + errText);
            }
        } catch (Exception ex) {
            FXUtils.showError("Import thất bại: " + ex.getMessage());
        }
    }

    @FXML
    private void handleExportCsv() {
        try {
            File target = CsvUtils.chooseSaveCsv(saveButton, "students_export.csv", "Lưu file CSV xuất sinh viên");
            if (target == null) return;

            List<Student> students = studentService.getAllStudents();
            String[] header = new String[] {"userId","username","fullName","email","studentClass","majorId","facultyId","status","password"};
            List<String[]> rows = new ArrayList<>();
            for (Student s : students) {
                rows.add(new String[] {
                    s.getStudentId(), s.getUsername(), s.getFullName(), s.getEmail(), s.getStudentClass(), s.getMajorId(), s.getFacultyId(), s.getStatus(), "0192023a7bbd73250516f069df18b500"
                });
            }
            CsvUtils.writeCsv(target, header, rows);
            FXUtils.showSuccess("Xuất CSV thành công: " + target.getAbsolutePath());
        } catch (Exception ex) {
            FXUtils.showError("Export thất bại: " + ex.getMessage());
        }
    }

    // use CsvUtils.escape when needed

    @FXML
    private void handleCancel() {
        if(cancelButton != null) closeWindow(cancelButton);
    }
}
