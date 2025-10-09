package model;

import java.time.LocalDateTime;

/**
 * Registration entity - Đăng ký môn học
 */
public class Registration {
    private String registrationId;
    private String studentId;
    private String courseOfferingId;
    private LocalDateTime registrationDate;
    private String status; // REGISTERED, CANCELLED, COMPLETED
    private String grade; // A, B+, B, C+, C, D+, D, F (nullable khi chưa có điểm)
    private String note;

    // Constructor mặc định
    public Registration() {
    }

    // Constructor đầy đủ
    public Registration(String registrationId, String studentId, String courseOfferingId,
                        LocalDateTime registrationDate, String status, String grade, String note) {
        this.registrationId = registrationId;
        this.studentId = studentId;
        this.courseOfferingId = courseOfferingId;
        this.registrationDate = registrationDate;
        this.status = status;
        this.grade = grade;
        this.note = note;
    }

    // Constructor khi đăng ký mới (chưa có grade)
    public Registration(String registrationId, String studentId, String courseOfferingId,
                        LocalDateTime registrationDate, String status) {
        this.registrationId = registrationId;
        this.studentId = studentId;
        this.courseOfferingId = courseOfferingId;
        this.registrationDate = registrationDate;
        this.status = status;
        this.grade = null;
        this.note = null;
    }

    // Getters
    public String getRegistrationId() {
        return registrationId;
    }

    public String getStudentId() {
        return studentId;
    }

    public String getCourseOfferingId() {
        return courseOfferingId;
    }

    public LocalDateTime getRegistrationDate() {
        return registrationDate;
    }

    public String getStatus() {
        return status;
    }

    public String getGrade() {
        return grade;
    }

    public String getNote() {
        return note;
    }

    @Override
    public String toString() {
        return "Registration{" +
                "registrationId='" + registrationId + '\'' +
                ", studentId='" + studentId + '\'' +
                ", courseOfferingId='" + courseOfferingId + '\'' +
                ", registrationDate=" + registrationDate +
                ", status='" + status + '\'' +
                ", grade='" + grade + '\'' +
                ", note='" + note + '\'' +
                '}';
    }
}
