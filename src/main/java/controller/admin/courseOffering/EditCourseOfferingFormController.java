package main.java.controller.admin.courseOffering;

import static main.java.utils.FXUtils.closeWindow;
import static main.java.utils.GenericUtils.isBlank;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import main.java.dto.courseOffering.ScheduleRow;
import main.java.model.Course;
import main.java.model.CourseOffering;
import main.java.model.Faculty;
import main.java.model.Schedule;
import main.java.model.Semester;
import main.java.service.impl.CourseOfferingScheduleServiceImpl;
import main.java.service.impl.CourseOfferingServiceImpl;
import main.java.service.impl.CourseServiceImpl;
import main.java.service.impl.FacultyServiceImpl;
import main.java.service.impl.RoomServiceImpl;
import main.java.service.impl.ScheduleServiceImpl;
import main.java.service.impl.SemesterServiceImpl;
import main.java.utils.FXUtils;

public class EditCourseOfferingFormController {
    @FXML private Button cancelButton;
    @FXML private Button saveButton;
    @FXML private TextField offeringIdField;
    @FXML private TextField lecturerField;
    @FXML private ComboBox<String> courseComboBox;
    @FXML private ComboBox<String> semesterComboBox;
    @FXML private ComboBox<String> roomComboBox;
    @FXML private ComboBox<String> facultyComboBox;
    @FXML private TextField capacityField;
    @FXML private TextField currentCapacityField;
    @FXML private ListView<ScheduleRow> availableSchedulesList;
    @FXML private ListView<ScheduleRow> selectedSchedulesList;
    @FXML private Button scheduleRemoveButton;
    @FXML private Button useSuggestedScheduleButton;

    private final ObservableList<ScheduleRow> availableSchedules = FXCollections.observableArrayList();
    private final ObservableList<ScheduleRow> chosenSchedules = FXCollections.observableArrayList();

    // Services
    private final CourseOfferingServiceImpl courseOfferingService = new CourseOfferingServiceImpl();
    private final CourseOfferingScheduleServiceImpl courseOfferingScheduleService = new CourseOfferingScheduleServiceImpl();
    private final CourseServiceImpl courseService = new CourseServiceImpl();
    private final SemesterServiceImpl semesterService = new SemesterServiceImpl();
    private final RoomServiceImpl roomService = new RoomServiceImpl();
    private final ScheduleServiceImpl scheduleService = new ScheduleServiceImpl();
    private final FacultyServiceImpl facultyService = new FacultyServiceImpl();

    @FXML
    public void initialize() {
        loadOptionData();
        setupScheduleInputs();
    }
    
    private void setupScheduleInputs() {
        // Schedule list binding
        if (availableSchedulesList != null) availableSchedulesList.setItems(availableSchedules);
        if (selectedSchedulesList != null) selectedSchedulesList.setItems(chosenSchedules);
        if (scheduleRemoveButton != null) scheduleRemoveButton.setOnAction(e -> handleRemoveSchedule());
        if (useSuggestedScheduleButton != null) useSuggestedScheduleButton.setOnAction(e -> handleUseSuggested());
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
            e.printStackTrace();
            if (courseComboBox != null) courseComboBox.setItems(FXCollections.observableArrayList());
        }

