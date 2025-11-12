package main.java.controller;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import main.java.model.*;
import main.java.repository.*;
import main.java.service.AdminService;
import main.java.service.impl.AdminServiceImpl;
import main.java.utils.FXUtils; // (Giả sử bạn có file này)
import main.java.view.NavigationManager; // (Giả sử bạn có file này)

import java.util.List;
import java.util.Optional;

/**
 * AdminController - PHIÊN BẢN FXML
 * Xử lý các sự kiện từ admindashboard.fxml (Đã có Kéo-Thả)
 */
public class AdminController {

    // === CÁC BIẾN @FXML LIÊN KẾT VỚI HEADER ===
    @FXML private Label adminNameLabel;
    @FXML private Button logoutButton;

    // === TAB 1: Quản lý Lớp học phần ===
    @FXML private TableView<CourseOffering> courseOfferingTableView;
    @FXML private TableColumn<CourseOffering, String> colOfferingMaHP;
    @FXML private TableColumn<CourseOffering, String> colOfferingMaMH;
    @FXML private TableColumn<CourseOffering, String> colOfferingTenMH;
    @FXML private TableColumn<CourseOffering, Integer> colOfferingSoTinChi;
    @FXML private TableColumn<CourseOffering, String> colOfferingGiangVien;
    @FXML private TableColumn<CourseOffering, String> colOfferingHocKy;
    @FXML private TableColumn<CourseOffering, String> colOfferingLichHoc;
    @FXML private TableColumn<CourseOffering, String> colOfferingPhong;
    @FXML private TableColumn<CourseOffering, Integer> colOfferingSiSo;
    @FXML private TableColumn<CourseOffering, Integer> colOfferingSiSoConLai;

    // === TAB 2: Quản lý Môn học ===
    @FXML private TableView<Course> courseTableView;
    @FXML private TableColumn<Course, String> colCourseId;
    @FXML private TableColumn<Course, String> colCourseName;
    @FXML private TableColumn<Course, Integer> colCourseCredits;
    @FXML private TableColumn<Course, String> colCourseFaculty;

    // === TAB 3: Quản lý Người dùng ===
    @FXML private TableView<User> userTableView; // (Hiển thị User)
    @FXML private TableColumn<User, String> colUserId;
    @FXML private TableColumn<User, String> colUserName;
    @FXML private TableColumn<User, String> colUserFullName;
    @FXML private TableColumn<User, String> colUserEmail;
    @FXML private TableColumn<User, Integer> colUserRole;

    // === TAB 4: Quản lý Đăng ký ===
    @FXML private TableView<Registration> registrationTableView;
    @FXML private TableColumn<Registration, String> colRegId;
    @FXML private TableColumn<Registration, String> colRegStudentId;
    @FXML private TableColumn<Registration, String> colRegStudentName;
    @FXML private TableColumn<Registration, String> colRegOfferingId;
    @FXML private TableColumn<Registration, String> colRegCourseName;
    @FXML private TableColumn<Registration, String> colRegTimestamp;
    @FXML private TableColumn<Registration, String> colRegStatus;

    // === TAB 5: Quản lý Học vụ (Khoa/Ngành) ===
    @FXML private TableView<Faculty> facultyTableView;
    @FXML private TableColumn<Faculty, String> colFacultyId;
    @FXML private TableColumn<Faculty, String> colFacultyName;
    @FXML private TableColumn<Faculty, String> colFacultyDean;
    
    @FXML private TableView<Major> majorTableView;
    @FXML private TableColumn<Major, String> colMajorId;
    @FXML private TableColumn<Major, String> colMajorName;
    @FXML private TableColumn<Major, String> colMajorFacultyId;

    // === TAB 6: Quản lý Cơ sở (Phòng/Học kỳ/Lịch) ===
    @FXML private TableView<Room> roomTableView;
    @FXML private TableColumn<Room, String> colRoomId;
    @FXML private TableColumn<Room, Integer> colRoomCapacity;
    @FXML private TableColumn<Room, Boolean> colRoomProjector;
    @FXML private TableColumn<Room, Boolean> colRoomAc;
    
