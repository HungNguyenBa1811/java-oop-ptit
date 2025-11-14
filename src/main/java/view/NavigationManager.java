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
import main.java.controller.form.edit.EditUserFormController;
import main.java.controller.form.edit.EditCourseFormController;
import main.java.controller.form.edit.EditCourseOfferingFormController;

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
}