package main.java.controller.form.read;

import java.time.format.DateTimeFormatter;
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
import main.java.service.impl.CourseOfferingScheduleServiceImpl;
import main.java.service.impl.CourseServiceImpl;
import main.java.service.impl.RoomServiceImpl;
import main.java.service.impl.SemesterServiceImpl;

public class ReadCourseOfferingFormController {
    @FXML private Label offeringCodeLabel;
    @FXML private Label courseLabel;
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

    private CourseOffering currentOffering;

    public void prefillFrom(CourseOffering offering) {
        this.currentOffering = offering;
        if (offering == null) return;
        if (offeringCodeLabel != null) offeringCodeLabel.setText(safe(offering.getCourseOfferingId()));

        // Course
        String courseText = "-";
        try {
            if (offering.getCourseId() != null) {
                Course c = courseService.getCourseById(offering.getCourseId());
                courseText = (c != null && c.getCourseName() != null) ? c.getCourseName() : offering.getCourseId();
            }
        } catch (Exception ignored) { }
        if (courseLabel != null) courseLabel.setText(courseText);

        // Lecturer
        if (lecturerLabel != null) lecturerLabel.setText(safe(offering.getInstructor()));

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
        if (capacityLabel != null) capacityLabel.setText(safe(offering.getMaxCapacity()));

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
        } catch (Exception ignored) { }
        if (semesterLabel != null) semesterLabel.setText(semesterText);

        // Schedules
        try {
            List<Schedule> schedules = scheduleService.getSchedulesByCourseOfferingId(offering.getCourseOfferingId());
            if (scheduleListView != null) {
                DateTimeFormatter tf = DateTimeFormatter.ofPattern("HH:mm");
                scheduleListView.setItems(FXCollections.observableArrayList(
                    schedules.stream().map(s -> {
                        String day;
                        switch (s.getDayOfWeek()) {
                            case 2: day = "T2"; break;
                            case 3: day = "T3"; break;
                            case 4: day = "T4"; break;
                            case 5: day = "T5"; break;
                            case 6: day = "T6"; break;
                            case 7: day = "T7"; break;
                            case 1:
                            case 8: day = "CN"; break;
                            default: day = "T?"; break;
                        }
                        String start = s.getStartTime() != null ? tf.format(s.getStartTime()) : "";
                        String end = s.getEndTime() != null ? tf.format(s.getEndTime()) : "";
                        String time = (!start.isEmpty() && !end.isEmpty()) ? (start + "-" + end) : (start + end);
                        return day + (time.isEmpty() ? "" : " " + time);
                    }).toList()
                ));
            }
        } catch (Exception ignored) { }
    }

    @FXML
    private void handleClose() {
        if (closeButton != null && closeButton.getScene() != null) {
            Stage st = (Stage) closeButton.getScene().getWindow();
            if (st != null) st.close();
        }
    }

    private String safe(String s) { return s == null ? "-" : s; }
}
