package main.java.controller.admin;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumnBase;
import javafx.scene.control.TableView;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import main.java.dto.AdminDashboardCourseRow;
import main.java.dto.AdminDashboardOfferingRow;
import main.java.dto.AdminDashboardUserRow;
import main.java.model.Course;
import main.java.model.CourseOffering;
import main.java.model.Room;
import main.java.model.Schedule;
import main.java.model.Semester;
import main.java.model.User;
import main.java.repository.RoomRepository;
import main.java.repository.SemesterRepository;
import main.java.service.AuthService;
import main.java.service.impl.AuthServiceImpl;
import main.java.service.impl.CourseOfferingScheduleServiceImpl;
import main.java.service.impl.CourseOfferingServiceImpl;
import main.java.service.impl.CourseServiceImpl;
import main.java.service.impl.UserServiceImpl;
import main.java.utils.FXUtils;
import main.java.view.NavigationManager;

/**
 * AdminController - Xử lý các request liên quan đến Admin
 * Chịu trách nhiệm: Course offering management, registration management
 */
public class AdminController {
    // Services
    private final AuthService auth = AuthServiceImpl.getInstance();
    private final CourseOfferingServiceImpl courseOfferingService = new CourseOfferingServiceImpl();
    private final CourseServiceImpl courseService = new CourseServiceImpl();
    private final CourseOfferingScheduleServiceImpl courseOfferingScheduleService = new CourseOfferingScheduleServiceImpl();
    private final UserServiceImpl userService = new UserServiceImpl();
    private final SemesterRepository semesterRepository = new SemesterRepository();
    private final RoomRepository roomRepository = new RoomRepository();

    // Data
    private final ObservableList<AdminDashboardOfferingRow> offeringData = FXCollections.observableArrayList();
    private final ObservableList<AdminDashboardCourseRow> courseData = FXCollections.observableArrayList();
    private final ObservableList<AdminDashboardUserRow> userData = FXCollections.observableArrayList();

    // Top bar
    @FXML private Text adminNameText;
    @FXML private Button logoutBtn;

    // Tabs
    @FXML private TabPane tabPane;
    @FXML private Tab offeringTab;
    @FXML private Tab courseTab;
    @FXML private Tab userTab;

    // ========== Offering tab ==========
    @FXML private TableView<AdminDashboardOfferingRow> offeringTable;
    @FXML private TableColumn<AdminDashboardOfferingRow, String> colOfferingCourseOfferingId;
    @FXML private TableColumn<AdminDashboardOfferingRow, String> colOfferingCourseId;
    @FXML private TableColumn<AdminDashboardOfferingRow, String> colOfferingCourseName;
    @FXML private TableColumn<AdminDashboardOfferingRow, Integer> colOfferingCredits;
    @FXML private TableColumn<AdminDashboardOfferingRow, String> colOfferingInstructor;
    @FXML private TableColumn<AdminDashboardOfferingRow, String> colOfferingSemesterId;
    @FXML private TableColumn<AdminDashboardOfferingRow, String> colOfferingSchedule;
    @FXML private TableColumn<AdminDashboardOfferingRow, String> colOfferingRoomId;
    @FXML private TableColumn<AdminDashboardOfferingRow, String> colOfferingMaxCapacity;
    @FXML private TableColumn<AdminDashboardOfferingRow, String> colOfferingRemaining;

    // Offering actions
    @FXML private Button offeringReloadBtn;
    @FXML private Button offeringAddBtn;
    @FXML private Button offeringEditBtn;
    @FXML private Button offeringDeleteBtn;
    @FXML private Button offeringDetailBtn;

    // ========== Course tab ==========
    @FXML private TableView<AdminDashboardCourseRow> courseTable;
    @FXML private TableColumn<AdminDashboardCourseRow, String> colCourseCourseId;
    @FXML private TableColumn<AdminDashboardCourseRow, String> colCourseCourseName;
    @FXML private TableColumn<AdminDashboardCourseRow, Integer> colCourseCredits;
    @FXML private TableColumn<AdminDashboardCourseRow, String> colFaculty; // New column for facultyId

    // Course actions
    @FXML private Button courseReloadBtn;
    @FXML private Button courseAddBtn;
    @FXML private Button courseEditBtn;
    @FXML private Button courseDeleteBtn;
    @FXML private Button courseDetailBtn;

