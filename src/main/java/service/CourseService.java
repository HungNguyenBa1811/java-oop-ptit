package main.java.service;

import java.util.List;
import main.java.model.Course;

/**
 * CourseService Interface - Business logic cho Course (Môn học)
 */
public interface CourseService {
    
    /**
     * Tạo môn học mới
     * @param course Course object
     * @param facultyId Faculty ID
     * @return Course đã tạo
     */
    Course createCourse(Course course, String facultyId);
    
    /**
     * Lấy môn học theo ID
     * @param courseId Course ID
     * @return Course nếu tìm thấy
     */
    Course getCourseById(String courseId);
    
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
    
    /**
     * Lấy môn học theo số tín chỉ
     * @param credits Số tín chỉ
     * @return List danh sách courses
     */
    List<Course> getCoursesByCredits(String credits);
    
    /**
     * Tìm kiếm môn học theo tên
     * @param courseName Tên môn học (có thể partial match)
     * @return List danh sách courses
     */
    List<Course> searchCoursesByName(String courseName);
    
    /**
     * Cập nhật thông tin môn học
     * @param course Course với thông tin mới
     * @return true nếu cập nhật thành công
     */
    boolean updateCourse(Course course);
    
    /**
     * Xóa môn học
     * @param courseId Course ID
     * @return true nếu xóa thành công
     */
    boolean deleteCourse(String courseId);
    
    /**
     * Kiểm tra môn học tồn tại
     * @param courseId Course ID
     * @return true nếu tồn tại
     */
    boolean existsCourse(String courseId);
    
    /**
     * Validate thông tin môn học
     * @param course Course cần validate
     * @return true nếu hợp lệ
     * @throws IllegalArgumentException nếu không hợp lệ
     */
    boolean validateCourse(Course course) throws IllegalArgumentException;
}
