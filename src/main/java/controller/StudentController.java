package main.java.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.property.BooleanProperty;

import java.util.List;

import main.java.model.Course;
import main.java.model.CourseOffering;
import main.java.model.Room;
import main.java.model.Semester;

import main.java.service.impl.CourseOfferingServiceImpl;
import main.java.service.impl.CourseServiceImpl;

import main.java.repository.SemesterRepository;
import main.java.repository.RoomRepository;
import javafx.stage.Stage;
import java.io.IOException;
import main.java.model.User;
import main.java.service.AuthService;
import main.java.service.impl.AuthServiceImpl;
import main.java.view.NavigationManager;
import main.java.utils.FXUtils;
import main.java.utils.GenericUtils;

public class StudentController {
    @FXML private TableView<StudentDashboardRow> studentMainTable;

    @FXML private TableColumn<StudentDashboardRow, String> colOfferingId;
    @FXML private TableColumn<StudentDashboardRow, String> colCourseId;
    @FXML private TableColumn<StudentDashboardRow, String> colCourseName;
    @FXML private TableColumn<StudentDashboardRow, String> colCredits;
    @FXML private TableColumn<StudentDashboardRow, String> colInstructor;
    @FXML private TableColumn<StudentDashboardRow, String> colSemester;
    @FXML private TableColumn<StudentDashboardRow, String> colSchedule;
    @FXML private TableColumn<StudentDashboardRow, String> colRoom;
    @FXML private TableColumn<StudentDashboardRow, String> colCapacity;
    @FXML private TableColumn<StudentDashboardRow, String> colRemaining;
    @FXML private TableColumn<StudentDashboardRow, Boolean> colSelect;

    @FXML private Button logoutButton;
    @FXML private Button showButton;
    @FXML private Button submitButton;

    @FXML private Text titleLabel;
    @FXML private Text studentNameText;

    private final CourseOfferingServiceImpl courseOfferingService = new CourseOfferingServiceImpl();
    private final CourseServiceImpl courseService = new CourseServiceImpl();
    private final SemesterRepository semesterRepository = new SemesterRepository();
    private final RoomRepository roomRepository = new RoomRepository();
    private final AuthService auth = AuthServiceImpl.getInstance();

    private final ObservableList<StudentDashboardRow> data = FXCollections.observableArrayList();

    public static class StudentDashboardRow {
        private final StringProperty offeringId;
        private final StringProperty courseId;
        private final StringProperty courseName;
        private final StringProperty credits;
        private final StringProperty instructor;
        private final StringProperty semester;
        private final StringProperty schedule;
        private final StringProperty room;
        private final StringProperty capacity;
        private final StringProperty remaining;
        private final BooleanProperty selected;
        
        public StudentDashboardRow(){
            this.offeringId = new SimpleStringProperty();
            this.courseId = new SimpleStringProperty();
            this.courseName = new SimpleStringProperty();
            this.credits = new SimpleStringProperty();
            this.instructor = new SimpleStringProperty();
            this.semester = new SimpleStringProperty();
            this.schedule = new SimpleStringProperty();
            this.room = new SimpleStringProperty();
            this.capacity = new SimpleStringProperty();
            this.remaining = new SimpleStringProperty();
            this.selected = new SimpleBooleanProperty(false);
        }

        public StudentDashboardRow(
            String offeringId,
            String courseId,
            String courseName,
            String credits,
            String instructor,
            String semester,
            String schedule,
            String room,
            String capacity,
            String remaining
        ) {
            this();
            this.offeringId.set(offeringId);
            this.courseId.set(courseId);
            this.courseName.set(courseName);
            this.credits.set(credits);
            this.instructor.set(instructor);
            this.semester.set(semester);
            this.schedule.set(schedule);
            this.room.set(room);
            this.capacity.set(capacity);
            this.remaining.set(remaining);
            this.selected.set(false);
        }

