package main.java.dto.admin.courseOffering;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class CourseOfferingFormData {
    private final StringProperty offeringId = new SimpleStringProperty();
    private final StringProperty courseId = new SimpleStringProperty();
    private final StringProperty instructor = new SimpleStringProperty();
    private final StringProperty semesterId = new SimpleStringProperty();
    private final StringProperty roomId = new SimpleStringProperty();
    private final StringProperty facultyId = new SimpleStringProperty();
    private final StringProperty maxCapacity = new SimpleStringProperty();
    private final StringProperty currentCapacity = new SimpleStringProperty();
    
    public StringProperty getOfferingIdProperty() { return offeringId; }
    public StringProperty getCourseIdProperty() { return courseId; }
    public StringProperty getInstructorProperty() { return instructor; }
    public StringProperty getSemesterIdProperty() { return semesterId; }
    public StringProperty getRoomIdProperty() { return roomId; }
    public StringProperty getFacultyIdProperty() { return facultyId; }
    public StringProperty getMaxCapacityProperty() { return maxCapacity; }
    public StringProperty getCurrentCapacityProperty() { return currentCapacity; }
    public String getOfferingId() { return offeringId.get(); }
    public String getCourseId() { return courseId.get(); }
    public String getInstructor() { return instructor.get(); }
    public String getSemesterId() { return semesterId.get(); }
    public String getRoomId() { return roomId.get(); }
    public String getFacultyId() { return facultyId.get(); }
    public String getMaxCapacity() { return maxCapacity.get(); }
    public String getCurrentCapacity() { return currentCapacity.get(); }
}