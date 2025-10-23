package main.java.service;

import java.util.List;
import main.java.model.CourseOffering;

/**
 * CourseOfferingService Interface - Business logic cho CourseOffering (Lớp mở)
 */
public interface CourseOfferingService {
    
    /**
     * Tạo lớp mở mới
     * @param courseOffering CourseOffering object
     * @param courseId Course ID
     * @param semesterId Semester ID
     * @param roomId Room ID
     * @return CourseOffering đã tạo
     */
    CourseOffering createCourseOffering(CourseOffering courseOffering, String courseId, 
                                       String semesterId, String roomId);
    
    /**
     * Lấy lớp mở theo ID
     * @param courseOfferingId CourseOffering ID
     * @return CourseOffering nếu tìm thấy
     */
    CourseOffering getCourseOfferingById(String courseOfferingId);
    
    /**
     * Lấy tất cả lớp mở
     * @return List danh sách course offerings
     */
    List<CourseOffering> getAllCourseOfferings();
    
    /**
     * Lấy lớp mở theo môn học
     * @param courseId Course ID
     * @return List danh sách course offerings
     */
    List<CourseOffering> getCourseOfferingsByCourse(String courseId);
    
    /**
     * Lấy lớp mở theo học kỳ
     * @param semesterId Semester ID
     * @return List danh sách course offerings
     */
    List<CourseOffering> getCourseOfferingsBySemester(String semesterId);
    
    /**
     * Lấy lớp mở theo phòng
     * @param roomId Room ID
     * @return List danh sách course offerings
     */
    List<CourseOffering> getCourseOfferingsByRoom(String roomId);
    
    /**
     * Lấy lớp mở theo môn học và học kỳ
     * @param courseId Course ID
     * @param semesterId Semester ID
     * @return List danh sách course offerings
     */
    List<CourseOffering> getCourseOfferingsByCourseAndSemester(String courseId, String semesterId);
    
    /**
     * Lấy lớp mở còn chỗ trống
     * @return List danh sách course offerings còn chỗ
     */
    List<CourseOffering> getAvailableCourseOfferings();
    
    /**
     * Lấy lớp mở còn chỗ trống theo học kỳ
     * @param semesterId Semester ID
     * @return List danh sách course offerings còn chỗ
     */
    List<CourseOffering> getAvailableCourseOfferingsBySemester(String semesterId);
    
    /**
     * Cập nhật thông tin lớp mở
     * @param courseOffering CourseOffering với thông tin mới
     * @return true nếu cập nhật thành công
     */
    boolean updateCourseOffering(CourseOffering courseOffering);
    
    /**
     * Xóa lớp mở
     * @param courseOfferingId CourseOffering ID
     * @return true nếu xóa thành công
     */
    boolean deleteCourseOffering(String courseOfferingId);
    
    /**
     * Kiểm tra lớp mở tồn tại
     * @param courseOfferingId CourseOffering ID
     * @return true nếu tồn tại
     */
    boolean existsCourseOffering(String courseOfferingId);
    
    /**
     * Kiểm tra lớp mở còn chỗ trống không
     * @param courseOfferingId CourseOffering ID
     * @return true nếu còn chỗ
     */
    boolean hasAvailableSlot(String courseOfferingId);
    
    /**
     * Lấy số chỗ trống còn lại
     * @param courseOfferingId CourseOffering ID
     * @return Số chỗ trống
     */
    int getAvailableSlots(String courseOfferingId);
    
    /**
     * Tăng số lượng sinh viên đã đăng ký (khi đăng ký thành công)
     * @param courseOfferingId CourseOffering ID
     * @return true nếu thành công
     */
    boolean incrementCurrentCapacity(String courseOfferingId);
    
    /**
     * Giảm số lượng sinh viên đã đăng ký (khi hủy đăng ký)
     * @param courseOfferingId CourseOffering ID
     * @return true nếu thành công
     */
    boolean decrementCurrentCapacity(String courseOfferingId);
    
    /**
     * Validate thông tin lớp mở
     * @param courseOffering CourseOffering cần validate
     * @return true nếu hợp lệ
     * @throws IllegalArgumentException nếu không hợp lệ
     */
    boolean validateCourseOffering(CourseOffering courseOffering) throws IllegalArgumentException;
}
