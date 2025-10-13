package main.java.repository;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import main.java.config.DatabaseConnection;
import main.java.model.Faculty;

/**
 * FacultyRepository - Quản lý CRUD cho bảng faculties (Khoa)
 */
public class FacultyRepository {
    
    private static final String INSERT_FACULTY =
        "INSERT INTO faculties (faculty_id, faculty_name, phone_number, email, website, dean, description, created_at) " +
        "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

    private static final String SELECT_ALL_FACULTIES =
        "SELECT * FROM faculties ORDER BY faculty_name";
    
    private static final String SELECT_FACULTY_BY_ID =
        "SELECT * FROM faculties WHERE faculty_id = ?";
    
    private static final String UPDATE_FACULTY =
        "UPDATE faculties SET faculty_name = ?, phone_number = ?, email = ?, website = ?, dean = ?, description = ? WHERE faculty_id = ?";
    
    private static final String DELETE_FACULTY =
        "DELETE FROM faculties WHERE faculty_id = ?";
    
    /**
     * Tạo khoa mới
     * @param faculty Faculty object cần tạo
     * @return true nếu tạo thành công
     */
    public boolean createFaculty(Faculty faculty) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(INSERT_FACULTY)) {

            stmt.setString(1, faculty.getFacultyId());
            stmt.setString(2, faculty.getFacultyName());
            stmt.setString(3, faculty.getPhoneNumber());
            stmt.setString(4, faculty.getEmail());
            stmt.setString(5, faculty.getWebsite());
            stmt.setString(6, faculty.getDean());
            stmt.setString(7, faculty.getDescription());
            stmt.setTimestamp(8, Timestamp.valueOf(LocalDateTime.now()));

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                System.out.println("Faculty created: " + faculty.getFacultyName());
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi tạo faculty: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Lấy tất cả khoa
     * @return List danh sách faculties
     */
    public List<Faculty> findAll() {
        List<Faculty> faculties = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_ALL_FACULTIES);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                faculties.add(mapResultSetToFaculty(rs));
            }
            System.out.println("Found " + faculties.size() + " faculties");
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy danh sách faculty: " + e.getMessage());
            e.printStackTrace();
        }
        return faculties;
    }

    /**
     * Tìm khoa theo ID
     * @param facultyId Faculty ID cần tìm
     * @return Faculty nếu tìm thấy, null nếu không
     */
    public Faculty findById(String facultyId) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_FACULTY_BY_ID)) {
            
            stmt.setString(1, facultyId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                System.out.println("Faculty found: " + facultyId);
                return mapResultSetToFaculty(rs);
            } else {
                System.out.println("Faculty not found: " + facultyId);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi tìm faculty by ID: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Cập nhật thông tin khoa
     * @param faculty Faculty object với thông tin mới
     * @return true nếu update thành công
     */
    public boolean updateFaculty(Faculty faculty) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(UPDATE_FACULTY)) {
            
            stmt.setString(1, faculty.getFacultyName());
            stmt.setString(2, faculty.getPhoneNumber());
            stmt.setString(3, faculty.getEmail());
            stmt.setString(4, faculty.getWebsite());
            stmt.setString(5, faculty.getDean());
            stmt.setString(6, faculty.getDescription());
            stmt.setString(7, faculty.getFacultyId());
            
            int rows = stmt.executeUpdate();
            if (rows > 0) {
                System.out.println("Faculty updated: " + faculty.getFacultyName());
                return true;
            } else {
                System.out.println("No faculty found to update: " + faculty.getFacultyId());
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi update faculty: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Xóa khoa
     * @param facultyId ID của khoa cần xóa
     * @return true nếu xóa thành công
     */
    public boolean deleteFaculty(String facultyId) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(DELETE_FACULTY)) {
            
            stmt.setString(1, facultyId);
            int rows = stmt.executeUpdate();
            
            if (rows > 0) {
                System.out.println("Faculty deleted: " + facultyId);
                return true;
            } else {
                System.out.println("No faculty found to delete: " + facultyId);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi xóa faculty: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Kiểm tra faculty ID đã tồn tại chưa
     * @param facultyId Faculty ID cần kiểm tra
     * @return true nếu đã tồn tại
     */
    public boolean existsById(String facultyId) {
        Faculty faculty = findById(facultyId);
        return faculty != null;
    }

    /**
     * Map ResultSet thành Faculty object
     */
    private Faculty mapResultSetToFaculty(ResultSet rs) throws SQLException {
        Faculty faculty = new Faculty();
        faculty.setFacultyId(rs.getString("faculty_id"));
        faculty.setFacultyName(rs.getString("faculty_name"));
        faculty.setPhoneNumber(rs.getString("phone_number"));
        faculty.setEmail(rs.getString("email"));
        faculty.setWebsite(rs.getString("website"));
        faculty.setDean(rs.getString("dean"));
        faculty.setDescription(rs.getString("description"));
        return faculty;
    }
}
