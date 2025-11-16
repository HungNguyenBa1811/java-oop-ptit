package main.java.repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import main.java.config.DatabaseConnection;
import main.java.model.Semester;

/**
 * SemesterRepository - Quản lý CRUD cho bảng semesters (Học kỳ)
 */
public class SemesterRepository {
    
    private static final String INSERT_SEMESTER =
        "INSERT INTO semesters (semester_id, term, academic_year, start_date, end_date) " +
        "VALUES (?, ?, ?, ?, ?)";

    private static final String SELECT_ALL_SEMESTERS =
        "SELECT * FROM semesters ORDER BY start_date DESC";
    
    private static final String SELECT_SEMESTER_BY_ID =
        "SELECT * FROM semesters WHERE semester_id = ?";
    
    private static final String SELECT_SEMESTERS_BY_YEAR =
        "SELECT * FROM semesters WHERE academic_year = ? ORDER BY start_date";
    
    private static final String SELECT_CURRENT_SEMESTER =
        "SELECT * FROM semesters WHERE CURDATE() BETWEEN start_date AND end_date LIMIT 1";
    
    private static final String UPDATE_SEMESTER =
        "UPDATE semesters SET term = ?, academic_year = ?, start_date = ?, end_date = ? WHERE semester_id = ?";
    
    private static final String DELETE_SEMESTER =
        "DELETE FROM semesters WHERE semester_id = ?";

    /**
     * Tạo học kỳ mới
     * @param semester Semester object cần tạo
     * @return true nếu tạo thành công
     */
    public boolean createSemester(Semester semester) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(INSERT_SEMESTER)) {

            stmt.setString(1, semester.getSemesterId());
            stmt.setString(2, semester.getTerm());
            stmt.setString(3, semester.getAcademicYear());
            stmt.setDate(4, Date.valueOf(semester.getStartDate()));
            stmt.setDate(5, Date.valueOf(semester.getEndDate()));

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                System.out.println("Semester created: " + semester.getSemesterId());
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi tạo semester: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Lấy tất cả học kỳ
     * @return List danh sách semesters
     */
    public List<Semester> findAll() {
        List<Semester> semesters = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_ALL_SEMESTERS);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                semesters.add(mapResultSetToSemester(rs));
            }
            System.out.println("Found " + semesters.size() + " semesters");
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy danh sách semester: " + e.getMessage());
            e.printStackTrace();
        }
        return semesters;
    }

    /**
     * Tìm học kỳ theo semester ID
     * @param semesterId Semester ID cần tìm
     * @return Semester nếu tìm thấy, null nếu không
     */
    public Semester findById(String semesterId) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_SEMESTER_BY_ID)) {
            
            stmt.setString(1, semesterId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                System.out.println("Semester found: " + semesterId);
                return mapResultSetToSemester(rs);
            } else {
                System.out.println("Semester not found: " + semesterId);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi tìm semester by ID: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Tìm học kỳ theo năm học
     * @param academicYear Năm học (VD: 2025-2026)
     * @return List danh sách semesters trong năm học đó
     */
    public List<Semester> findByAcademicYear(String academicYear) {
        List<Semester> semesters = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_SEMESTERS_BY_YEAR)) {
            
            stmt.setString(1, academicYear);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                semesters.add(mapResultSetToSemester(rs));
            }
            System.out.println("Found " + semesters.size() + " semesters in year: " + academicYear);
        } catch (SQLException e) {
            System.err.println("Lỗi khi tìm semester by year: " + e.getMessage());
            e.printStackTrace();
        }
        return semesters;
    }

    /**
     * Lấy học kỳ hiện tại (đang diễn ra)
     * @return Semester hiện tại, null nếu không có
     */
    public Semester findCurrentSemester() {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_CURRENT_SEMESTER);
             ResultSet rs = stmt.executeQuery()) {
            
            if (rs.next()) {
                Semester semester = mapResultSetToSemester(rs);
                System.out.println("Current semester: " + semester.getSemesterId());
                return semester;
            } else {
                System.out.println("No current semester found");
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi tìm current semester: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Cập nhật thông tin học kỳ
     * @param semester Semester object với thông tin mới
     * @return true nếu update thành công
     */
    public boolean updateSemester(Semester semester) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(UPDATE_SEMESTER)) {
            
            stmt.setString(1, semester.getTerm());
            stmt.setString(2, semester.getAcademicYear());
            stmt.setDate(3, Date.valueOf(semester.getStartDate()));
            stmt.setDate(4, Date.valueOf(semester.getEndDate()));
            stmt.setString(5, semester.getSemesterId());
            
            int rows = stmt.executeUpdate();
            if (rows > 0) {
                System.out.println("Semester updated: " + semester.getSemesterId());
                return true;
            } else {
                System.out.println("No semester found to update: " + semester.getSemesterId());
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi update semester: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Xóa học kỳ
     * @param semesterId ID của học kỳ cần xóa
     * @return true nếu xóa thành công
     */
    public boolean deleteSemester(String semesterId) {
        try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(DELETE_SEMESTER)) {
            
            stmt.setString(1, semesterId);
            int rows = stmt.executeUpdate();
            
            if (rows > 0) {
                System.out.println("Semester deleted: " + semesterId);
                return true;
            } else {
                System.out.println("No semester found to delete: " + semesterId);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi xóa semester: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Kiểm tra semester ID đã tồn tại chưa
     * @param semesterId Semester ID cần kiểm tra
     * @return true nếu đã tồn tại
     */
    public boolean existsById(String semesterId) {
        Semester semester = findById(semesterId);
        return semester != null;
    }

    /**
     * Map ResultSet thành Semester object
     */
    private Semester mapResultSetToSemester(ResultSet rs) throws SQLException {
        String semesterId = rs.getString("semester_id");
        String term = rs.getString("term");
        String academicYear = rs.getString("academic_year");
        Date startDate = rs.getDate("start_date");
        Date endDate = rs.getDate("end_date");
        
        return new Semester(semesterId, term, academicYear, startDate.toLocalDate(), endDate.toLocalDate());
    }
}
