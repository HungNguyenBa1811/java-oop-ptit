package main.java.view;

import java.io.IOException;
import java.util.function.Consumer;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import main.java.controller.admin.course.DeleteCourseFormController;
import main.java.controller.admin.course.EditCourseFormController;
import main.java.controller.admin.course.ReadCourseFormController;
import main.java.controller.admin.courseOffering.DeleteCourseOfferingFormController;
import main.java.controller.admin.courseOffering.EditCourseOfferingFormController;
import main.java.controller.admin.courseOffering.ReadCourseOfferingFormController;
import main.java.controller.admin.user.DeleteUserFormController;
import main.java.controller.admin.user.EditUserFormController;
import main.java.controller.admin.user.ReadUserFormController;
import main.java.model.Course;
import main.java.model.CourseOffering;
import main.java.model.User;
import main.java.utils.FXUtils;

public class NavigationManager {
    private final Stage stage;
    public NavigationManager(Stage stage) {
        this.stage = stage;
    }
    // ================= Main Screen Navigation =================
    public void showLoginScreen() {
        showScreen("fxml/login.fxml", "Đăng nhập");
    }
    public void showStudentDashboard() {
        showScreen("fxml/studentdashboard.fxml", "Bảng điều khiển");
    }

    public void showStudentCalendar() {
        showModal("fxml/studentcalendar.fxml", "Thời khóa biểu", null);
    }

    public void showAdminDashboard() {
        showScreen("fxml/admindashboard.fxml", "Bảng điều khiển quản trị viên");
    }
    // ================= Popup helpers =================
    public void showCourseOfferingAddForm() {
        showModal("fxml/courseOffering/courseOfferingForm.fxml", "Thêm Lớp Học Phần", null);
    }
    public void showCourseAddForm() {
        showModal("fxml/course/courseForm.fxml", "Thêm Môn Học", null);
    }
    public void showUserAddForm() {
        showModal("fxml/user/userForm.fxml", "Thêm Người Dùng", null);
    }
    public void showCourseOfferingEditForm(CourseOffering offering) {
        showModal("fxml/courseOffering/editCourseOffering.fxml", "Sửa Lớp Học Phần", 
            (EditCourseOfferingFormController c) -> {
                if (offering != null) c.prefillFrom(offering);
            });
    }
    public void showCourseEditForm(Course course) {
        showModal("fxml/course/editCourse.fxml", "Sửa Môn Học", 
            (EditCourseFormController c) -> {
                if (course != null) c.prefillFrom(course);
            });
    }
    public void showUserEditForm(User user) {
        showModal("fxml/user/editUser.fxml", "Sửa Người dùng", 
            (EditUserFormController c) -> {
                if (user != null) c.prefillFrom(user);
            });
    }
    public void showUserDetailForm(User user) {
        showModal("fxml/user/userDetail.fxml", "Chi tiết Người dùng", 
            (ReadUserFormController c) -> {
                if (user != null) c.prefillFrom(user);
            });
    }
    public void showUserDeleteConfirm(User user) {
        showModal("fxml/user/deleteUser.fxml", "Xóa Người dùng", 
            (DeleteUserFormController c) -> {
                if (user != null) c.prefillFrom(user);
            });
    }
    public void showCourseDetailForm(Course course) {
        showModal("fxml/course/courseDetail.fxml", "Chi tiết Môn học", 
            (ReadCourseFormController c) -> {
                if (course != null) c.prefillFrom(course);
            });
    }
    public void showCourseDeleteConfirm(Course course) {
        showModal("fxml/course/deleteCourse.fxml", "Xoá Môn học", 
            (DeleteCourseFormController c) -> {
                if (course != null) c.prefillFrom(course);
            });
    }
    public void showCourseOfferingDetailForm(CourseOffering offering) {
        showModal("fxml/courseOffering/courseOfferingDetail.fxml", "Chi tiết Lớp học phần", 
            (ReadCourseOfferingFormController c) -> {
                if (offering != null) c.prefillFrom(offering);
            });
    }
    public void showCourseOfferingDeleteConfirm(CourseOffering offering) {
        showModal("fxml/courseOffering/deleteCourseOffering.fxml", "Xoá Lớp học phần", 
            (DeleteCourseOfferingFormController c) -> {
                if (offering != null) c.prefillFrom(offering);
            });
    }
    // Helper
    private <T> void showScreen(String fxmlPath, String title) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(FXUtils.fxml(fxmlPath));
            Scene scene = new Scene(fxmlLoader.load());
            String cssPath = getClass().getResource("/main/resources/css/style.css").toExternalForm();
            scene.getStylesheets().add(cssPath);
            stage.setTitle(title);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            FXUtils.showError("Không thể mở màn hình: " + e.getMessage());
            e.printStackTrace();
        }
    }
    private <T> void showModal(String fxmlPath, String title, Consumer<T> controllerPrefill) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(FXUtils.fxml(fxmlPath));
            Parent root = fxmlLoader.load();
            // Apply controller prefill if provided
            if (controllerPrefill != null) {
                T controller = fxmlLoader.getController();
                if (controller != null) {
                    controllerPrefill.accept(controller);
                }
            }
            Stage modalStage = new Stage();
            modalStage.setTitle(title);
            modalStage.initModality(Modality.APPLICATION_MODAL);
            modalStage.setScene(new Scene(root));
            FXUtils.setAppIcon(modalStage);
            modalStage.showAndWait();
        } catch (IOException e) {
            FXUtils.showError("Không thể mở form: " + e.getMessage());
            e.printStackTrace();
        }
    }
}