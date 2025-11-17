package main.java.controller.admin.courseOffering;

import java.util.List;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.scene.control.TextField;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import main.java.model.CourseOffering;
import main.java.model.Schedule;
import main.java.service.impl.CourseOfferingServiceImpl;
import main.java.service.impl.CourseOfferingScheduleServiceImpl;
import main.java.service.impl.CourseServiceImpl;
import main.java.service.impl.SemesterServiceImpl;
import main.java.service.impl.RoomServiceImpl;
import main.java.service.impl.ScheduleServiceImpl;
import main.java.service.impl.MajorServiceImpl;
// form data binding removed - read directly from UI controls on save
import main.java.dto.admin.courseOffering.ScheduleRow;
import main.java.model.Course;
import main.java.model.Semester;
import main.java.model.Major;
import main.java.utils.FXUtils;
import java.time.LocalDate;

import static main.java.utils.GenericUtils.getStageFromSource;

import java.sql.Date;

public class CreateCourseOfferingFormController {
    @FXML private Button cancelButton;
    @FXML private Button saveButton;
    @FXML private TextField offeringIdField;
    @FXML private ComboBox<String> courseComboBox;
    @FXML private TextField instructorField;
    @FXML private ComboBox<String> semesterComboBox;
    @FXML private ComboBox<String> roomComboBox;
    @FXML private ComboBox<String> majorComboBox;
    @FXML private TextField maxCapacityField;
    @FXML private TextField currentCapacityField;
    @FXML private ListView<ScheduleRow> availableSchedulesList;
    @FXML private ListView<ScheduleRow> selectedSchedulesList;
    @FXML private Button addScheduleButton;
    @FXML private Button removeScheduleButton;

    private final ObservableList<ScheduleRow> availableSchedules = FXCollections.observableArrayList();
    private final ObservableList<ScheduleRow> chosenSchedules = FXCollections.observableArrayList();

    // Services
    private final CourseOfferingServiceImpl courseOfferingService = new CourseOfferingServiceImpl();
    private final CourseOfferingScheduleServiceImpl courseOfferingScheduleService = new CourseOfferingScheduleServiceImpl();
    private final CourseServiceImpl courseService = new CourseServiceImpl();
    private final SemesterServiceImpl semesterService = new SemesterServiceImpl();
    private final RoomServiceImpl roomService = new RoomServiceImpl();
    private final ScheduleServiceImpl scheduleService = new ScheduleServiceImpl();
    private final MajorServiceImpl majorService = new MajorServiceImpl();

    @FXML
    public void initialize() {
        loadOptionData();
        bindFields();
    }
    
    private void bindFields() {
        // Schedule list binding
        if (availableSchedulesList != null) availableSchedulesList.setItems(availableSchedules);
        if (selectedSchedulesList != null) selectedSchedulesList.setItems(chosenSchedules);
        if (addScheduleButton != null) addScheduleButton.setOnAction(e -> handleAddSchedule());
        if (removeScheduleButton != null) removeScheduleButton.setOnAction(e -> handleRemoveSchedule());
        if (currentCapacityField != null && (currentCapacityField.getText() == null || currentCapacityField.getText().isEmpty())) {
            currentCapacityField.setText("0");
        }
    }

    private void loadOptionData() {
        // Courses
        try {
            List<Course> courses = courseService.getAllCourses();
            if (courseComboBox != null) {
                courseComboBox.setItems(FXCollections.observableArrayList(
                    courses.stream().map(Course::getCourseId).toList()
                ));
            }
        } catch (Exception e) {
            if (courseComboBox != null) courseComboBox.setItems(FXCollections.observableArrayList());
        }

        // Semesters
        try {
            List<Semester> semesters = semesterService.getAllSemesters();
            if (semesterComboBox != null) {
                semesterComboBox.setItems(FXCollections.observableArrayList(
                    semesters.stream().map(Semester::getSemesterId).toList()
                ));
            }
        } catch (Exception e) {
            if (semesterComboBox != null) semesterComboBox.setItems(FXCollections.observableArrayList());
        }

        // Rooms
        try {
            var rooms = roomService.getAllRooms();
            if (roomComboBox != null) {
                roomComboBox.setItems(FXCollections.observableArrayList(
                    rooms.stream().map(r -> r.getRoomId()).toList()
                ));
            }
        } catch (Exception e) {
            if (roomComboBox != null) roomComboBox.setItems(FXCollections.observableArrayList());
        }

        // Majors
        try {
            var majors = majorService.getAllMajors();
            if (majorComboBox != null) {
                majorComboBox.setItems(FXCollections.observableArrayList(
                    majors.stream().map(Major::getMajorId).toList()
                ));
            }
        } catch (Exception e) {
            if (majorComboBox != null) majorComboBox.setItems(FXCollections.observableArrayList());
        }

        // Schedules
        availableSchedules.clear();
        try {
            List<Schedule> all = scheduleService.getAllSchedules();
            for (Schedule s : all) {
                if (s != null && s.getScheduleId() != null) {
                    availableSchedules.add(new ScheduleRow(s));
                }
            }
            if (availableSchedules.isEmpty()) {
                // Fallback
                availableSchedules.addAll(
                    new ScheduleRow("T2","07:30-09:30"),
                    new ScheduleRow("T3","09:45-11:45"),
                    new ScheduleRow("T5","13:00-15:00")
                );
            }
        } catch (Exception e) {
            availableSchedules.addAll(
                new ScheduleRow("T2","07:30-09:30"),
                new ScheduleRow("T3","09:45-11:45")
            );
        }
    }

