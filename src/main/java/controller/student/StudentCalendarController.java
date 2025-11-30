package main.java.controller.student;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.text.TextAlignment;
import main.java.dto.student.TimetableCell;
import main.java.model.Course;
import main.java.model.CourseOffering;
import main.java.model.Registration;
import main.java.model.Schedule;
import main.java.model.Semester;
import main.java.service.AuthService;
import main.java.service.impl.AuthServiceImpl;
import main.java.service.impl.CourseOfferingScheduleServiceImpl;
import main.java.service.impl.CourseOfferingServiceImpl;
import main.java.service.impl.CourseServiceImpl;
import main.java.service.impl.RegistrationServiceImpl;
import main.java.service.impl.SemesterServiceImpl;
import main.java.utils.FXUtils;

/**
 * Controller for displaying student's weekly timetable view.
 * Shows registered courses in a grid format: 7 days (Mon-Sun) x 17 time slots (7:00-23:00).
 */
public class StudentCalendarController {

    @FXML private ComboBox<String> semesterComboBox;
    @FXML private GridPane timetableGrid;
    @FXML private ScrollPane scrollPane;
    @FXML private Button backButton;

    private final AuthService auth = AuthServiceImpl.getInstance();
    private final SemesterServiceImpl semesterService = new SemesterServiceImpl();
    private final RegistrationServiceImpl registrationService = new RegistrationServiceImpl();
    private final CourseOfferingServiceImpl courseOfferingService = new CourseOfferingServiceImpl();
    private final CourseOfferingScheduleServiceImpl scheduleService = new CourseOfferingScheduleServiceImpl();
    private final CourseServiceImpl courseService = new CourseServiceImpl();

    private final Map<String, String> semesterDisplayMap = new HashMap<>();
    private final Map<String, String> displayToSemesterIdMap = new HashMap<>();

    // Time slots: 7:00 - 23:00 (17 slots)
    private static final int START_HOUR = 7;
    private static final int END_HOUR = 24; // 00:00 next day
    private static final int SLOT_COUNT = END_HOUR - START_HOUR; // 17 slots

    // Days: Monday (2) to Sunday (8)
    private static final String[] DAY_LABELS = {"Thứ 2", "Thứ 3", "Thứ 4", "Thứ 5", "Thứ 6", "Thứ 7", "CN"};
    private static final int[] DAY_VALUES = {2, 3, 4, 5, 6, 7, 8};

    @FXML
    public void initialize() {
        loadSemesters();
        setupTimetableGrid();
        bindActions();

        // Load timetable for first semester if available
        if (semesterComboBox.getItems() != null && !semesterComboBox.getItems().isEmpty()) {
            semesterComboBox.getSelectionModel().selectFirst();
            loadTimetable();
        }
    }

    private void loadSemesters() {
        try {
            List<Semester> semesters = semesterService.getAllSemesters();
            List<String> displayItems = new ArrayList<>();

            for (Semester s : semesters) {
                String display = s.getTerm(); // e.g., "Fall 2025"
                if (display == null || display.isEmpty()) {
                    display = s.getSemesterId();
                }
                semesterDisplayMap.put(s.getSemesterId(), display);
                displayToSemesterIdMap.put(display, s.getSemesterId());
                displayItems.add(display);
            }

            semesterComboBox.setItems(FXCollections.observableArrayList(displayItems));
        } catch (Exception e) {
            e.printStackTrace();
            FXUtils.showError("Không thể tải danh sách học kỳ: " + e.getMessage());
        }
    }

    private void setupTimetableGrid() {
        timetableGrid.getChildren().clear();
        timetableGrid.getRowConstraints().clear();
        timetableGrid.setGridLinesVisible(false);
        timetableGrid.setStyle("-fx-background-color: white;");
        timetableGrid.setHgap(1);
        timetableGrid.setVgap(1);

        // Header row constraint
        RowConstraints headerRowConstraint = new RowConstraints();
        headerRowConstraint.setMinHeight(40);
        headerRowConstraint.setVgrow(Priority.NEVER);
        timetableGrid.getRowConstraints().add(headerRowConstraint);

        // Header row: empty corner + day labels
        Label cornerLabel = createHeaderLabel("Giờ \\ Thứ");
        cornerLabel.setMinWidth(80);
        cornerLabel.setPrefWidth(80);
        cornerLabel.setMaxHeight(Double.MAX_VALUE);
        timetableGrid.add(cornerLabel, 0, 0);

        for (int col = 0; col < DAY_LABELS.length; col++) {
            Label dayLabel = createHeaderLabel(DAY_LABELS[col]);
            dayLabel.setMinWidth(140);
            dayLabel.setPrefWidth(140);
            dayLabel.setMaxHeight(Double.MAX_VALUE);
            timetableGrid.add(dayLabel, col + 1, 0);
        }

        // Time slot rows with auto-grow constraints
        for (int row = 0; row < SLOT_COUNT; row++) {
            // Add row constraint for each time slot
            RowConstraints rowConstraint = new RowConstraints();
            rowConstraint.setMinHeight(60);
            rowConstraint.setVgrow(Priority.ALWAYS);
            timetableGrid.getRowConstraints().add(rowConstraint);
            
            int hour = START_HOUR + row;
            String timeLabel = String.format("%02d:00", hour);
            Label timeLbl = createTimeLabel(timeLabel);
            timetableGrid.add(timeLbl, 0, row + 1);

            // Empty cells for each day
            for (int col = 0; col < DAY_LABELS.length; col++) {
                VBox cell = createEmptyCell();
                timetableGrid.add(cell, col + 1, row + 1);
            }
        }
    }

