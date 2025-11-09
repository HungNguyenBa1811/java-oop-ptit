package main.java.repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import main.java.config.DatabaseConnection;
import main.java.model.Student;

/**
 * StudentRepository - Quản lý CRUD cho bảng students (Sinh viên)
 * Lưu ý: Student kế thừa từ User, vì vậy cần thao tác cả 2 bảng users và students
 */
public class StudentRepository {
    
    private static final String INSERT_STUDENT =
        "INSERT INTO students (student_id, class, major_id, status) " +
        "VALUES (?, ?, ?, ?)";

    private static final String SELECT_ALL_STUDENTS =
        "SELECT s.*, u.user_id AS user_id, u.username, u.full_name, u.email, u.role " +
        "FROM students s " +
        "JOIN users u ON s.student_id = u.user_id " +
        "ORDER BY s.student_id";
    
    private static final String SELECT_STUDENT_BY_ID =
        "SELECT s.*, u.user_id AS user_id, u.username, u.full_name, u.email, u.role " +
        "FROM students s " +
        "JOIN users u ON s.student_id = u.user_id " +
        "WHERE s.student_id = ?";
    
    private static final String SELECT_STUDENT_BY_USER_ID =
        "SELECT s.*, u.user_id AS user_id, u.username, u.full_name, u.email, u.role " +
        "FROM students s " +
        "JOIN users u ON s.student_id = u.user_id " +
        "WHERE s.student_id = ?";
    
    private static final String SELECT_STUDENTS_BY_MAJOR =
        "SELECT s.*, u.user_id AS user_id, u.username, u.full_name, u.email, u.role " +
        "FROM students s " +
        "JOIN users u ON s.student_id = u.user_id " +
        "WHERE s.major_id = ? " +
        "ORDER BY s.student_id";
    
    private static final String SELECT_STUDENTS_BY_CLASS =
        "SELECT s.*, u.user_id AS user_id, u.username, u.full_name, u.email, u.role " +
        "FROM students s " +
        "JOIN users u ON s.student_id = u.user_id " +
        "WHERE s.class = ? " +
        "ORDER BY s.student_id";
    
    private static final String UPDATE_STUDENT =
        "UPDATE students SET class = ?, major_id = ?, status = ? WHERE student_id = ?";
    
    private static final String DELETE_STUDENT =
        "DELETE FROM students WHERE student_id = ?";

    /**
     * Tạo sinh viên mới
     * @param student Student object cần tạo
     * @return true nếu tạo thành công
     */
    public boolean createStudent(Student student) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(INSERT_STUDENT)) {

            stmt.setString(1, student.getStudentId());
            stmt.setString(2, student.getStudentClass());
            stmt.setString(3, student.getMajorId());
            stmt.setString(4, student.getStatus());

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                System.out.println("Student created: " + student.getStudentId());
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi tạo student: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Lấy tất cả sinh viên
     * @return List danh sách students
     */
    public List<Student> findAll() {
        List<Student> students = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_ALL_STUDENTS);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                students.add(mapResultSetToStudent(rs));
            }
            System.out.println("Found " + students.size() + " students");
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy danh sách student: " + e.getMessage());
            e.printStackTrace();
        }
        return students;
    }

    /**
     * Tìm sinh viên theo student ID
     * @param studentId Student ID cần tìm
     * @return Student nếu tìm thấy, null nếu không
     */
    public Student findById(String studentId) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_STUDENT_BY_ID)) {
            
            stmt.setString(1, studentId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                System.out.println("Student found: " + studentId);
                return mapResultSetToStudent(rs);
            } else {
                System.out.println("Student not found: " + studentId);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi tìm student by ID: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Tìm sinh viên theo user ID
     * @param userId User ID
     * @return Student nếu tìm thấy, null nếu không
     */
    public Student findByUserId(String userId) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_STUDENT_BY_USER_ID)) {
            
            stmt.setString(1, userId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                System.out.println("Student found by user ID: " + userId);
                return mapResultSetToStudent(rs);
            } else {
                System.out.println("Student not found with user ID: " + userId);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi tìm student by user ID: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Tìm sinh viên theo ngành
     * @param majorId Major ID
     * @return List danh sách students thuộc ngành đó
     */
    public List<Student> findByMajor(String majorId) {
        List<Student> students = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_STUDENTS_BY_MAJOR)) {
            
            stmt.setString(1, majorId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                students.add(mapResultSetToStudent(rs));
            }
            System.out.println("Found " + students.size() + " students in major: " + majorId);
        } catch (SQLException e) {
            System.err.println("Lỗi khi tìm student by major: " + e.getMessage());
            e.printStackTrace();
        }
        return students;
    }

    /**
     * Tìm sinh viên theo lớp
     * @param studentClass Lớp học
     * @return List danh sách students trong lớp đó
     */
    public List<Student> findByClass(String studentClass) {
        List<Student> students = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_STUDENTS_BY_CLASS)) {
            
            stmt.setString(1, studentClass);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                students.add(mapResultSetToStudent(rs));
            }
            System.out.println("Found " + students.size() + " students in class: " + studentClass);
        } catch (SQLException e) {
            System.err.println("Lỗi khi tìm student by class: " + e.getMessage());
            e.printStackTrace();
        }
        return students;
    }

    /**
     * Cập nhật thông tin sinh viên
     * @param student Student object với thông tin mới
     * @return true nếu update thành công
     */
    public boolean updateStudent(Student student) {
        try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(UPDATE_STUDENT)) {
            
            stmt.setString(1, student.getStudentClass());
            stmt.setString(2, student.getMajorId());
            stmt.setString(3, student.getStatus());
            stmt.setString(4, student.getStudentId());
            
            int rows = stmt.executeUpdate();
            if (rows > 0) {
                System.out.println("Student updated: " + student.getStudentId());
                return true;
            } else {
                System.out.println("No student found to update: " + student.getStudentId());
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi update student: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Xóa sinh viên
     * @param studentId ID của sinh viên cần xóa
     * @return true nếu xóa thành công
     */
    public boolean deleteStudent(String studentId) {
        try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(DELETE_STUDENT)) {
            
            stmt.setString(1, studentId);
            int rows = stmt.executeUpdate();
            
            if (rows > 0) {
                System.out.println("Student deleted: " + studentId);
                return true;
            } else {
                System.out.println("No student found to delete: " + studentId);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi xóa student: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Kiểm tra student ID đã tồn tại chưa
     * @param studentId Student ID cần kiểm tra
     * @return true nếu đã tồn tại
     */
    public boolean existsById(String studentId) {
        Student student = findById(studentId);
        return student != null;
    }

    /**
     * Map ResultSet thành Student object
     * JOIN từ bảng students và users
     */
    private Student mapResultSetToStudent(ResultSet rs) throws SQLException {
        Student student = new Student();
        
        // Thông tin từ bảng students
        student.setStudentId(rs.getString("student_id"));
        student.setStudentClass(rs.getString("class"));
        student.setMajorId(rs.getString("major_id"));
        student.setStatus(rs.getString("status"));
        
        // Thông tin từ bảng users (kế thừa)
        student.setUserId(rs.getString("user_id"));
        student.setUsername(rs.getString("username"));
        student.setFullName(rs.getString("full_name"));
        student.setEmail(rs.getString("email"));
        student.setRole(rs.getInt("role"));
        
        return student;
    }
}
