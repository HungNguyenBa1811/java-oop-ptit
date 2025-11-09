package main.java.service.impl;

import java.sql.Date;
import java.util.List;
import main.java.model.CourseOfferingSchedule;
import main.java.model.Schedule;
import main.java.repository.CourseOfferingScheduleRepository;
import main.java.service.CourseOfferingScheduleService;

/**
 * CourseOfferingScheduleServiceImpl - Implementation của CourseOfferingScheduleService
 */
public class CourseOfferingScheduleServiceImpl implements CourseOfferingScheduleService {
    
    private final CourseOfferingScheduleRepository repository;
    
    public CourseOfferingScheduleServiceImpl() {
        this.repository = new CourseOfferingScheduleRepository();
    }
    
    public CourseOfferingScheduleServiceImpl(CourseOfferingScheduleRepository repository) {
        this.repository = repository;
    }
    
    @Override
    public boolean assignScheduleToCourseOffering(String courseOfferingId, String scheduleId, 
                                                 Date startDate, Date endDate) {
        // Validate
        if (courseOfferingId == null || courseOfferingId.trim().isEmpty()) {
            throw new IllegalArgumentException("Course offering ID không được để trống");
        }
        
        if (scheduleId == null || scheduleId.trim().isEmpty()) {
            throw new IllegalArgumentException("Schedule ID không được để trống");
        }
        
        if (startDate == null) {
            throw new IllegalArgumentException("Start date không được null");
        }
        
        if (endDate == null) {
            throw new IllegalArgumentException("End date không được null");
        }
        
        // Validate start date < end date
        if (startDate.after(endDate)) {
            throw new IllegalArgumentException("Start date phải trước end date");
        }
        
        // Tạo liên kết
        boolean created = repository.create(courseOfferingId, scheduleId, startDate, endDate);
        
        if (created) {
            System.out.println("Gán schedule " + scheduleId + " cho course offering " + 
                             courseOfferingId + " thành công");
        }
        
        return created;
    }
    
    @Override
    public List<CourseOfferingSchedule> getAllCourseOfferingSchedules() {
        return repository.findAll();
    }
    
    @Override
    public CourseOfferingSchedule getCourseOfferingScheduleById(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("ID phải lớn hơn 0");
        }
        
        return repository.findById(id);
    }
    
    @Override
    public List<CourseOfferingSchedule> getCourseOfferingSchedulesByCourseOffering(String courseOfferingId) {
        if (courseOfferingId == null || courseOfferingId.trim().isEmpty()) {
            throw new IllegalArgumentException("Course offering ID không được để trống");
        }
        
        return repository.findByCourseOfferingId(courseOfferingId);
    }
    
    @Override
    public List<Schedule> getSchedulesByCourseOfferingId(String courseOfferingId) {
        if (courseOfferingId == null || courseOfferingId.trim().isEmpty()) {
            throw new IllegalArgumentException("Course offering ID không được để trống");
        }
        
        return repository.findSchedulesByCourseOfferingId(courseOfferingId);
    }
    
    @Override
    public List<CourseOfferingSchedule> getCourseOfferingSchedulesBySchedule(String scheduleId) {
        if (scheduleId == null || scheduleId.trim().isEmpty()) {
            throw new IllegalArgumentException("Schedule ID không được để trống");
        }
        
        return repository.findByScheduleId(scheduleId);
    }
    
    @Override
    public boolean deleteCourseOfferingSchedule(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("ID phải lớn hơn 0");
        }
        
        boolean deleted = repository.deleteById(id);
        
        if (deleted) {
            System.out.println("Xóa course offering schedule ID " + id + " thành công");
        }
        
        return deleted;
    }
    
    @Override
    public boolean deleteAllSchedulesOfCourseOffering(String courseOfferingId) {
        if (courseOfferingId == null || courseOfferingId.trim().isEmpty()) {
            throw new IllegalArgumentException("Course offering ID không được để trống");
        }
        
        boolean deleted = repository.deleteByCourseOfferingId(courseOfferingId);
        
        if (deleted) {
            System.out.println("Xóa tất cả schedules của course offering " + 
                             courseOfferingId + " thành công");
        }
        
        return deleted;
    }
    
    @Override
    public boolean deleteAllCourseOfferingsOfSchedule(String scheduleId) {
        if (scheduleId == null || scheduleId.trim().isEmpty()) {
            throw new IllegalArgumentException("Schedule ID không được để trống");
        }
        
        boolean deleted = repository.deleteByScheduleId(scheduleId);
        
        if (deleted) {
            System.out.println("Xóa tất cả course offerings của schedule " + 
                             scheduleId + " thành công");
        }
        
        return deleted;
    }
}