    @FXML private TableView<Semester> semesterTableView;
    @FXML private TableColumn<Semester, String> colSemesterId;
    @FXML private TableColumn<Semester, String> colSemesterTerm;
    @FXML private TableColumn<Semester, String> colSemesterYear;
    @FXML private TableColumn<Semester, String> colSemesterStart;
    @FXML private TableColumn<Semester, String> colSemesterEnd;
    
    @FXML private TableView<Schedule> scheduleTableView;
    @FXML private TableColumn<Schedule, String> colScheduleId;
    @FXML private TableColumn<Schedule, Integer> colScheduleDay;
    @FXML private TableColumn<Schedule, String> colScheduleStart;
    @FXML private TableColumn<Schedule, String> colScheduleEnd;

    // === BIẾN CHO THÙNG RÁC KÉO-THẢ ===
    @FXML private ImageView deleteTrashCan;
    
    // Biến tạm để giữ đối tượng đang được kéo
    private Object draggedItem = null; 

    // === SERVICES VÀ REPOSITORIES ===
    private AdminService adminService; 
    private RegistrationRepository registrationRepository;
    private FacultyRepository facultyRepository;
    private MajorRepository majorRepository;
    private RoomRepository roomRepository;
    private SemesterRepository semesterRepository;
    private ScheduleRepository scheduleRepository;

    @FXML
    void initialize() {
        // Khởi tạo các Service và Repo cần thiết
        this.adminService = new AdminServiceImpl(); 
        this.registrationRepository = new RegistrationRepository();
        this.facultyRepository = new FacultyRepository();
        this.majorRepository = new MajorRepository();
        this.roomRepository = new RoomRepository();
        this.semesterRepository = new SemesterRepository();
        this.scheduleRepository = new ScheduleRepository();
        
        // Thiết lập CellValueFactory cho TẤT CẢ các cột
        setupAllTableColumns();
        
        // Tải dữ liệu ban đầu cho tất cả các bảng
        handleReloadAllTables(null);
        
        // TODO: Lấy tên admin từ AuthService và hiển thị
        // adminNameLabel.setText(...);
    }
    
    /**
     * Helper: Thiết lập PropertyValueFactory cho tất cả các cột
     */
    private void setupAllTableColumns() {
        // Tab 1
        colOfferingMaHP.setCellValueFactory(new PropertyValueFactory<>("courseOfferingId"));
        colOfferingMaMH.setCellValueFactory(new PropertyValueFactory<>("courseId"));
        // (Bạn sẽ cần tạo các Property "Ảo" cho các cột join như Tên môn, Tên học kỳ...)
        // colOfferingTenMH.setCellValueFactory(...); 
        colOfferingGiangVien.setCellValueFactory(new PropertyValueFactory<>("instructor"));
        colOfferingSiSo.setCellValueFactory(new PropertyValueFactory<>("maxCapacity"));
        colOfferingSiSoConLai.setCellValueFactory(new PropertyValueFactory<>("currentCapacity"));
        // ...

        // Tab 2
        colCourseId.setCellValueFactory(new PropertyValueFactory<>("courseId"));
        colCourseName.setCellValueFactory(new PropertyValueFactory<>("courseName"));
        colCourseCredits.setCellValueFactory(new PropertyValueFactory<>("credits"));
        colCourseFaculty.setCellValueFactory(new PropertyValueFactory<>("facultyId"));

        // Tab 3
        colUserId.setCellValueFactory(new PropertyValueFactory<>("userId"));
        colUserName.setCellValueFactory(new PropertyValueFactory<>("username"));
        colUserFullName.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        colUserEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colUserRole.setCellValueFactory(new PropertyValueFactory<>("role")); // (Cần converter 0/1 -> Student/Admin)

        // ... (Thiết lập cho các tab 4, 5, 6) ...
    }
    
