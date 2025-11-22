package main.java.dto;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

public class StudentDashboardRow {
    private final StringProperty offeringId;
    private final StringProperty courseId;
    private final StringProperty courseName;
    private final StringProperty credits;
    private final StringProperty instructor;
    private final StringProperty semester;
    private final StringProperty schedule;
    private final StringProperty room;
    private final StringProperty capacity;
    private final StringProperty remaining;
    private final BooleanProperty selected;
    public StudentDashboardRow(){
        this.offeringId = new SimpleStringProperty();
        this.courseId = new SimpleStringProperty();
        this.courseName = new SimpleStringProperty();
        this.credits = new SimpleStringProperty();
        this.instructor = new SimpleStringProperty();
        this.semester = new SimpleStringProperty();
        this.schedule = new SimpleStringProperty();
        this.room = new SimpleStringProperty();
        this.capacity = new SimpleStringProperty();
        this.remaining = new SimpleStringProperty();
        this.selected = new SimpleBooleanProperty(false);
    }
    public StudentDashboardRow(
        String offeringId,
        String courseId,
        String courseName,
        String credits,
        String instructor,
        String semester,
        String schedule,
        String room,
        String capacity,
        String remaining
    ) {
        this();
        this.offeringId.set(offeringId);
        this.courseId.set(courseId);
        this.courseName.set(courseName);
        this.credits.set(credits);
        this.instructor.set(instructor);
        this.semester.set(semester);
        this.schedule.set(schedule);
        this.room.set(room);
        this.capacity.set(capacity);
        this.remaining.set(remaining);
        this.selected.set(false);
    }

    // Ref của UI
    public StringProperty getOfferingIdProperty() {
        return offeringId;
    }
    public StringProperty getCourseIdProperty() {
        return courseId;
    }
    public StringProperty getCourseNameProperty() {
        return courseName;
    }
    public StringProperty getCreditsProperty() {
        return credits;
    }
    public StringProperty getInstructorProperty() {
        return instructor;
    }
    public StringProperty getSemesterProperty() {
        return semester;
    }
    public StringProperty getScheduleProperty() {
        return schedule;
    }
    public StringProperty getRoomProperty() {
        return room;
    }
    public StringProperty getCapacityProperty() {
        return capacity;
    }
    public StringProperty getRemainingProperty() {
        return remaining;
    }
    public BooleanProperty getSelectedProperty() {
        return selected;
    }

    // Get dữ liệu
    public String getOfferingId() { 
        return offeringId.get(); 
    }
    public String getCourseId() { 
        return courseId.get(); 
    }
    public String getCourseName() { 
        return courseName.get(); 
    }
    public String getCredits() { 
        return credits.get(); 
    }
    public String getInstructor() { 
        return instructor.get(); 
    }
    public String getSemester() { 
        return semester.get(); 
    }
    public String getSchedule() { 
        return schedule.get(); 
    }
    public String getRoom() { 
        return room.get(); 
    }
    public String getCapacity() { 
        return capacity.get(); 
    }
    public String getRemaining() { 
        return remaining.get(); 
    }
    public boolean isSelected() { 
        return selected.get(); 
    }

    public static void bindColumns(TableColumn<StudentDashboardRow, String> colOfferingId,
                           TableColumn<StudentDashboardRow, String> colCourseId,
                           TableColumn<StudentDashboardRow, String> colCourseName,
                           TableColumn<StudentDashboardRow, String> colCredits,
                           TableColumn<StudentDashboardRow, String> colInstructor,
                           TableColumn<StudentDashboardRow, String> colSemester,
                           TableColumn<StudentDashboardRow, String> colSchedule,
                           TableColumn<StudentDashboardRow, String> colRoom,
                           TableColumn<StudentDashboardRow, String> colCapacity,
                           TableColumn<StudentDashboardRow, String> colRemaining,
                           TableColumn<StudentDashboardRow, Boolean> colSelect) {
        colOfferingId.setCellValueFactory(cell -> cell.getValue().getOfferingIdProperty());
        colCourseId.setCellValueFactory(cell -> cell.getValue().getCourseIdProperty());
        colCourseName.setCellValueFactory(cell -> cell.getValue().getCourseNameProperty());
        colCredits.setCellValueFactory(cell -> cell.getValue().getCreditsProperty());
        colInstructor.setCellValueFactory(cell -> cell.getValue().getInstructorProperty());
        colSemester.setCellValueFactory(cell -> cell.getValue().getSemesterProperty());
        colSchedule.setCellValueFactory(cell -> cell.getValue().getScheduleProperty());
        colRoom.setCellValueFactory(cell -> cell.getValue().getRoomProperty());
        colCapacity.setCellValueFactory(cell -> cell.getValue().getCapacityProperty());
        colRemaining.setCellValueFactory(cell -> cell.getValue().getRemainingProperty());
        colSelect.setCellValueFactory(cell -> cell.getValue().getSelectedProperty());
        colSelect.setCellFactory(CheckBoxTableCell.forTableColumn(colSelect));
        
        // Set custom cell factory for schedule column to enable multiline text
        colSchedule.setCellFactory(column -> {
            TableCell<StudentDashboardRow, String> cell = new TableCell<>() {
                private final Text text = new Text();
                
                {
                    text.wrappingWidthProperty().bind(column.widthProperty().subtract(10));
                    text.setTextAlignment(TextAlignment.CENTER);
                    text.setStyle("-fx-font-family: 'Roboto'; -fx-font-size: 12px;");
                }
                
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setGraphic(null);
                    } else {
                        text.setText(item);
                        setGraphic(text);
                    }
                }
            };
            return cell;
        });
    }
}

