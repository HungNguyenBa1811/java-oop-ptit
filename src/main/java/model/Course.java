package main.java.model;

/**
 * Course entity - Môn học
 */
public class Course {
    private String courseId;
    private String courseName;
    private int credits;
    private String description;
    private String facultyId;

    // Constructor mặc định
    public Course() {
    }

    // Constructor đầy đủ
    public Course(String courseId, String courseName, int credits, String description, String facultyId) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.credits = credits;
        this.description = description;
        this.facultyId = facultyId;
    }

    // Constructor không có prerequisite
    public Course(String courseId, String courseName, int credits, String description) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.credits = credits;
        this.description = description;
    }

    // Getters
    public String getCourseId() {
        return courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public int getCredits() {
        return credits;
    }

    public String getDescription() {
        return description;
    }

    public String getFacultyId() {
        return facultyId;
    }

    // Setters
    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public void setCredits(int credits) {
        this.credits = credits;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setFacultyId(String facultyId) {
        this.facultyId = facultyId;
    }

    @Override
    public String toString() {
        return "Course{" +
                "courseId='" + courseId + '\'' +
                ", courseName='" + courseName + '\'' +
                ", credits=" + credits +
                ", description='" + description + '\'' +
                ", facultyId='" + facultyId + '\'' +
                '}';
    }
}
