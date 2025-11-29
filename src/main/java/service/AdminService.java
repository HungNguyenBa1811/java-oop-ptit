package main.java.service;

import java.util.List;
import main.java.model.Admin;
import main.java.model.Course;
import main.java.model.CourseOffering;
import main.java.model.Student;
import main.java.model.User;

/**
 * AdminService Interface - Business logic cho Admin
 * Kế thừa từ UserService và bổ sung chức năng quản lý
 */
public interface AdminService extends UserService {
    
    /**
     * Đăng ký sinh viên mới (Admin tạo cho sinh viên)
     * @param student Student object
     * @param password Mật khẩu
     * @param majorId ID của ngành học
     * @return Student đã tạo, null nếu thất bại
     */
    Student registerStudent(Student student, String password, String majorId);
    
    /**
     * Đăng ký admin mới
     * @param admin Admin object
     * @param password Mật khẩu
     * @return Admin đã tạo, null nếu thất bại
     */
    Admin registerAdmin(Admin admin, String password);
    
    /**
     * Lấy admin theo ID
     * @param adminId Admin ID (User ID)
     * @return Admin nếu tìm thấy
     */
    Admin getAdminById(String adminId);
    
    /**
     * Lấy tất cả admins
     * @return List danh sách admins
     */
    List<Admin> getAllAdmins();
    
    /**
     * Xóa sinh viên (Admin có quyền xóa)
     * @param studentId Student ID
     * @return true nếu xóa thành công
     */
    boolean deleteStudent(String studentId);
    
    /**
     * Cập nhật thông tin sinh viên (Admin có quyền cập nhật)
     * @param student Student object với thông tin mới
     * @return true nếu cập nhật thành công
     */
    boolean updateStudent(Student student);
    
    /**
     * Reset mật khẩu cho user (Admin có quyền reset)
     * @param userId User ID
     * @param newPassword Mật khẩu mới
     * @return true nếu reset thành công
     */
    boolean resetPassword(String userId, String newPassword);
    
    /**
     * Đăng ký tín chỉ hộ sinh viên (Admin đăng ký hộ)
     * @param studentId Student ID
     * @param courseOfferingId Course Offering ID
     * @return Registration đã tạo, null nếu thất bại
     */
    main.java.model.Registration registerCourseForStudent(String studentId, String courseOfferingId);
    
    /**
     * Xóa đăng ký tín chỉ của sinh viên (Admin xóa hộ)
     * @param registrationId Registration ID
     * @return true nếu xóa thành công
     */
    boolean cancelRegistrationForStudent(String registrationId);
    
    /**
     * Lấy tất cả users (Admin có quyền xem tất cả)
     * @return List danh sách users
     */
    List<User> getAllUsersWithDetails();
    
    /**
     * Thống kê số lượng user theo role
     * @return Array [số admin, số sinh viên]
     */
    int[] getUserStatistics();
    
    // ========== QUẢN LÝ MÔN HỌC (COURSE) ==========
    
    /**
     * Tạo môn học mới (Admin)
     * @param course Course object
     * @param facultyId Faculty ID
     * @return Course đã tạo
     */
    Course createCourse(Course course, String facultyId);
    
    /**
     * Cập nhật môn học (Admin)
     * @param course Course với thông tin mới
     * @return true nếu thành công
     */
    boolean updateCourse(Course course);
    
    /**
     * Xóa môn học (Admin)
     * @param courseId Course ID
     * @return true nếu thành công
     */
    boolean deleteCourse(String courseId);
    
    /**
     * Lấy tất cả môn học
     * @return List danh sách courses
     */
    List<Course> getAllCourses();
    
    /**
     * Lấy môn học theo khoa
     * @param facultyId Faculty ID
     * @return List danh sách courses
     */
    List<Course> getCoursesByFaculty(String facultyId);
    
    // ========== QUẢN LÝ LỚP MỞ (COURSE OFFERING) ==========
    
    /**
     * Tạo lớp mở mới (Admin)
     * @param courseOffering CourseOffering object
     * @param courseId Course ID
     * @param semesterId Semester ID
     * @param roomId Room ID
     * @return CourseOffering đã tạo
     */
    CourseOffering createCourseOffering(CourseOffering courseOffering,
                                                       String courseId, String semesterId, String roomId);
    
    /**
     * Cập nhật lớp mở (Admin)
     * @param courseOffering CourseOffering với thông tin mới
     * @return true nếu thành công
     */
    boolean updateCourseOffering(CourseOffering courseOffering);
    
    /**
     * Xóa lớp mở (Admin)
     * @param courseOfferingId CourseOffering ID
     * @return true nếu thành công
     */
    boolean deleteCourseOffering(String courseOfferingId);
    
    /**
     * Lấy tất cả lớp mở
     * @return List danh sách course offerings
     */
    List<CourseOffering> getAllCourseOfferings();
    
    /**
     * Lấy lớp mở theo học kỳ
     * @param semesterId Semester ID
     * @return List danh sách course offerings
     */
    List<CourseOffering> getCourseOfferingsBySemester(String semesterId);
    
    /**
     * Lấy lớp mở còn chỗ trống
     * @return List danh sách course offerings còn chỗ
     */
    List<CourseOffering> getAvailableCourseOfferings();
}
