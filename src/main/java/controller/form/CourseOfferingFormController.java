package main.java.controller.form;

import java.util.List;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.scene.control.TextField;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import main.java.model.CourseOffering;
import main.java.model.Schedule;
import main.java.service.impl.CourseOfferingServiceImpl;
import main.java.service.impl.CourseOfferingScheduleServiceImpl;
import main.java.service.impl.CourseServiceImpl;
import main.java.repository.ScheduleRepository;
import main.java.repository.SemesterRepository;
import main.java.repository.RoomRepository;
import main.java.model.Course;
import main.java.model.Semester;
import main.java.utils.FXUtils;
import java.time.LocalDate;
import java.sql.Date;

public class CourseOfferingFormController {
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

    // Form data model (binding convention giống StudentDashboardRow)
    public static class CourseOfferingFormData {
        private final StringProperty offeringId = new SimpleStringProperty();
        private final StringProperty courseId = new SimpleStringProperty();
        private final StringProperty instructor = new SimpleStringProperty();
        private final StringProperty semesterId = new SimpleStringProperty();
        private final StringProperty roomId = new SimpleStringProperty();
        private final StringProperty majorId = new SimpleStringProperty();
        private final StringProperty maxCapacity = new SimpleStringProperty();
        private final StringProperty currentCapacity = new SimpleStringProperty();
        public StringProperty getOfferingIdProperty() { return offeringId; }
        public StringProperty getCourseIdProperty() { return courseId; }
        public StringProperty getInstructorProperty() { return instructor; }
        public StringProperty getSemesterIdProperty() { return semesterId; }
        public StringProperty getRoomIdProperty() { return roomId; }
        public StringProperty getMajorIdProperty() { return majorId; }
        public StringProperty getMaxCapacityProperty() { return maxCapacity; }
        public StringProperty getCurrentCapacityProperty() { return currentCapacity; }
        public String getOfferingId() { return offeringId.get(); }
        public String getCourseId() { return courseId.get(); }
        public String getInstructor() { return instructor.get(); }
        public String getSemesterId() { return semesterId.get(); }
        public String getRoomId() { return roomId.get(); }
        public String getMajorId() { return majorId.get(); }
        public String getMaxCapacity() { return maxCapacity.get(); }
        public String getCurrentCapacity() { return currentCapacity.get(); }
    }

    private final ObservableList<ScheduleRow> availableSchedules = FXCollections.observableArrayList();
    private final ObservableList<ScheduleRow> chosenSchedules = FXCollections.observableArrayList();
    private final CourseOfferingFormData formData = new CourseOfferingFormData();

    // Services
    private final CourseOfferingServiceImpl courseOfferingService = new CourseOfferingServiceImpl();
    private final CourseOfferingScheduleServiceImpl courseOfferingScheduleService = new CourseOfferingScheduleServiceImpl();
    private final CourseServiceImpl courseService = new CourseServiceImpl();
    private final SemesterRepository semesterRepository = new SemesterRepository();
    private final RoomRepository roomRepository = new RoomRepository();
    private final ScheduleRepository scheduleRepository = new ScheduleRepository();

    public static class ScheduleRow {
        private final StringProperty scheduleId;
        private final StringProperty day;
        private final StringProperty timeRange;
        private final StringProperty display;
        public ScheduleRow(Schedule schedule) {
            this.scheduleId = new SimpleStringProperty(schedule.getScheduleId());
            String d = mapDay(schedule.getDayOfWeek());
            String start = schedule.getStartTime() != null ? schedule.getStartTime().toString() : "";
            String end = schedule.getEndTime() != null ? schedule.getEndTime().toString() : "";
            String range = (!start.isEmpty() && !end.isEmpty()) ? start + "-" + end : start + end;
            this.day = new SimpleStringProperty(d);
            this.timeRange = new SimpleStringProperty(range);
            this.display = new SimpleStringProperty(d + (range.isEmpty() ? "" : " " + range));
        }
        // legacy constructor kept (optional) if you still add manual rows
        public ScheduleRow(String day, String timeRange) {
            this.scheduleId = new SimpleStringProperty("MANUAL_" + day + "_" + timeRange);
            this.day = new SimpleStringProperty(day);
            this.timeRange = new SimpleStringProperty(timeRange);
            this.display = new SimpleStringProperty(day + " " + timeRange);
        }
        public StringProperty getScheduleIdProperty() { return scheduleId; }
        public String getScheduleId() { return scheduleId.get(); }
        public StringProperty getDayProperty() { return day; }
        public StringProperty getTimeRangeProperty() { return timeRange; }
        public StringProperty getDisplayProperty() { return display; }
        public String getDay() { return day.get(); }
        public String getTimeRange() { return timeRange.get(); }
        @Override public String toString() { return display.get(); }
        private String mapDay(int dow) {
            switch (dow) {
                case 2: return "T2";
                case 3: return "T3";
                case 4: return "T4";
                case 5: return "T5";
                case 6: return "T6";
                case 7: return "T7";
                case 8: 
                case 1: return "CN";
                default: return "T?";
            }
        }
    }

    @FXML
    public void initialize() {
        loadOptionData();
        bindFields();
        // Optional: preset currentCapacity = 0
    }
    
