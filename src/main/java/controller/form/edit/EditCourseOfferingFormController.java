package main.java.controller.form.edit;

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
import main.java.service.impl.SemesterServiceImpl;
import main.java.service.impl.RoomServiceImpl;
import main.java.service.impl.ScheduleServiceImpl;
import main.java.model.Course;
import main.java.model.Semester;
import main.java.utils.FXUtils;
import java.time.LocalDate;
import java.sql.Date;

public class EditCourseOfferingFormController {
    @FXML private Button cancelButton;
    @FXML private Button saveButton;
    @FXML private TextField offeringIdField;
    @FXML private ComboBox<String> courseComboBox;
    // Edit form uses a lecturer ComboBox instead of a free-text instructor field
    @FXML private ComboBox<String> lecturerComboBox;
    @FXML private ComboBox<String> semesterComboBox;
    @FXML private ComboBox<String> roomComboBox;
    // Edit form has a single capacity field
    @FXML private TextField capacityField;
    // Optional (keep compatibility if later added)
    @FXML private TextField maxCapacityField;
    @FXML private TextField currentCapacityField;
    @FXML private ListView<ScheduleRow> availableSchedulesList;
    @FXML private ListView<ScheduleRow> selectedSchedulesList;
    // IDs follow FXML: scheduleAddButton/scheduleRemoveButton and useSuggestedScheduleButton
    @FXML private Button scheduleAddButton;
    @FXML private Button scheduleRemoveButton;
    @FXML private Button useSuggestedScheduleButton;
    @FXML private ComboBox<String> scheduleDayComboBox;
    @FXML private ComboBox<String> scheduleStartTimeComboBox;
    @FXML private ComboBox<String> scheduleEndTimeComboBox;

    // Form data model
    public static class CourseOfferingFormData {
        private final StringProperty offeringId = new SimpleStringProperty();
        private final StringProperty courseId = new SimpleStringProperty();
        private final StringProperty instructor = new SimpleStringProperty();
        private final StringProperty semesterId = new SimpleStringProperty();
        private final StringProperty roomId = new SimpleStringProperty();
        // private final StringProperty majorId = new SimpleStringProperty();
        private final StringProperty maxCapacity = new SimpleStringProperty();
        private final StringProperty currentCapacity = new SimpleStringProperty();
        public StringProperty getOfferingIdProperty() { return offeringId; }
        public StringProperty getCourseIdProperty() { return courseId; }
        public StringProperty getInstructorProperty() { return instructor; }
        public StringProperty getSemesterIdProperty() { return semesterId; }
        public StringProperty getRoomIdProperty() { return roomId; }
        // public StringProperty getMajorIdProperty() { return majorId; }
        public StringProperty getMaxCapacityProperty() { return maxCapacity; }
        public StringProperty getCurrentCapacityProperty() { return currentCapacity; }
        public String getOfferingId() { return offeringId.get(); }
        public String getCourseId() { return courseId.get(); }
        public String getInstructor() { return instructor.get(); }
        public String getSemesterId() { return semesterId.get(); }
        public String getRoomId() { return roomId.get(); }
        // public String getMajorId() { return majorId.get(); }
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
    private final SemesterServiceImpl semesterService = new SemesterServiceImpl();
    private final RoomServiceImpl roomService = new RoomServiceImpl();
    private final ScheduleServiceImpl scheduleService = new ScheduleServiceImpl();
    // No majors manipulation in edit form currently

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
    }
    
