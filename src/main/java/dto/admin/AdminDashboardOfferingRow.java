package main.java.dto.admin;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.TableColumn;

public class AdminDashboardOfferingRow {
    private final StringProperty courseOfferingId;
    private final StringProperty courseId;
    private final StringProperty courseName;
    private final IntegerProperty credits;
    private final StringProperty instructor;
    private final StringProperty major;
    private final StringProperty semesterId;
    private final StringProperty schedule;
    private final StringProperty roomId;
    private final StringProperty maxCapacity;
    private final StringProperty currentCapacity;

    public AdminDashboardOfferingRow() {
        this.courseOfferingId = new SimpleStringProperty();
        this.courseId = new SimpleStringProperty();
        this.courseName = new SimpleStringProperty();
        this.credits = new SimpleIntegerProperty();
        this.instructor = new SimpleStringProperty();
        this.major = new SimpleStringProperty();
        this.semesterId = new SimpleStringProperty();
        this.schedule = new SimpleStringProperty();
        this.roomId = new SimpleStringProperty();
        this.maxCapacity = new SimpleStringProperty();
        this.currentCapacity = new SimpleStringProperty();
    }

    public AdminDashboardOfferingRow(
        String courseOfferingId,
        String courseId,
        String courseName,
        int credits,
        String instructor,
        String major,
        String semesterId,
        String schedule,
        String roomId,
        String maxCapacity,
        String currentCapacity
    ) {
        this();
        this.courseOfferingId.set(courseOfferingId);
        this.courseId.set(courseId);
        this.courseName.set(courseName);
        this.credits.set(credits);
        this.instructor.set(instructor);
        this.major.set(major);
        this.semesterId.set(semesterId);
        this.schedule.set(schedule);
        this.roomId.set(roomId);
        this.maxCapacity.set(maxCapacity);
        this.currentCapacity.set(currentCapacity);
    }

    // Property getters
    public StringProperty courseOfferingIdProperty() { return courseOfferingId; }
    public StringProperty courseIdProperty() { return courseId; }
    public StringProperty courseNameProperty() { return courseName; }
    public IntegerProperty creditsProperty() { return credits; }
    public StringProperty instructorProperty() { return instructor; }
    public StringProperty majorProperty() { return major; }
    public StringProperty semesterIdProperty() { return semesterId; }
    public StringProperty scheduleProperty() { return schedule; }
    public StringProperty roomIdProperty() { return roomId; }
    public StringProperty maxCapacityProperty() { return maxCapacity; }
    public StringProperty currentCapacityProperty() { return currentCapacity; }

    // Value getters
    public String getCourseOfferingId() { return courseOfferingId.get(); }
    public String getCourseId() { return courseId.get(); }
    public String getCourseName() { return courseName.get(); }
    public int getCredits() { return credits.get(); }
    public String getInstructor() { return instructor.get(); }
    public String getMajor() { return major.get(); }
    public String getSemesterId() { return semesterId.get(); }
    public String getSchedule() { return schedule.get(); }
    public String getRoomId() { return roomId.get(); }
    public String getMaxCapacity() { return maxCapacity.get(); }
    public String getCurrentCapacity() { return currentCapacity.get(); }
    
    // Computed property for remaining capacity
    public String getRemaining() {
        try {
            int max = Integer.parseInt(maxCapacity.get());
            int current = Integer.parseInt(currentCapacity.get());
            return String.valueOf(max - current);
        } catch (NumberFormatException e) {
            return "N/A";
        }
    }

    public static void bindColumns(TableColumn<AdminDashboardOfferingRow, String> colOfferingCourseOfferingId,
                                   TableColumn<AdminDashboardOfferingRow, String> colOfferingCourseId,
                                   TableColumn<AdminDashboardOfferingRow, String> colOfferingCourseName,
                                   TableColumn<AdminDashboardOfferingRow, Integer> colOfferingCredits,
                                   TableColumn<AdminDashboardOfferingRow, String> colOfferingInstructor,
                                   TableColumn<AdminDashboardOfferingRow, String> colOfferingMajor,
                                   TableColumn<AdminDashboardOfferingRow, String> colOfferingSemesterId,
                                   TableColumn<AdminDashboardOfferingRow, String> colOfferingSchedule,
                                   TableColumn<AdminDashboardOfferingRow, String> colOfferingRoomId,
                                   TableColumn<AdminDashboardOfferingRow, String> colOfferingMaxCapacity,
                                   TableColumn<AdminDashboardOfferingRow, String> colOfferingRemaining) {
        colOfferingCourseOfferingId.setCellValueFactory(cell -> cell.getValue().courseOfferingIdProperty());
        colOfferingCourseId.setCellValueFactory(cell -> cell.getValue().courseIdProperty());
        colOfferingCourseName.setCellValueFactory(cell -> cell.getValue().courseNameProperty());
        colOfferingCredits.setCellValueFactory(cell -> cell.getValue().creditsProperty().asObject());
        colOfferingInstructor.setCellValueFactory(cell -> cell.getValue().instructorProperty());
        colOfferingMajor.setCellValueFactory(cell -> cell.getValue().majorProperty());
        colOfferingSemesterId.setCellValueFactory(cell -> cell.getValue().semesterIdProperty());
        colOfferingSchedule.setCellValueFactory(cell -> cell.getValue().scheduleProperty());
        colOfferingRoomId.setCellValueFactory(cell -> cell.getValue().roomIdProperty());
        colOfferingMaxCapacity.setCellValueFactory(cell -> cell.getValue().maxCapacityProperty());
        colOfferingRemaining.setCellValueFactory(cell -> {
            AdminDashboardOfferingRow row = cell.getValue();
            return new SimpleStringProperty(row.getRemaining());
        });
    }
}


