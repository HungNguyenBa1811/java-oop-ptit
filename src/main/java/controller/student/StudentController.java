package main.java.controller.student;

import java.util.*;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.text.Text;
import main.java.dto.student.StudentDashboardRow;
import main.java.model.CourseOffering;
import main.java.model.Registration;
import main.java.model.Student;
import main.java.model.User;

import java.util.stream.Collectors;
import main.java.repository.RoomRepository;
import main.java.repository.SemesterRepository;
import main.java.service.AuthService;
import main.java.service.impl.AuthServiceImpl;
import main.java.service.impl.CourseOfferingScheduleServiceImpl;
import main.java.service.impl.CourseOfferingServiceImpl;
import main.java.service.impl.CourseServiceImpl;
import main.java.service.impl.RegistrationServiceImpl;
import main.java.utils.AuthUtils;
import main.java.utils.FXUtils;
import main.java.utils.StudentControllerUtils;
import main.java.utils.helper.StudentControllerHelper;
import main.java.view.NavigationManager;

import static main.java.utils.AuthUtils.appLogout;
import static main.java.utils.TableUtils.setupTable;

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
    @FXML private Button reloadButton;
    @FXML private Button showButton;
    @FXML private Button calendarButton;
    @FXML private Button submitButton;
    @FXML private Text titleLabel;
    @FXML private Text studentNameText;
    private final ObservableList<StudentDashboardRow> data = FXCollections.observableArrayList();

    private final AuthService auth = AuthServiceImpl.getInstance();
    private final CourseServiceImpl courseService = new CourseServiceImpl();
    private final CourseOfferingServiceImpl courseOfferingService = new CourseOfferingServiceImpl();
    private final SemesterRepository semesterRepository = new SemesterRepository();
    private final RoomRepository roomRepository = new RoomRepository();
    private final CourseOfferingScheduleServiceImpl courseOfferingScheduleService = new CourseOfferingScheduleServiceImpl();
    private final RegistrationServiceImpl registrationService = new RegistrationServiceImpl();

    private boolean showOnlyRegistered = false;

    @FXML
    public void initialize() {
        bindColumns();
        bindActions();
        loadData();
    }

    private void bindColumns() {
        StudentDashboardRow.bindColumns(colOfferingId, colCourseId, colCourseName, colCredits, colInstructor, colSemester, colSchedule, colRoom, colCapacity, colRemaining, colSelect);
        setupTable(studentMainTable, data, colOfferingId, colCourseId, colCourseName, colCredits, colInstructor, colSemester, colSchedule, colRoom, colCapacity, colRemaining, colSelect);
        // UI Patch
        studentMainTable.setEditable(true);
        colSelect.setEditable(true);
        colSelect.setSortable(false);
    }

    private void bindActions() {
        if (submitButton != null) submitButton.setDisable(showOnlyRegistered);
    }

    @FXML
    private void handleLogout() {
        appLogout(auth, logoutButton);
    }

    @FXML
    private void handleOpenCalendar() {
        try {
            javafx.stage.Stage currentStage = (javafx.stage.Stage) calendarButton.getScene().getWindow();
            NavigationManager nav = new NavigationManager(currentStage);
            nav.showStudentCalendar();
        } catch (Exception e) {
            e.printStackTrace();
            FXUtils.showError("Không thể mở thời khóa biểu: " + e.getMessage());
        }
    }
    
    @FXML
    private void handleToggleShow() {
        try {
            showOnlyRegistered = !showOnlyRegistered;
            if (showButton != null) {
                showButton.setText(showOnlyRegistered ? "Hiển thị tất cả" : "Hiển thị môn học đã đăng kí");
            }
            if (submitButton != null) {
                submitButton.setDisable(showOnlyRegistered);
            }
            loadData();
        } catch (Exception ex) {
            FXUtils.showError("Không thể chuyển chế độ hiển thị: " + ex.getMessage());
        }
    }

    @FXML
    private void handleSubmit() {
        try {
            // Validate student
            String studentId = StudentControllerHelper.validateStudentAccess(auth);
            if (studentId == null) {
                FXUtils.showError("Chỉ sinh viên mới được thao tác đăng ký/hủy đăng ký");
                return;
            }

            // Build registration lookup map
            Map<String, Registration> regsByOfferingId = StudentControllerHelper.buildRegistrationMap(studentId, registrationService);
            int regSuccess = 0, cancelSuccess = 0;
            StringBuilder errors = new StringBuilder();
            for (StudentDashboardRow row : data) {
                String offeringId = row.getOfferingId();
                if (offeringId == null || offeringId.trim().isEmpty()) continue;

                boolean selected = row.isSelected();
                boolean already = registrationService.hasRegistered(studentId, offeringId);

                if (selected && !already) {
                    // Register new course
                    try {
                        registrationService.registerCourse(studentId, offeringId);
                        regSuccess++;
                    } catch (Exception ex) {
                        errors.append("- Đăng ký ").append(offeringId).append(": ").append(ex.getMessage()).append("\n");
                    }
                } else if (!selected && already) {
                    // Cancel registration
                    String regId = StudentControllerHelper.findRegistrationId(offeringId, studentId, regsByOfferingId, registrationService);
                    
                    if (regId != null) {
                        try {
                            registrationService.cancelRegistration(regId);
                            cancelSuccess++;
                        } catch (Exception ex) {
                            errors.append("- Hủy đăng ký ").append(offeringId).append(": ").append(ex.getMessage()).append("\n");
                        }
                    } else {
                        errors.append("- Hủy đăng ký ").append(offeringId).append(": Không tìm thấy registrationId").append("\n");
                    }
                }
            }
            // Show results
            String successMessage = StudentControllerHelper.buildSuccessMessage(regSuccess, cancelSuccess);
            StudentControllerHelper.showRegistrationResults(successMessage, errors);
            loadData();
        } catch (Exception ex) {
            FXUtils.showError("Xác nhận thất bại: " + ex.getMessage());
        }
    }

    @FXML
    private void handleReload() {
        loadData();
    }

    private void loadData() {
        String studentIdForPreselect = null;
        // Hiển thị tên
        studentIdForPreselect = AuthUtils.setUserLabel(studentNameText, "Học viên: ", auth);

        // Get registered course offering IDs
        Set<String> registeredOfferingIds = StudentControllerHelper.getRegisteredOfferingIds(
            studentIdForPreselect, registrationService
        );
        // Load and display data
        data.clear();
        User currentUser = auth.getCurrentUser();
        Student currentStudent = auth.getCurrentStudent();
        List<CourseOffering> offerings = courseOfferingService.getAllCourseOfferings(currentUser);
        if (currentStudent != null && currentStudent.getFacultyId() != null) {
            String studentFacultyId = currentStudent.getFacultyId();
            offerings = offerings.stream()
                .filter(o -> studentFacultyId.equals(o.getFacultyId()))
                .collect(Collectors.toList());
        }
        for (CourseOffering offering : offerings) {
            // Convert to row
            StudentDashboardRow row = StudentControllerUtils.toStudentRow(
                offering,
                courseService,
                semesterRepository,
                roomRepository,
                courseOfferingScheduleService
            );
            // Check registration
            boolean isRegistered = offering.getCourseOfferingId() != null && 
                registeredOfferingIds.contains(offering.getCourseOfferingId());
            if (isRegistered) {
                row.getSelectedProperty().set(true);
            }
            // Add row 
            if (!showOnlyRegistered || isRegistered) {
                data.add(row);
            }
        }
    }
}
