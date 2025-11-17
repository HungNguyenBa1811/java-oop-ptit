package main.java.dto.course;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class CourseFormData {
    private final StringProperty courseId = new SimpleStringProperty();
    private final StringProperty courseName = new SimpleStringProperty();
    private final StringProperty credits = new SimpleStringProperty();
    private final StringProperty prerequisiteCourseId = new SimpleStringProperty();
    private final StringProperty facultyId = new SimpleStringProperty();
    public StringProperty courseIdProperty() { return courseId; }
    public StringProperty courseNameProperty() { return courseName; }
    public StringProperty creditsProperty() { return credits; }
    public StringProperty prerequisiteCourseIdProperty() { return prerequisiteCourseId; }
    public StringProperty facultyIdProperty() { return facultyId; }
    public String getCourseId() { return courseId.get(); }
    public String getCourseName() { return courseName.get(); }
    public String getCredits() { return credits.get(); }
    public String getPrerequisiteCourseId() { return prerequisiteCourseId.get(); }
    public String getFacultyId() { return facultyId.get(); }
}