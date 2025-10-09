package model;

/**
 * Major entity - Ngành học
 */
public class Major {
    private String majorId;
    private String majorName;
    private String facultyId;
    private int totalCredits;

    // Constructor mặc định
    public Major() {
    }

    // Constructor đầy đủ
    public Major(String majorId, String majorName, String facultyId, int totalCredits) {
        this.majorId = majorId;
        this.majorName = majorName;
        this.facultyId = facultyId;
        this.totalCredits = totalCredits;
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

    public int getTotalCredits() {
        return totalCredits;
    }

    @Override
    public String toString() {
        return "Major{" +
                "majorId='" + majorId + '\'' +
                ", majorName='" + majorName + '\'' +
                ", facultyId='" + facultyId + '\'' +
                ", totalCredits=" + totalCredits +
                '}';
    }
}
