package main.java.model;

import java.time.LocalDate;

/**
 * CourseOfferingSchedule entity - Liên kết giữa CourseOffering và Schedule
 * Một lớp học phần có thể có nhiều lịch học (nhiều buổi trong tuần)
 */
public class CourseOfferingSchedule {
    private String courseOfferingScheduleId;
    private String courseOfferingId;
    private String scheduleId;
    private LocalDate startDate;
    private LocalDate endDate;

    // Constructor mặc định
    public CourseOfferingSchedule() {
    }

    // Constructor đầy đủ
    public CourseOfferingSchedule(String courseOfferingScheduleId, String courseOfferingId, String scheduleId) {
        this.courseOfferingScheduleId = courseOfferingScheduleId;
        this.courseOfferingId = courseOfferingId;
        this.scheduleId = scheduleId;
    }
    
    // Constructor với đầy đủ thông tin
    public CourseOfferingSchedule(String courseOfferingScheduleId, String courseOfferingId, 
                                   String scheduleId, LocalDate startDate, LocalDate endDate) {
        this.courseOfferingScheduleId = courseOfferingScheduleId;
        this.courseOfferingId = courseOfferingId;
        this.scheduleId = scheduleId;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    // Getters
    public String getCourseOfferingScheduleId() {
        return courseOfferingScheduleId;
    }

    public String getCourseOfferingId() {
        return courseOfferingId;
    }

    public String getScheduleId() {
        return scheduleId;
    }
    
    public LocalDate getStartDate() {
        return startDate;
    }
    
    public LocalDate getEndDate() {
        return endDate;
    }
    
    // Setters
    public void setCourseOfferingScheduleId(String courseOfferingScheduleId) {
        this.courseOfferingScheduleId = courseOfferingScheduleId;
    }
    
    public void setCourseOfferingId(String courseOfferingId) {
        this.courseOfferingId = courseOfferingId;
    }
    
    public void setScheduleId(String scheduleId) {
        this.scheduleId = scheduleId;
    }
    
    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }
    
    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }


    @Override
    public String toString() {
        return "CourseOfferingSchedule{" +
                "courseOfferingScheduleId='" + courseOfferingScheduleId + '\'' +
                ", courseOfferingId='" + courseOfferingId + '\'' +
                ", scheduleId='" + scheduleId + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                '}';
    }
}