    // ========== User tab ==========
    @FXML private TableView<AdminDashboardUserRow> userTable;
    @FXML private TableColumn<AdminDashboardUserRow, String> colUserUserId;
    @FXML private TableColumn<AdminDashboardUserRow, String> colUserUsername;
    @FXML private TableColumn<AdminDashboardUserRow, String> colUserFullname;
    @FXML private TableColumn<AdminDashboardUserRow, String> colUserEmail;
    @FXML private TableColumn<AdminDashboardUserRow, String> colUserRole;

    // User actions
    @FXML private Button userReloadBtn;
    @FXML private Button userAddBtn;
    @FXML private Button userEditBtn;
    @FXML private Button userDeleteBtn;
    @FXML private Button userDetailBtn;

    // ========== Row Models ==========

    private void bindOfferingColumns() {
        // bind columns
        AdminDashboardOfferingRow.bindColumns(colOfferingCourseOfferingId, colOfferingCourseId, colOfferingCourseName, colOfferingCredits, colOfferingInstructor, colOfferingSemesterId, colOfferingSchedule, colOfferingRoomId, colOfferingMaxCapacity, colOfferingRemaining);

        // UI settings
        for (TableColumn<?, ?> col : offeringTable.getColumns()) {
            col.setReorderable(false);
        }
        offeringTable.setItems(offeringData);
        offeringTable.setColumnResizePolicy(param -> {
            double tableWidth = offeringTable.getWidth();
            int columnCount = offeringTable.getColumns().size();
            double totalMinWidth = offeringTable.getColumns().stream().mapToDouble(TableColumnBase::getMinWidth).sum();
            double extraWidth = Math.max(0, tableWidth - totalMinWidth);
            for (TableColumn<?, ?> col : offeringTable.getColumns()) {
                double newWidth = col.getMinWidth() + extraWidth / columnCount;
                col.setPrefWidth(newWidth);
            }
            return true;
        });
    }

    private void bindCourseColumns() {
        // bind columns
        AdminDashboardCourseRow.bindColumns(colCourseCourseId, colCourseCourseName, colCourseCredits, colFaculty);

        // UI settings
        for (TableColumn<?, ?> col : courseTable.getColumns()) {
            col.setReorderable(false);
        }
        courseTable.setItems(courseData);
        courseTable.setColumnResizePolicy(param -> {
            double tableWidth = courseTable.getWidth();
            int columnCount = courseTable.getColumns().size();
            double totalMinWidth = courseTable.getColumns().stream().mapToDouble(TableColumnBase::getMinWidth).sum();
            double extraWidth = Math.max(0, tableWidth - totalMinWidth);
            for (TableColumn<?, ?> col : courseTable.getColumns()) {
                double newWidth = col.getMinWidth() + extraWidth / columnCount;
                col.setPrefWidth(newWidth);
            }
            return true;
        });
    }

    private void bindUserColumns() {
        // bind columns
        AdminDashboardUserRow.bindColumns(colUserUserId, colUserUsername, colUserFullname, colUserEmail, colUserRole);

        // UI settings
        for (TableColumn<?, ?> col : userTable.getColumns()) {
            col.setReorderable(false);
        }
        userTable.setItems(userData);
        userTable.setColumnResizePolicy(param -> {
            double tableWidth = userTable.getWidth();
            int columnCount = userTable.getColumns().size();
            double totalMinWidth = userTable.getColumns().stream().mapToDouble(TableColumnBase::getMinWidth).sum();
            double extraWidth = Math.max(0, tableWidth - totalMinWidth);
            for (TableColumn<?, ?> col : userTable.getColumns()) {
                double newWidth = col.getMinWidth() + extraWidth / columnCount;
                col.setPrefWidth(newWidth);
            }
            return true;
        });
    }

    @FXML
    public void initialize() {
        bindColumns();
        bindActions();
        loadData();
    }

    private void bindColumns() {
        bindOfferingColumns();
        bindCourseColumns();
        bindUserColumns();
    }

