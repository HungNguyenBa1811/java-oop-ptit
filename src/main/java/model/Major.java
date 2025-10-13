package main.java.model;

/**
 * Major entity - Ngành học
 */
public class Major {
    private String majorId;
    private String majorName;
    private String facultyId;
    private String degreeLevel;
    private int durationYears;
    private String description;

    // Constructor mặc định
    public Major() {
    }

    // Constructor đầy đủ

    public Major(String degreeLevel, String description, int durationYears, String facultyId, String majorId, String majorName) {
        this.degreeLevel = degreeLevel;
        this.description = description;
        this.durationYears = durationYears;
        this.facultyId = facultyId;
        this.majorId = majorId;
        this.majorName = majorName;
    }
    
    // Getters
    public String getMajorId() {
        return majorId;
    }

    public String getMajorName() {
        return majorName;
    }

    public String getFacultyId() {
        return facultyId;
    }


    @Override
    public String toString() {
        return "Major{" +
                "majorId='" + majorId + '\'' +
                ", majorName='" + majorName + '\'' +
                ", facultyId='" + facultyId + '\'' +
                '}';
    }

    public String getDegreeLevel() {
        return degreeLevel;
    }

    public void setDegreeLevel(String degreeLevel) {
        this.degreeLevel = degreeLevel;
    }

    public int getDurationYears() {
        return durationYears;
    }

    public void setDurationYears(int durationYears) {
        this.durationYears = durationYears;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setMajorId(String majorId) {
        this.majorId = majorId;
    }

    public void setMajorName(String majorName) {
        this.majorName = majorName;
    }

    public void setFacultyId(String facultyId) {
        this.facultyId = facultyId;
    }
}
