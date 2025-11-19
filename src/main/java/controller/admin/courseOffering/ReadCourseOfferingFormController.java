package main.java.controller.admin.courseOffering;

import java.util.List;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import main.java.model.Course;
import main.java.model.CourseOffering;
import main.java.model.Room;
import main.java.model.Schedule;
import main.java.model.Semester;
import main.java.model.Faculty;
import main.java.service.impl.CourseOfferingScheduleServiceImpl;
import main.java.service.impl.CourseServiceImpl;
import main.java.service.impl.RoomServiceImpl;
import main.java.service.impl.SemesterServiceImpl;
import main.java.service.impl.FacultyServiceImpl;

import static main.java.utils.GenericUtils.safeParseString;

public class ReadCourseOfferingFormController {
    @FXML private Label offeringCodeLabel;
    @FXML private Label courseLabel;
    @FXML private Label facultyLabel;
    @FXML private Label lecturerLabel;
    @FXML private Label roomLabel;
    @FXML private Label capacityLabel;
    @FXML private Label semesterLabel;
    @FXML private ListView<String> scheduleListView;
    @FXML private Button editButton;
    @FXML private Button deleteButton;
    @FXML private Button closeButton;

    private final CourseServiceImpl courseService = new CourseServiceImpl();
    private final SemesterServiceImpl semesterService = new SemesterServiceImpl();
    private final RoomServiceImpl roomService = new RoomServiceImpl();
    private final CourseOfferingScheduleServiceImpl scheduleService = new CourseOfferingScheduleServiceImpl();
    private final FacultyServiceImpl facultyService = new FacultyServiceImpl();

    @SuppressWarnings("unused")
    private CourseOffering currentOffering;

    public void prefillFrom(CourseOffering offering) {
        this.currentOffering = offering;
        if (offering == null) return;
        if (offeringCodeLabel != null) offeringCodeLabel.setText(safeParseString(offering.getCourseOfferingId()));

        // Course
        String courseText = "-";
        try {
            if (offering.getCourseId() != null) {
                Course c = courseService.getCourseById(offering.getCourseId());
                courseText = (c != null && c.getCourseName() != null) ? c.getCourseName() : offering.getCourseId();
            }
        } catch (Exception ignored) { }
        if (courseLabel != null) courseLabel.setText(courseText);

        // Faculty
        String facultyText = "-";
        try {
            if (offering.getFacultyId() != null) {
                Faculty f = facultyService.getFacultyById(offering.getFacultyId());
                facultyText = (f != null && f.getFacultyName() != null) ? f.getFacultyName() : offering.getFacultyId();
            }
        } catch (Exception ignored) { }
        if (facultyLabel != null) facultyLabel.setText(facultyText);

        // Lecturer
        if (lecturerLabel != null) lecturerLabel.setText(safeParseString(offering.getInstructor()));

        // Room
        String roomText = "-";
        try {
            if (offering.getRoomId() != null) {
                Room r = roomService.getRoomById(offering.getRoomId());
                roomText = (r != null && r.getRoomId() != null) ? r.getRoomId() : offering.getRoomId();
            }
        } catch (Exception ignored) { }
        if (roomLabel != null) roomLabel.setText(roomText);

        // Capacity (max only per FXML)
        if (capacityLabel != null) capacityLabel.setText(safeParseString(offering.getMaxCapacity()));

        // Semester term/year
        String semesterText = "-";
        try {
            if (offering.getSemesterId() != null) {
                Semester s = semesterService.getSemesterById(offering.getSemesterId());
                if (s != null) {
                    String term = s.getTerm();
                    String year = s.getAcademicYear();
                    if (term != null && year != null) semesterText = term + "/" + year;
                    else if (term != null) semesterText = term;
                    else semesterText = offering.getSemesterId();
                } else {
                    semesterText = offering.getSemesterId();
                }
            }
        } catch (Exception ex) { 
            ex.printStackTrace();
        }
        if (semesterLabel != null) semesterLabel.setText(semesterText);

        try {
            List<Schedule> schedules = scheduleService.getSchedulesByCourseOfferingId(offering.getCourseOfferingId());
            if (scheduleListView != null) {
                if (schedules == null || schedules.isEmpty()) {
                    scheduleListView.setItems(FXCollections.observableArrayList());
                } else {
                    scheduleListView.setItems(FXCollections.observableArrayList(
                        schedules.stream()
                            .map(Schedule::getFullSchedule)
                            .toList()
                    ));
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    private void handleClose() {
        if (closeButton != null && closeButton.getScene() != null) {
            Stage st = (Stage) closeButton.getScene().getWindow();
            if (st != null) st.close();
        }
    }

}
