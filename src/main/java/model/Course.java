package main.java.model;

/**
 * Course entity - Môn học
 */
public class Course {
    private String courseId;
    private String courseName;
    private int credits;
    private String description;

    // Constructor mặc định
    public Course() {
    }

    // Constructor đầy đủ
    public Course(String courseId, String courseName, int credits, String description, String prerequisiteCourseId) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.credits = credits;
        this.description = description;
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


    @Override
    public String toString() {
        return "Course{" +
                "courseId='" + courseId + '\'' +
                ", courseName='" + courseName + '\'' +
                ", credits=" + credits +
                ", description='" + description + '\'' +
                '}';
    }
}
