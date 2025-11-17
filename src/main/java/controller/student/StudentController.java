package main.java.controller.student;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import main.java.dto.StudentDashboardRow;
import main.java.model.Course;
import main.java.model.CourseOffering;
import main.java.model.Room;
import main.java.model.Schedule;
import main.java.model.Semester;
import main.java.model.User;
import main.java.model.Registration;
import main.java.repository.RoomRepository;
import main.java.repository.SemesterRepository;
import main.java.service.AuthService;
import main.java.service.impl.AuthServiceImpl;
import main.java.service.impl.CourseOfferingScheduleServiceImpl;
import main.java.service.impl.CourseOfferingServiceImpl;
import main.java.service.impl.CourseServiceImpl;
import main.java.service.impl.RegistrationServiceImpl;
import main.java.view.NavigationManager;
import main.java.utils.FXUtils;

import static main.java.utils.GenericUtils.safeParseInt;


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
        // Bind cột với data
        StudentDashboardRow.bindColumns(colOfferingId, colCourseId, colCourseName, colCredits, colInstructor, colSemester, colSchedule, colRoom, colCapacity, colRemaining, colSelect);
        
        // UI Patch
        studentMainTable.setEditable(true);
        colSelect.setEditable(true);
        colSelect.setSortable(false);
        for (TableColumn<?, ?> c : studentMainTable.getColumns()) c.setReorderable(false);
        studentMainTable.setItems(data);
    }

    private void bindActions() {
        //  Bind EventListener của nút
        if (reloadButton != null) reloadButton.setOnAction(e -> loadData());
        if (logoutButton != null) logoutButton.setOnAction(e -> handleLogout());
        if (showButton != null) showButton.setOnAction(e -> handleToggleShow());
        if (submitButton != null) {
            submitButton.setOnAction(e -> handleSubmit());
            submitButton.setDisable(showOnlyRegistered);
        }
    }

    private void handleLogout() {
        // Đăng xuất
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

    private void handleSubmit() {
        try {
            User current = auth.getCurrentUser();
            if (current == null || !auth.isStudent()) {
                FXUtils.showError("Chỉ sinh viên mới được thao tác đăng ký/hủy đăng ký");
                return;
            }

            String studentId = current.getUserId();

            Map<String, Registration> regsByOfferingId = new java.util.HashMap<>();
            try {
                List<Registration> existingRegs = registrationService.getRegistrationsByStudent(studentId);
                for (Registration r : existingRegs) {
                    if (r.getCourseOfferingId() != null) {
                        regsByOfferingId.put(r.getCourseOfferingId(), r);
                    }
                }
            } catch (Exception e) {
            }

            int regSuccess = 0, regFail = 0, cancelSuccess = 0, cancelFail = 0;
            StringBuilder errors = new StringBuilder();

            for (StudentDashboardRow row : data) {
                String offeringId = row.getOfferingId();
                if (offeringId == null || offeringId.trim().isEmpty()) continue;

                boolean selected = row.isSelected();
                boolean already = registrationService.hasRegistered(studentId, offeringId);

                if (selected) {
                    // Chưa tick mà tick vào = đăng ký
                    if (!already) {
                        try {
                            registrationService.registerCourse(studentId, offeringId);
                            regSuccess++;
                        } catch (Exception ex) {
                            regFail++;
                            errors.append("- Đăng ký ").append(offeringId).append(": ").append(ex.getMessage()).append("\n");
                        }
                    }
                } else {
                    // Đã tick mà bỏ tick = hủy đăng ký
                    if (already) {
                        Registration reg = regsByOfferingId.get(offeringId);
                        String regId = reg != null ? reg.getRegistrationId() : null;

                        if (regId == null) {
                            // Fallback bằng cách reload danh sách để tìm lại
                            try {
                                List<Registration> fallback = registrationService.getRegistrationsByStudent(studentId);
                                for (Registration r : fallback) {
                                    if (offeringId.equals(r.getCourseOfferingId())) {
                                        regId = r.getRegistrationId();
                                        break;
                                    }
                                }
                            } catch (Exception e) {
                            }
                        }
                        
                        // Nếu reload xong và tìm được
                        if (regId != null) {
                            try {
                                registrationService.cancelRegistration(regId);
                                cancelSuccess++;
                            } catch (Exception ex) {
                                cancelFail++;
                                errors.append("- Hủy đăng ký ").append(offeringId).append(": ").append(ex.getMessage()).append("\n");
                            }
                        } else {
                            cancelFail++;
                            errors.append("- Hủy đăng ký ").append(offeringId).append(": Không tìm thấy registrationId").append("\n");
                        }
                    }
                }
            }

            // Msg builder
            StringBuilder successMsg = new StringBuilder();
            if (regSuccess > 0) {
                successMsg.append("Đăng ký thành công ").append(regSuccess).append(" lớp. ");
            }
            if (cancelSuccess > 0) {
                successMsg.append("Hủy đăng ký thành công ").append(cancelSuccess).append(" lớp.");
            }
            if (successMsg.length() > 0) {
                FXUtils.showSuccess(successMsg.toString().trim());
            }
            if (errors.length() > 0) {
                FXUtils.showError("Một số thao tác thất bại:\n" + errors.toString());
            }

            // Refresh table
            loadData();
        } catch (Exception ex) {
            FXUtils.showError("Xác nhận thất bại: " + ex.getMessage());
        }
    }

    private void loadData() {
        String studentIdForPreselect = null;

        // Hien thi ten
        try {
            AuthService auth = AuthServiceImpl.getInstance();
            User current = auth.getCurrentUser();
            String displayName = (current != null && current.getFullName() != null && !current.getFullName().trim().isEmpty())
                ? current.getFullName().trim()
                : "Không xác định 1";
            studentNameText.setText("Học viên: " + displayName);
            if (current != null) {
                studentIdForPreselect = current.getUserId();
            }
        } catch (Exception ex) {
            studentNameText.setText("Học viên: Không xác định 2");
        }

        // Tick fetch
        Set<String> registeredOfferingIds = new HashSet<>();
        try {
            if (studentIdForPreselect != null) {
                List<Registration> regs = registrationService.getRegistrationsByStudent(studentIdForPreselect);
                registeredOfferingIds.addAll(
                    regs.stream()
                        .filter(r -> r.getCourseOfferingId() != null)
                        .map(Registration::getCourseOfferingId)
                        .collect(Collectors.toSet())
                );
            }
        } catch (Exception ex) {
        }

        // Hien thi data
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
                if (semester != null) {
                    String term = semester.getTerm();
                    String year = semester.getAcademicYear();
                    if (term != null && year != null) {
                        semesterText = term + "/" + year;
                    } else if (term != null) {
                        semesterText = term;
                    } else {
                        semesterText = oitem.getSemesterId();
                    }
                } else {
                    semesterText = oitem.getSemesterId();
                }
            }
            String roomText = "-";
            if (oitem.getRoomId() != null) {
                Room room = roomRepository.findById(oitem.getRoomId());
                roomText = (room != null && room.getRoomId() != null) ? room.getRoomId() : oitem.getRoomId();
            }
            String instructor = oitem.getInstructor() != null ? oitem.getInstructor() : "-";
            String scheduleText = "-";
            try {
                List<Schedule> schedules = courseOfferingScheduleService.getSchedulesByCourseOfferingId(oitem.getCourseOfferingId());
                if (schedules != null && !schedules.isEmpty()) {
                    DateTimeFormatter tf = DateTimeFormatter.ofPattern("HH:mm");
                    scheduleText = schedules.stream().map(s -> {
                        int d = s.getDayOfWeek();
                        String day;
                        switch (d) {
                            case 2: day = "T2"; break;
                            case 3: day = "T3"; break;
                            case 4: day = "T4"; break;
                            case 5: day = "T5"; break;
                            case 6: day = "T6"; break;
                            case 7: day = "T7"; break;
                            case 8: day = "CN"; break;
                            case 1: day = "CN"; break;
                            default: day = "T?"; break;
                        }
                        String start = s.getStartTime() != null ? tf.format(s.getStartTime()) : "";
                        String end = s.getEndTime() != null ? tf.format(s.getEndTime()) : "";
                        String time = (!start.isEmpty() && !end.isEmpty()) ? (start + "-" + end) : (start + end);
                        return day + (time.isEmpty() ? "" : " " + time);
                    }).collect(Collectors.joining("\n"));
                }
            } catch (Exception e) {
                scheduleText = "-";
            }

            int current = safeParseInt(oitem.getCurrentCapacity(), 0);
            int max = safeParseInt(oitem.getMaxCapacity(), 0);
            String capacity = String.valueOf(max);
            String remaining = String.valueOf(Math.max(0, max - current));

            StudentDashboardRow row = new StudentDashboardRow(
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
            );

            // Determine registration status for filtering and tick
            boolean isRegistered = oitem.getCourseOfferingId() != null && registeredOfferingIds.contains(oitem.getCourseOfferingId());
            if (isRegistered) {
                row.getSelectedProperty().set(true);
            }

            // Add row depending on show mode
            if (!showOnlyRegistered || isRegistered) {
                data.add(row);
            }
        }
    }
}
