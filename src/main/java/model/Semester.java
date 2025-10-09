package model;

import java.time.LocalDate;

/**
 * Semester entity - Học kỳ
 */
public class Semester {
    private String semesterId;
    private String semesterName;
    private LocalDate startDate;
    private LocalDate endDate;
    private String status; // UPCOMING, ONGOING, COMPLETED

    // Constructor mặc định
    public Semester() {
    }

    // Constructor đầy đủ
    public Semester(String semesterId, String semesterName, LocalDate startDate, LocalDate endDate, String status) {
        this.semesterId = semesterId;
        this.semesterName = semesterName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
    }

    // Getters
    public String getSemesterId() {
        return semesterId;
    }

    public String getSemesterName() {
        return semesterName;
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

    @Override
    public String toString() {
        return "Semester{" +
                "semesterId='" + semesterId + '\'' +
                ", semesterName='" + semesterName + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", status='" + status + '\'' +
                '}';
    }
}