        // Ref của UI
        public StringProperty getOfferingIdProperty() { return offeringId; }
        public StringProperty getCourseIdProperty() { return courseId; }
        public StringProperty getCourseNameProperty() { return courseName; }
        public StringProperty getCreditsProperty() { return credits; }
        public StringProperty getInstructorProperty() { return instructor; }
        public StringProperty getSemesterProperty() { return semester; }
        public StringProperty getScheduleProperty() { return schedule; }
        public StringProperty getRoomProperty() { return room; }
        public StringProperty getCapacityProperty() { return capacity; }
        public StringProperty getRemainingProperty() { return remaining; }
        public BooleanProperty getSelectedProperty() { return selected; }

        // Get dữ liệu
        public String getOfferingId() { return offeringId.get(); }
        public String getCourseId() { return courseId.get(); }
        public String getCourseName() { return courseName.get(); }
        public String getCredits() { return credits.get(); }
        public String getInstructor() { return instructor.get(); }
        public String getSemester() { return semester.get(); }
        public String getSchedule() { return schedule.get(); }
        public String getRoom() { return room.get(); }
        public String getCapacity() { return capacity.get(); }
        public String getRemaining() { return remaining.get(); }
        public boolean isSelected() { return selected.get(); }
    }

    @FXML
    public void initialize() {
        bindColumns();
        bindActions();
        loadData();
    }

    private void bindColumns() {
        // Bind Ref của bảng với data
        colOfferingId.setCellValueFactory(cell -> cell.getValue().getOfferingIdProperty());
        colCourseId.setCellValueFactory(cell -> cell.getValue().getCourseIdProperty());
        colCourseName.setCellValueFactory(cell -> cell.getValue().getCourseNameProperty());
        colCredits.setCellValueFactory(cell -> cell.getValue().getCreditsProperty());
        colInstructor.setCellValueFactory(cell -> cell.getValue().getInstructorProperty());
        colSemester.setCellValueFactory(cell -> cell.getValue().getSemesterProperty());
        colSchedule.setCellValueFactory(cell -> cell.getValue().getScheduleProperty());
        colRoom.setCellValueFactory(cell -> cell.getValue().getRoomProperty());
        colCapacity.setCellValueFactory(cell -> cell.getValue().getCapacityProperty());
        colRemaining.setCellValueFactory(cell -> cell.getValue().getRemainingProperty());

        colSelect.setCellValueFactory(cell -> cell.getValue().getSelectedProperty());
        colSelect.setCellFactory(CheckBoxTableCell.forTableColumn(colSelect));

        studentMainTable.setItems(data);
    }

    private void bindActions() {
        //  Bind EventListener của nút
        if (showButton != null) {
            showButton.setOnAction(e -> loadData());
        }
        if (logoutButton != null) {
            logoutButton.setOnAction(e -> handleLogout());
        }
    }

    private void handleLogout() {
        // Logout
        
        try {
            User current = auth.getCurrentUser();
            if (current != null) {
                auth.logout(current.getUserId());
            } else {
                auth.clearSession();
            }
            FXUtils.showSuccess("Đăng xuất thành công");
            Stage stage = (Stage) logoutButton.getScene().getWindow();
            NavigationManager navigationManager = new NavigationManager(stage);
            navigationManager.showLoginScreen();
        } catch (IOException ex) {
            FXUtils.showError("Không thể điều hướng về màn hình đăng nhập, vui lòng thử lại sau.");
        } catch (Exception ex) {
            FXUtils.showError("Đăng xuất thất bại: " + ex.getMessage());
        }
    }

