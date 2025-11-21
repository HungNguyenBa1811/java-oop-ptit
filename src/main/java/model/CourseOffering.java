package main.java.model;

import java.util.List;

/**
 * CourseOffering entity - Lớp học phần
 */
public class CourseOffering {
    private String courseOfferingId;
    private String courseId;
    private String facultyId;
    private String instructor;
    private String roomId;
    private String semesterId;
    private String maxCapacity;
    private String currentCapacity;
    private List<Schedule> schedules; // Danh sách lịch học của lớp học phần này



    // Constructor mặc định
    public CourseOffering() {
    }

    public CourseOffering(String courseId, String courseOfferingId, String currentCapacity, String instructor, String facultyId, String maxCapacity, String roomId, String semesterId) {
        this.courseId = courseId;
        this.courseOfferingId = courseOfferingId;
        this.currentCapacity = currentCapacity;
        this.instructor = instructor;
        this.facultyId = facultyId;
        this.maxCapacity = maxCapacity;
        this.roomId = roomId;
        this.semesterId = semesterId;
    }

    
    
    @Override
    public String toString() {
        return "CourseOffering{" +
                "courseOfferingId='" + courseOfferingId + '\'' +
                ", courseId='" + courseId + '\'' +
                ", semesterId='" + semesterId + '\'' +
                ", instructor='" + instructor + '\'' +
                ", maxCapacity=" + maxCapacity +
                '}';
    }

    public String getCourseOfferingId() {
        return courseOfferingId;
    }

    public void setCourseOfferingId(String courseOfferingId) {
        this.courseOfferingId = courseOfferingId;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getFacultyId() {
        return facultyId;
    }

    public void setFacultyId(String facultyId) {
        this.facultyId = facultyId;
    }

    public String getInstructor() {
        return instructor;
    }

    public void setInstructor(String instructor) {
        this.instructor = instructor;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getSemesterId() {
        return semesterId;
    }

    public void setSemesterId(String semesterId) {
        this.semesterId = semesterId;
    }

    public String getMaxCapacity() {
        return maxCapacity;
    }

    public void setMaxCapacity(String maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    public String getCurrentCapacity() {
        return currentCapacity;
    }

    public void setCurrentCapacity(String currentCapacity) {
        this.currentCapacity = currentCapacity;
    }

    public List<Schedule> getSchedules() {
        return schedules;
    }

    public void setSchedules(List<Schedule> schedules) {
        this.schedules = schedules;
    }
}
