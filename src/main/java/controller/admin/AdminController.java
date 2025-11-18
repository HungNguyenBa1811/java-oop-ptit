package main.java.controller.admin;

import java.io.IOException;
 
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.text.Text;

import main.java.dto.admin.AdminDashboardCourseRow;
import main.java.dto.admin.AdminDashboardOfferingRow;
import main.java.dto.admin.AdminDashboardUserRow;

import main.java.model.Course;
import main.java.model.CourseOffering;
import main.java.model.User;

import main.java.service.impl.RoomServiceImpl;
import main.java.service.impl.SemesterServiceImpl;
import main.java.service.AuthService;
import main.java.service.impl.AuthServiceImpl;
import main.java.service.impl.CourseOfferingScheduleServiceImpl;
import main.java.service.impl.CourseOfferingServiceImpl;
import main.java.service.impl.CourseServiceImpl;
import main.java.service.impl.UserServiceImpl;
import main.java.utils.FXUtils;
 
import main.java.utils.AuthUtils;
import main.java.utils.AdminControllerUtils;
import main.java.utils.helper.AdminControllerHelper;
 
import main.java.view.NavigationManager;

import static main.java.utils.GenericUtils.getStageFromSource;
import static main.java.utils.TableUtils.setupTable;
import static main.java.utils.AuthUtils.appLogout;

/**
 * AdminController - Xử lý các request liên quan đến Admin
 * Chịu trách nhiệm: Course offering management, registration management
 */
