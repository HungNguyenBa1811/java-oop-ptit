package main.java.utils.helper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

import javafx.event.ActionEvent;
import javafx.scene.control.TableView;
import main.java.model.Student;
import main.java.service.impl.AdminServiceImpl;
import main.java.service.impl.StudentServiceImpl;
import main.java.utils.CsvUtils;
import main.java.utils.FXUtils;

public class AdminControllerHelper {
    /**
     * Generic helper to require table selection, fetch full entity, and execute callback.
     * 
     * @param <T> DTO type
     * @param <E> Entity type
     * @param <ID> ID type
     */
    public static <T, E, ID> void requireSelectedAndFetch(
        TableView<T> table,
        String noSelectionMessage,
        String invalidRowMessage,
        String notFoundMessage,
        Function<T, ID> idExtractor,
        Function<ID, E> fetcher,
        Consumer<E> onSuccess
    ) {
        // Check selection
        T selected = table != null ? table.getSelectionModel().getSelectedItem() : null;
        if (selected == null) {
            FXUtils.showError(noSelectionMessage);
            return;
        }
        
        // Extract ID
        ID id = idExtractor.apply(selected);
        if (id == null || (id instanceof String && ("-".equals(id) || ((String)id).trim().isEmpty()))) {
            FXUtils.showError(invalidRowMessage);
            return;
        }
        
        // Fetch entity
        E entity = fetcher.apply(id);
        if (entity == null) {
            FXUtils.showError(notFoundMessage);
            return;
        }
        
        // Execute callback
        onSuccess.accept(entity);
    }

    /**
     * Import students từ CSV file.
     * CSV format: userId;username;fullName;email;studentClass;majorId;facultyId;status;password
     */
    public static void importStudentsFromCsv(ActionEvent event, AdminServiceImpl adminService, Runnable onComplete) {
        try {
            File f = CsvUtils.chooseOpenCsv(event.getSource(), "Chọn file CSV sinh viên (.csv)");
            if (f == null) return;

            List<String> lines = CsvUtils.readLines(f);
            if (lines.isEmpty()) {
                FXUtils.showError("File rỗng");
                return;
            }

            int success = 0;
            List<String> errors = new ArrayList<>();
            int rowNo = 0;
            boolean firstIsHeader = lines.get(0).toLowerCase().contains("userid") 
                                 || lines.get(0).toLowerCase().contains("username");
            
            for (String line : lines) {
                rowNo++;
                if (firstIsHeader && rowNo == 1) continue;
                if (line == null || line.trim().isEmpty()) continue;

                String[] cols = line.split(";", -1);
                if (cols.length < 9) {
                    errors.add("Dòng " + rowNo + ": Số cột không hợp lệ (cần 9). Detected:" + cols.length);
                    continue;
                }

                String validationError = validateStudentCsvRow(cols, rowNo);
                if (validationError != null) {
                    errors.add(validationError);
                    continue;
                }

                Student s = mapCsvRowToStudent(cols);
                String password = cols[8].trim();
                String facultyId = cols[6].trim();

                try {
                    var created = adminService.registerStudent(s, password, facultyId);
                    if (created != null) success++;
                    else errors.add("Dòng " + rowNo + ": Đăng ký thất bại.");
                } catch (Exception ex) {
                    errors.add("Dòng " + rowNo + ": Lỗi khi tạo - " + ex.getMessage());
                }
            }

            FXUtils.showSuccess("Import hoàn tất. Thành công: " + success + ", Lỗi: " + errors.size());
            if (!errors.isEmpty()) FXUtils.showError(String.join("\n", errors));
            if (onComplete != null) onComplete.run();
        } catch (Exception ex) {
            FXUtils.showError("Import thất bại: " + ex.getMessage());
        }
    }

    /**
     * Export students sang CSV file.
     */
    public static void exportStudentsToCsv(ActionEvent event, StudentServiceImpl studentService) {
        try {
            File target = CsvUtils.chooseSaveCsv(event.getSource(), "students_export.csv", "Lưu file CSV xuất sinh viên");
            if (target == null) return;

            List<Student> students = studentService.getAllStudents();
            String[] header = {"userId", "username", "fullName", "email", "studentClass", "majorId", "facultyId", "status", "password"};
            List<String[]> rows = new ArrayList<>();
            
            for (Student s : students) {
                rows.add(new String[] {
                    s.getStudentId(), 
                    s.getUsername(), 
                    s.getFullName(), 
                    s.getEmail(), 
                    s.getStudentClass(), 
                    s.getMajorId(), 
                    s.getFacultyId(), 
                    s.getStatus(), 
                    "0192023a7bbd73250516f069df18b500" // Placeholder hash MD5 cho "123456"
                });
            }
            
            CsvUtils.writeCsv(target, header, rows);
            FXUtils.showSuccess("Xuất CSV thành công: " + target.getAbsolutePath());
        } catch (Exception ex) {
            FXUtils.showError("Export thất bại: " + ex.getMessage());
        }
    }

    /**
     * Validate a CSV row for student import.
     * @return error message if invalid, null if valid
     */
    private static String validateStudentCsvRow(String[] cols, int rowNo) {
        StringBuilder sb = new StringBuilder();
        
        if (cols[0].trim().isEmpty()) sb.append("userId trống; ");
        if (cols[1].trim().isEmpty()) sb.append("username trống; ");
        if (cols[2].trim().isEmpty()) sb.append("fullName trống; ");
        if (cols[3].trim().isEmpty()) sb.append("email trống; ");
        if (cols[4].trim().isEmpty()) sb.append("class trống; ");
        if (cols[5].trim().isEmpty()) sb.append("major trống; ");
        if (cols[6].trim().isEmpty()) sb.append("faculty trống; ");
        if (cols[7].trim().isEmpty()) sb.append("status trống; ");
        if (cols[8].trim().isEmpty()) sb.append("password trống; ");

        return sb.length() > 0 ? "Dòng " + rowNo + ": " + sb.toString() : null;
    }

    /**
     * Map CSV columns to Student object.
     */
    private static Student mapCsvRowToStudent(String[] cols) {
        Student s = new Student();
        s.setStudentId(cols[0].trim());
        s.setUsername(cols[1].trim());
        s.setFullName(cols[2].trim());
        s.setEmail(cols[3].trim());
        s.setRole(0);
        s.setStudentClass(cols[4].trim());
        s.setMajorId(cols[5].trim());
        s.setFacultyId(cols[6].trim());
        s.setStatus(cols[7].trim());
        return s;
    }
}