    private void loadData() {
        
        // Hiển thị tên học viên
        try {
            AuthService auth = AuthServiceImpl.getInstance();
            User current = auth.getCurrentUser();
            String displayName = (current != null && current.getFullName() != null && !current.getFullName().trim().isEmpty())
                ? current.getFullName().trim()
                : "Không xác định 1";
            studentNameText.setText("Học viên: " + displayName);
        } catch (Exception ex) {
            studentNameText.setText("Học viên: Không xác định 2");
        }

        data.clear();
        List<CourseOffering> ol = courseOfferingService.getAllCourseOfferings();
        for (CourseOffering oitem : ol) {
            Course course = oitem.getCourseId() != null ? courseService.getCourseById(oitem.getCourseId()) : null;
            String courseId = oitem.getCourseId() != null ? oitem.getCourseId() : "-";
            String courseName = course != null && course.getCourseName() != null ? course.getCourseName() : "-";
            String credits = course != null ? String.valueOf(course.getCredits()) : "-";
            String semesterText = "-";
            if (oitem.getSemesterId() != null) {
                Semester semester = semesterRepository.findById(oitem.getSemesterId());
                if (semester != null && semester.getSemesterName() != null) {
                    // Lấy tên semester
                    semesterText = semester.getSemesterName();
                } else {
                    // Fallback
                    semesterText = oitem.getSemesterId();
                }
            }
            String roomText = "-";
            if (oitem.getRoomId() != null) {
                Room room = roomRepository.findById(oitem.getRoomId());
                roomText = (room != null && room.getRoomId() != null) ? room.getRoomId() : oitem.getRoomId();
            }
            String instructor = oitem.getInstructor() != null ? oitem.getInstructor() : "-";

            String scheduleText = "Huzano will tell you later..";

            int current = GenericUtils.safeParseInt(oitem.getCurrentCapacity(), 0);
            int max = GenericUtils.safeParseInt(oitem.getMaxCapacity(), 0);
            String capacity = String.valueOf(max);
            String remaining = String.valueOf(Math.max(0, max - current));

            data.add(new StudentDashboardRow(
                oitem.getCourseOfferingId() != null ? oitem.getCourseOfferingId() : "Bullshit DB found no ID",
                courseId,
                courseName,
                credits,
                instructor,
                semesterText,
                scheduleText,
                roomText,
                capacity,
                remaining
            ));
        }
    }



    // Các hàm còn lại (đăng ký/hủy/kiểm tra...) sẽ triển khai ở các bước sau.
    // /**
    //  * Đăng ký môn học
    //  */
    // public void registerCourse(String studentId, String courseOfferingId) {
    //     // TODO: Implement register course logic
    //     // - Kiểm tra trùng môn học
    //     // - Kiểm tra trùng lịch
    //     // - Kiểm tra lớp đã đầy
    //     // - Tạo registration record
    // }

    // /**
    //  * Hủy đăng ký môn học
    //  */
    // public void cancelRegistration(String registrationId) {
    //     // TODO: Implement cancel registration logic
    // }

    // /**
    //  * Xem danh sách môn học đã đăng ký
    //  */
    // public void getRegisteredCourses(String studentId) {
    //     // TODO: Implement get registered courses
    // }

    // /**
    //  * Xem lịch học
    //  */
    // public void getSchedule(String studentId) {
    //     // TODO: Implement get schedule
    //     // - Lấy tất cả course offerings đã đăng ký
    //     // - Lấy schedule cho mỗi offering
    //     // - Hiển thị lịch theo thứ
    // }

    // /**
    //  * Xem thông tin cá nhân
    //  */
    // public void getStudentInfo(String studentId) {
    //     // TODO: Implement get student info
    // }

    // /**
    //  * Xem danh sách lớp học phần khả dụng
    //  */
    // public void getAvailableCourseOfferings(String semesterId) {
    //     // TODO: Implement get available course offerings
    // }

    // /**
    //  * Kiểm tra điều kiện đăng ký môn học
    //  */
    // public void checkRegistrationEligibility(String studentId, String courseOfferingId) {
    //     // TODO: Implement check eligibility
    //     // - Check duplicate course
    //     // - Check schedule conflict
    //     // - Check capacity
    // }
}
