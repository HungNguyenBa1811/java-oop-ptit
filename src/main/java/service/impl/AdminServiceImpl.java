package main.java.service.impl;

import java.util.ArrayList;
import java.util.List;
import main.java.model.Admin;
import main.java.model.Course;
import main.java.model.CourseOffering;
import main.java.model.Registration;
import main.java.model.Student;
import main.java.model.User;
import main.java.repository.MajorRepository;
import main.java.repository.StudentRepository;
import main.java.repository.UserRepository;
import main.java.service.AdminService;
import main.java.service.CourseOfferingService;
import main.java.service.CourseService;
import main.java.service.RegistrationService;

/**
 * AdminServiceImpl - Implementation của AdminService
 * Kế thừa từ UserServiceImpl và bổ sung chức năng admin
 */
public class AdminServiceImpl extends UserServiceImpl implements AdminService {
    
    private final StudentRepository studentRepository;
    private final MajorRepository majorRepository;
    private final RegistrationService registrationService;
    private final CourseService courseService;
    private final CourseOfferingService courseOfferingService;
    
    public AdminServiceImpl() {
        super();
        this.studentRepository = new StudentRepository();
        this.majorRepository = new MajorRepository();
        this.registrationService = new RegistrationServiceImpl();
        this.courseService = new CourseServiceImpl();
        this.courseOfferingService = new CourseOfferingServiceImpl();
    }
    
    public AdminServiceImpl(UserRepository userRepository, 
                           StudentRepository studentRepository,
                           MajorRepository majorRepository,
                           RegistrationService registrationService,
                           CourseService courseService,
                           CourseOfferingService courseOfferingService) {
        super(userRepository);
        this.studentRepository = studentRepository;
        this.majorRepository = majorRepository;
        this.registrationService = registrationService;
        this.courseService = courseService;
        this.courseOfferingService = courseOfferingService;
    }
    
    @Override
    public Student registerStudent(Student student, String password, String majorId) {
        // Validate input
        if (student == null) {
            throw new IllegalArgumentException("Student không được null");
        }
        
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Password không được để trống");
        }
        
        if (majorId == null || majorId.trim().isEmpty()) {
            throw new IllegalArgumentException("Major ID không được để trống");
        }
        
        // Kiểm tra major tồn tại
        if (majorRepository.findById(majorId) == null) {
            throw new IllegalArgumentException("Ngành học không tồn tại: " + majorId);
        }
        
        // Validate student
        validateStudent(student);
        
        // Tạo User trước (vì student_id FK to users)
        User user = new User();
        user.setUserId(student.getStudentId()); // Student ID = User ID
        user.setUsername(student.getUsername());
        user.setPassword(password); // TODO: Hash password
        user.setFullName(student.getFullName());
        user.setEmail(student.getEmail());
        user.setRole(0); // 0 = sinh viên
        
        // Kiểm tra username đã tồn tại chưa
        if (isUsernameExists(user.getUsername())) {
            throw new IllegalArgumentException("Username đã tồn tại: " + user.getUsername());
        }
        
        // Kiểm tra email đã tồn tại chưa
        if (isEmailExists(user.getEmail())) {
            throw new IllegalArgumentException("Email đã tồn tại: " + user.getEmail());
        }
        
        // Tạo User
        boolean userCreated = userRepository.createUser(user);
        
        if (!userCreated) {
            throw new RuntimeException("Không thể tạo User cho sinh viên");
        }
        
        // Set major_id cho student
        student.setMajorId(majorId);
        
        // Tạo Student
        boolean studentCreated = studentRepository.createStudent(student);
        