    /**
     * Helper: Tải lại dữ liệu cho TẤT CẢ các bảng
     */
    @FXML
    private void handleReloadAllTables(ActionEvent event) {
        handleReloadCourseOfferings(null);
        handleReloadCourses(null);
        handleReloadUsers(null);
        handleReloadRegistrations(null);
        handleReloadFaculties(null);
        handleReloadMajors(null);
        handleReloadRooms(null);
        handleReloadSemesters(null);
        handleReloadSchedules(null);
    }

    @FXML
    void handleLogout(ActionEvent event) {
        System.out.println("Đăng xuất");
        // TODO: Gọi NavigationManager để quay về màn hình Login
        // NavigationManager nav = new NavigationManager((Stage) logoutButton.getScene().getWindow());
        // nav.showLoginScreen();
    }

    // === [MỚI] CÁC HÀM XỬ LÝ KÉO-THẢ ===

    /**
     * B1: Kích hoạt khi bắt đầu kéo một hàng
     */
    @FXML
    void handleDragDetected(MouseEvent event) {
        TableView<?> sourceTable = (TableView<?>) event.getSource();
        Object selectedItem = sourceTable.getSelectionModel().getSelectedItem();
        
        if (selectedItem == null) {
            return;
        }

        // Lưu trữ đối tượng đang kéo
        draggedItem = selectedItem; 

        Dragboard db = sourceTable.startDragAndDrop(TransferMode.MOVE);
        ClipboardContent content = new ClipboardContent();
        
        // Xác định loại đối tượng đang kéo
        if (selectedItem instanceof CourseOffering) content.putString("CourseOffering");
        else if (selectedItem instanceof Course) content.putString("Course");
        else if (selectedItem instanceof User) content.putString("User");
        // (Thêm các loại khác nếu cần)
        
        db.setContent(content);
        event.consume();
    }

    /**
     * B2: Kích hoạt khi có thứ gì đó được kéo VÀO khu vực thùng rác
     */
    @FXML
    void handleDragOver(DragEvent event) {
        if (event.getGestureSource() instanceof TableView && event.getDragboard().hasString()) {
            event.acceptTransferModes(TransferMode.MOVE);
        }
        event.consume();
    }

    /**
     * B3: Kích hoạt khi thả item VÀO thùng rác (Chỉ để Xoá)
     */
    @FXML
    void handleDragDropped(DragEvent event) {
        boolean success = false;
        if (draggedItem != null) {
            
            // Hiển thị xác nhận trước khi xoá
            if (showDeleteConfirmation(draggedItem)) {
                try {
                    // PHÂN LOẠI ĐỐI TƯỢNG VÀ GỌI HÀM XOÁ TƯƠNG ỨNG
                    if (draggedItem instanceof CourseOffering) {
                        adminService.deleteCourseOffering(((CourseOffering) draggedItem).getCourseOfferingId());
                        handleReloadCourseOfferings(null); // Tải lại bảng
                        
                    } else if (draggedItem instanceof Course) {
                        adminService.deleteCourse(((Course) draggedItem).getCourseId());
                        handleReloadCourses(null); // Tải lại bảng
                        
                    } else if (draggedItem instanceof User) {
                        // (Cẩn thận: Xoá User sẽ xoá cả Student/Admin do ràng buộc CSDL)
                        adminService.deleteUser(((User) draggedItem).getUserId());
                        handleReloadUsers(null); // Tải lại bảng
                    
                    } else if (draggedItem instanceof Registration) {
                        adminService.cancelRegistrationForStudent(((Registration) draggedItem).getRegistrationId());
                        handleReloadRegistrations(null);
                    
                    } else if (draggedItem instanceof Faculty) {
                        facultyRepository.deleteFaculty(((Faculty) draggedItem).getFacultyId());
                        handleReloadFaculties(null);
                    
                    } else if (draggedItem instanceof Major) {
                        majorRepository.deleteMajor(((Major) draggedItem).getMajorId());
                        handleReloadMajors(null);
                    
                    } else if (draggedItem instanceof Room) {
                        roomRepository.deleteRoom(((Room) draggedItem).getRoomId());
                        handleReloadRooms(null);
                    
                    } else if (draggedItem instanceof Semester) {
                        semesterRepository.deleteSemester(((Semester) draggedItem).getSemesterId());
                        handleReloadSemesters(null);
                    
                    } else if (draggedItem instanceof Schedule) {
                        scheduleRepository.deleteSchedule(((Schedule) draggedItem).getScheduleId());
                        handleReloadSchedules(null);
                    }
                    
                    success = true;
                } catch (Exception e) {
                    FXUtils.showError("Lỗi khi xoá: " + e.getMessage());
                    e.printStackTrace(); 
                }
            }
        }
        
        event.setDropCompleted(success);
        draggedItem = null; // Xóa tham chiếu
        event.consume();
    }

