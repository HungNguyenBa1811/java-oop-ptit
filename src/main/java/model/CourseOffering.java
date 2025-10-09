package main.java.model;

import java.time.LocalDate;

/**
 * CourseOffering entity - Lớp học phần
 */
public class CourseOffering {
    private String courseOfferingId;
    private String courseId;
    private String semesterId;
    private String instructor;
    private int maxCapacity;
    private int currentEnrollment;
    private LocalDate startDate;
    private LocalDate endDate;
    private String status; // OPEN, CLOSED, FULL, CANCELLED

    // Constructor mặc định
    public CourseOffering() {
    }

    // Constructor đầy đủ
    public CourseOffering(String courseOfferingId, String courseId, String semesterId,
                          String instructor, int maxCapacity, int currentEnrollment,
                          LocalDate startDate, LocalDate endDate, String status) {
        this.courseOfferingId = courseOfferingId;
        this.courseId = courseId;
        this.semesterId = semesterId;
        this.instructor = instructor;
        this.maxCapacity = maxCapacity;
        this.currentEnrollment = currentEnrollment;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
    }

    // Getters
    public String getCourseOfferingId() {
        return courseOfferingId;
    }

    public String getCourseId() {
        return courseId;
    }

    public String getSemesterId() {
        return semesterId;
    }

    public String getInstructor() {
        return instructor;
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }

    public int getCurrentEnrollment() {
        return currentEnrollment;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public String getStatus() {
        return status;
    }

    // Helper method: Check if có chỗ trống
    public boolean hasAvailableSlots() {
        return currentEnrollment < maxCapacity;
    }

    @Override
    public String toString() {
        return "CourseOffering{" +
                "courseOfferingId='" + courseOfferingId + '\'' +
                ", courseId='" + courseId + '\'' +
                ", semesterId='" + semesterId + '\'' +
                ", instructor='" + instructor + '\'' +
                ", maxCapacity=" + maxCapacity +
                ", currentEnrollment=" + currentEnrollment +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", status='" + status + '\'' +
                '}';
    }
}
