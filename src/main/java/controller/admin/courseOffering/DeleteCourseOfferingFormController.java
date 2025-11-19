package main.java.controller.admin.courseOffering;

import static main.java.utils.GenericUtils.safeParseString;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import main.java.model.Course;
import main.java.model.CourseOffering;
import main.java.model.Faculty;
import main.java.service.impl.CourseOfferingServiceImpl;
import main.java.service.impl.CourseServiceImpl;
import main.java.service.impl.FacultyServiceImpl;
import main.java.utils.FXUtils;

public class DeleteCourseOfferingFormController {
    @FXML private Label courseNameLabel;
    @FXML private Label offeringCodeLabel;
    @FXML private Label lecturerLabel;
    @FXML private Label facultyLabel;
    @FXML private Button cancelButton;
    @FXML private Button confirmDeleteButton;

    private final CourseOfferingServiceImpl offeringService = new CourseOfferingServiceImpl();
    private final CourseServiceImpl courseService = new CourseServiceImpl();
    private final FacultyServiceImpl facultyService = new FacultyServiceImpl();
    private CourseOffering targetOffering;

    public void prefillFrom(CourseOffering offering) {
        this.targetOffering = offering;
        String courseText = "-";
        try {
            if (offering != null && offering.getCourseId() != null) {
                Course c = courseService.getCourseById(offering.getCourseId());
                courseText = (c != null && c.getCourseName() != null) ? c.getCourseName() : offering.getCourseId();
            }
        } catch (Exception ignored) { }
        if (courseNameLabel != null) courseNameLabel.setText(courseText);
        if (offeringCodeLabel != null) offeringCodeLabel.setText(offering != null ? safeParseString(offering.getCourseOfferingId()) : "-");
        if (lecturerLabel != null) lecturerLabel.setText(offering != null ? safeParseString(offering.getInstructor()) : "-");
        
        String facultyText = "-";
        try {
            if (offering != null && offering.getFacultyId() != null) {
                Faculty f = facultyService.getFacultyById(offering.getFacultyId());
                facultyText = (f != null && f.getFacultyName() != null) ? f.getFacultyName() : offering.getFacultyId();
            }
        } catch (Exception ignored) { }
        if (facultyLabel != null) facultyLabel.setText(facultyText);
    }

    @FXML
    private void handleCancel() { 
        close(); 
    }

    @FXML
    private void handleConfirm() {
        try {
            if (targetOffering == null || targetOffering.getCourseOfferingId() == null || targetOffering.getCourseOfferingId().trim().isEmpty()) {
                FXUtils.showError("Không xác định được lớp học phần để xoá");
                return;
            }
            boolean ok = offeringService.deleteCourseOffering(targetOffering.getCourseOfferingId());
            if (ok) {
                FXUtils.showSuccess("Đã xoá lớp học phần thành công");
                close();
            } else {
                FXUtils.showError("Xoá lớp học phần thất bại");
            }
        } catch (Exception ex) {
            FXUtils.showError("Xoá lớp học phần thất bại: " + ex.getMessage());
        }
    }

    private void close() {
        if (cancelButton != null && cancelButton.getScene() != null) {
            Stage st = (Stage) cancelButton.getScene().getWindow();
            if (st != null) st.close();
        }
    }
}
