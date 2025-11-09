package main.java.service.impl;

import java.time.LocalTime;
import java.util.List;
import main.java.model.Schedule;
import main.java.repository.ScheduleRepository;
import main.java.service.ScheduleService;

/**
 * ScheduleServiceImpl - Implementation của ScheduleService
 */
public class ScheduleServiceImpl implements ScheduleService {
    
    private final ScheduleRepository scheduleRepository;
    
    public ScheduleServiceImpl() {
        this.scheduleRepository = new ScheduleRepository();
    }
    
    public ScheduleServiceImpl(ScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
    }
    
    @Override
    public Schedule createSchedule(Schedule schedule) {
        // Validate
        validateSchedule(schedule);
        
        // Kiểm tra trùng schedule ID
        if (scheduleRepository.existsById(schedule.getScheduleId())) {
            throw new IllegalArgumentException("Schedule ID đã tồn tại: " + schedule.getScheduleId());
        }
        
        // Tạo schedule
        boolean created = scheduleRepository.createSchedule(schedule);
        
        if (created) {
            System.out.println("Tạo schedule thành công: " + schedule.getScheduleId());
            return schedule;
        }
        
        throw new RuntimeException("Không thể tạo schedule");
    }
    
    @Override
    public Schedule getScheduleById(String scheduleId) {
        if (scheduleId == null || scheduleId.trim().isEmpty()) {
            throw new IllegalArgumentException("Schedule ID không được để trống");
        }
        
        return scheduleRepository.findById(scheduleId);
    }
    
    @Override
    public List<Schedule> getAllSchedules() {
        return scheduleRepository.findAll();
    }
    
    @Override
    public List<Schedule> getSchedulesByDay(int dayOfWeek) {
        // Validate day of week (2-8: T2-CN)
        if (dayOfWeek < 2 || dayOfWeek > 8) {
            throw new IllegalArgumentException("Day of week phải từ 2 (T2) đến 8 (CN)");
        }
        
        return scheduleRepository.findByDay(dayOfWeek);
    }
    
    @Override
    public List<Schedule> getSchedulesByCourseOfferingId(String courseOfferingId) {
        if (courseOfferingId == null || courseOfferingId.trim().isEmpty()) {
            throw new IllegalArgumentException("Course offering ID không được để trống");
        }
        
        return scheduleRepository.findByCourseOfferingId(courseOfferingId);
    }
    
    @Override
    public boolean updateSchedule(Schedule schedule) {
        // Validate
        validateSchedule(schedule);
        
        // Kiểm tra schedule có tồn tại không
        if (!scheduleRepository.existsById(schedule.getScheduleId())) {
            throw new IllegalArgumentException("Schedule không tồn tại: " + schedule.getScheduleId());
        }
        
        // Update
        boolean updated = scheduleRepository.updateSchedule(schedule);
        
        if (updated) {
            System.out.println("Cập nhật schedule thành công: " + schedule.getScheduleId());
        }
        
        return updated;
    }
    
    @Override
    public boolean deleteSchedule(String scheduleId) {
        if (scheduleId == null || scheduleId.trim().isEmpty()) {
            throw new IllegalArgumentException("Schedule ID không được để trống");
        }
        
        // Kiểm tra schedule có tồn tại không
        if (!scheduleRepository.existsById(scheduleId)) {
            throw new IllegalArgumentException("Schedule không tồn tại: " + scheduleId);
        }
        
        // Xóa schedule
        boolean deleted = scheduleRepository.deleteSchedule(scheduleId);
        
        if (deleted) {
            System.out.println("Xóa schedule thành công: " + scheduleId);
        }
        
        return deleted;
    }
    
    @Override
    public boolean existsSchedule(String scheduleId) {
        if (scheduleId == null || scheduleId.trim().isEmpty()) {
            return false;
        }
        
        return scheduleRepository.existsById(scheduleId);
    }
    
    @Override
    public boolean validateSchedule(Schedule schedule) {
        if (schedule == null) {
            throw new IllegalArgumentException("Schedule không được null");
        }
        
        // Validate schedule ID
        if (schedule.getScheduleId() == null || schedule.getScheduleId().trim().isEmpty()) {
            throw new IllegalArgumentException("Schedule ID không được để trống");
        }
        
        // Validate day of week (2-8: T2-CN)
        if (schedule.getDayOfWeek() < 2 || schedule.getDayOfWeek() > 8) {
            throw new IllegalArgumentException("Day of week phải từ 2 (T2) đến 8 (CN)");
        }
        
        // Validate start time and end time
        if (schedule.getStartTime() == null) {
            throw new IllegalArgumentException("Start time không được null");
        }
        
        if (schedule.getEndTime() == null) {
            throw new IllegalArgumentException("End time không được null");
        }
        
        // Validate start time < end time
        if (schedule.getStartTime().isAfter(schedule.getEndTime()) || 
            schedule.getStartTime().equals(schedule.getEndTime())) {
            throw new IllegalArgumentException("Start time phải nhỏ hơn end time");
        }
        
        // Validate giờ học hợp lý (7h sáng đến 22h tối)
        LocalTime minTime = LocalTime.of(7, 0);
        LocalTime maxTime = LocalTime.of(22, 0);
        
        if (schedule.getStartTime().isBefore(minTime) || schedule.getEndTime().isAfter(maxTime)) {
            throw new IllegalArgumentException("Giờ học phải trong khoảng 7:00 - 22:00");
        }
        
        return true;
    }
}
