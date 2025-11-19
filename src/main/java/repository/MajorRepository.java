package main.java.repository;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import main.java.config.DatabaseConnection;
import main.java.model.Major;

/**
 * MajorRepository - Quản lý CRUD cho bảng majors (Ngành học)
 */
public class MajorRepository {
    
    private static final String INSERT_MAJOR =
        "INSERT INTO majors (major_id, major_name, faculty_id, degree_level, duration_years, description, created_at) " +
        "VALUES (?, ?, ?, ?, ?, ?, ?)";

    private static final String SELECT_ALL_MAJORS =
        "SELECT * FROM majors ORDER BY major_name";
    
    private static final String SELECT_MAJOR_BY_ID =
        "SELECT * FROM majors WHERE major_id = ?";
    
    private static final String SELECT_MAJORS_BY_FACULTY =
        "SELECT * FROM majors WHERE faculty_id = ? ORDER BY major_name";
    
    private static final String UPDATE_MAJOR =
        "UPDATE majors SET major_name = ?, faculty_id = ?, degree_level = ?, duration_years = ?, description = ? WHERE major_id = ?";
    
    private static final String DELETE_MAJOR =
        "DELETE FROM majors WHERE major_id = ?";
    
    // private static final String COUNT_MAJORS =
    //     "SELECT COUNT(*) FROM majors";

    /**
     * Tạo ngành mới
     * @param major Major object cần tạo
     * @return true nếu tạo thành công
     */
    public boolean createMajor(Major major) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(INSERT_MAJOR)) {

            stmt.setString(1, major.getMajorId());
            stmt.setString(2, major.getMajorName());
            stmt.setString(3, major.getFacultyId());
            stmt.setString(4, major.getDegreeLevel());
            stmt.setInt(5, major.getDurationYears());
            stmt.setString(6, major.getDescription());
            stmt.setTimestamp(7, Timestamp.valueOf(LocalDateTime.now()));

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                System.out.println("Đã tạo major: " + major.getMajorName());
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi tạo major: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Lấy tất cả ngành
     * @return List danh sách majors
     */
    public List<Major> findAll() {
        List<Major> majors = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_ALL_MAJORS);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                majors.add(mapResultSetToMajor(rs));
            }
            System.out.println("Đã tìm thấy " + majors.size() + " major");
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy danh sách major: " + e.getMessage());
            e.printStackTrace();
        }
        return majors;
    }

    /**
     * Tìm ngành theo ID
     * @param majorId Major ID cần tìm
     * @return Major nếu tìm thấy, null nếu không
     */
    public Major findById(String majorId) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_MAJOR_BY_ID)) {
            
            stmt.setString(1, majorId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                System.out.println("Tìm thấy major: " + majorId);
                return mapResultSetToMajor(rs);
            } else {
                System.out.println(" Không tìm thấy major: " + majorId);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi tìm major bằng ID: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Tìm các ngành theo Faculty
     * @param facultyId Faculty ID
     * @return List danh sách majors thuộc faculty đó
     */
    public List<Major> findByFaculty(String facultyId) {
        List<Major> majors = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_MAJORS_BY_FACULTY)) {
            
            stmt.setString(1, facultyId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                majors.add(mapResultSetToMajor(rs));
            }
            System.out.println("Đã tìm thấy " + majors.size() + " major trong faculty: " + facultyId);
        } catch (SQLException e) {
            System.err.println("Lỗi khi tìm major trong faculty: " + e.getMessage());
            e.printStackTrace();
        }
        return majors;
    }

    /**
     * Cập nhật thông tin ngành
     * @param major Major object với thông tin mới
     * @return true nếu update thành công
     */
    public boolean updateMajor(Major major) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(UPDATE_MAJOR)) {
            
            stmt.setString(1, major.getMajorName());
            stmt.setString(2, major.getFacultyId());
            stmt.setString(3, major.getDegreeLevel());
            stmt.setInt(4, major.getDurationYears());
            stmt.setString(5, major.getDescription());
            stmt.setString(6, major.getMajorId());
            
            int rows = stmt.executeUpdate();
            if (rows > 0) {
                System.out.println("Đã cập nhật major: " + major.getMajorName());
                return true;
            } else {
                System.out.println(" Không tìm thấy major để cập nhật: " + major.getMajorId());
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi cập nhật major: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Xóa ngành
     * @param majorId ID của ngành cần xóa
     * @return true nếu xóa thành công
     */
    public boolean deleteMajor(String majorId) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(DELETE_MAJOR)) {
            
            stmt.setString(1, majorId);
            int rows = stmt.executeUpdate();
            
            if (rows > 0) {
                System.out.println("Đã xóa major: " + majorId);
                return true;
            } else {
                System.out.println(" Không tồn tại major để xóa: " + majorId);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi xóa major: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Kiểm tra major ID đã tồn tại chưa
     * @param majorId Major ID cần kiểm tra
     * @return true nếu đã tồn tại
     */
    public boolean existsById(String majorId) {
        Major major = findById(majorId);
        return major != null;
    }

    /**
     * Map ResultSet thành Major object
     */
    private Major mapResultSetToMajor(ResultSet rs) throws SQLException {
        Major major = new Major();
        major.setMajorId(rs.getString("major_id"));
        major.setMajorName(rs.getString("major_name"));
        major.setFacultyId(rs.getString("faculty_id"));
        major.setDegreeLevel(rs.getString("degree_level"));
        major.setDurationYears(rs.getInt("duration_years"));
        major.setDescription(rs.getString("description"));
        return major;
    }
}