    private void bindActions() {
        if (semesterComboBox != null) {
            semesterComboBox.setOnAction(e -> loadTimetable());
        }
    }

    @FXML
    private void handleBack() {
        try {
            Stage currentStage = (Stage) backButton.getScene().getWindow();
            currentStage.close();
        } catch (Exception e) {
            e.printStackTrace();
            FXUtils.showError("Không thể đóng: " + e.getMessage());
        }
    }

    @FXML
    private void handleReload() {
        loadTimetable();
    }

    private void loadTimetable() {
        // Reset grid to empty
        setupTimetableGrid();

        String selectedDisplay = semesterComboBox.getValue();
        if (selectedDisplay == null || selectedDisplay.isEmpty()) {
            return;
        }

        String semesterId = displayToSemesterIdMap.get(selectedDisplay);
        if (semesterId == null) {
            return;
        }

        try {
            // Get current student ID
            String studentId = null;
            if (auth.getCurrentUser() != null) {
                studentId = auth.getCurrentUser().getUserId();
            }
            if (studentId == null) {
                FXUtils.showError("Không xác định được sinh viên");
                return;
            }

            // Get all registrations for this student
            List<Registration> registrations = registrationService.getRegistrationsByStudent(studentId);
            Set<String> registeredOfferingIds = registrations.stream()
                    .filter(r -> "Thành công".equals(r.getStatus()))
                    .map(Registration::getCourseOfferingId)
                    .collect(Collectors.toSet());

            // Build timetable data: Map<dayOfWeek, Map<hour, List<TimetableCell>>>
            Map<Integer, Map<Integer, List<TimetableCell>>> timetableData = new HashMap<>();
            for (int day : DAY_VALUES) {
                timetableData.put(day, new HashMap<>());
            }

            // For each registered course offering
            for (String offeringId : registeredOfferingIds) {
                CourseOffering offering = courseOfferingService.getCourseOfferingById(offeringId);
                if (offering == null) continue;

                // Check if this offering belongs to the selected semester
                if (!semesterId.equals(offering.getSemesterId())) continue;

                // Get course name
                String courseName = "";
                if (offering.getCourseId() != null) {
                    Course course = courseService.getCourseById(offering.getCourseId());
                    if (course != null && course.getCourseName() != null) {
                        courseName = course.getCourseName();
                    }
                }

                String instructor = offering.getInstructor() != null ? offering.getInstructor() : "";
                String room = offering.getRoomId() != null ? offering.getRoomId() : "";

                // Get schedules for this offering
                List<Schedule> schedules = scheduleService.getSchedulesByCourseOfferingId(offeringId);
                for (Schedule schedule : schedules) {
                    if (schedule == null) continue;

                    int dayOfWeek = schedule.getDayOfWeek();
                    int startHour = schedule.getStartTime() != null ? schedule.getStartTime().getHour() : -1;
                    int endHour = schedule.getEndTime() != null ? schedule.getEndTime().getHour() : startHour + 1;
                    
                    // Calculate duration (number of hours/rows)
                    int duration = Math.max(1, endHour - startHour);

                    if (startHour >= START_HOUR && startHour < END_HOUR && timetableData.containsKey(dayOfWeek)) {
                        Map<Integer, List<TimetableCell>> dayData = timetableData.get(dayOfWeek);
                        dayData.computeIfAbsent(startHour, k -> new ArrayList<>());
                        dayData.get(startHour).add(new TimetableCell(courseName, instructor, room, offeringId, duration));
                    }
                }
            }

            // Render timetable data to grid
            renderTimetableData(timetableData);

        } catch (Exception e) {
            e.printStackTrace();
            FXUtils.showError("Không thể tải thời khóa biểu: " + e.getMessage());
        }
    }