    private void bindActions() {
        if (logoutBtn != null) logoutBtn.setOnAction(e -> handleLogout());
        if (offeringReloadBtn != null) offeringReloadBtn.setOnAction(e -> handleReload(e));
        if (courseReloadBtn != null) courseReloadBtn.setOnAction(e -> handleReload(e));
        if (userReloadBtn != null) userReloadBtn.setOnAction(e -> handleReload(e));
        if (offeringDetailBtn != null) offeringDetailBtn.setOnAction(e -> handleViewDetail(e));
        if (courseDetailBtn != null) courseDetailBtn.setOnAction(e -> handleViewDetail(e));
        if (userDetailBtn != null) userDetailBtn.setOnAction(e -> handleViewDetail(e));
        if (offeringDeleteBtn != null) offeringDeleteBtn.setOnAction(e -> handleDelete(e));
        if (courseDeleteBtn != null) courseDeleteBtn.setOnAction(e -> handleDelete(e));
        if (userDeleteBtn != null) userDeleteBtn.setOnAction(e -> handleDelete(e));
    }

    @FXML
    private void handleLogout() {
        try {
            User current = auth.getCurrentUser();
            if (current != null) {
                auth.logout(current.getUserId());
            } else {
                auth.clearSession();
            }
            FXUtils.showSuccess("Đăng xuất thành công");
            Stage stage = (Stage) logoutBtn.getScene().getWindow();
            NavigationManager navigationManager = new NavigationManager(stage);
            navigationManager.showLoginScreen();
        } catch (IOException ex) {
            FXUtils.showError("Không thể điều hướng về màn hình đăng nhập, vui lòng thử lại sau.");
        } catch (Exception ex) {
            FXUtils.showError("Đăng xuất thất bại: " + ex.getMessage());
        }
    }

