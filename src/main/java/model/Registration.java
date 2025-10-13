package main.java.model;

import java.time.LocalDateTime;

/**
 * Registration entity - Đăng ký môn học
 */
public class Registration {
    private String registrationId;
    private String studentId;
    private String courseOfferingId;
    private LocalDateTime registeredAt;
    private String status; 
    private String note;

    // Constructor mặc định
    public Registration() {
    }

    public Registration(String courseOfferingId, String note, LocalDateTime registeredAt, String registrationId, String status, String studentId) {
        this.courseOfferingId = courseOfferingId;
        this.note = note;
        this.registeredAt = registeredAt;
        this.registrationId = registrationId;
        this.status = status;
        this.studentId = studentId;
    }

    

    public String getRegistrationId() {
        return registrationId;
    }

    public void setRegistrationId(String registrationId) {
        this.registrationId = registrationId;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getCourseOfferingId() {
        return courseOfferingId;
    }

    public void setCourseOfferingId(String courseOfferingId) {
        this.courseOfferingId = courseOfferingId;
    }

    public LocalDateTime getRegisteredAt() {
        return registeredAt;
    }

    public void setRegisteredAt(LocalDateTime registeredAt) {
        this.registeredAt = registeredAt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    
    @Override
    public String toString() {
        return "Registration{" +
                "registrationId='" + registrationId + '\'' +
                ", studentId='" + studentId + '\'' +
                ", courseOfferingId='" + courseOfferingId + '\'' +
                ", registrationDate=" + registeredAt +
                ", status='" + status + '\'' +
                ", note='" + note + '\'' +
                '}';
    }
}
