package main.java.dto.admin;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.TableColumn;

public class AdminDashboardCourseRow {
    private final StringProperty courseId;
    private final StringProperty courseName;
    private final IntegerProperty credits;
    private final StringProperty facultyId;

    public AdminDashboardCourseRow() {
        this.courseId = new SimpleStringProperty();
        this.courseName = new SimpleStringProperty();
        this.credits = new SimpleIntegerProperty();
        this.facultyId = new SimpleStringProperty();
    }

    public AdminDashboardCourseRow(
        String courseId,
        String courseName,
        int credits,
        String facultyId
    ) {
        this();
        this.courseId.set(courseId);
        this.courseName.set(courseName);
        this.credits.set(credits);
        this.facultyId.set(facultyId);
    }

    // Property getters
    public StringProperty courseIdProperty() { return courseId; }
    public StringProperty courseNameProperty() { return courseName; }
    public IntegerProperty creditsProperty() { return credits; }
    public StringProperty facultyIdProperty() { return facultyId; }

    // Value getters
    public String getCourseId() { return courseId.get(); }
    public String getCourseName() { return courseName.get(); }
    public int getCredits() { return credits.get(); }
    public String getFacultyId() { return facultyId.get(); }

    public static void bindColumns(TableColumn<AdminDashboardCourseRow, String> colCourseCourseId,
                                   TableColumn<AdminDashboardCourseRow, String> colCourseCourseName,
                                   TableColumn<AdminDashboardCourseRow, Integer> colCourseCredits,
                                   TableColumn<AdminDashboardCourseRow, String> colFaculty) {
        colCourseCourseId.setCellValueFactory(cell -> cell.getValue().courseIdProperty());
        colCourseCourseName.setCellValueFactory(cell -> cell.getValue().courseNameProperty());
        colCourseCredits.setCellValueFactory(cell -> cell.getValue().creditsProperty().asObject());
        if (colFaculty != null) {
            colFaculty.setCellValueFactory(cell -> cell.getValue().facultyIdProperty());
        }
    }
}