        // Semesters
        try {
            List<Semester> semesters = semesterService.getAllSemesters();
            if (semesterComboBox != null) {
                semesterComboBox.setItems(FXCollections.observableArrayList(
                    semesters.stream().map(s -> {
                        String term = s.getTerm() != null ? s.getTerm() : "";
                        String year = s.getAcademicYear() != null ? s.getAcademicYear() : "";
                        String display = term + "/" + year;
                        if (display.equals("/")) display = s.getSemesterId();
                        return s.getSemesterId() + " - " + display;
                    }).toList()
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
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
            e.printStackTrace();
            if (roomComboBox != null) roomComboBox.setItems(FXCollections.observableArrayList());
        }

        // Faculties
        try {
            List<Faculty> faculties = facultyService.getAllFaculties();
            if (facultyComboBox != null) {
                facultyComboBox.setItems(FXCollections.observableArrayList(
                    faculties.stream().map(f -> f.getFacultyId() + " - " + f.getFacultyName()).toList()
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (facultyComboBox != null) facultyComboBox.setItems(FXCollections.observableArrayList());
        }

        // Load all available schedules from database
        availableSchedules.clear();
        try {
            List<Schedule> all = scheduleService.getAllSchedules();
            for (Schedule s : all) {
                if (s != null && s.getScheduleId() != null) {
                    availableSchedules.add(new ScheduleRow(s));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleRemoveSchedule() {
        ScheduleRow selected = selectedSchedulesList.getSelectionModel().getSelectedItem();
        if (selected != null) {
            chosenSchedules.remove(selected);
        }
    }

    @FXML
    private void handleUseSuggested() {
        ScheduleRow selected = availableSchedulesList.getSelectionModel().getSelectedItem();
        if (selected == null) return;
        
        // Check if already in chosen list
        if (chosenSchedules.contains(selected)) {
            FXUtils.showError("Lịch này đã được chọn rồi!");
            return;
        }
        
        // Check for time conflict with existing schedules
        try {
            Schedule newSchedule = scheduleService.getScheduleById(selected.getScheduleId());
            if (newSchedule != null) {
                for (ScheduleRow existing : chosenSchedules) {
                    Schedule existingSchedule = scheduleService.getScheduleById(existing.getScheduleId());
                    if (existingSchedule != null && hasTimeConflict(newSchedule, existingSchedule)) {
                        FXUtils.showError("Lịch này trùng thời gian với lịch đã chọn:\n" + existing.toString());
                        return;
                    }
                }
            }
            chosenSchedules.add(selected);
        } catch (Exception e) {
            e.printStackTrace();
            FXUtils.showError("Lỗi khi kiểm tra lịch: " + e.getMessage());
        }
    }
    
    /**
     * Check if two schedules have time conflict (same day and overlapping time)
     */
    private boolean hasTimeConflict(Schedule s1, Schedule s2) {
        // Must be same day
        if (s1.getDayOfWeek() != s2.getDayOfWeek()) {
            return false;
        }
        
        // Check time overlap: s1.start < s2.end AND s2.start < s1.end
        return s1.getStartTime().isBefore(s2.getEndTime()) && 
               s2.getStartTime().isBefore(s1.getEndTime());
    }

    @FXML
    private void handleCancel() {
        if(cancelButton != null) closeWindow(cancelButton);
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
            
            // Update schedules: delete all then assign selected
            courseOfferingScheduleService.deleteAllSchedulesOfCourseOffering(offering.getCourseOfferingId());
            
            Date startDate = deriveStartDate();
            Date endDate = deriveEndDate(startDate);
            
            // Assign each selected schedule
            for (ScheduleRow row : chosenSchedules) {
                if (row.getScheduleId() != null) {
                    courseOfferingScheduleService.assignScheduleToCourseOffering(
                        offering.getCourseOfferingId(),
                        row.getScheduleId(),
                        startDate,
                        endDate
                    );
                }
            }
            FXUtils.showSuccess("Cập nhật lớp học phần thành công");
            if(cancelButton != null) closeWindow(cancelButton);
        } catch (Exception ex) {
            System.err.println("Error in handleSave:");
            ex.printStackTrace();
            FXUtils.showError("Lưu thất bại: " + ex.getMessage());
        }
    }

    // Validation
    private void validateForm() {
        StringBuilder sb = new StringBuilder();
        String offeringId = offeringIdField != null ? offeringIdField.getText() : null;
        String courseId = courseComboBox != null ? courseComboBox.getValue() : null;
        String semesterDisplay = semesterComboBox != null ? semesterComboBox.getValue() : null;
        String semesterId = null;
        if (!isBlank(semesterDisplay)) {
            semesterId = semesterDisplay.split(" - ")[0].trim();
        }
        String roomId = roomComboBox != null ? roomComboBox.getValue() : null;
        String maxCapacity = capacityField != null ? capacityField.getText() : null;
        
        if (isBlank(offeringId)) sb.append("- Mã lớp mở trống\n");
        if (isBlank(courseId)) sb.append("- Chưa chọn môn học\n");
        if (isBlank(semesterId)) sb.append("- Chưa chọn học kỳ\n");
        if (isBlank(roomId)) sb.append("- Chưa chọn phòng\n");
        if (isBlank(maxCapacity)) sb.append("- Sĩ số tối đa trống\n");
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
        
        if (sb.length() > 0) throw new IllegalArgumentException(sb.toString().trim());
    }

    private CourseOffering buildCourseOffering() {
        CourseOffering co = new CourseOffering();
        co.setCourseOfferingId(offeringIdField != null ? offeringIdField.getText() : null);
        co.setInstructor(lecturerField != null ? lecturerField.getText() : null);
        co.setMaxCapacity(capacityField != null ? capacityField.getText() : null);
        co.setCurrentCapacity(currentCapacityField != null && !isBlank(currentCapacityField.getText()) 
            ? currentCapacityField.getText() : "0");
        co.setCourseId(courseComboBox != null ? courseComboBox.getValue() : null);
        
        // Extract semesterId from "ID - term/academicYear" format
        if (semesterComboBox != null && semesterComboBox.getValue() != null) {
            String selected = semesterComboBox.getValue();
            String semesterId = selected.split(" - ")[0].trim();
            co.setSemesterId(semesterId);
        }
        
        co.setRoomId(roomComboBox != null ? roomComboBox.getValue() : null);
        
        // Extract facultyId from "ID - Name" format
        if (facultyComboBox != null && facultyComboBox.getValue() != null) {
            String selected = facultyComboBox.getValue();
            String facultyId = selected.split(" - ")[0].trim();
            co.setFacultyId(facultyId);
        }
        
        return co;
    }

    private Date deriveStartDate() {
        try {
            String semesterDisplay = semesterComboBox != null ? semesterComboBox.getValue() : null;
            String semId = null;
            if (!isBlank(semesterDisplay)) {
                semId = semesterDisplay.split(" - ")[0].trim();
            }
            Semester s = (semId != null) ? semesterService.getSemesterById(semId) : null;
            if (s != null && s.getStartDate() != null) {
                return Date.valueOf(s.getStartDate());
            }
        } catch (Exception ignored) {}
        return Date.valueOf(LocalDate.now());
    }

    private Date deriveEndDate(Date start) {
        try {
            String semesterDisplay = semesterComboBox != null ? semesterComboBox.getValue() : null;
            String semId = null;
            if (!isBlank(semesterDisplay)) {
                semId = semesterDisplay.split(" - ")[0].trim();
            }
            Semester s = (semId != null) ? semesterService.getSemesterById(semId) : null;
            if (s != null && s.getEndDate() != null) {
                return Date.valueOf(s.getEndDate());
            }
        } catch (Exception ignored) {}
        LocalDate st = start.toLocalDate();
        return Date.valueOf(st.plusDays(90));
    }


    // Prefill form from existing course offering
    public void prefillFrom(CourseOffering offering) {
        if (offering == null) return;
        
        if (offeringIdField != null) {
            offeringIdField.setText(offering.getCourseOfferingId());
            offeringIdField.setDisable(true); // disable id editing to avoid key change
        }
        if (courseComboBox != null) courseComboBox.setValue(offering.getCourseId());
        if (lecturerField != null) lecturerField.setText(offering.getInstructor());
        
        // Prefill semester with display format
        if (semesterComboBox != null && !isBlank(offering.getSemesterId())) {
            try {
                Semester semester = semesterService.getSemesterById(offering.getSemesterId());
                if (semester != null) {
                    String term = semester.getTerm() != null ? semester.getTerm() : "";
                    String year = semester.getAcademicYear() != null ? semester.getAcademicYear() : "";
                    String display = term + "/" + year;
                    if (display.equals("/")) display = semester.getSemesterId();
                    String displayValue = semester.getSemesterId() + " - " + display;
                    semesterComboBox.setValue(displayValue);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
        if (roomComboBox != null) roomComboBox.setValue(offering.getRoomId());
        if (capacityField != null) capacityField.setText(offering.getMaxCapacity());
        if (currentCapacityField != null) currentCapacityField.setText(offering.getCurrentCapacity());
        
        // Prefill faculty if facultyId exists
        if (facultyComboBox != null && !isBlank(offering.getFacultyId())) {
            try {
                Faculty faculty = facultyService.getFacultyById(offering.getFacultyId());
                if (faculty != null) {
                    String displayValue = faculty.getFacultyId() + " - " + faculty.getFacultyName();
                    facultyComboBox.setValue(displayValue);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
        // Prefill selected schedules
        try {
            chosenSchedules.clear();
            List<Schedule> schedules = courseOfferingScheduleService.getSchedulesByCourseOfferingId(offering.getCourseOfferingId());
            for (Schedule s : schedules) {
                if (s != null) chosenSchedules.add(new ScheduleRow(s));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}