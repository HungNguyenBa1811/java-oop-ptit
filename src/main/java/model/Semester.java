package main.java.model;

import java.time.LocalDate;

/**
 * Semester entity - Học kỳ
 */
public class Semester {
    private String semesterId;
    private String term;
    private String academicYear;
    private LocalDate startDate;
    private LocalDate endDate;

    // Constructor mặc định
    public Semester() {
    }

    // Constructor đầy đủ
    public Semester(String semesterId, String term, String academicYear, LocalDate startDate, LocalDate endDate) {
        this.semesterId = semesterId;
        this.term = term;
        this.academicYear = academicYear;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    // Getters
    public String getSemesterId() {
        return semesterId;
    }

    public String getTerm() {
        return term;
    }

    public String getAcademicYear() {
        return academicYear;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    // Setters
    public void setSemesterId(String semesterId) {
        this.semesterId = semesterId;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public void setAcademicYear(String academicYear) {
        this.academicYear = academicYear;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    @Override
    public String toString() {
        return "Semester{" +
                "semesterId='" + semesterId + '\'' +
                ", term='" + term + '\'' +
                ", academicYear='" + academicYear + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                '}';
    }
}
