package main.java.controller;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
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
import main.java.model.Course;
import main.java.model.CourseOffering;
import main.java.model.Room;
import main.java.model.Schedule;
import main.java.model.Semester;
import main.java.model.Student;
import main.java.model.Admin;
import main.java.model.User;
import main.java.repository.RoomRepository;
import main.java.repository.SemesterRepository;
import main.java.service.AuthService;
import main.java.service.impl.AuthServiceImpl;
import main.java.service.impl.CourseOfferingScheduleServiceImpl;
import main.java.service.impl.CourseOfferingServiceImpl;
import main.java.service.impl.CourseServiceImpl;
import main.java.service.impl.StudentServiceImpl;
import main.java.service.impl.AdminServiceImpl;
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
    private final StudentServiceImpl studentService = new StudentServiceImpl();
    private final AdminServiceImpl adminService = new AdminServiceImpl();
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
    @FXML private TableColumn<AdminDashboardCourseRow, String> colCourseDescription;

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
    public static class AdminDashboardOfferingRow {
        private final StringProperty courseOfferingId;
        private final StringProperty courseId;
        private final StringProperty courseName;
        private final IntegerProperty credits;
        private final StringProperty instructor;
        private final StringProperty semesterId;
        private final StringProperty schedule;
        private final StringProperty roomId;
        private final StringProperty maxCapacity;
        private final StringProperty currentCapacity;

        public AdminDashboardOfferingRow() {
            this.courseOfferingId = new SimpleStringProperty();
            this.courseId = new SimpleStringProperty();
            this.courseName = new SimpleStringProperty();
            this.credits = new SimpleIntegerProperty();
            this.instructor = new SimpleStringProperty();
            this.semesterId = new SimpleStringProperty();
            this.schedule = new SimpleStringProperty();
            this.roomId = new SimpleStringProperty();
            this.maxCapacity = new SimpleStringProperty();
            this.currentCapacity = new SimpleStringProperty();
        }

        public AdminDashboardOfferingRow(
            String courseOfferingId,
            String courseId,
            String courseName,
            int credits,
            String instructor,
            String semesterId,
            String schedule,
            String roomId,
            String maxCapacity,
            String currentCapacity
        ) {
            this();
            this.courseOfferingId.set(courseOfferingId);
            this.courseId.set(courseId);
            this.courseName.set(courseName);
            this.credits.set(credits);
            this.instructor.set(instructor);
            this.semesterId.set(semesterId);
            this.schedule.set(schedule);
            this.roomId.set(roomId);
            this.maxCapacity.set(maxCapacity);
            this.currentCapacity.set(currentCapacity);
        }

        // Property getters
        public StringProperty courseOfferingIdProperty() { return courseOfferingId; }
        public StringProperty courseIdProperty() { return courseId; }
        public StringProperty courseNameProperty() { return courseName; }
        public IntegerProperty creditsProperty() { return credits; }
        public StringProperty instructorProperty() { return instructor; }
        public StringProperty semesterIdProperty() { return semesterId; }
        public StringProperty scheduleProperty() { return schedule; }
        public StringProperty roomIdProperty() { return roomId; }
        public StringProperty maxCapacityProperty() { return maxCapacity; }
        public StringProperty currentCapacityProperty() { return currentCapacity; }

        // Value getters
        public String getCourseOfferingId() { return courseOfferingId.get(); }
        public String getCourseId() { return courseId.get(); }
        public String getCourseName() { return courseName.get(); }
        public int getCredits() { return credits.get(); }
        public String getInstructor() { return instructor.get(); }
        public String getSemesterId() { return semesterId.get(); }
        public String getSchedule() { return schedule.get(); }
        public String getRoomId() { return roomId.get(); }
        public String getMaxCapacity() { return maxCapacity.get(); }
        public String getCurrentCapacity() { return currentCapacity.get(); }
        
        // Computed property for remaining capacity
        public String getRemaining() {
            try {
                int max = Integer.parseInt(maxCapacity.get());
                int current = Integer.parseInt(currentCapacity.get());
                return String.valueOf(max - current);
            } catch (NumberFormatException e) {
                return "N/A";
            }
        }
    }
    public static class AdminDashboardCourseRow {
        private final StringProperty courseId;
        private final StringProperty courseName;
        private final IntegerProperty credits;
        private final StringProperty description;
        private final StringProperty prerequisiteCourseId;

        public AdminDashboardCourseRow() {
            this.courseId = new SimpleStringProperty();
            this.courseName = new SimpleStringProperty();
            this.credits = new SimpleIntegerProperty();
            this.description = new SimpleStringProperty();
            this.prerequisiteCourseId = new SimpleStringProperty();
        }

        public AdminDashboardCourseRow(
            String courseId,
            String courseName,
            int credits,
            String description,
            String prerequisiteCourseId
        ) {
            this();
            this.courseId.set(courseId);
            this.courseName.set(courseName);
            this.credits.set(credits);
            this.description.set(description);
            this.prerequisiteCourseId.set(prerequisiteCourseId);
        }

        // Property getters
        public StringProperty courseIdProperty() { return courseId; }
        public StringProperty courseNameProperty() { return courseName; }
        public IntegerProperty creditsProperty() { return credits; }
        public StringProperty descriptionProperty() { return description; }
        public StringProperty prerequisiteCourseIdProperty() { return prerequisiteCourseId; }

        // Value getters
        public String getCourseId() { return courseId.get(); }
        public String getCourseName() { return courseName.get(); }
        public int getCredits() { return credits.get(); }
        public String getDescription() { return description.get(); }
        public String getPrerequisiteCourseId() { return prerequisiteCourseId.get(); }
    }
    public static class AdminDashboardUserRow {
        private final StringProperty userId;
        private final StringProperty username;
        private final StringProperty fullname;
        private final StringProperty email;
        private final IntegerProperty role;
        public AdminDashboardUserRow() {
            this.userId = new SimpleStringProperty();
            this.username = new SimpleStringProperty();
            this.fullname = new SimpleStringProperty();
            this.email = new SimpleStringProperty();
            this.role = new SimpleIntegerProperty();
        }
        public AdminDashboardUserRow(
            String userId,
            String username,
            String password,
            String fullname,
            String email,
            int role
        ) {
            this();
            this.userId.set(userId);
            this.username.set(username);
            this.fullname.set(fullname);
            this.email.set(email);
            this.role.set(role);
        }

        public StringProperty getUserIdProperty() {
            return userId;
        }
        public StringProperty getUsernameProperty() {
            return username;
        }
        public StringProperty getFullnameProperty() {
            return fullname;
        }
        public StringProperty getEmailProperty() {
            return email;
        }
        public IntegerProperty getRoleProperty() {
            return role;
        }

        public String getUserId() {
            return userId.get();
        }
        public String getUsername() {
            return username.get();
        }
        public String getFullname() {
            return fullname.get();
        }
        public String getEmail() {
            return email.get();
        }
        public int getRole() {
            return role.get();
        }
    }

    private void bindOfferingColumns() {
        colOfferingCourseOfferingId.setCellValueFactory(cell -> cell.getValue().courseOfferingIdProperty());
        colOfferingCourseId.setCellValueFactory(cell -> cell.getValue().courseIdProperty());
        colOfferingCourseName.setCellValueFactory(cell -> cell.getValue().courseNameProperty());
        colOfferingCredits.setCellValueFactory(cell -> cell.getValue().creditsProperty().asObject());
        colOfferingInstructor.setCellValueFactory(cell -> cell.getValue().instructorProperty());
        colOfferingSemesterId.setCellValueFactory(cell -> cell.getValue().semesterIdProperty());
        colOfferingSchedule.setCellValueFactory(cell -> cell.getValue().scheduleProperty());
        colOfferingRoomId.setCellValueFactory(cell -> cell.getValue().roomIdProperty());
        colOfferingMaxCapacity.setCellValueFactory(cell -> cell.getValue().maxCapacityProperty());
        colOfferingRemaining.setCellValueFactory(cell -> {
            AdminDashboardOfferingRow row = cell.getValue();
            return new SimpleStringProperty(row.getRemaining());
        });

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
        colCourseCourseId.setCellValueFactory(cell -> cell.getValue().courseIdProperty());
        colCourseCourseName.setCellValueFactory(cell -> cell.getValue().courseNameProperty());
        colCourseCredits.setCellValueFactory(cell -> cell.getValue().creditsProperty().asObject());
        colCourseDescription.setCellValueFactory(cell -> cell.getValue().descriptionProperty());
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
        colUserUserId.setCellValueFactory(cell -> cell.getValue().getUserIdProperty());
        colUserUsername.setCellValueFactory(cell -> cell.getValue().getUsernameProperty());
        colUserFullname.setCellValueFactory(cell -> cell.getValue().getFullnameProperty());
        colUserEmail.setCellValueFactory(cell -> cell.getValue().getEmailProperty());
        colUserRole.setCellValueFactory(cell -> {
            AdminDashboardUserRow row = cell.getValue();
            int roleInt = row.getRole();
            String roleText = (roleInt == 1) ? "Admin" : (roleInt == 0) ? "Sinh viên" : "Không xác định";
            return new SimpleStringProperty(roleText);
        });
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

                // Get semester name
                String semesterText = "-";
                if (offering.getSemesterId() != null) {
                    Semester semester = semesterRepository.findById(offering.getSemesterId());
                    semesterText = semester != null && semester.getSemesterName() != null 
                        ? semester.getSemesterName() 
                        : offering.getSemesterId();
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
                    course.getDescription() != null ? course.getDescription() : "-",
                    course.getPrerequisiteCourseId() != null ? course.getPrerequisiteCourseId() : "-"
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
        // TODO: implement later
    }

    @FXML
    private void handleEdit(ActionEvent event) {
        // TODO: implement later
    }

    @FXML
    private void handleDelete(ActionEvent event) {
        // TODO: implement later
    }

    @FXML
    private void handleViewDetail(ActionEvent event) {
        // TODO: implement later
    }
}