    private void bindFields() {
        if (offeringIdField != null) offeringIdField.textProperty().bindBidirectional(formData.getOfferingIdProperty());
        if (courseComboBox != null) courseComboBox.valueProperty().bindBidirectional(formData.getCourseIdProperty());
        if (instructorField != null) instructorField.textProperty().bindBidirectional(formData.getInstructorProperty());
        if (semesterComboBox != null) semesterComboBox.valueProperty().bindBidirectional(formData.getSemesterIdProperty());
        if (roomComboBox != null) roomComboBox.valueProperty().bindBidirectional(formData.getRoomIdProperty());
        if (majorComboBox != null) majorComboBox.valueProperty().bindBidirectional(formData.getMajorIdProperty());
        if (maxCapacityField != null) maxCapacityField.textProperty().bindBidirectional(formData.getMaxCapacityProperty());
        if (currentCapacityField != null) currentCapacityField.textProperty().bindBidirectional(formData.getCurrentCapacityProperty());
        // Schedule list binding
        if (availableSchedulesList != null) availableSchedulesList.setItems(availableSchedules);
        if (selectedSchedulesList != null) selectedSchedulesList.setItems(chosenSchedules);
        if (addScheduleButton != null) addScheduleButton.setOnAction(e -> handleAddSchedule());
        if (removeScheduleButton != null) removeScheduleButton.setOnAction(e -> handleRemoveSchedule());
        if (formData.getCurrentCapacityProperty().get() == null || formData.getCurrentCapacityProperty().get().isEmpty()) {
            formData.getCurrentCapacityProperty().set("0");
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
            List<Semester> semesters = semesterRepository.findAll();
            if (semesterComboBox != null) {
                semesterComboBox.setItems(FXCollections.observableArrayList(
                    semesters.stream()
                        .map(Semester::getSemesterId)
                        .toList()
                ));
            }
        } catch (Exception e) {
            if (semesterComboBox != null) semesterComboBox.setItems(FXCollections.observableArrayList());
        }

        // Rooms
        try {
            var rooms = roomRepository.findAll();
            if (roomComboBox != null) {
                roomComboBox.setItems(FXCollections.observableArrayList(
                    rooms.stream()
                         .map(r -> r.getRoomId())
                         .toList()
                ));
            }
        } catch (Exception e) {
            if (roomComboBox != null) roomComboBox.setItems(FXCollections.observableArrayList());
        }

        // Majors (TODO: replace with MajorRepository when available)
        if (majorComboBox != null) {
            majorComboBox.setItems(FXCollections.observableArrayList()); // keep empty until repo exists
        }

        // Schedules
        availableSchedules.clear();
        try {
            List<Schedule> all = scheduleRepository.findAll();
            for (Schedule s : all) {
                if (s != null && s.getScheduleId() != null) {
                    availableSchedules.add(new ScheduleRow(s));
                }
            }
            if (availableSchedules.isEmpty()) {
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
        // Later: map formData + chosenSchedules -> CourseOffering + schedules
        try {
            validateForm();
            CourseOffering offering = buildCourseOffering();
            courseOfferingService.createCourseOffering(
                offering,
                formData.getCourseId(),
                formData.getSemesterId(),
                formData.getRoomId()
            );
            // Derive dates from selected semester if possible
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
        if (isBlank(formData.getOfferingId())) sb.append("- Mã lớp mở trống\n");
        if (isBlank(formData.getCourseId())) sb.append("- Chưa chọn môn học\n");
        if (isBlank(formData.getSemesterId())) sb.append("- Chưa chọn học kỳ\n");
        if (isBlank(formData.getRoomId())) sb.append("- Chưa chọn phòng\n");
        if (isBlank(formData.getMaxCapacity())) sb.append("- Sĩ số tối đa trống\n");
        if (isBlank(formData.getMajorId())) sb.append("- Chưa chọn ngành\n");
        if (chosenSchedules.isEmpty()) sb.append("- Chưa chọn lịch học\n");
        // Numeric check
        if (!isBlank(formData.getMaxCapacity())) {
            try {
                int max = Integer.parseInt(formData.getMaxCapacity());
                if (max <= 0) sb.append("- Sĩ số tối đa phải > 0\n");
            } catch (NumberFormatException e) {
                sb.append("- Sĩ số tối đa không phải số nguyên\n");
            }
        }
        if (!isBlank(formData.getCurrentCapacity())) {
            try {
                int cur = Integer.parseInt(formData.getCurrentCapacity());
                if (cur < 0) sb.append("- Sĩ số hiện tại âm\n");
            } catch (NumberFormatException e) {
                sb.append("- Sĩ số hiện tại không phải số nguyên\n");
            }
        }
        if (sb.length() > 0) throw new IllegalArgumentException(sb.toString().trim());
    }

    private CourseOffering buildCourseOffering() {
        CourseOffering co = new CourseOffering();
        co.setCourseOfferingId(formData.getOfferingId());
        co.setInstructor(formData.getInstructor());
        co.setMaxCapacity(formData.getMaxCapacity());
        // default current capacity
        co.setCurrentCapacity(isBlank(formData.getCurrentCapacity()) ? "0" : formData.getCurrentCapacity());
        co.setMajorId(formData.getMajorId());
        // courseId, semesterId, roomId set in service
        return co;
    }

    private Date deriveStartDate() {
        try {
            String semId = formData.getSemesterId();
            Semester s = (semId != null) ? semesterRepository.findById(semId) : null;
            if (s != null && s.getStartDate() != null) {
                return Date.valueOf(s.getStartDate());
            }
        } catch (Exception ignored) {}
        return Date.valueOf(LocalDate.now());
    }

    private Date deriveEndDate(Date start) {
        try {
            String semId = formData.getSemesterId();
            Semester s = (semId != null) ? semesterRepository.findById(semId) : null;
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
        if (cancelButton != null && cancelButton.getScene() != null) {
            Stage stage = (Stage) cancelButton.getScene().getWindow();
            if (stage != null) stage.close();
        }
    }
}
