package main.java.service;

import java.sql.Date;
import java.util.List;
import main.java.model.CourseOfferingSchedule;
import main.java.model.Schedule;

/**
 * CourseOfferingScheduleService - Interface cho business logic của CourseOfferingSchedule
 */
public interface CourseOfferingScheduleService {
    
    /**
     * Tạo liên kết mới giữa course offering và schedule
     * @param courseOfferingId ID của course offering
     * @param scheduleId ID của schedule
     * @param startDate Ngày bắt đầu
     * @param endDate Ngày kết thúc
     * @return true nếu tạo thành công
     */
    boolean assignScheduleToCourseOffering(String courseOfferingId, String scheduleId, 
                                          Date startDate, Date endDate);
    
    /**
     * Lấy tất cả liên kết
     * @return List<CourseOfferingSchedule>
     */
    List<CourseOfferingSchedule> getAllCourseOfferingSchedules();
    
    /**
     * Lấy liên kết theo ID
     * @param id ID của liên kết
     * @return CourseOfferingSchedule hoặc null
     */
    CourseOfferingSchedule getCourseOfferingScheduleById(int id);
    
    /**
     * Lấy tất cả liên kết của một course offering
     * @param courseOfferingId ID của course offering
     * @return List<CourseOfferingSchedule>
     */
    List<CourseOfferingSchedule> getCourseOfferingSchedulesByCourseOffering(String courseOfferingId);
    
    /**
     * Lấy danh sách Schedule objects của một course offering
     * @param courseOfferingId ID của course offering
     * @return List<Schedule> danh sách lịch học đầy đủ
     */
    List<Schedule> getSchedulesByCourseOfferingId(String courseOfferingId);
    
    /**
     * Lấy tất cả liên kết của một schedule
     * @param scheduleId ID của schedule
     * @return List<CourseOfferingSchedule>
     */
    List<CourseOfferingSchedule> getCourseOfferingSchedulesBySchedule(String scheduleId);
    
    /**
     * Xóa liên kết theo ID
     * @param id ID của liên kết
     * @return true nếu xóa thành công
     */
    boolean deleteCourseOfferingSchedule(int id);
    
    /**
     * Xóa tất cả liên kết của một course offering
     * @param courseOfferingId ID của course offering
     * @return true nếu xóa thành công
     */
    boolean deleteAllSchedulesOfCourseOffering(String courseOfferingId);
    
    /**
     * Xóa tất cả liên kết của một schedule
     * @param scheduleId ID của schedule
     * @return true nếu xóa thành công
     */
    boolean deleteAllCourseOfferingsOfSchedule(String scheduleId);
}
