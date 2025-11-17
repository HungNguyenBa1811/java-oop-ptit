package main.java.view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import main.java.utils.FXUtils;
import main.java.model.User;
import main.java.model.Course;
import main.java.model.CourseOffering;
import main.java.controller.admin.course.DeleteCourseFormController;
import main.java.controller.admin.course.EditCourseFormController;
import main.java.controller.admin.course.ReadCourseFormController;
import main.java.controller.admin.courseOffering.DeleteCourseOfferingFormController;
import main.java.controller.admin.courseOffering.EditCourseOfferingFormController;
import main.java.controller.admin.courseOffering.ReadCourseOfferingFormController;
import main.java.controller.admin.user.DeleteUserFormController;
import main.java.controller.admin.user.EditUserFormController;
import main.java.controller.admin.user.ReadUserFormController;

import java.io.IOException;

public class NavigationManager {
    private final Stage stage;

    public NavigationManager(Stage stage) {
        this.stage = stage;
    }

    public void showLoginScreen() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(FXUtils.fxml("fxml/login.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        String cssPath = getClass().getResource("/main/resources/css/style.css").toExternalForm();
        scene.getStylesheets().add(cssPath);
        stage.setTitle("Đăng nhập");
        stage.setScene(scene);
        stage.show();
    }

    public void showStudentDashboard() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(FXUtils.fxml("fxml/studentdashboard.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        String cssPath = getClass().getResource("/main/resources/css/style.css").toExternalForm();
        scene.getStylesheets().add(cssPath);
        stage.setTitle("Bảng điều khiển");
        stage.setScene(scene);
        stage.show();
    }

    public void showAdminDashboard() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(FXUtils.fxml("fxml/admindashboard.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        String cssPath = getClass().getResource("/main/resources/css/style.css").toExternalForm();
        scene.getStylesheets().add(cssPath);
        stage.setTitle("Bảng điều khiển quản trị viên");
        stage.setScene(scene);
        stage.show();
    }

    // ================= Popup helpers =================
    public void showCourseOfferingAddForm() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(FXUtils.fxml("fxml/courseOffering/courseOfferingForm.fxml"));
        Parent root = fxmlLoader.load();
        Stage stage = new Stage();
        stage.setTitle("Thêm Lớp Học Phần");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(new Scene(root));
        stage.showAndWait();
    }
    public void showCourseAddForm() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(FXUtils.fxml("fxml/course/courseForm.fxml"));
        Parent root = fxmlLoader.load();
        Stage stage = new Stage();
        stage.setTitle("Thêm Lớp Học Phần");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(new Scene(root));
        stage.showAndWait();
    }
    public void showUserAddForm() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(FXUtils.fxml("fxml/user/userForm.fxml"));
        Parent root = fxmlLoader.load();
        Stage stage = new Stage();
        stage.setTitle("Thêm Lớp Học Phần");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(new Scene(root));
        stage.showAndWait();
    }

    public void showCourseOfferingEditForm(CourseOffering offering) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(FXUtils.fxml("fxml/courseOffering/editCourseOffering.fxml"));
        Parent root = fxmlLoader.load();
        EditCourseOfferingFormController controller = fxmlLoader.getController();
        if (controller != null && offering != null) {
            controller.prefillFrom(offering);
        }
        Stage stage = new Stage();
        stage.setTitle("Sửa Lớp Học Phần");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(new Scene(root));
        stage.showAndWait();
    }
    
    public void showCourseEditForm(Course course) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(FXUtils.fxml("fxml/course/editCourse.fxml"));
        Parent root = fxmlLoader.load();
        EditCourseFormController controller = fxmlLoader.getController();
        if (controller != null && course != null) {
            controller.prefillFrom(course);
        }
        Stage stage = new Stage();
        stage.setTitle("Sửa Môn Học");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(new Scene(root));
        stage.showAndWait();
    }

    public void showUserEditForm(User user) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(FXUtils.fxml("fxml/user/editUser.fxml"));
        Parent root = fxmlLoader.load();
        EditUserFormController controller = fxmlLoader.getController();
        if (controller != null && user != null) {
            controller.prefillFrom(user);
        }
        Stage stage = new Stage();
        stage.setTitle("Sửa Người dùng");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(new Scene(root));
        stage.showAndWait();
    }

    public void showUserDetailForm(User user) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(FXUtils.fxml("fxml/user/userDetail.fxml"));
        Parent root = fxmlLoader.load();
        ReadUserFormController controller = fxmlLoader.getController();
        if (controller != null && user != null) {
            controller.prefillFrom(user);
        }
        Stage stage = new Stage();
        stage.setTitle("Chi tiết Người dùng");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(new Scene(root));
        stage.showAndWait();
    }

    public void showUserDeleteConfirm(User user) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(FXUtils.fxml("fxml/user/deleteUser.fxml"));
        Parent root = fxmlLoader.load();
        DeleteUserFormController controller = fxmlLoader.getController();
        if (controller != null && user != null) {
            controller.prefillFrom(user);
        }
        Stage stage = new Stage();
        stage.setTitle("Xóa Người dùng");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(new Scene(root));
        stage.showAndWait();
    }

    public void showCourseDetailForm(Course course) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(FXUtils.fxml("fxml/course/courseDetail.fxml"));
        Parent root = fxmlLoader.load();
        ReadCourseFormController controller = fxmlLoader.getController();
        if (controller != null && course != null) {
            controller.prefillFrom(course);
        }
        Stage stage = new Stage();
        stage.setTitle("Chi tiết Môn học");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(new Scene(root));
        stage.showAndWait();
    }

    public void showCourseDeleteConfirm(Course course) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(FXUtils.fxml("fxml/course/deleteCourse.fxml"));
        Parent root = fxmlLoader.load();
        DeleteCourseFormController controller = fxmlLoader.getController();
        if (controller != null && course != null) {
            controller.prefillFrom(course);
        }
        Stage stage = new Stage();
        stage.setTitle("Xoá Môn học");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(new Scene(root));
        stage.showAndWait();
    }

    public void showCourseOfferingDetailForm(CourseOffering offering) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(FXUtils.fxml("fxml/courseOffering/courseOfferingDetail.fxml"));
        Parent root = fxmlLoader.load();
        ReadCourseOfferingFormController controller = fxmlLoader.getController();
        if (controller != null && offering != null) {
            controller.prefillFrom(offering);
        }
        Stage stage = new Stage();
        stage.setTitle("Chi tiết Lớp học phần");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(new Scene(root));
        stage.showAndWait();
    }

    public void showCourseOfferingDeleteConfirm(CourseOffering offering) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(FXUtils.fxml("fxml/courseOffering/deleteCourseOffering.fxml"));
        Parent root = fxmlLoader.load();
        DeleteCourseOfferingFormController controller = fxmlLoader.getController();
        if (controller != null && offering != null) {
            controller.prefillFrom(offering);
        }
        Stage stage = new Stage();
        stage.setTitle("Xoá Lớp học phần");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(new Scene(root));
        stage.showAndWait();
    }
}