    private void handleAddSchedule() {
        ScheduleRow sel = availableSchedulesList.getSelectionModel().getSelectedItem();
        if (sel != null && !chosenSchedules.contains(sel)) chosenSchedules.add(sel);
    }

    private void handleRemoveSchedule() {
        ScheduleRow sel = selectedSchedulesList.getSelectionModel().getSelectedItem();
        if (sel != null) chosenSchedules.remove(sel);
    }

    @FXML
    private void handleCancel() {
        closeWindow();
    }

    @FXML
    private void handleSave() {
        try {
            validateForm();
            CourseOffering offering = buildCourseOffering();
            courseOfferingService.createCourseOffering(
                offering,
                courseComboBox != null ? courseComboBox.getValue() : null,
                semesterComboBox != null ? semesterComboBox.getValue() : null,
                roomComboBox != null ? roomComboBox.getValue() : null
            );
            Date startDate = deriveStartDate();
            Date endDate = deriveEndDate(startDate);
            for (ScheduleRow row : chosenSchedules) {
                if (row.getScheduleId() != null && !row.getScheduleId().startsWith("MANUAL_")) {
                    courseOfferingScheduleService.assignScheduleToCourseOffering(
                        offering.getCourseOfferingId(),
                        row.getScheduleId(),
                        startDate,
                        endDate
                    );
                }
            }
            FXUtils.showSuccess("Tạo lớp học phần thành công");
            closeWindow();
        } catch (Exception ex) {
            FXUtils.showError("Lưu thất bại: " + ex.getMessage());
        }
    }

    // Validation
    private void validateForm() {
        StringBuilder sb = new StringBuilder();
        String offeringId = offeringIdField != null ? offeringIdField.getText() : null;
        String courseId = courseComboBox != null ? courseComboBox.getValue() : null;
        String semesterId = semesterComboBox != null ? semesterComboBox.getValue() : null;
        String roomId = roomComboBox != null ? roomComboBox.getValue() : null;
        String maxCapacity = maxCapacityField != null ? maxCapacityField.getText() : null;
        String majorId = majorComboBox != null ? majorComboBox.getValue() : null;

        if (isBlank(offeringId)) sb.append("- Mã lớp mở trống\n");
        if (isBlank(courseId)) sb.append("- Chưa chọn môn học\n");
        if (isBlank(semesterId)) sb.append("- Chưa chọn học kỳ\n");
        if (isBlank(roomId)) sb.append("- Chưa chọn phòng\n");
        if (isBlank(maxCapacity)) sb.append("- Sĩ số tối đa trống\n");
        if (isBlank(majorId)) sb.append("- Chưa chọn ngành\n");
        if (chosenSchedules.isEmpty()) sb.append("- Chưa chọn lịch học\n");
        // Numeric check
        if (!isBlank(maxCapacity)) {
            try {
                int max = Integer.parseInt(maxCapacity);
                if (max <= 0) sb.append("- Sĩ số tối đa phải > 0\n");
            } catch (NumberFormatException e) {
                sb.append("- Sĩ số tối đa không phải số nguyên\n");
            }
        }
        String currentCapacity = currentCapacityField != null ? currentCapacityField.getText() : null;
        if (!isBlank(currentCapacity)) {
            try {
                int cur = Integer.parseInt(currentCapacity);
                if (cur < 0) sb.append("- Sĩ số hiện tại âm\n");
            } catch (NumberFormatException e) {
                sb.append("- Sĩ số hiện tại không phải số nguyên\n");
            }
        }
        if (sb.length() > 0) throw new IllegalArgumentException(sb.toString().trim());
    }

    private CourseOffering buildCourseOffering() {
        CourseOffering co = new CourseOffering();
        co.setCourseOfferingId(offeringIdField != null ? offeringIdField.getText() : null);
        co.setInstructor(instructorField != null ? instructorField.getText() : null);
        co.setMaxCapacity(maxCapacityField != null ? maxCapacityField.getText() : null);
        String cur = currentCapacityField != null ? currentCapacityField.getText() : null;
        co.setCurrentCapacity(isBlank(cur) ? "0" : cur);
        co.setMajorId(majorComboBox != null ? majorComboBox.getValue() : null);
        return co;
    }

    private Date deriveStartDate() {
        try {
            String semId = semesterComboBox != null ? semesterComboBox.getValue() : null;
            Semester s = (semId != null) ? semesterService.getSemesterById(semId) : null;
            if (s != null && s.getStartDate() != null) {
                return Date.valueOf(s.getStartDate());
            }
        } catch (Exception ignored) {}
        return Date.valueOf(LocalDate.now());
    }

    private Date deriveEndDate(Date start) {
        try {
            String semId = semesterComboBox != null ? semesterComboBox.getValue() : null;
            Semester s = (semId != null) ? semesterService.getSemesterById(semId) : null;
            if (s != null && s.getEndDate() != null) {
                return Date.valueOf(s.getEndDate());
            }
        } catch (Exception ignored) {}
        LocalDate st = start.toLocalDate();
        return Date.valueOf(st.plusDays(90));
    }

    private boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }
    private void closeWindow() {
        Stage stage = getStageFromSource(cancelButton);
        if (stage != null) stage.close();
    }
}

// 355