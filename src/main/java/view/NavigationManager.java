package main.java.view;

import java.io.IOException;
import java.util.function.Consumer;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
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
import main.java.utils.TetAudioManager;
import main.java.utils.TetDecorationManager;

public class NavigationManager {
    private final Stage stage;
    public NavigationManager(Stage stage) {
        this.stage = stage;
    }
    // ================= Main Screen Navigation =================
    public void showLoginScreen() {
        showScreen("fxml/login.fxml", "Đăng nhập", false);
    }
    public void showStudentDashboard() {
        // 🎵 Play nhạc Tết khi đăng nhập thành công
        TetAudioManager.getInstance().play();
        showScreen("fxml/studentdashboard.fxml", "Bảng điều khiển", true);
    }

    public void showStudentCalendar() {
        showModal("fxml/studentcalendar.fxml", "Thời khóa biểu", null);
    }

    public void showAdminDashboard() {
        // 🎵 Play nhạc Tết khi đăng nhập thành công
        TetAudioManager.getInstance().play();
        showScreen("fxml/admindashboard.fxml", "Bảng điều khiển quản trị viên", true);
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
    private <T> void showScreen(String fxmlPath, String title, boolean withTetDecoration) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(FXUtils.fxml(fxmlPath));
            Parent root = fxmlLoader.load();
            
            Scene scene;
            if (withTetDecoration) {
                // Wrap với decoration Tết (pháo hoa, bánh chưng)
                StackPane wrapper = new StackPane();
                Pane decorations = TetDecorationManager.createTetDecorationLayer(1366, 768);
                wrapper.getChildren().addAll(root, decorations);
                scene = new Scene(wrapper);
            } else {
                scene = new Scene(root);
            }
            
            applyStylesheets(scene);
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
            Scene scene = new Scene(root);
            applyStylesheets(scene);
            modalStage.setScene(scene);
            FXUtils.setAppIcon(modalStage);
            modalStage.showAndWait();
        } catch (IOException e) {
            FXUtils.showError("Không thể mở form: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Apply CSS stylesheets to scene.
     * - style.css: Main theme (always loaded)
     * - tet.css: Tết theme plugin (loaded as overlay)
     */
    private void applyStylesheets(Scene scene) {
        try {
            // Main theme - always load
            String mainCss = getClass().getResource("/main/resources/css/style.css").toExternalForm();
            scene.getStylesheets().add(mainCss);
            
            // Tết theme plugin - load as overlay (CSS cascade: later stylesheets override earlier ones)
            String tetCss = getClass().getResource("/main/resources/css/tet.css").toExternalForm();
            if (tetCss != null) {
                scene.getStylesheets().add(tetCss);
            }
        } catch (Exception e) {
            System.err.println("Warning: Could not load some stylesheets: " + e.getMessage());
        }
    }
}