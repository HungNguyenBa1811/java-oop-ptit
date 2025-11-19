package main.java.controller.admin.course;

import static main.java.utils.FXUtils.closeWindow;
import static main.java.utils.GenericUtils.isBlank;
import static main.java.utils.GenericUtils.blankToNull;

import java.util.List;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import main.java.dto.course.CourseFormData;
import main.java.model.Course;
import main.java.model.Faculty;
import main.java.service.impl.AdminServiceImpl;
import main.java.service.impl.CourseServiceImpl;
import main.java.service.impl.FacultyServiceImpl;
import main.java.utils.FXUtils;

public class CreateCourseFormController {
    @FXML private TextField courseIdField;
    @FXML private TextField courseNameField;
    @FXML private TextField creditsField;
    @FXML private ComboBox<String> prerequisiteComboBox;
    @FXML private ComboBox<String> facultyComboBox;
    @FXML private Button saveButton;
    @FXML private Button cancelButton;

    private final CourseFormData formData = new CourseFormData();
    private final CourseServiceImpl courseService = new CourseServiceImpl();
    private final AdminServiceImpl adminService = new AdminServiceImpl();
    private final FacultyServiceImpl facultyService = new FacultyServiceImpl();

    @FXML
    public void initialize() {
        bindFields();
        loadFaculties();
        loadPrerequisites();
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
                    faculties.stream().map(f -> f.getFacultyId() + " - " + f.getFacultyName()).toList()
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

        if (!isBlank(formData.getCourseId()) && courseService.getCourseById(formData.getCourseId()) != null) {
            sb.append("- Mã môn học đã tồn tại\n");
        }

        if (sb.length() > 0) throw new IllegalArgumentException(sb.toString().trim());
    }

    @FXML
    private void handleSave() {
        try {
            validateForm();
            Course course = new Course(
                formData.getCourseId(),
                formData.getCourseName(),
                Integer.parseInt(formData.getCredits()),
                blankToNull(formData.getPrerequisiteCourseId())
            );

            // Extract facultyId from "ID - Name" format
            String facultyId = extractFacultyId(formData.getFacultyId());
            Course created = adminService.createCourse(course, facultyId);
            if (created != null) {
                FXUtils.showSuccess("Tạo môn học thành công");
                if(cancelButton != null) closeWindow(cancelButton);
            } else {
                FXUtils.showError("Không thể tạo môn học");
            }
        } catch (Exception ex) {
            FXUtils.showError("Lưu thất bại: " + ex.getMessage());
        }
    }

    @FXML
    private void handleCancel() {
        if(cancelButton != null) closeWindow(cancelButton);
    }

    private String extractFacultyId(String facultyDisplay) {
        if (isBlank(facultyDisplay)) return null;
        int idx = facultyDisplay.indexOf(" - ");
        return idx > 0 ? facultyDisplay.substring(0, idx).trim() : facultyDisplay.trim();
    }
}