    /**
     * Hiển thị hộp thoại xác nhận xoá
     */
    private boolean showDeleteConfirmation(Object item) {
        String itemName = "mục đã chọn";
        try {
            if (item instanceof Course) itemName = "Môn học: " + ((Course) item).getCourseName();
            else if (item instanceof CourseOffering) itemName = "Lớp HP: " + ((CourseOffering) item).getCourseOfferingId();
            else if (item instanceof User) itemName = "Người dùng: " + ((User) item).getFullName();
            else if (item instanceof Registration) itemName = "Đăng ký: " + ((Registration) item).getRegistrationId();
            else if (item instanceof Faculty) itemName = "Khoa: " + ((Faculty) item).getFacultyName();
            else if (item instanceof Major) itemName = "Ngành: " + ((Major) item).getMajorName();
        } catch (Exception e) {}

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Xác nhận Xóa");
        alert.setHeaderText("Bạn có chắc chắn muốn xóa mục này?");
        alert.setContentText(itemName);

        Optional<ButtonType> result = alert.showAndWait();
        return (result.isPresent() && result.get() == ButtonType.OK);
    }
    
    // === CÁC HÀM XỬ LÝ NÚT BẤM (Thêm/Sửa/Xoá/Tải lại) ===

    // --- TAB 1: LỚP HỌC PHẦN ---
    @FXML void handleReloadCourseOfferings(ActionEvent event) {
        // List<CourseOffering> data = adminService.getAllCourseOfferings();
        // courseOfferingTableView.setItems(FXCollections.observableArrayList(data));
        System.out.println("Tải lại Lớp học phần");
    }
    @FXML void handleAddCourseOffering(ActionEvent event) {
        System.out.println("Mở form Thêm Lớp học phần");
        // TODO: Mở form 'courseOfferingForm.fxml'
    }
    @FXML void handleEditCourseOffering(ActionEvent event) { System.out.println("Mở form Sửa Lớp học phần"); }
    @FXML void handleDeleteCourseOffering(ActionEvent event) {
        // Đây là xoá bằng nút bấm (thay thế cho kéo-thả)
        Object selected = courseOfferingTableView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            handleDragDropped(new DragEvent(DragEvent.DRAG_DROPPED, null, 0, 0, TransferMode.MOVE, null, null, null));
        }
    }

    // --- TAB 2: MÔN HỌC ---
    @FXML void handleReloadCourses(ActionEvent event) {
        // List<Course> data = adminService.getAllCourses();
        // courseTableView.setItems(FXCollections.observableArrayList(data));
        System.out.println("Tải lại Môn học");
    }
    @FXML void handleAddCourse(ActionEvent event) {
         System.out.println("Mở form Thêm Môn học");
        // TODO: Mở form 'courseForm.fxml'
    }
    @FXML void handleEditCourse(ActionEvent event) { System.out.println("Mở form Sửa Môn học"); }
    @FXML void handleDeleteCourse(ActionEvent event) {
        Object selected = courseTableView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            handleDragDropped(new DragEvent(DragEvent.DRAG_DROPPED, null, 0, 0, TransferMode.MOVE, null, null, null));
        }
    }

    // --- TAB 3: NGƯỜI DÙNG ---
    @FXML void handleReloadUsers(ActionEvent event) {
        // List<User> data = adminService.getAllUsersWithDetails();
        // userTableView.setItems(FXCollections.observableArrayList(data));
        System.out.println("Tải lại Người dùng");
    }
    @FXML void handleAddUser(ActionEvent event) {
         System.out.println("Mở form Thêm Người dùng");
         // TODO: Mở form 'userForm.fxml'
    }
    @FXML void handleEditUser(ActionEvent event) { System.out.println("Mở form Sửa Người dùng"); }
    @FXML void handleDeleteUser(ActionEvent event) {
        Object selected = userTableView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            handleDragDropped(new DragEvent(DragEvent.DRAG_DROPPED, null, 0, 0, TransferMode.MOVE, null, null, null));
        }
    }

    // --- TAB 4: ĐĂNG KÝ ---
    @FXML void handleReloadRegistrations(ActionEvent event) {
        // List<Registration> data = registrationRepository.findAll();
        // registrationTableView.setItems(FXCollections.observableArrayList(data));
        System.out.println("Tải lại Đăng ký");
    }
    @FXML void handleApproveRegistration(ActionEvent event) { System.out.println("Duyệt Đăng ký"); }
    @FXML void handleCancelRegistration(ActionEvent event) {
        Object selected = registrationTableView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            handleDragDropped(new DragEvent(DragEvent.DRAG_DROPPED, null, 0, 0, TransferMode.MOVE, null, null, null));
        }
    }

    // --- TAB 5: KHOA/NGÀNH ---
    @FXML void handleReloadFaculties(ActionEvent event) {
        // List<Faculty> data = facultyRepository.findAll();
        // facultyTableView.setItems(FXCollections.observableArrayList(data));
        System.out.println("Tải lại Khoa");
    }
    @FXML void handleAddFaculty(ActionEvent event) { System.out.println("Thêm Khoa"); }
    @FXML void handleEditFaculty(ActionEvent event) { System.out.println("Sửa Khoa"); }
    
    @FXML void handleReloadMajors(ActionEvent event) {
        // List<Major> data = majorRepository.findAll();
        // majorTableView.setItems(FXCollections.observableArrayList(data));
        System.out.println("Tải lại Ngành");
    }
    @FXML void handleAddMajor(ActionEvent event) { System.out.println("Thêm Ngành"); }
    @FXML void handleEditMajor(ActionEvent event) { System.out.println("Sửa Ngành"); }

    // --- TAB 6: CƠ SỞ ---
    @FXML void handleReloadRooms(ActionEvent event) {
        // List<Room> data = roomRepository.findAll();
        // roomTableView.setItems(FXCollections.observableArrayList(data));
        System.out.println("Tải lại Phòng");
    }
    @FXML void handleAddRoom(ActionEvent event) { System.out.println("Thêm Phòng"); }
    @FXML void handleEditRoom(ActionEvent event) { System.out.println("Sửa Phòng"); }
    
    @FXML void handleReloadSemesters(ActionEvent event) {
        // List<Semester> data = semesterRepository.findAll();
        // semesterTableView.setItems(FXCollections.observableArrayList(data));
        System.out.println("Tải lại Học kỳ");
    }
    @FXML void handleAddSemester(ActionEvent event) { System.out.println("Thêm Học kỳ"); }
    @FXML void handleEditSemester(ActionEvent event) { System.out.println("Sửa Học kỳ"); }
    
    @FXML void handleReloadSchedules(ActionEvent event) {
        // List<Schedule> data = scheduleRepository.findAll();
        // scheduleTableView.setItems(FXCollections.observableArrayList(data));
        System.out.println("Tải lại Lịch học");
    }
    @FXML void handleAddSchedule(ActionEvent event) { System.out.println("Thêm Lịch học"); }
    @FXML void handleEditSchedule(ActionEvent event) { System.out.println("Sửa Lịch học"); }
}