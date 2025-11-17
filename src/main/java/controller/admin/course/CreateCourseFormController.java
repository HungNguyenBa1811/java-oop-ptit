package main.java.controller.admin.course;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import main.java.model.Course;
import main.java.model.Faculty;
import main.java.service.impl.AdminServiceImpl;
import main.java.service.impl.CourseServiceImpl;
import main.java.service.impl.FacultyServiceImpl;
import main.java.utils.FXUtils;

import java.util.List;

public class CreateCourseFormController {
    @FXML private TextField courseIdField;
    @FXML private TextField courseNameField;
    @FXML private TextField creditsField;
    @FXML private ComboBox<String> prerequisiteComboBox;
    @FXML private ComboBox<String> facultyComboBox;
    @FXML private Button saveButton;
    @FXML private Button cancelButton;

    // Data model
    public static class CourseFormData {
        private final StringProperty courseId = new SimpleStringProperty();
        private final StringProperty courseName = new SimpleStringProperty();
        private final StringProperty credits = new SimpleStringProperty();
        private final StringProperty prerequisiteCourseId = new SimpleStringProperty();
        private final StringProperty facultyId = new SimpleStringProperty();
        public StringProperty courseIdProperty() { return courseId; }
        public StringProperty courseNameProperty() { return courseName; }
        public StringProperty creditsProperty() { return credits; }
        public StringProperty prerequisiteCourseIdProperty() { return prerequisiteCourseId; }
        public StringProperty facultyIdProperty() { return facultyId; }
        public String getCourseId() { return courseId.get(); }
        public String getCourseName() { return courseName.get(); }
        public String getCredits() { return credits.get(); }
        public String getPrerequisiteCourseId() { return prerequisiteCourseId.get(); }
        public String getFacultyId() { return facultyId.get(); }
    }

    private final CourseFormData formData = new CourseFormData();
    private final CourseServiceImpl courseService = new CourseServiceImpl();
    private final AdminServiceImpl adminService = new AdminServiceImpl();
    private final FacultyServiceImpl facultyService = new FacultyServiceImpl();

    @FXML
    public void initialize() {
        bindFields();
        bindActions();
        // Load options
        loadFaculties();
        loadPrerequisites();
    }
    
    private void bindActions() {
        if (saveButton != null) saveButton.setOnAction(e -> handleSave());
        if (cancelButton != null) cancelButton.setOnAction(e -> handleCancel());
    }

    private void bindFields() {
        if (courseIdField != null) courseIdField.textProperty().bindBidirectional(formData.courseIdProperty());
        if (courseNameField != null) courseNameField.textProperty().bindBidirectional(formData.courseNameProperty());
        if (creditsField != null) creditsField.textProperty().bindBidirectional(formData.creditsProperty());
        if (prerequisiteComboBox != null) prerequisiteComboBox.valueProperty().bindBidirectional(formData.prerequisiteCourseIdProperty());
        if (facultyComboBox != null) facultyComboBox.valueProperty().bindBidirectional(formData.facultyIdProperty());
    }

    private void loadFaculties() {
        try {
            List<Faculty> faculties = facultyService.getAllFaculties();
            if (facultyComboBox != null) {
                facultyComboBox.setItems(FXCollections.observableArrayList(
                    faculties.stream().map(Faculty::getFacultyId).toList()
                ));
            }
        } catch (Exception e) {
            if (facultyComboBox != null) facultyComboBox.setItems(FXCollections.observableArrayList());
        }
    }

    private void loadPrerequisites() {
        try {
            List<Course> courses = courseService.getAllCourses();
            if (prerequisiteComboBox != null) {
                prerequisiteComboBox.setItems(FXCollections.observableArrayList(
                    courses.stream()
                           .map(Course::getCourseId)
                           .toList()
                ));
            }
        } catch (Exception e) {
            if (prerequisiteComboBox != null) prerequisiteComboBox.setItems(FXCollections.observableArrayList());
        }
    }

    private void validateForm() {
        StringBuilder sb = new StringBuilder();
        if (isBlank(formData.getCourseId())) sb.append("- Mã môn học trống\n");
        if (isBlank(formData.getCourseName())) sb.append("- Tên môn học trống\n");
        if (isBlank(formData.getCredits())) sb.append("- Số tín chỉ trống\n");
        if (isBlank(formData.getFacultyId())) sb.append("- Chưa chọn khoa\n");

        if (!isBlank(formData.getCredits())) {
            try {
                int c = Integer.parseInt(formData.getCredits());
                if (c <= 0) sb.append("- Số tín chỉ phải > 0\n");
            } catch (NumberFormatException e) {
                sb.append("- Số tín chỉ không phải số nguyên\n");
            }
        }

        // Unique check for courseId
        if (!isBlank(formData.getCourseId()) && courseService.getCourseById(formData.getCourseId()) != null) {
            sb.append("- Mã môn học đã tồn tại\n");
        }

        if (sb.length() > 0) throw new IllegalArgumentException(sb.toString().trim());
    }

    @FXML
    private void handleSave() {
        try {
            validateForm();
            // Prepare Course object
            Course course = new Course(
                formData.getCourseId(),
                formData.getCourseName(),
                Integer.parseInt(formData.getCredits()),
                blankToNull(formData.getPrerequisiteCourseId())
            );

            // Create using admin service (needs facultyId)
            Course created = adminService.createCourse(course, formData.getFacultyId());
            if (created != null) {
                FXUtils.showSuccess("Tạo môn học thành công");
                closeWindow();
            } else {
                FXUtils.showError("Không thể tạo môn học");
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

    private String blankToNull(String s) {
        return isBlank(s) ? null : s.trim();
    }
}
