package main.java.controller.form.read;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import main.java.model.Course;
import main.java.model.Faculty;
import main.java.service.impl.FacultyServiceImpl;

public class ReadCourseFormController {
    @FXML private Label codeLabel;
    @FXML private Label nameLabel;
    @FXML private Label creditsLabel;
    @FXML private Label facultyLabel;
    @FXML private Button editButton;
    @FXML private Button closeButton;

    private final FacultyServiceImpl facultyService = new FacultyServiceImpl();
    private Course currentCourse;

    public void prefillFrom(Course course) {
        this.currentCourse = course;
        if (course == null) return;
        if (codeLabel != null) codeLabel.setText(safe(course.getCourseId()));
        if (nameLabel != null) nameLabel.setText(safe(course.getCourseName()));
        if (creditsLabel != null) creditsLabel.setText(String.valueOf(course.getCredits()));
        String facultyText = "-";
        try {
            String fid = course.getFacultyId();
            if (fid != null && !fid.trim().isEmpty()) {
                Faculty f = facultyService.getFacultyById(fid);
                facultyText = (f != null && f.getFacultyName() != null) ? f.getFacultyName() : fid;
            }
        } catch (Exception ignored) { }
        if (facultyLabel != null) facultyLabel.setText(facultyText);
    }
    @FXML
    private void handleClose() {
        if (closeButton != null && closeButton.getScene() != null) {
            Stage st = (Stage) closeButton.getScene().getWindow();
            if (st != null) st.close();
        }
    }

    private String safe(String s) { return s == null ? "-" : s; }
}
