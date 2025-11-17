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
import main.java.dto.admin.courseOffering.ScheduleRow;
import main.java.model.Course;
import main.java.model.Semester;
import main.java.model.Major;
import main.java.utils.FXUtils;
import java.time.LocalDate;
import java.time.LocalTime;
import java.sql.Date;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class EditCourseOfferingFormController {
    @FXML private Button cancelButton;
    @FXML private Button saveButton;
    @FXML private TextField offeringIdField;
    @FXML private TextField lecturerField;
    @FXML private ComboBox<String> courseComboBox;
    @FXML private ComboBox<String> lecturerComboBox;
    @FXML private ComboBox<String> semesterComboBox;
    @FXML private ComboBox<String> roomComboBox;
    @FXML private ComboBox<String> majorComboBox;
    @FXML private TextField capacityField;
    @FXML private TextField maxCapacityField;
    @FXML private TextField currentCapacityField;
    @FXML private ListView<ScheduleRow> availableSchedulesList;
    @FXML private ListView<ScheduleRow> selectedSchedulesList;
    @FXML private Button scheduleAddButton;
    @FXML private Button scheduleRemoveButton;
    @FXML private Button useSuggestedScheduleButton;
    @FXML private ComboBox<String> scheduleDayComboBox;
    @FXML private ComboBox<String> scheduleStartTimeComboBox;
    @FXML private ComboBox<String> scheduleEndTimeComboBox;

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
        setupScheduleInputs();
    }
    
    private void setupScheduleInputs() {
        // Days: T2 to CN
        if (scheduleDayComboBox != null) {
            scheduleDayComboBox.setItems(FXCollections.observableArrayList(
                "Thứ 2", "Thứ 3", "Thứ 4", "Thứ 5", "Thứ 6", "Thứ 7", "Chủ nhật"
            ));
        }
        
        // Times: 07:00 to 21:00 in 30-min increments
        List<String> times = IntStream.rangeClosed(7, 21)
            .boxed()
            .flatMap(h -> java.util.stream.Stream.of(
                String.format("%02d:00", h),
                String.format("%02d:30", h)
            ))
            .collect(Collectors.toList());
        
        if (scheduleStartTimeComboBox != null) {
            scheduleStartTimeComboBox.setItems(FXCollections.observableArrayList(times));
        }
        if (scheduleEndTimeComboBox != null) {
            scheduleEndTimeComboBox.setItems(FXCollections.observableArrayList(times));
        }
        
        // Schedule list binding
        if (availableSchedulesList != null) availableSchedulesList.setItems(availableSchedules);
        if (selectedSchedulesList != null) selectedSchedulesList.setItems(chosenSchedules);
        if (scheduleAddButton != null) scheduleAddButton.setOnAction(e -> handleAddSchedule());
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
                    semesters.stream().map(Semester::getSemesterId).toList()
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

        // Majors
        try {
            List<Major> majors = majorService.getAllMajors();
            if (majorComboBox != null) {
                majorComboBox.setItems(FXCollections.observableArrayList(
                    majors.stream().map(m -> m.getMajorId() + " - " + m.getMajorName()).toList()
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
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
                // Mẫu
                availableSchedules.addAll(
                    new ScheduleRow("T2","07:30-09:30"),
                    new ScheduleRow("T3","09:45-11:45"),
                    new ScheduleRow("T5","13:00-15:00")
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Mẫu
            availableSchedules.addAll(
                new ScheduleRow("T2","07:30-09:30"),
                new ScheduleRow("T3","09:45-11:45")
            );
        }
    }

    @FXML
    private void handleAddSchedule() {
        // Manual add from day/time combos
        if (scheduleDayComboBox != null && scheduleStartTimeComboBox != null && scheduleEndTimeComboBox != null) {
            String day = scheduleDayComboBox.getValue();
            String startStr = scheduleStartTimeComboBox.getValue();
            String endStr = scheduleEndTimeComboBox.getValue();
            
            if (day == null || startStr == null || endStr == null) {
                FXUtils.showError("Vui lòng chọn đầy đủ Ngày, Giờ bắt đầu và Giờ kết thúc");
                return;
            }
            
            // Validate time range
            try {
                LocalTime start = LocalTime.parse(startStr);
                LocalTime end = LocalTime.parse(endStr);
                
                if (!start.isBefore(end)) {
                    FXUtils.showError("Giờ bắt đầu phải trước giờ kết thúc");
                    return;
                }
                
                // Validate time range: 07:00 - 21:00
                if (start.isBefore(LocalTime.of(7, 0)) || end.isAfter(LocalTime.of(21, 0))) {
                    FXUtils.showError("Lịch học phải trong khung giờ 07:00 - 21:00");
                    return;
                }
                
                ScheduleRow manual = new ScheduleRow(day, startStr + "-" + endStr);
                if (!chosenSchedules.contains(manual)) {
                    chosenSchedules.add(manual);
                }
                return;
            } catch (Exception e) {
                FXUtils.showError("Định dạng giờ không hợp lệ");
                return;
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
            try {
                courseOfferingScheduleService.deleteAllSchedulesOfCourseOffering(offering.getCourseOfferingId());
                System.out.println("Deleted all schedules for: " + offering.getCourseOfferingId());
            } catch (Exception e) {
                System.err.println("Error deleting schedules:");
                e.printStackTrace();
            }
            
            Date startDate = deriveStartDate();
            Date endDate = deriveEndDate(startDate);
            
            int savedCount = 0;
            for (ScheduleRow row : chosenSchedules) {
                if (row.getScheduleId() != null) {
                    if (row.getScheduleId().startsWith("MANUAL_")) {
                        // Create new schedule for MANUAL entries
                        try {
                            Schedule newSchedule = createScheduleFromManual(row);
                            Schedule created = scheduleService.createSchedule(newSchedule);
                            if (created != null) {
                                boolean assigned = courseOfferingScheduleService.assignScheduleToCourseOffering(
                                    offering.getCourseOfferingId(),
                                    created.getScheduleId(),
                                    startDate,
                                    endDate
                                );
                                if (assigned) {
                                    savedCount++;
                                    System.out.println("Created and assigned MANUAL schedule: " + created.getScheduleId());
                                } else {
                                    System.err.println("Failed to assign MANUAL schedule: " + created.getScheduleId());
                                }
                            } else {
                                System.err.println("Failed to create MANUAL schedule");
                            }
                        } catch (Exception e) {
                            System.err.println("Error creating MANUAL schedule " + row.getScheduleId() + ":");
                            e.printStackTrace();
                        }
                    } else {
                        // Assign existing schedule
                        try {
                            boolean assigned = courseOfferingScheduleService.assignScheduleToCourseOffering(
                                offering.getCourseOfferingId(),
                                row.getScheduleId(),
                                startDate,
                                endDate
                            );
                            if (assigned) {
                                savedCount++;
                                System.out.println("Assigned schedule: " + row.getScheduleId() + " to " + offering.getCourseOfferingId());
                            } else {
                                System.err.println("Failed to assign schedule: " + row.getScheduleId());
                            }
                        } catch (Exception e) {
                            System.err.println("Error assigning schedule " + row.getScheduleId() + ":");
                            e.printStackTrace();
                        }
                    }
                }
            }
            
            System.out.println("Total schedules saved: " + savedCount + " out of " + chosenSchedules.size());
            FXUtils.showSuccess("Cập nhật lớp học phần thành công");
            closeWindow();
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
        String semesterId = semesterComboBox != null ? semesterComboBox.getValue() : null;
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
        co.setSemesterId(semesterComboBox != null ? semesterComboBox.getValue() : null);
        co.setRoomId(roomComboBox != null ? roomComboBox.getValue() : null);
        
        // Extract majorId from "ID - Name" format
        if (majorComboBox != null && majorComboBox.getValue() != null) {
            String selected = majorComboBox.getValue();
            String majorId = selected.split(" - ")[0].trim();
            co.setMajorId(majorId);
        }
        
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
    
    private Schedule createScheduleFromManual(ScheduleRow row) {
        String display = row.toString();
        String[] parts = display.split(" ");
        
        String dayStr = parts[0] + " " + parts[1]; // "Thứ 2", "Thứ 3", etc
        int dayOfWeek = mapDayToInt(dayStr);
        
        String timeRange = parts[2]; // "HH:mm-HH:mm"
        String[] times = timeRange.split("-");
        LocalTime startTime = LocalTime.parse(times[0]);
        LocalTime endTime = LocalTime.parse(times[1]);
        
        // Generate unique schedule ID by checking existing schedules
        String scheduleId = generateUniqueScheduleId(dayOfWeek, startTime, endTime);
        
        Schedule schedule = new Schedule();
        schedule.setScheduleId(scheduleId);
        schedule.setDayOfWeek(dayOfWeek);
        schedule.setStartTime(startTime);
        schedule.setEndTime(endTime);
        
        return schedule;
    }
    
    private String generateUniqueScheduleId(int dayOfWeek, LocalTime startTime, LocalTime endTime) {
        try {
            // Check if schedule already exists with same day/time
            List<Schedule> allSchedules = scheduleService.getAllSchedules();
            for (Schedule s : allSchedules) {
                if (s.getDayOfWeek() == dayOfWeek 
                    && s.getStartTime().equals(startTime) 
                    && s.getEndTime().equals(endTime)) {
                    // Schedule already exists, return its ID
                    System.out.println("Found existing schedule: " + s.getScheduleId());
                    return s.getScheduleId();
                }
            }
            
            // Generate new ID in format SCH### (find max number and increment)
            int maxNum = 0;
            for (Schedule s : allSchedules) {
                if (s.getScheduleId() != null && s.getScheduleId().startsWith("SCH")) {
                    try {
                        String numStr = s.getScheduleId().substring(3);
                        int num = Integer.parseInt(numStr);
                        if (num > maxNum) maxNum = num;
                    } catch (Exception ignored) {}
                }
            }
            
            String newId = String.format("SCH%03d", maxNum + 1);
            System.out.println("Generated new schedule ID: " + newId);
            return newId;
        } catch (Exception e) {
            e.printStackTrace();
            // Fallback: use timestamp-based ID
            return "SCH_" + System.currentTimeMillis();
        }
    }
    
    private int mapDayToInt(String day) {
        switch (day) {
            case "Thứ 2": return 2;
            case "Thứ 3": return 3;
            case "Thứ 4": return 4;
            case "Thứ 5": return 5;
            case "Thứ 6": return 6;
            case "Thứ 7": return 7;
            case "Chủ nhật": return 8;
            default: return 2; // Default to Monday
        }
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
        
        if (offeringIdField != null) {
            offeringIdField.setText(offering.getCourseOfferingId());
            offeringIdField.setDisable(true); // disable id editing to avoid key change
        }
        if (courseComboBox != null) courseComboBox.setValue(offering.getCourseId());
        if (lecturerField != null) lecturerField.setText(offering.getInstructor());
        if (semesterComboBox != null) semesterComboBox.setValue(offering.getSemesterId());
        if (roomComboBox != null) roomComboBox.setValue(offering.getRoomId());
        if (capacityField != null) capacityField.setText(offering.getMaxCapacity());
        if (currentCapacityField != null) currentCapacityField.setText(offering.getCurrentCapacity());
        
        // Prefill major if majorId exists
        if (majorComboBox != null && !isBlank(offering.getMajorId())) {
            try {
                Major major = majorService.getMajorById(offering.getMajorId());
                if (major != null) {
                    String displayValue = major.getMajorId() + " - " + major.getMajorName();
                    majorComboBox.setValue(displayValue);
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