    private void bindFields() {
        if (offeringIdField != null) offeringIdField.textProperty().bindBidirectional(formData.getOfferingIdProperty());
        if (courseComboBox != null) courseComboBox.valueProperty().bindBidirectional(formData.getCourseIdProperty());
        // Bind instructor to lecturer combo if available
        if (lecturerComboBox != null) lecturerComboBox.valueProperty().bindBidirectional(formData.getInstructorProperty());
        if (semesterComboBox != null) semesterComboBox.valueProperty().bindBidirectional(formData.getSemesterIdProperty());
        if (roomComboBox != null) roomComboBox.valueProperty().bindBidirectional(formData.getRoomIdProperty());
        // Capacity: bind whichever field exists in the form
        if (capacityField != null) capacityField.textProperty().bindBidirectional(formData.getMaxCapacityProperty());
        else if (maxCapacityField != null) maxCapacityField.textProperty().bindBidirectional(formData.getMaxCapacityProperty());
        if (currentCapacityField != null) currentCapacityField.textProperty().bindBidirectional(formData.getCurrentCapacityProperty());
        // Schedule list binding
        if (availableSchedulesList != null) availableSchedulesList.setItems(availableSchedules);
        if (selectedSchedulesList != null) selectedSchedulesList.setItems(chosenSchedules);
        if (scheduleAddButton != null) scheduleAddButton.setOnAction(e -> handleAddSchedule());
        if (scheduleRemoveButton != null) scheduleRemoveButton.setOnAction(e -> handleRemoveSchedule());
        if (useSuggestedScheduleButton != null) useSuggestedScheduleButton.setOnAction(e -> handleUseSuggested());
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

        // No majors field in edit form UI; skip loading majors

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
                // Mẫu
                availableSchedules.addAll(
                    new ScheduleRow("T2","07:30-09:30"),
                    new ScheduleRow("T3","09:45-11:45"),
                    new ScheduleRow("T5","13:00-15:00")
                );
            }
        } catch (Exception e) {
            // Mẫu
            availableSchedules.addAll(
                new ScheduleRow("T2","07:30-09:30"),
                new ScheduleRow("T3","09:45-11:45")
            );
        }
    }

    @FXML
    private void handleAddSchedule() {
        // Prefer manual add from day/time combos if provided
        if (scheduleDayComboBox != null && scheduleStartTimeComboBox != null && scheduleEndTimeComboBox != null) {
            String day = scheduleDayComboBox.getValue();
            String start = scheduleStartTimeComboBox.getValue();
            String end = scheduleEndTimeComboBox.getValue();
            if (day != null && start != null && end != null) {
                ScheduleRow manual = new ScheduleRow(day, start + "-" + end);
                if (!chosenSchedules.contains(manual)) {
                    chosenSchedules.add(manual);
                    return;
                }
            }
        }
        // Fallback: move from available list
        if (availableSchedulesList != null) {
            ScheduleRow sel = availableSchedulesList.getSelectionModel().getSelectedItem();
            if (sel != null && !chosenSchedules.contains(sel)) chosenSchedules.add(sel);
        }
    }

    @FXML
    private void handleRemoveSchedule() {
        ScheduleRow sel = selectedSchedulesList.getSelectionModel().getSelectedItem();
        if (sel != null) chosenSchedules.remove(sel);
    }

    @FXML
    private void handleUseSuggested() {
        if (availableSchedulesList == null) return;
        ScheduleRow sel = availableSchedulesList.getSelectionModel().getSelectedItem();
        if (sel != null && !chosenSchedules.contains(sel)) chosenSchedules.add(sel);
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
            boolean updated = courseOfferingService.updateCourseOffering(offering);
            if (!updated) {
                FXUtils.showError("Không thể cập nhật lớp học phần");
                return;
            }
            // Refresh schedules: delete all then assign selected
            courseOfferingScheduleService.deleteAllSchedulesOfCourseOffering(offering.getCourseOfferingId());
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
            FXUtils.showSuccess("Cập nhật lớp học phần thành công");
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
        // Edit form không có ngành, bỏ bắt buộc major
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
        // currentCapacity không có trên form; bỏ kiểm tra nếu trống
        if (sb.length() > 0) throw new IllegalArgumentException(sb.toString().trim());
    }

    private CourseOffering buildCourseOffering() {
        CourseOffering co = new CourseOffering();
        co.setCourseOfferingId(formData.getOfferingId());
        co.setInstructor(formData.getInstructor());
        co.setMaxCapacity(formData.getMaxCapacity());
        // default current capacity
        co.setCurrentCapacity(isBlank(formData.getCurrentCapacity()) ? "0" : formData.getCurrentCapacity());
        // Update flow: set courseId, semesterId, roomId directly on the model
        co.setCourseId(formData.getCourseId());
        co.setSemesterId(formData.getSemesterId());
        co.setRoomId(formData.getRoomId());
        return co;
    }

    private Date deriveStartDate() {
        try {
            String semId = formData.getSemesterId();
            Semester s = (semId != null) ? semesterService.getSemesterById(semId) : null;
            if (s != null && s.getStartDate() != null) {
                return Date.valueOf(s.getStartDate());
            }
        } catch (Exception ignored) {}
        return Date.valueOf(LocalDate.now());
    }

    private Date deriveEndDate(Date start) {
        try {
            String semId = formData.getSemesterId();
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
        if (cancelButton != null && cancelButton.getScene() != null) {
            Stage stage = (Stage) cancelButton.getScene().getWindow();
            if (stage != null) stage.close();
        }
    }

    // Prefill form from existing course offering
    public void prefillFrom(CourseOffering offering) {
        if (offering == null) return;
        formData.getOfferingIdProperty().set(offering.getCourseOfferingId());
        formData.getCourseIdProperty().set(offering.getCourseId());
        formData.getInstructorProperty().set(offering.getInstructor());
        formData.getSemesterIdProperty().set(offering.getSemesterId());
        formData.getRoomIdProperty().set(offering.getRoomId());
        formData.getMaxCapacityProperty().set(offering.getMaxCapacity());
        formData.getCurrentCapacityProperty().set(offering.getCurrentCapacity());
        // Prefill selected schedules
        try {
            chosenSchedules.clear();
            List<Schedule> schedules = scheduleService.getSchedulesByCourseOfferingId(offering.getCourseOfferingId());
            for (Schedule s : schedules) {
                if (s != null) chosenSchedules.add(new ScheduleRow(s));
            }
        } catch (Exception ignored) {}
        // disable id editing to avoid key change
        if (offeringIdField != null) offeringIdField.setDisable(true);
    }
}
