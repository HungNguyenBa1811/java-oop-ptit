package main.java.repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import main.java.config.DatabaseConnection;
import main.java.model.CourseOffering;

/**
 * CourseOfferingRepository - Quản lý CRUD cho bảng course_offerings (Lớp mở môn học)
 */
public class CourseOfferingRepository {
    
    private static final String INSERT_COURSE_OFFERING =
        "INSERT INTO course_offerings (course_offering_id, course_id, major_id, instructor, room_id, semester_id, max_capacity, current_capacity) " +
        "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

    private static final String SELECT_ALL_COURSE_OFFERINGS =
        "SELECT * FROM course_offerings ORDER BY semester_id, course_id";
    
    private static final String SELECT_COURSE_OFFERING_BY_ID =
        "SELECT * FROM course_offerings WHERE course_offering_id = ?";
    
    private static final String SELECT_COURSE_OFFERINGS_BY_COURSE =
        "SELECT * FROM course_offerings WHERE course_id = ? ORDER BY semester_id";
    
    private static final String SELECT_COURSE_OFFERINGS_BY_SEMESTER =
        "SELECT * FROM course_offerings WHERE semester_id = ? ORDER BY course_id";
    
    private static final String SELECT_COURSE_OFFERINGS_BY_MAJOR =
        "SELECT * FROM course_offerings WHERE major_id = ? ORDER BY semester_id, course_id";
    
    private static final String SELECT_COURSE_OFFERINGS_BY_ROOM =
        "SELECT * FROM course_offerings WHERE room_id = ? ORDER BY semester_id";
    
    private static final String SELECT_AVAILABLE_COURSE_OFFERINGS =
        "SELECT * FROM course_offerings WHERE current_capacity < max_capacity ORDER BY semester_id, course_id";
    
    private static final String UPDATE_COURSE_OFFERING =
        "UPDATE course_offerings SET course_id = ?, major_id = ?, instructor = ?, room_id = ?, semester_id = ?, max_capacity = ?, current_capacity = ? WHERE course_offering_id = ?";
    
    private static final String UPDATE_CURRENT_CAPACITY =
        "UPDATE course_offerings SET current_capacity = ? WHERE course_offering_id = ?";
    
    private static final String INCREMENT_CAPACITY =
        "UPDATE course_offerings SET current_capacity = current_capacity + 1 WHERE course_offering_id = ?";
    
    private static final String DECREMENT_CAPACITY =
        "UPDATE course_offerings SET current_capacity = current_capacity - 1 WHERE course_offering_id = ? AND current_capacity > 0";
    
    private static final String DELETE_COURSE_OFFERING =
        "DELETE FROM course_offerings WHERE course_offering_id = ?";