    private void loadData() {
        // Display admin name
        try {
            User current = auth.getCurrentUser();
            String displayName = (current != null && current.getFullName() != null && !current.getFullName().trim().isEmpty())
                ? current.getFullName().trim()
                : "Admin";
            if (adminNameText != null) {
                adminNameText.setText(displayName);
            }
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
        offeringData.clear();
        try {
            List<CourseOffering> offerings = courseOfferingService.getAllCourseOfferings();
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

            for (CourseOffering offering : offerings) {
                // Get course details
                Course course = offering.getCourseId() != null 
                    ? courseService.getCourseById(offering.getCourseId()) 
                    : null;
                
                String courseId = offering.getCourseId() != null ? offering.getCourseId() : "-";
                String courseName = course != null && course.getCourseName() != null 
                    ? course.getCourseName() 
                    : "-";
                int credits = course != null ? course.getCredits() : 0;

                // Get semester (term/year)
                String semesterText = "-";
                if (offering.getSemesterId() != null) {
                    Semester semester = semesterRepository.findById(offering.getSemesterId());
                    if (semester != null) {
                        String term = semester.getTerm();
                        String year = semester.getAcademicYear();
                        if (term != null && year != null) {
                            semesterText = term + "/" + year;
                        } else if (term != null) {
                            semesterText = term;
                        } else {
                            semesterText = offering.getSemesterId();
                        }
                    } else {
                        semesterText = offering.getSemesterId();
                    }
                }

                // Get room
                String roomText = "-";
                if (offering.getRoomId() != null) {
                    Room room = roomRepository.findById(offering.getRoomId());
                    roomText = room != null && room.getRoomId() != null 
                        ? room.getRoomId() 
                        : offering.getRoomId();
                }

                // Get instructor
                String instructor = offering.getInstructor() != null 
                    ? offering.getInstructor() 
                    : "-";

                // Get schedule
                String scheduleText = "-";
                try {
                    List<Schedule> schedules = courseOfferingScheduleService
                        .getSchedulesByCourseOfferingId(offering.getCourseOfferingId());
                    if (schedules != null && !schedules.isEmpty()) {
                        scheduleText = schedules.stream().map(s -> {
                            String day;
                            switch (s.getDayOfWeek()) {
                                case 2: day = "T2"; break;
                                case 3: day = "T3"; break;
                                case 4: day = "T4"; break;
                                case 5: day = "T5"; break;
                                case 6: day = "T6"; break;
                                case 7: day = "T7"; break;
                                case 1:
                                case 8: day = "CN"; break;
                                default: day = "T?"; break;
                            }
                            String start = s.getStartTime() != null ? timeFormatter.format(s.getStartTime()) : "";
                            String end = s.getEndTime() != null ? timeFormatter.format(s.getEndTime()) : "";
                            String time = (!start.isEmpty() && !end.isEmpty()) ? (start + "-" + end) : (start + end);
                            return day + (time.isEmpty() ? "" : " " + time);
                        }).collect(Collectors.joining(", "));
                    }
                } catch (Exception e) {
                    scheduleText = "-";
                }

                // Capacity info
                String maxCapacity = offering.getMaxCapacity() != null ? offering.getMaxCapacity() : "0";
                String currentCapacity = offering.getCurrentCapacity() != null ? offering.getCurrentCapacity() : "0";

                AdminDashboardOfferingRow row = new AdminDashboardOfferingRow(
                    offering.getCourseOfferingId() != null ? offering.getCourseOfferingId() : "-",
                    courseId,
                    courseName,
                    credits,
                    instructor,
                    semesterText,
                    scheduleText,
                    roomText,
                    maxCapacity,
                    currentCapacity
                );
                offeringData.add(row);
            }
        } catch (Exception ex) {
            FXUtils.showError("Không thể tải dữ liệu lớp học phần: " + ex.getMessage());
        }
    }

    private void loadCourseData() {
        courseData.clear();
        try {
            List<Course> courses = courseService.getAllCourses();
            for (Course course : courses) {
                AdminDashboardCourseRow row = new AdminDashboardCourseRow(
                    course.getCourseId() != null ? course.getCourseId() : "-",
                    course.getCourseName() != null ? course.getCourseName() : "-",
                    course.getCredits(),
                    course.getFacultyId() != null ? course.getFacultyId() : "-"
                );
                courseData.add(row);
            }
        } catch (Exception ex) {
            FXUtils.showError("Không thể tải dữ liệu môn học: " + ex.getMessage());
        }
    }

    private void loadUserData() {
        userData.clear();
        try {
            List<User> users = userService.getAllUsers();
            for (User user : users) {
                AdminDashboardUserRow row = new AdminDashboardUserRow(
                    user.getUserId() != null ? user.getUserId() : "-",
                    user.getUsername() != null ? user.getUsername() : "-",
                    null, // password not displayed
                    user.getFullName() != null ? user.getFullName() : "-",
                    user.getEmail() != null ? user.getEmail() : "-",
                    user.getRole()
                );
                userData.add(row);
            }
        } catch (Exception ex) {
            FXUtils.showError("Không thể tải dữ liệu người dùng: " + ex.getMessage());
        }
    }

    @FXML
    private void handleReload(ActionEvent event) {
        loadData();
    }

    @FXML
    private void handleAdd(ActionEvent event) {
        try {
            if (event.getSource() == offeringAddBtn) {
                Stage stage = (Stage) offeringAddBtn.getScene().getWindow();
                NavigationManager navigationManager = new NavigationManager(stage);
                navigationManager.showCourseOfferingAddForm();
            } else if (event.getSource() == courseAddBtn) {
                Stage stage = (Stage) courseAddBtn.getScene().getWindow();
                NavigationManager navigationManager = new NavigationManager(stage);
                navigationManager.showCourseAddForm();
            } else if (event.getSource() == userAddBtn) {
                Stage stage = (Stage) userAddBtn.getScene().getWindow();
                NavigationManager navigationManager = new NavigationManager(stage);
                navigationManager.showUserAddForm();
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
                // Require selection and prefill
                AdminDashboardOfferingRow selected = offeringTable != null ? offeringTable.getSelectionModel().getSelectedItem() : null;
                if (selected == null) {
                    FXUtils.showError("Vui lòng chọn 1 lớp học phần để sửa");
                    return;
                }
                String offeringId = selected.getCourseOfferingId();
                if (offeringId == null || offeringId.trim().isEmpty() || "-".equals(offeringId)) {
                    FXUtils.showError("Dòng được chọn không hợp lệ");
                    return;
                }
                CourseOffering fullOffering = courseOfferingService.getCourseOfferingById(offeringId);
                if (fullOffering == null) {
                    FXUtils.showError("Không tìm thấy lớp học phần");
                    return;
                }
                Stage stage = (Stage) offeringEditBtn.getScene().getWindow();
                NavigationManager navigationManager = new NavigationManager(stage);
                navigationManager.showCourseOfferingEditForm(fullOffering);
            } else if (event.getSource() == courseEditBtn) {
                // Require selection and prefill
                AdminDashboardCourseRow selected = courseTable != null ? courseTable.getSelectionModel().getSelectedItem() : null;
                if (selected == null) {
                    FXUtils.showError("Vui lòng chọn 1 môn học để sửa");
                    return;
                }
                String courseId = selected.getCourseId();
                if (courseId == null || courseId.trim().isEmpty() || "-".equals(courseId)) {
                    FXUtils.showError("Dòng được chọn không hợp lệ");
                    return;
                }
                Course fullCourse = courseService.getCourseById(courseId);
                if (fullCourse == null) {
                    FXUtils.showError("Không tìm thấy môn học");
                    return;
                }
                Stage stage = (Stage) courseEditBtn.getScene().getWindow();
                NavigationManager navigationManager = new NavigationManager(stage);
                navigationManager.showCourseEditForm(fullCourse);
            } else if (event.getSource() == userEditBtn) {
                // Require selection and prefill the edit form
                AdminDashboardUserRow selected = userTable != null ? userTable.getSelectionModel().getSelectedItem() : null;
                if (selected == null) {
                    FXUtils.showError("Vui lòng chọn 1 người dùng để sửa");
                    return;
                }

                String selectedUserId = selected.getUserId();
                if (selectedUserId == null || selectedUserId.trim().isEmpty() || "-".equals(selectedUserId)) {
                    FXUtils.showError("Dòng được chọn không hợp lệ");
                    return;
                }

                User fullUser = userService.getUserById(selectedUserId);
                if (fullUser == null) {
                    FXUtils.showError("Không tìm thấy người dùng");
                    return;
                }

                Stage stage = (Stage) userEditBtn.getScene().getWindow();
                NavigationManager navigationManager = new NavigationManager(stage);
                navigationManager.showUserEditForm(fullUser);
                // Refresh 
                loadUserData();
            }
        } catch (IOException ex) {
            FXUtils.showError("Không thể mở form sửa (lớp/môn học), vui lòng thử lại sau.");
            ex.printStackTrace();
        } catch (Exception ex) {
            FXUtils.showError("Hành động thất bại: " + ex.getMessage());
        }
    }

    @FXML
    private void handleDelete(ActionEvent event) {
        try {
            if (event.getSource() == userDeleteBtn) {
                AdminDashboardUserRow selected = userTable != null ? userTable.getSelectionModel().getSelectedItem() : null;
                if (selected == null) {
                    FXUtils.showError("Vui lòng chọn 1 người dùng để xoá");
                    return;
                }
                String selectedUserId = selected.getUserId();
                if (selectedUserId == null || selectedUserId.trim().isEmpty() || "-".equals(selectedUserId)) {
                    FXUtils.showError("Dòng được chọn không hợp lệ");
                    return;
                }
                User fullUser = userService.getUserById(selectedUserId);
                if (fullUser == null) {
                    FXUtils.showError("Không tìm thấy người dùng");
                    return;
                }
                Stage stage = (Stage) userDeleteBtn.getScene().getWindow();
                NavigationManager navigationManager = new NavigationManager(stage);
                navigationManager.showUserDeleteConfirm(fullUser);
                // Optionally refresh after delete attempt
                loadUserData();
            } else if (event.getSource() == courseDeleteBtn) {
                AdminDashboardCourseRow selected = courseTable != null ? courseTable.getSelectionModel().getSelectedItem() : null;
                if (selected == null) {
                    FXUtils.showError("Vui lòng chọn 1 môn học để xoá");
                    return;
                }
                String courseId = selected.getCourseId();
                if (courseId == null || courseId.trim().isEmpty() || "-".equals(courseId)) {
                    FXUtils.showError("Dòng được chọn không hợp lệ");
                    return;
                }
                Course fullCourse = courseService.getCourseById(courseId);
                if (fullCourse == null) {
                    FXUtils.showError("Không tìm thấy môn học");
                    return;
                }
                Stage stage = (Stage) courseDeleteBtn.getScene().getWindow();
                NavigationManager navigationManager = new NavigationManager(stage);
                navigationManager.showCourseDeleteConfirm(fullCourse);
                loadCourseData();
            } else if (event.getSource() == offeringDeleteBtn) {
                AdminDashboardOfferingRow selected = offeringTable != null ? offeringTable.getSelectionModel().getSelectedItem() : null;
                if (selected == null) {
                    FXUtils.showError("Vui lòng chọn 1 lớp học phần để xoá");
                    return;
                }
                String offeringId = selected.getCourseOfferingId();
                if (offeringId == null || offeringId.trim().isEmpty() || "-".equals(offeringId)) {
                    FXUtils.showError("Dòng được chọn không hợp lệ");
                    return;
                }
                CourseOffering fullOffering = courseOfferingService.getCourseOfferingById(offeringId);
                if (fullOffering == null) {
                    FXUtils.showError("Không tìm thấy lớp học phần");
                    return;
                }
                Stage stage = (Stage) offeringDeleteBtn.getScene().getWindow();
                NavigationManager navigationManager = new NavigationManager(stage);
                navigationManager.showCourseOfferingDeleteConfirm(fullOffering);
                loadOfferingData();
            }
        } catch (IOException ex) {
            FXUtils.showError("Không thể mở hộp thoại xoá");
        } catch (Exception ex) {
            FXUtils.showError("Hành động thất bại: " + ex.getMessage());
        }
    }

    @FXML
    private void handleViewDetail(ActionEvent event) {
        try {
            if (event.getSource() == userDetailBtn) {
                AdminDashboardUserRow selected = userTable != null ? userTable.getSelectionModel().getSelectedItem() : null;
                if (selected == null) {
                    FXUtils.showError("Vui lòng chọn 1 người dùng để xem chi tiết");
                    return;
                }
                String selectedUserId = selected.getUserId();
                if (selectedUserId == null || selectedUserId.trim().isEmpty() || "-".equals(selectedUserId)) {
                    FXUtils.showError("Dòng được chọn không hợp lệ");
                    return;
                }
                User fullUser = userService.getUserById(selectedUserId);
                if (fullUser == null) {
                    FXUtils.showError("Không tìm thấy người dùng");
                    return;
                }
                Stage stage = (Stage) userDetailBtn.getScene().getWindow();
                NavigationManager navigationManager = new NavigationManager(stage);
                navigationManager.showUserDetailForm(fullUser);
            } else if (event.getSource() == courseDetailBtn) {
                AdminDashboardCourseRow selected = courseTable != null ? courseTable.getSelectionModel().getSelectedItem() : null;
                if (selected == null) {
                    FXUtils.showError("Vui lòng chọn 1 môn học để xem chi tiết");
                    return;
                }
                String courseId = selected.getCourseId();
                if (courseId == null || courseId.trim().isEmpty() || "-".equals(courseId)) {
                    FXUtils.showError("Dòng được chọn không hợp lệ");
                    return;
                }
                Course fullCourse = courseService.getCourseById(courseId);
                if (fullCourse == null) {
                    FXUtils.showError("Không tìm thấy môn học");
                    return;
                }
                Stage stage = (Stage) courseDetailBtn.getScene().getWindow();
                NavigationManager navigationManager = new NavigationManager(stage);
                navigationManager.showCourseDetailForm(fullCourse);
            } else if (event.getSource() == offeringDetailBtn) {
                AdminDashboardOfferingRow selected = offeringTable != null ? offeringTable.getSelectionModel().getSelectedItem() : null;
                if (selected == null) {
                    FXUtils.showError("Vui lòng chọn 1 lớp học phần để xem chi tiết");
                    return;
                }
                String offeringId = selected.getCourseOfferingId();
                if (offeringId == null || offeringId.trim().isEmpty() || "-".equals(offeringId)) {
                    FXUtils.showError("Dòng được chọn không hợp lệ");
                    return;
                }
                CourseOffering fullOffering = courseOfferingService.getCourseOfferingById(offeringId);
                if (fullOffering == null) {
                    FXUtils.showError("Không tìm thấy lớp học phần");
                    return;
                }
                Stage stage = (Stage) offeringDetailBtn.getScene().getWindow();
                NavigationManager navigationManager = new NavigationManager(stage);
                navigationManager.showCourseOfferingDetailForm(fullOffering);
            }
        } catch (IOException ex) {
            FXUtils.showError("Không thể mở hộp thoại chi tiết");
            ex.printStackTrace();
        } catch (Exception ex) {
            FXUtils.showError("Hành động thất bại: " + ex.getMessage());
        }
    }
}