        if (studentCreated) {
            System.out.println("Admin đã tạo sinh viên thành công: " + student.getStudentId());
            return student;
        } else {
            // Rollback: Xóa user nếu không tạo được student
            userRepository.deleteUser(student.getStudentId());
            throw new RuntimeException("Không thể tạo Student");
        }
    }
    
    @Override
    public Admin registerAdmin(Admin admin, String password) {
        // Validate input
        if (admin == null) {
            throw new IllegalArgumentException("Admin không được null");
        }
        
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Password không được để trống");
        }
        
        // Validate admin (sử dụng validateUser từ UserServiceImpl)
        User userValidation = new User();
        userValidation.setUserId(admin.getUserId());
        userValidation.setUsername(admin.getUsername());
        userValidation.setFullName(admin.getFullName());
        userValidation.setEmail(admin.getEmail());
        userValidation.setRole(1); // Admin
        
        validateUser(userValidation);
        
        // Kiểm tra username đã tồn tại chưa
        if (isUsernameExists(admin.getUsername())) {
            throw new IllegalArgumentException("Username đã tồn tại: " + admin.getUsername());
        }
        
        // Kiểm tra email đã tồn tại chưa
        if (isEmailExists(admin.getEmail())) {
            throw new IllegalArgumentException("Email đã tồn tại: " + admin.getEmail());
        }
        
        // Tạo User với role = 1 (admin)
        User user = new User();
        user.setUserId(admin.getUserId());
        user.setUsername(admin.getUsername());
        user.setPassword(password); // TODO: Hash password
        user.setFullName(admin.getFullName());
        user.setEmail(admin.getEmail());
        user.setRole(1);
        
        boolean created = userRepository.createUser(user);
        
        if (created) {
            System.out.println("Đăng ký admin thành công: " + admin.getUserId());
            return admin;
        }
        
        throw new RuntimeException("Không thể tạo Admin");
    }
    
    @Override
    public Admin getAdminById(String adminId) {
        if (adminId == null || adminId.trim().isEmpty()) {
            throw new IllegalArgumentException("Admin ID không được để trống");
        }
        
        User user = userRepository.findById(adminId);
        
        if (user == null) {
            return null;
        }
        
        // Kiểm tra role có phải admin không
        if (user.getRole() != 1) {
            return null;
        }
        
        // Convert User to Admin
        Admin admin = new Admin();
        admin.setUserId(user.getUserId());
        admin.setUsername(user.getUsername());
        admin.setFullName(user.getFullName());
        admin.setEmail(user.getEmail());
        admin.setRole(user.getRole());
        
        return admin;
    }
    
    @Override
    public List<Admin> getAllAdmins() {
        List<User> allUsers = userRepository.findAll();
        List<Admin> admins = new ArrayList<>();
        
        for (User user : allUsers) {
            if (user.getRole() == 1) {
                Admin admin = new Admin();
                admin.setUserId(user.getUserId());
                admin.setUsername(user.getUsername());
                admin.setFullName(user.getFullName());
                admin.setEmail(user.getEmail());
                admin.setRole(user.getRole());
                admins.add(admin);
            }
        }
        
        return admins;
    }
    
    @Override
    public boolean deleteStudent(String studentId) {
        if (studentId == null || studentId.trim().isEmpty()) {
            throw new IllegalArgumentException("Student ID không được để trống");
        }
        
        // Kiểm tra student tồn tại
        Student student = studentRepository.findById(studentId);
        if (student == null) {
            throw new IllegalArgumentException("Sinh viên không tồn tại: " + studentId);
        }
        
        // Xóa student (cascade sẽ xóa user)
        boolean deleted = studentRepository.deleteStudent(studentId);
        
        if (deleted) {
            System.out.println("Admin đã xóa sinh viên: " + studentId);
        }
        
        return deleted;
    }
    
    @Override
    public boolean updateStudent(Student student) {
        if (student == null) {
            throw new IllegalArgumentException("Student không được null");
        }
        
        // Validate student
        validateStudent(student);
        
        // Kiểm tra student tồn tại
        if (studentRepository.findById(student.getStudentId()) == null) {
            throw new IllegalArgumentException("Sinh viên không tồn tại: " + student.getStudentId());
        }
        
        // Cập nhật Student
        boolean studentUpdated = studentRepository.updateStudent(student);
        
        if (!studentUpdated) {
            return false;
        }
        
        // Cập nhật User
        User user = new User();
        user.setUserId(student.getStudentId());
        user.setUsername(student.getUsername());
        user.setFullName(student.getFullName());
        user.setEmail(student.getEmail());
        user.setRole(0); // Student
        
        boolean userUpdated = userRepository.updateUser(user);
        
        if (userUpdated) {
            System.out.println("Admin đã cập nhật sinh viên: " + student.getStudentId());
        }
        
        return userUpdated;
    }
    
    @Override
    public boolean resetPassword(String userId, String newPassword) {
        if (userId == null || userId.trim().isEmpty()) {
            throw new IllegalArgumentException("User ID không được để trống");
        }
        
        if (newPassword == null || newPassword.trim().isEmpty()) {
            throw new IllegalArgumentException("Password mới không được để trống");
        }
        
        // Validate password
        if (newPassword.length() < 6) {
            throw new IllegalArgumentException("Password phải có ít nhất 6 ký tự");
        }
        
        // Kiểm tra user tồn tại
        if (userRepository.findById(userId) == null) {
            throw new IllegalArgumentException("User không tồn tại: " + userId);
        }
        
        // TODO: Hash password trước khi lưu
        boolean updated = userRepository.updatePassword(userId, newPassword);
        
        if (updated) {
            System.out.println("Admin đã reset password cho user: " + userId);
        }
        
        return updated;
    }
    
    @Override
    public Registration registerCourseForStudent(String studentId, String courseOfferingId) {
        // Validate input
        if (studentId == null || studentId.trim().isEmpty()) {
            throw new IllegalArgumentException("Student ID không được để trống");
        }
        
        if (courseOfferingId == null || courseOfferingId.trim().isEmpty()) {
            throw new IllegalArgumentException("Course Offering ID không được để trống");
        }
        
        // Kiểm tra student tồn tại
        Student student = studentRepository.findById(studentId);
        if (student == null) {
            throw new IllegalArgumentException("Sinh viên không tồn tại: " + studentId);
        }
        
        // Gọi RegistrationService để đăng ký
        try {
            Registration registration = registrationService.registerCourse(studentId, courseOfferingId);
            System.out.println("Admin đã đăng ký tín chỉ hộ sinh viên " + studentId + ": " + registration.getRegistrationId());
            return registration;
        } catch (Exception e) {
            System.err.println("Lỗi khi admin đăng ký tín chỉ hộ: " + e.getMessage());
            throw e;
        }
    }
    
    @Override
    public boolean cancelRegistrationForStudent(String registrationId) {
        // Validate input
        if (registrationId == null || registrationId.trim().isEmpty()) {
            throw new IllegalArgumentException("Registration ID không được để trống");
        }
        
        // Kiểm tra registration tồn tại
        Registration registration = registrationService.getRegistrationById(registrationId);
        if (registration == null) {
            throw new IllegalArgumentException("Đăng ký không tồn tại: " + registrationId);
        }
        
        // Gọi RegistrationService để hủy đăng ký
        try {
            boolean cancelled = registrationService.cancelRegistration(registrationId);
            if (cancelled) {
                System.out.println("Admin đã hủy đăng ký tín chỉ hộ sinh viên: " + registrationId);
            }
            return cancelled;
        } catch (Exception e) {
            System.err.println("Lỗi khi admin hủy đăng ký tín chỉ: " + e.getMessage());
            throw e;
        }
    }
    
    @Override
    public List<User> getAllUsersWithDetails() {
        return userRepository.findAll();
    }
    
    @Override
    public int[] getUserStatistics() {
        List<User> allUsers = userRepository.findAll();
        
        int adminCount = 0;
        int studentCount = 0;
        
        for (User user : allUsers) {
            if (user.getRole() == 1) {
                adminCount++;
            } else {
                studentCount++;
            }
        }
        
        return new int[]{adminCount, studentCount};
    }
    
    /**
     * Validate Student data
     */
    private void validateStudent(Student student) {
        if (student.getStudentId() == null || student.getStudentId().trim().isEmpty()) {
            throw new IllegalArgumentException("Student ID không được để trống");
        }
        
        if (student.getUsername() == null || student.getUsername().trim().isEmpty()) {
            throw new IllegalArgumentException("Username không được để trống");
        }
        
        if (student.getFullName() == null || student.getFullName().trim().isEmpty()) {
            throw new IllegalArgumentException("Full name không được để trống");
        }
        
        if (student.getEmail() == null || student.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("Email không được để trống");
        }
        
        // Validate email format
        if (!student.getEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new IllegalArgumentException("Email không hợp lệ");
        }
        
        // Validate status
        if (student.getStatus() != null) {
            if (!"Đang học".equals(student.getStatus()) && !"Nghỉ học".equals(student.getStatus())) {
                throw new IllegalArgumentException("Status phải là 'Đang học' hoặc 'Nghỉ học'");
            }
        }
    }
    
    // ========== QUẢN LÝ MÔN HỌC (COURSE) ==========
    
    @Override
    public Course createCourse(Course course, String facultyId) {
        System.out.println("Admin đang tạo môn học: " + course.getCourseId());
        return courseService.createCourse(course, facultyId);
    }
    
    @Override
    public boolean updateCourse(Course course) {
        System.out.println("Admin đang cập nhật môn học: " + course.getCourseId());
        return courseService.updateCourse(course);
    }
    
    @Override
    public boolean deleteCourse(String courseId) {
        System.out.println("Admin đang xóa môn học: " + courseId);
        return courseService.deleteCourse(courseId);
    }
    
    @Override
    public List<Course> getAllCourses() {
        return courseService.getAllCourses();
    }
    
    @Override
    public List<Course> getCoursesByFaculty(String facultyId) {
        return courseService.getCoursesByFaculty(facultyId);
    }
    
    // ========== QUẢN LÝ LỚP MỞ (COURSE OFFERING) ==========
    
    @Override
    public CourseOffering createCourseOffering(CourseOffering courseOffering, String courseId, 
                                               String semesterId, String roomId) {
        System.out.println("Admin đang tạo lớp mở: " + courseOffering.getCourseOfferingId());
        return courseOfferingService.createCourseOffering(courseOffering, courseId, semesterId, roomId);
    }
    
    @Override
    public boolean updateCourseOffering(CourseOffering courseOffering) {
        System.out.println("Admin đang cập nhật lớp mở: " + courseOffering.getCourseOfferingId());
        return courseOfferingService.updateCourseOffering(courseOffering);
    }
    
    @Override
    public boolean deleteCourseOffering(String courseOfferingId) {
        System.out.println("Admin đang xóa lớp mở: " + courseOfferingId);
        return courseOfferingService.deleteCourseOffering(courseOfferingId);
    }
    
    @Override
    public List<CourseOffering> getAllCourseOfferings() {
        return courseOfferingService.getAllCourseOfferings();
    }
    
    @Override
    public List<CourseOffering> getCourseOfferingsBySemester(String semesterId) {
        return courseOfferingService.getCourseOfferingsBySemester(semesterId);
    }
    
    @Override
    public List<CourseOffering> getAvailableCourseOfferings() {
        return courseOfferingService.getAvailableCourseOfferings();
    }
    
}
