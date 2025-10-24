package main.java.service;

import java.util.List;
import main.java.model.Registration;

/**
 * RegistrationService Interface - Business logic cho đăng ký môn học
 */
public interface RegistrationService {
    
    /**
     * Đăng ký môn học cho sinh viên
     * @param studentId Student ID
     * @param courseOfferingId Course Offering ID
     * @return Registration nếu thành công
     */
    Registration registerCourse(String studentId, String courseOfferingId);
    
    /**
     * Hủy đăng ký môn học
     * @param registrationId Registration ID
     * @return true nếu thành công
     */
    boolean cancelRegistration(String registrationId);
    
    /**
     * Lấy registration theo ID
     * @param registrationId Registration ID
     * @return Registration nếu tìm thấy
     */
    Registration getRegistrationById(String registrationId);
    
    /**
     * Lấy tất cả registrations của sinh viên
     * @param studentId Student ID
     * @return List danh sách registrations
     */
    List<Registration> getRegistrationsByStudent(String studentId);
    
    /**
     * Lấy tất cả registrations của lớp mở
     * @param courseOfferingId Course Offering ID
     * @return List danh sách registrations
     */
    List<Registration> getRegistrationsByCourseOffering(String courseOfferingId);
    
    /**
     * Lấy registrations theo trạng thái
     * @param status Trạng thái ('Thành công' hoặc 'Thất bại')
     * @return List danh sách registrations
     */
    List<Registration> getRegistrationsByStatus(String status);
    
    /**
     * Kiểm tra sinh viên đã đăng ký lớp mở này chưa
     * @param studentId Student ID
     * @param courseOfferingId Course Offering ID
     * @return true nếu đã đăng ký
     */
    boolean hasRegistered(String studentId, String courseOfferingId);
    
    /**
     * Kiểm tra lớp mở còn chỗ không
     * @param courseOfferingId Course Offering ID
     * @return true nếu còn chỗ
     */
    boolean hasAvailableSlot(String courseOfferingId);
    
    /**
     * Cập nhật trạng thái đăng ký
     * @param registrationId Registration ID
     * @param status Trạng thái mới
     * @return true nếu thành công
     */
    boolean updateRegistrationStatus(String registrationId, String status);
}
