package main.java.controller.admin;
 
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
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import main.java.utils.CsvUtils;
import main.java.service.impl.AdminServiceImpl;
import main.java.service.impl.StudentServiceImpl;

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
    @FXML private TableColumn<AdminDashboardOfferingRow, String> colOfferingFaculty;
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
    @FXML private Button userImportBtn;
    @FXML private Button userExportBtn;
    // Services
    private final AuthService auth = AuthServiceImpl.getInstance();
    private final CourseOfferingServiceImpl courseOfferingService = new CourseOfferingServiceImpl();
    private final CourseServiceImpl courseService = new CourseServiceImpl();
    private final CourseOfferingScheduleServiceImpl courseOfferingScheduleService = new CourseOfferingScheduleServiceImpl();
    private final UserServiceImpl userService = new UserServiceImpl();
    private final AdminServiceImpl adminService = new AdminServiceImpl();
    private final StudentServiceImpl studentService = new StudentServiceImpl();
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
        AdminDashboardOfferingRow.bindColumns( colOfferingCourseOfferingId, colOfferingCourseId, colOfferingCourseName, colOfferingCredits, colOfferingInstructor, colOfferingFaculty, colOfferingSemesterId, colOfferingSchedule, colOfferingRoomId, colOfferingMaxCapacity, colOfferingRemaining);
        AdminDashboardCourseRow.bindColumns(colCourseCourseId, colCourseCourseName, colCourseCredits, colFaculty);
        AdminDashboardUserRow.bindColumns(colUserUserId, colUserUsername, colUserFullname, colUserEmail, colUserRole);
        // Attach observable lists and UI settings
        setupTable(offeringTable, offeringData, colOfferingCourseOfferingId, colOfferingCourseId, colOfferingCourseName, colOfferingCredits, colOfferingInstructor, colOfferingFaculty, colOfferingSemesterId, colOfferingSchedule, colOfferingRoomId, colOfferingMaxCapacity, colOfferingRemaining);
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
            () -> {
                User currentUser = auth.getCurrentUser();
                return courseOfferingService.getAllCourseOfferings(currentUser);
            },
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
    private void handleUserImport(ActionEvent event) {
        try {
            File f = CsvUtils.chooseOpenCsv(event.getSource(), "Chọn file CSV sinh viên (.csv)");
            if (f == null) return;

            List<String> lines = CsvUtils.readLines(f);
            if (lines.isEmpty()) {
                FXUtils.showError("File rỗng");
                return;
            }

            int success = 0;
            List<String> errors = new ArrayList<>();
            int rowNo = 0;
            boolean firstIsHeader = lines.get(0).toLowerCase().contains("userid") || lines.get(0).toLowerCase().contains("username");
            for (String line : lines) {
                rowNo++;
                if (firstIsHeader && rowNo == 1) continue;
                if (line == null || line.trim().isEmpty()) continue;

                String[] cols = line.split(";", -1);
                if (cols.length < 9) {
                    errors.add("Dòng " + rowNo + ": Số cột không hợp lệ (cần 9). Detected:" + cols.length);
                    continue;
                }

                String userId = cols[0].trim();
                String username = cols[1].trim();
                String fullName = cols[2].trim();
                String email = cols[3].trim();
                String studentClass = cols[4].trim();
                String majorId = cols[5].trim();
                String facultyId = cols[6].trim();
                String status = cols[7].trim();
                String password = cols[8].trim();

                StringBuilder sb = new StringBuilder();
                if (userId.isEmpty()) sb.append("userId trống; ");
                if (username.isEmpty()) sb.append("username trống; ");
                if (fullName.isEmpty()) sb.append("fullName trống; ");
                if (email.isEmpty()) sb.append("email trống; ");
                if (studentClass.isEmpty()) sb.append("class trống; ");
                if (majorId.isEmpty()) sb.append("major trống; ");
                if (facultyId.isEmpty()) sb.append("faculty trống; ");
                if (status.isEmpty()) sb.append("status trống; ");
                if (password.isEmpty()) sb.append("password trống; ");

                if (sb.length() > 0) {
                    errors.add("Dòng " + rowNo + ": " + sb.toString());
                    continue;
                }

                main.java.model.Student s = new main.java.model.Student();
                s.setStudentId(userId);
                s.setUsername(username);
                s.setFullName(fullName);
                s.setEmail(email);
                s.setRole(0);
                s.setStudentClass(studentClass);
                s.setMajorId(majorId);
                s.setFacultyId(facultyId);
                s.setStatus(status);

                try {
                    var created = adminService.registerStudent(s, password, facultyId);
                    if (created != null) success++;
                    else errors.add("Dòng " + rowNo + ": Đăng ký thất bại.");
                } catch (Exception ex) {
                    errors.add("Dòng " + rowNo + ": Lỗi khi tạo - " + ex.getMessage());
                }
            }

            FXUtils.showSuccess("Import hoàn tất. Thành công: " + success + ", Lỗi: " + errors.size());
            if (!errors.isEmpty()) FXUtils.showError(String.join("\n", errors));
            loadUserData();
        } catch (Exception ex) {
            FXUtils.showError("Import thất bại: " + ex.getMessage());
        }
    }

    @FXML
    private void handleUserExport(ActionEvent event) {
        try {
            File target = CsvUtils.chooseSaveCsv(event.getSource(), "students_export.csv", "Lưu file CSV xuất sinh viên");
            if (target == null) return;

            List<main.java.model.Student> students = studentService.getAllStudents();
            String[] header = new String[] {"userId","username","fullName","email","studentClass","majorId","facultyId","status","password"};
            List<String[]> rows = new ArrayList<>();
            for (main.java.model.Student s : students) {
                rows.add(new String[] {
                    s.getStudentId(), s.getUsername(), s.getFullName(), s.getEmail(), s.getStudentClass(), s.getMajorId(), s.getFacultyId(), s.getStatus(), "123456"
                });
            }
            CsvUtils.writeCsv(target, header, rows);
            FXUtils.showSuccess("Xuất CSV thành công: " + target.getAbsolutePath());
        } catch (Exception ex) {
            FXUtils.showError("Export thất bại: " + ex.getMessage());
        }
    }

    // use CsvUtils.escape when needed
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
                        navigationManager.showCourseOfferingEditForm(fullOffering);
                        loadOfferingData(); // Reload after edit
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
                        navigationManager.showCourseEditForm(fullCourse);
                        loadCourseData(); // Reload after edit
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
                        navigationManager.showUserEditForm(fullUser);
                        loadUserData(); // Reload after edit
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
                        navigationManager.showUserDeleteConfirm(fullUser);
                        loadUserData();
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
                        navigationManager.showCourseDeleteConfirm(fullCourse);
                        loadCourseData();
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
                        navigationManager.showCourseOfferingDeleteConfirm(fullOffering);
                        loadOfferingData();
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
                        navigationManager.showUserDetailForm(fullUser);
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
                        navigationManager.showCourseDetailForm(fullCourse);
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
                        navigationManager.showCourseOfferingDetailForm(fullOffering);
                    }
                );
            }
        } catch (Exception ex) {
            FXUtils.showError("Hành động thất bại: " + ex.getMessage());
        }
    }
}

// 559