    /**
     * Tạo lớp mở môn học mới
     * @param courseOffering CourseOffering object cần tạo
     * @return true nếu tạo thành công
     */
    public boolean createCourseOffering(CourseOffering courseOffering) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(INSERT_COURSE_OFFERING)) {

            stmt.setString(1, courseOffering.getCourseOfferingId());
            stmt.setString(2, courseOffering.getCourseId());
            stmt.setString(3, courseOffering.getMajorId());
            stmt.setString(4, courseOffering.getInstructor());
            stmt.setString(5, courseOffering.getRoomId());
            stmt.setString(6, courseOffering.getSemesterId());
            stmt.setInt(7, Integer.parseInt(courseOffering.getMaxCapacity()));
            stmt.setInt(8, Integer.parseInt(courseOffering.getCurrentCapacity()));

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                System.out.println("CourseOffering created: " + courseOffering.getCourseOfferingId());
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi tạo course offering: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Lấy tất cả lớp mở môn học
     * @return List danh sách course offerings
     */
    public List<CourseOffering> findAll() {
        List<CourseOffering> courseOfferings = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_ALL_COURSE_OFFERINGS);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                courseOfferings.add(mapResultSetToCourseOffering(rs));
            }
            System.out.println("Found " + courseOfferings.size() + " course offerings");
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy danh sách course offering: " + e.getMessage());
            e.printStackTrace();
        }
        return courseOfferings;
    }

    /**
     * Tìm lớp mở theo ID
     * @param courseOfferingId Course Offering ID cần tìm
     * @return CourseOffering nếu tìm thấy, null nếu không
     */
    public CourseOffering findById(String courseOfferingId) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_COURSE_OFFERING_BY_ID)) {
            
            stmt.setString(1, courseOfferingId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                System.out.println("CourseOffering found: " + courseOfferingId);
                return mapResultSetToCourseOffering(rs);
            } else {
                System.out.println("CourseOffering not found: " + courseOfferingId);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi tìm course offering by ID: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Tìm các lớp mở theo môn học
     * @param courseId Course ID
     * @return List danh sách course offerings của môn học
     */
    public List<CourseOffering> findByCourse(String courseId) {
        List<CourseOffering> courseOfferings = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_COURSE_OFFERINGS_BY_COURSE)) {
            
            stmt.setString(1, courseId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                courseOfferings.add(mapResultSetToCourseOffering(rs));
            }
            System.out.println("Found " + courseOfferings.size() + " course offerings for course: " + courseId);
        } catch (SQLException e) {
            System.err.println("Lỗi khi tìm course offering by course: " + e.getMessage());
            e.printStackTrace();
        }
        return courseOfferings;
    }

    /**
     * Tìm các lớp mở theo học kỳ
     * @param semesterId Semester ID
     * @return List danh sách course offerings trong học kỳ
     */
    public List<CourseOffering> findBySemester(String semesterId) {
        List<CourseOffering> courseOfferings = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_COURSE_OFFERINGS_BY_SEMESTER)) {
            
            stmt.setString(1, semesterId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                courseOfferings.add(mapResultSetToCourseOffering(rs));
            }
            System.out.println("Found " + courseOfferings.size() + " course offerings in semester: " + semesterId);
        } catch (SQLException e) {
            System.err.println("Lỗi khi tìm course offering by semester: " + e.getMessage());
            e.printStackTrace();
        }
        return courseOfferings;
    }

    /**
     * Tìm các lớp mở theo ngành
     * @param majorId Major ID
     * @return List danh sách course offerings của ngành
     */
    public List<CourseOffering> findByMajor(String majorId) {
        List<CourseOffering> courseOfferings = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_COURSE_OFFERINGS_BY_MAJOR)) {
            
            stmt.setString(1, majorId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                courseOfferings.add(mapResultSetToCourseOffering(rs));
            }
            System.out.println("Found " + courseOfferings.size() + " course offerings for major: " + majorId);
        } catch (SQLException e) {
            System.err.println("Lỗi khi tìm course offering by major: " + e.getMessage());
            e.printStackTrace();
        }
        return courseOfferings;
    }

    /**
     * Tìm các lớp mở theo phòng học
     * @param roomId Room ID
     * @return List danh sách course offerings trong phòng
     */
    public List<CourseOffering> findByRoom(String roomId) {
        List<CourseOffering> courseOfferings = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_COURSE_OFFERINGS_BY_ROOM)) {
            
            stmt.setString(1, roomId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                courseOfferings.add(mapResultSetToCourseOffering(rs));
            }
            System.out.println("Found " + courseOfferings.size() + " course offerings in room: " + roomId);
        } catch (SQLException e) {
            System.err.println("Lỗi khi tìm course offering by room: " + e.getMessage());
            e.printStackTrace();
        }
        return courseOfferings;
    }

    /**
     * Tìm các lớp mở còn chỗ (current_capacity < max_capacity)
     * @return List danh sách course offerings còn chỗ
     */
    public List<CourseOffering> findAvailableCourseOfferings() {
        List<CourseOffering> courseOfferings = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_AVAILABLE_COURSE_OFFERINGS);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                courseOfferings.add(mapResultSetToCourseOffering(rs));
            }
            System.out.println("Found " + courseOfferings.size() + " available course offerings");
        } catch (SQLException e) {
            System.err.println("Lỗi khi tìm available course offerings: " + e.getMessage());
            e.printStackTrace();
        }
        return courseOfferings;
    }

    /**
     * Cập nhật thông tin lớp mở
     * @param courseOffering CourseOffering object với thông tin mới
     * @return true nếu update thành công
     */
    public boolean updateCourseOffering(CourseOffering courseOffering) {
        try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(UPDATE_COURSE_OFFERING)) {
            
            stmt.setString(1, courseOffering.getCourseId());
            stmt.setString(2, courseOffering.getMajorId());
            stmt.setString(3, courseOffering.getInstructor());
            stmt.setString(4, courseOffering.getRoomId());
            stmt.setString(5, courseOffering.getSemesterId());
            stmt.setInt(6, Integer.parseInt(courseOffering.getMaxCapacity()));
            stmt.setInt(7, Integer.parseInt(courseOffering.getCurrentCapacity()));
            stmt.setString(8, courseOffering.getCourseOfferingId());
            
            int rows = stmt.executeUpdate();
            if (rows > 0) {
                System.out.println("CourseOffering updated: " + courseOffering.getCourseOfferingId());
                return true;
            } else {
                System.out.println("No course offering found to update: " + courseOffering.getCourseOfferingId());
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi update course offering: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Cập nhật current capacity
     * @param courseOfferingId Course Offering ID
     * @param currentCapacity Số lượng sinh viên hiện tại
     * @return true nếu update thành công
     */
    public boolean updateCurrentCapacity(String courseOfferingId, int currentCapacity) {
        try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(UPDATE_CURRENT_CAPACITY)) {
            
            stmt.setInt(1, currentCapacity);
            stmt.setString(2, courseOfferingId);
            
            int rows = stmt.executeUpdate();
            if (rows > 0) {
                System.out.println("Current capacity updated for: " + courseOfferingId);
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi update current capacity: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Tăng current capacity lên 1 (khi sinh viên đăng ký)
     * @param courseOfferingId Course Offering ID
     * @return true nếu tăng thành công
     */
    public boolean incrementCapacity(String courseOfferingId) {
        try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(INCREMENT_CAPACITY)) {
            
            stmt.setString(1, courseOfferingId);
            
            int rows = stmt.executeUpdate();
            if (rows > 0) {
                System.out.println("Capacity incremented for: " + courseOfferingId);
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi increment capacity: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Giảm current capacity xuống 1 (khi sinh viên hủy đăng ký)
     * @param courseOfferingId Course Offering ID
     * @return true nếu giảm thành công
     */
    public boolean decrementCapacity(String courseOfferingId) {
        try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(DECREMENT_CAPACITY)) {
            
            stmt.setString(1, courseOfferingId);
            
            int rows = stmt.executeUpdate();
            if (rows > 0) {
                System.out.println("Capacity decremented for: " + courseOfferingId);
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi decrement capacity: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Xóa lớp mở
     * @param courseOfferingId ID của lớp mở cần xóa
     * @return true nếu xóa thành công
     */
    public boolean deleteCourseOffering(String courseOfferingId) {
        try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(DELETE_COURSE_OFFERING)) {
            
            stmt.setString(1, courseOfferingId);
            int rows = stmt.executeUpdate();
            
            if (rows > 0) {
                System.out.println("CourseOffering deleted: " + courseOfferingId);
                return true;
            } else {
                System.out.println("No course offering found to delete: " + courseOfferingId);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi xóa course offering: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Kiểm tra course offering ID đã tồn tại chưa
     * @param courseOfferingId Course Offering ID cần kiểm tra
     * @return true nếu đã tồn tại
     */
    public boolean existsById(String courseOfferingId) {
        CourseOffering courseOffering = findById(courseOfferingId);
        return courseOffering != null;
    }

    /**
     * Map ResultSet thành CourseOffering object
     */
    private CourseOffering mapResultSetToCourseOffering(ResultSet rs) throws SQLException {
        CourseOffering courseOffering = new CourseOffering();
        
        courseOffering.setCourseOfferingId(rs.getString("course_offering_id"));
        courseOffering.setCourseId(rs.getString("course_id"));
        courseOffering.setMajorId(rs.getString("major_id"));
        courseOffering.setInstructor(rs.getString("instructor"));
        courseOffering.setRoomId(rs.getString("room_id"));
        courseOffering.setSemesterId(rs.getString("semester_id"));
        courseOffering.setMaxCapacity(String.valueOf(rs.getInt("max_capacity")));
        courseOffering.setCurrentCapacity(String.valueOf(rs.getInt("current_capacity")));
        
        return courseOffering;
    }
}