public class AdminController {
    @FXML private Text adminNameText;
    @FXML private Button logoutBtn;
    @FXML private TabPane tabPane;
    @FXML private Tab offeringTab;
    @FXML private Tab courseTab;
    @FXML private Tab userTab;
    // Course Offering
    @FXML private TableView<AdminDashboardOfferingRow> offeringTable;
    @FXML private TableColumn<AdminDashboardOfferingRow, String> colOfferingCourseOfferingId;
    @FXML private TableColumn<AdminDashboardOfferingRow, String> colOfferingCourseId;
    @FXML private TableColumn<AdminDashboardOfferingRow, String> colOfferingCourseName;
    @FXML private TableColumn<AdminDashboardOfferingRow, Integer> colOfferingCredits;
    @FXML private TableColumn<AdminDashboardOfferingRow, String> colOfferingInstructor;
    @FXML private TableColumn<AdminDashboardOfferingRow, String> colOfferingMajor;
    @FXML private TableColumn<AdminDashboardOfferingRow, String> colOfferingSemesterId;
    @FXML private TableColumn<AdminDashboardOfferingRow, String> colOfferingSchedule;
    @FXML private TableColumn<AdminDashboardOfferingRow, String> colOfferingRoomId;
    @FXML private TableColumn<AdminDashboardOfferingRow, String> colOfferingMaxCapacity;
    @FXML private TableColumn<AdminDashboardOfferingRow, String> colOfferingRemaining;
    @FXML private Button offeringReloadBtn;
    @FXML private Button offeringAddBtn;
    @FXML private Button offeringEditBtn;
    @FXML private Button offeringDeleteBtn;
    @FXML private Button offeringDetailBtn;
    // Course
    @FXML private TableView<AdminDashboardCourseRow> courseTable;
    @FXML private TableColumn<AdminDashboardCourseRow, String> colCourseCourseId;
    @FXML private TableColumn<AdminDashboardCourseRow, String> colCourseCourseName;
    @FXML private TableColumn<AdminDashboardCourseRow, Integer> colCourseCredits;
    @FXML private TableColumn<AdminDashboardCourseRow, String> colFaculty;
    @FXML private Button courseReloadBtn;
    @FXML private Button courseAddBtn;
    @FXML private Button courseEditBtn;
    @FXML private Button courseDeleteBtn;
    @FXML private Button courseDetailBtn;
    // User
    @FXML private TableView<AdminDashboardUserRow> userTable;
    @FXML private TableColumn<AdminDashboardUserRow, String> colUserUserId;
    @FXML private TableColumn<AdminDashboardUserRow, String> colUserUsername;
    @FXML private TableColumn<AdminDashboardUserRow, String> colUserFullname;
    @FXML private TableColumn<AdminDashboardUserRow, String> colUserEmail;
    @FXML private TableColumn<AdminDashboardUserRow, String> colUserRole;
    @FXML private Button userReloadBtn;
    @FXML private Button userAddBtn;
    @FXML private Button userEditBtn;
    @FXML private Button userDeleteBtn;
    @FXML private Button userDetailBtn;
    // Services
    private final AuthService auth = AuthServiceImpl.getInstance();
    private final CourseOfferingServiceImpl courseOfferingService = new CourseOfferingServiceImpl();
    private final CourseServiceImpl courseService = new CourseServiceImpl();
    private final CourseOfferingScheduleServiceImpl courseOfferingScheduleService = new CourseOfferingScheduleServiceImpl();
    private final UserServiceImpl userService = new UserServiceImpl();
    private final SemesterServiceImpl semesterService = new SemesterServiceImpl();
    private final RoomServiceImpl roomService = new RoomServiceImpl();
    // Data
    private final ObservableList<AdminDashboardOfferingRow> offeringData = FXCollections.observableArrayList();
    private final ObservableList<AdminDashboardCourseRow> courseData = FXCollections.observableArrayList();
    private final ObservableList<AdminDashboardUserRow> userData = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        bindColumns();
        loadData();
    }
    private void bindColumns() {
        // Bind column cell factories from DTO helpers so values appear in the table
        AdminDashboardOfferingRow.bindColumns( colOfferingCourseOfferingId, colOfferingCourseId, colOfferingCourseName, colOfferingCredits, colOfferingInstructor, colOfferingMajor, colOfferingSemesterId, colOfferingSchedule, colOfferingRoomId, colOfferingMaxCapacity, colOfferingRemaining);
        AdminDashboardCourseRow.bindColumns(colCourseCourseId, colCourseCourseName, colCourseCredits, colFaculty);
        AdminDashboardUserRow.bindColumns(colUserUserId, colUserUsername, colUserFullname, colUserEmail, colUserRole);
        // Attach observable lists and UI settings
        setupTable(offeringTable, offeringData, colOfferingCourseOfferingId, colOfferingCourseId, colOfferingCourseName, colOfferingCredits, colOfferingInstructor, colOfferingMajor, colOfferingSemesterId, colOfferingSchedule, colOfferingRoomId, colOfferingMaxCapacity, colOfferingRemaining);
        setupTable(courseTable, courseData, colCourseCourseId, colCourseCourseName, colCourseCredits, colFaculty);
        setupTable(userTable, userData, colUserUserId, colUserUsername, colUserFullname, colUserEmail, colUserRole);
    }
    private void loadData() {
        // Hiển thị tên
        try {
            AuthUtils.setUserLabel(adminNameText, "", auth);
        } catch (Exception ex) {
            if (adminNameText != null) {
                adminNameText.setText("Admin");
            }
        }
        loadOfferingData();
        loadCourseData();
        loadUserData();
    }
    private void loadOfferingData() {
        AdminControllerUtils.loadDataFromList(
            offeringData,
            () -> courseOfferingService.getAllCourseOfferings(),
            o -> AdminControllerUtils.toOfferingRow(o, courseService, semesterService, roomService, courseOfferingScheduleService),
            "Không thể tải dữ liệu lớp học phần"
        );
    }
    private void loadCourseData() {
        AdminControllerUtils.loadDataFromList(
            courseData,
            () -> courseService.getAllCourses(),
            AdminControllerUtils::toCourseRow,
            "Không thể tải dữ liệu môn học"
        );
    }
    private void loadUserData() {
        AdminControllerUtils.loadDataFromList(
            userData,
            () -> userService.getAllUsers(),
            AdminControllerUtils::toUserRow,
            "Không thể tải dữ liệu người dùng"
        );
    }

    @FXML
    private void handleLogout() {
        appLogout(auth, logoutBtn);
    }
    @FXML
    private void handleReload(ActionEvent event) {
        loadData();
    }
    @FXML
    private void handleAdd(ActionEvent event) {
        try {
            Object src = event.getSource();
            NavigationManager navigationManager = new NavigationManager(getStageFromSource(src));
            if (src == offeringAddBtn) {
                navigationManager.showCourseOfferingAddForm();
                loadOfferingData(); // Reload after add
            } else if (src == courseAddBtn) {
                navigationManager.showCourseAddForm();
                loadCourseData(); // Reload after add
            } else if (src == userAddBtn) {
                navigationManager.showUserAddForm();
                loadUserData(); // Reload after add
            }
        } catch (IOException ex) {
            FXUtils.showError("Không thể mở form thêm (lớp/môn học), vui lòng thử lại sau.");
            ex.printStackTrace();
        } catch (Exception ex) {
            FXUtils.showError("Hành động thất bại: " + ex.getMessage());
        }
    }
    @FXML
    private void handleEdit(ActionEvent event) {
        try {
            if (event.getSource() == offeringEditBtn) {
                AdminControllerHelper.requireSelectedAndFetch(
                    offeringTable,
                    "Vui lòng chọn 1 lớp học phần để sửa",
                    "Dòng được chọn không hợp lệ",
                    "Không tìm thấy lớp học phần",
                    AdminDashboardOfferingRow::getCourseOfferingId,
                    courseOfferingService::getCourseOfferingById,
                    (CourseOffering fullOffering) -> {
                        NavigationManager navigationManager = new NavigationManager(getStageFromSource(offeringEditBtn));
                        try {
                            navigationManager.showCourseOfferingEditForm(fullOffering);
                            loadOfferingData(); // Reload after edit
                        } catch (IOException e) {
                            FXUtils.showError("Không thể mở form sửa (lớp/môn học), vui lòng thử lại sau.");
                        }
                    }
                );
            } else if (event.getSource() == courseEditBtn) {
                AdminControllerHelper.requireSelectedAndFetch(
                    courseTable,
                    "Vui lòng chọn 1 môn học để sửa",
                    "Dòng được chọn không hợp lệ",
                    "Không tìm thấy môn học",
                    AdminDashboardCourseRow::getCourseId,
                    courseService::getCourseById,
                    (Course fullCourse) -> {
                        NavigationManager navigationManager = new NavigationManager(getStageFromSource(courseEditBtn));
                        try {
                            navigationManager.showCourseEditForm(fullCourse);
                            loadCourseData(); // Reload after edit
                        } catch (IOException e) {
                            FXUtils.showError("Không thể mở form sửa (môn học), vui lòng thử lại sau.");
                        }
                    }
                );
            } else if (event.getSource() == userEditBtn) {
                AdminControllerHelper.requireSelectedAndFetch(
                    userTable,
                    "Vui lòng chọn 1 người dùng để sửa",
                    "Dòng được chọn không hợp lệ",
                    "Không tìm thấy người dùng",
                    AdminDashboardUserRow::getUserId,
                    userService::getUserById,
                    (User fullUser) -> {
                        NavigationManager navigationManager = new NavigationManager(getStageFromSource(userEditBtn));
                        try {
                            navigationManager.showUserEditForm(fullUser);
                            loadUserData(); // Reload after edit
                        } catch (IOException e) {
                            FXUtils.showError("Không thể mở form sửa (người dùng), vui lòng thử lại sau.");
                        }
                    }
                );
            }
        } catch (Exception ex) {
            FXUtils.showError("Hành động thất bại: " + ex.getMessage());
        }
    }
    @FXML
    private void handleDelete(ActionEvent event) {
        try {
            if (event.getSource() == userDeleteBtn) {
                AdminControllerHelper.requireSelectedAndFetch(
                    userTable,
                    "Vui lòng chọn 1 người dùng để xoá",
                    "Dòng được chọn không hợp lệ",
                    "Không tìm thấy người dùng",
                    AdminDashboardUserRow::getUserId,
                    userService::getUserById,
                    (User fullUser) -> {
                        NavigationManager navigationManager = new NavigationManager(getStageFromSource(userDeleteBtn));
                        try {
                            navigationManager.showUserDeleteConfirm(fullUser);
                            loadUserData(); // Reload after delete
                        } catch (IOException e) {
                            FXUtils.showError("Không thể mở hộp thoại xoá");
                        }
                    }
                );
            } else if (event.getSource() == courseDeleteBtn) {
                AdminControllerHelper.requireSelectedAndFetch(
                    courseTable,
                    "Vui lòng chọn 1 môn học để xoá",
                    "Dòng được chọn không hợp lệ",
                    "Không tìm thấy môn học",
                    AdminDashboardCourseRow::getCourseId,
                    courseService::getCourseById,
                    (Course fullCourse) -> {
                        NavigationManager navigationManager = new NavigationManager(getStageFromSource(courseDeleteBtn));
                        try {
                            navigationManager.showCourseDeleteConfirm(fullCourse);
                            loadCourseData(); // Reload after delete
                        } catch (IOException e) {
                            FXUtils.showError("Không thể mở hộp thoại xoá");
                        }
                    }
                );
            } else if (event.getSource() == offeringDeleteBtn) {
                AdminControllerHelper.requireSelectedAndFetch(
                    offeringTable,
                    "Vui lòng chọn 1 lớp học phần để xoá",
                    "Dòng được chọn không hợp lệ",
                    "Không tìm thấy lớp học phần",
                    AdminDashboardOfferingRow::getCourseOfferingId,
                    courseOfferingService::getCourseOfferingById,
                    (CourseOffering fullOffering) -> {
                        NavigationManager navigationManager = new NavigationManager(getStageFromSource(offeringDeleteBtn));
                        try {
                            navigationManager.showCourseOfferingDeleteConfirm(fullOffering);
                            loadOfferingData(); // Reload after delete
                        } catch (IOException e) {
                            FXUtils.showError("Không thể mở hộp thoại xoá");
                        }
                    }
                );
            }
        } catch (Exception ex) {
            FXUtils.showError("Hành động thất bại: " + ex.getMessage());
        }
    }
    @FXML
    private void handleViewDetail(ActionEvent event) {
        try {
            if (event.getSource() == userDetailBtn) {
                AdminControllerHelper.requireSelectedAndFetch(
                    userTable,
                    "Vui lòng chọn 1 người dùng để xem chi tiết",
                    "Dòng được chọn không hợp lệ",
                    "Không tìm thấy người dùng",
                    AdminDashboardUserRow::getUserId,
                    userService::getUserById,
                    (User fullUser) -> {
                        NavigationManager navigationManager = new NavigationManager(getStageFromSource(userDetailBtn));
                        try {
                            navigationManager.showUserDetailForm(fullUser);
                        } catch (IOException e) {
                            FXUtils.showError("Không thể mở hộp thoại chi tiết");
                        }
                    }
                );
            } else if (event.getSource() == courseDetailBtn) {
                AdminControllerHelper.requireSelectedAndFetch(
                    courseTable,
                    "Vui lòng chọn 1 môn học để xem chi tiết",
                    "Dòng được chọn không hợp lệ",
                    "Không tìm thấy môn học",
                    AdminDashboardCourseRow::getCourseId,
                    courseService::getCourseById,
                    (Course fullCourse) -> {
                        NavigationManager navigationManager = new NavigationManager(getStageFromSource(courseDetailBtn));
                        try {
                            navigationManager.showCourseDetailForm(fullCourse);
                        } catch (IOException e) {
                            FXUtils.showError("Không thể mở hộp thoại chi tiết");
                        }
                    }
                );
            } else if (event.getSource() == offeringDetailBtn) {
                AdminControllerHelper.requireSelectedAndFetch(
                    offeringTable,
                    "Vui lòng chọn 1 lớp học phần để xem chi tiết",
                    "Dòng được chọn không hợp lệ",
                    "Không tìm thấy lớp học phần",
                    AdminDashboardOfferingRow::getCourseOfferingId,
                    courseOfferingService::getCourseOfferingById,
                    (CourseOffering fullOffering) -> {
                        NavigationManager navigationManager = new NavigationManager(getStageFromSource(offeringDetailBtn));
                        try {
                            navigationManager.showCourseOfferingDetailForm(fullOffering);
                        } catch (IOException e) {
                            FXUtils.showError("Không thể mở hộp thoại chi tiết");
                        }
                    }
                );
            }
        } catch (Exception ex) {
            FXUtils.showError("Hành động thất bại: " + ex.getMessage());
        }
    }
}

// 559