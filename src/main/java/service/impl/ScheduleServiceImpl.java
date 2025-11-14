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
    
    private final ScheduleRepository repository;
    
    // Constants for validation
    private static final int MIN_DAY_OF_WEEK = 2; // Monday
    private static final int MAX_DAY_OF_WEEK = 8; // Sunday
    private static final LocalTime MIN_TIME = LocalTime.of(7, 0);
    private static final LocalTime MAX_TIME = LocalTime.of(22, 0);
    
    public ScheduleServiceImpl() {
        this.repository = new ScheduleRepository();
    }
    
    public ScheduleServiceImpl(ScheduleRepository repository) {
        this.repository = repository;
    }
    
    @Override
    public Schedule createSchedule(Schedule schedule) {
        // Validate
        if (schedule == null) {
            System.err.println("Schedule không được null");
            return null;
        }
        
        if (schedule.getScheduleId() == null || schedule.getScheduleId().trim().isEmpty()) {
            System.err.println("Schedule ID không được rỗng");
            return null;
        }
        
        // Validate schedule
        try {
            if (!validateSchedule(schedule)) {
                return null;
            }
        } catch (IllegalArgumentException e) {
            System.err.println("Validation failed: " + e.getMessage());
            return null;
        }
        
        // Kiểm tra trùng ID
        if (existsSchedule(schedule.getScheduleId())) {
            System.err.println("Schedule ID đã tồn tại: " + schedule.getScheduleId());
            return null;
        }
        
        boolean success = repository.createSchedule(schedule);
        return success ? schedule : null;
    }
    
    @Override
    public List<Schedule> getAllSchedules() {
        return repository.findAll();
    }
    
    @Override
    public Schedule getScheduleById(String scheduleId) {
        if (scheduleId == null || scheduleId.trim().isEmpty()) {
            System.err.println("Schedule ID không được rỗng");
            return null;
        }
        return repository.findById(scheduleId);
    }
    
    @Override
    public List<Schedule> getSchedulesByDay(int dayOfWeek) {
        if (!isValidDayOfWeek(dayOfWeek)) {
            System.err.println("Day of week phải từ 2-8");
            return List.of();
        }
        return repository.findByDay(dayOfWeek);
    }
    
    @Override
    public List<Schedule> getSchedulesByCourseOfferingId(String courseOfferingId) {
        if (courseOfferingId == null || courseOfferingId.trim().isEmpty()) {
            System.err.println("Course Offering ID không được rỗng");
            return List.of();
        }
        // Sử dụng CourseOfferingScheduleRepository để lấy schedules
        return repository.findAll(); // Tạm thời return all, cần implement đúng
    }

    public List<Schedule> getSchedulesByTimeRange(LocalTime startTime, LocalTime endTime) {
        if (!isValidTimeRange(startTime, endTime)) {
            System.err.println("Time range không hợp lệ");
            return List.of();
        }
        return repository.findByTimeRange(startTime, endTime);
    }
    
    @Override
    public boolean updateSchedule(Schedule schedule) {
        // Validate
        if (schedule == null) {
            System.err.println("Schedule không được null");
            return false;
        }
        
        if (schedule.getScheduleId() == null || schedule.getScheduleId().trim().isEmpty()) {
            System.err.println("Schedule ID không được rỗng");
            return false;
        }
        
        // Validate day of week
        if (!isValidDayOfWeek(schedule.getDayOfWeek())) {
            System.err.println("Day of week phải từ 2-8");
            return false;
        }
        
        // Validate time range
        if (!isValidTimeRange(schedule.getStartTime(), schedule.getEndTime())) {
            System.err.println("Time range không hợp lệ");
            return false;
        }
        
        // Kiểm tra tồn tại
        if (!existsById(schedule.getScheduleId())) {
            System.err.println("Schedule không tồn tại: " + schedule.getScheduleId());
            return false;
        }
        
        return repository.updateSchedule(schedule);
    }
    
    @Override
    public boolean deleteSchedule(String scheduleId) {
        if (scheduleId == null || scheduleId.trim().isEmpty()) {
            System.err.println("Schedule ID không được rỗng");
            return false;
        }
        
        if (!existsById(scheduleId)) {
            System.err.println("Schedule không tồn tại: " + scheduleId);
            return false;
        }
        
        return repository.deleteSchedule(scheduleId);
    }
    
    @Override
    public boolean existsSchedule(String scheduleId) {
        Schedule schedule = repository.findById(scheduleId);
        return schedule != null;
    }
    
    @Override
    public boolean validateSchedule(Schedule schedule) {
        if (schedule == null) {
            throw new IllegalArgumentException("Schedule không được null");
        }
        
        if (schedule.getScheduleId() == null || schedule.getScheduleId().trim().isEmpty()) {
            throw new IllegalArgumentException("Schedule ID không được rỗng");
        }
        
        // Validate day of week
        if (!isValidDayOfWeek(schedule.getDayOfWeek())) {
            throw new IllegalArgumentException("Day of week phải từ 2-8 (2=Monday, 8=Sunday)");
        }
        
        // Validate time range
        if (!isValidTimeRange(schedule.getStartTime(), schedule.getEndTime())) {
            throw new IllegalArgumentException("Time range không hợp lệ (7:00-22:00, start < end)");
        }
        
        return true;
    }

    public boolean existsById(String scheduleId) {
        return existsSchedule(scheduleId);
    }
    
    public boolean isValidDayOfWeek(int dayOfWeek) {
        return dayOfWeek >= MIN_DAY_OF_WEEK && dayOfWeek <= MAX_DAY_OF_WEEK;
    }
    
    public boolean isValidTimeRange(LocalTime startTime, LocalTime endTime) {
        if (startTime == null || endTime == null) {
            return false;
        }
        
        // Start time phải trước end time
        if (!startTime.isBefore(endTime)) {
            return false;
        }
        
        // Kiểm tra trong giờ học hợp lệ (7:00 - 22:00)
        return !startTime.isBefore(MIN_TIME) && !endTime.isAfter(MAX_TIME);
    }
}
