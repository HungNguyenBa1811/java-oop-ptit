package main.java.controller.admin.course;

import static main.java.utils.FXUtils.closeWindow;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import main.java.model.Course;
import main.java.service.impl.CourseServiceImpl;
import main.java.utils.FXUtils;

public class DeleteCourseFormController {
    @FXML private Label messageLabel;
    @FXML private Button cancelButton;
    @FXML private Button continueButton;

    private final CourseServiceImpl courseService = new CourseServiceImpl();
    private Course targetCourse;

    public void prefillFrom(Course course) {
        this.targetCourse = course;
        String name = (course != null && course.getCourseName() != null && !course.getCourseName().isEmpty())
                ? course.getCourseName() : (course != null ? course.getCourseId() : "");
        if (messageLabel != null) {
            messageLabel.setText("Thao tác này có thể ảnh hưởng đến các lớp học phần và đăng ký liên quan. Môn: " + name);
        }
    }

    @FXML
    private void handleCancel() {
        if(cancelButton != null) closeWindow(cancelButton);
    }

    @FXML
    private void handleContinue() {
        try {
            if (targetCourse == null || targetCourse.getCourseId() == null || targetCourse.getCourseId().trim().isEmpty()) {
                FXUtils.showError("Không xác định được môn học để xoá");
                return;
            }
            boolean ok = courseService.deleteCourse(targetCourse.getCourseId());
            if (ok) {
                FXUtils.showSuccess("Đã xoá môn học thành công");
                if(cancelButton != null) closeWindow(cancelButton);
            } else {
                FXUtils.showError("Xoá môn học thất bại");
            }
        } catch (Exception ex) {
            FXUtils.showError("Xoá môn học thất bại: " + ex.getMessage());
        }
    }
}