    private void renderTimetableData(Map<Integer, Map<Integer, List<TimetableCell>>> timetableData) {
        // Track occupied cells: key = "col_row"
        Set<String> occupiedCells = new HashSet<>();
        
        for (int dayIndex = 0; dayIndex < DAY_VALUES.length; dayIndex++) {
            int dayOfWeek = DAY_VALUES[dayIndex];
            int col = dayIndex + 1;

            Map<Integer, List<TimetableCell>> dayData = timetableData.get(dayOfWeek);
            if (dayData == null) continue;

            for (int hour = START_HOUR; hour < END_HOUR; hour++) {
                int row = hour - START_HOUR + 1;
                String cellKey = col + "_" + row;
                
                // Skip if this cell is already occupied by a rowspan from above
                if (occupiedCells.contains(cellKey)) {
                    continue;
                }
                
                List<TimetableCell> cells = dayData.get(hour);

                if (cells != null && !cells.isEmpty()) {
                    // Calculate max rowspan among all cells at this position
                    int maxRowSpan = cells.stream().mapToInt(TimetableCell::getDuration).max().orElse(1);
                    // Ensure rowspan doesn't exceed grid bounds
                    maxRowSpan = Math.min(maxRowSpan, SLOT_COUNT - (hour - START_HOUR));
                    
                    VBox cellBox = createFilledCell(cells, maxRowSpan);
                    
                    // Remove existing empty cells that will be covered by rowspan
                    final int finalCol = col;
                    final int finalRow = row;
                    final int finalRowSpan = maxRowSpan;
                    timetableGrid.getChildren().removeIf(node -> {
                        Integer nodeCol = GridPane.getColumnIndex(node);
                        Integer nodeRow = GridPane.getRowIndex(node);
                        if (nodeCol == null || nodeRow == null) return false;
                        return nodeCol == finalCol && nodeRow >= finalRow && nodeRow < finalRow + finalRowSpan;
                    });
                    
                    // Add the cell with rowspan
                    timetableGrid.add(cellBox, col, row);
                    GridPane.setRowSpan(cellBox, maxRowSpan);
                    
                    // Mark cells as occupied
                    for (int r = row; r < row + maxRowSpan; r++) {
                        occupiedCells.add(col + "_" + r);
                    }
                }
            }
        }
    }

    private Label createHeaderLabel(String text) {
        Label label = new Label(text);
        label.setStyle("-fx-background-color: #a3151a; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 13px;");
        label.setAlignment(Pos.CENTER);
        label.setMaxWidth(Double.MAX_VALUE);
        label.setMinHeight(40);
        label.setPrefHeight(40);
        label.setPadding(new Insets(5));
        return label;
    }

    private Label createTimeLabel(String text) {
        Label label = new Label(text);
        label.setStyle("-fx-background-color: #f0f0f0; -fx-font-weight: bold; -fx-font-size: 12px;");
        label.setAlignment(Pos.CENTER);
        label.setMinWidth(80);
        label.setPrefWidth(80);
        label.setMinHeight(60);
        label.setMaxHeight(Double.MAX_VALUE);
        label.setPadding(new Insets(5));
        return label;
    }

    private VBox createEmptyCell() {
        VBox cell = new VBox();
        cell.setStyle("-fx-background-color: #fafafa; -fx-border-color: #e0e0e0; -fx-border-width: 0.5;");
        cell.setMinWidth(140);
        cell.setPrefWidth(140);
        cell.setMinHeight(60);
        cell.setMaxHeight(Double.MAX_VALUE);
        cell.setAlignment(Pos.CENTER);
        return cell;
    }

    private VBox createFilledCell(List<TimetableCell> cells, int rowSpan) {
        VBox cellBox = new VBox(2);
        
        // If 2+ courses overlap, show red background (conflict)
        boolean hasConflict = cells.size() >= 2;
        if (hasConflict) {
            cellBox.setStyle("-fx-background-color: #ffcdd2; -fx-border-color: #f44336; -fx-border-width: 1;");
        } else {
            cellBox.setStyle("-fx-background-color: #e3f2fd; -fx-border-color: #2196f3; -fx-border-width: 1;");
        }
        
        cellBox.setMinWidth(140);
        cellBox.setPrefWidth(140);
        // Let GridPane row constraints handle height, just set min height
        cellBox.setMinHeight(60 * rowSpan);
        cellBox.setMaxHeight(Double.MAX_VALUE);
        cellBox.setAlignment(Pos.CENTER);
        cellBox.setPadding(new Insets(5));
        // Allow VBox to grow
        VBox.setVgrow(cellBox, Priority.ALWAYS);

        for (TimetableCell cell : cells) {
            Text text = new Text(cell.toDisplayString());
            text.setStyle("-fx-font-size: 10px;");
            text.setTextAlignment(TextAlignment.CENTER);
            text.setWrappingWidth(130);
            cellBox.getChildren().add(text);

            // Add separator if multiple courses
            if (cells.size() > 1 && cells.indexOf(cell) < cells.size() - 1) {
                Label separator = new Label("───");
                separator.setStyle(hasConflict ? "-fx-text-fill: #ef9a9a; -fx-font-size: 8px;" : "-fx-text-fill: #90caf9; -fx-font-size: 8px;");
                cellBox.getChildren().add(separator);
            }
        }

        return cellBox;
    }
}
