package main.java.model;

/**
 * Faculty entity - Khoa
 */
public class Faculty {
    private String facultyId;
    private String facultyName;
    private String description;

    // Constructor mặc định
    public Faculty() {
    }

    // Constructor đầy đủ
    public Faculty(String facultyId, String facultyName, String description) {
        this.facultyId = facultyId;
        this.facultyName = facultyName;
        this.description = description;
    }

    // Getters
    public String getFacultyId() {
        return facultyId;
    }

    public String getFacultyName() {
        return facultyName;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return "Faculty{" +
                "facultyId='" + facultyId + '\'' +
                ", facultyName='" + facultyName + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
