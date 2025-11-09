package main.java.service;

import java.util.List;
import main.java.model.Schedule;

/**
 * ScheduleService - Interface cho business logic của Schedule
 */
public interface ScheduleService {
    
    /**
     * Tạo lịch học mới
     * @param schedule Schedule object cần tạo
     * @return Schedule đã được tạo
     */
    Schedule createSchedule(Schedule schedule);
    
    /**
     * Lấy thông tin lịch học theo ID
     * @param scheduleId ID của lịch học
     * @return Schedule object hoặc null nếu không tìm thấy
     */
    Schedule getScheduleById(String scheduleId);
    
    /**
     * Lấy tất cả lịch học
     * @return List<Schedule> danh sách tất cả lịch học
     */
    List<Schedule> getAllSchedules();
    
    /**
     * Lấy danh sách lịch học theo ngày trong tuần
     * @param dayOfWeek Ngày trong tuần (2=T2, 3=T3, ..., 8=CN)
     * @return List<Schedule> danh sách lịch học
     */
    List<Schedule> getSchedulesByDay(int dayOfWeek);
    
    /**
     * Lấy danh sách lịch học theo course offering ID
     * @param courseOfferingId ID của course offering
     * @return List<Schedule> danh sách lịch học của lớp mở đó
     */
    List<Schedule> getSchedulesByCourseOfferingId(String courseOfferingId);
    
    /**
     * Cập nhật thông tin lịch học
     * @param schedule Schedule object với thông tin mới
     * @return true nếu cập nhật thành công
     */
    boolean updateSchedule(Schedule schedule);
    
    /**
     * Xóa lịch học
     * @param scheduleId ID của lịch học cần xóa
     * @return true nếu xóa thành công
     */
    boolean deleteSchedule(String scheduleId);
    
    /**
     * Kiểm tra lịch học có tồn tại không
     * @param scheduleId ID của lịch học
     * @return true nếu tồn tại
     */
    boolean existsSchedule(String scheduleId);
    
    /**
     * Validate dữ liệu lịch học
     * @param schedule Schedule object cần validate
     * @return true nếu hợp lệ
     * @throws IllegalArgumentException nếu dữ liệu không hợp lệ
     */
    boolean validateSchedule(Schedule schedule);
}
