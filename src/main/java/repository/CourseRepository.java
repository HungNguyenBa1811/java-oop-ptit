package main.java.repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import main.java.config.DatabaseConnection;
import main.java.model.Course;

/**
 * CourseRepository - Quản lý CRUD cho bảng courses (Môn học)
 */
public class CourseRepository {
    
    private static final String INSERT_COURSE =
        "INSERT INTO courses (course_id, course_name, credits, faculty_id) " +
        "VALUES (?, ?, ?, ?)";

    private static final String SELECT_ALL_COURSES =
        "SELECT * FROM courses ORDER BY course_name";
    
    private static final String SELECT_COURSE_BY_ID =
        "SELECT * FROM courses WHERE course_id = ?";
    
    private static final String SELECT_COURSES_BY_FACULTY =
        "SELECT * FROM courses WHERE faculty_id = ? ORDER BY course_name";
    
    private static final String SELECT_COURSES_BY_CREDITS =
        "SELECT * FROM courses WHERE credits = ? ORDER BY course_name";
    
    private static final String UPDATE_COURSE =
        "UPDATE courses SET course_name = ?, credits = ?, faculty_id = ? WHERE course_id = ?";
    
    private static final String DELETE_COURSE =
        "DELETE FROM courses WHERE course_id = ?";

    /**
     * Tạo môn học mới
     * @param course Course object cần tạo
     * @param facultyId Faculty ID
     * @return true nếu tạo thành công
     */
    public boolean createCourse(Course course, String facultyId) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(INSERT_COURSE)) {

            stmt.setString(1, course.getCourseId());
            stmt.setString(2, course.getCourseName());
            stmt.setInt(3, course.getCredits());
            stmt.setString(4, facultyId);

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                System.out.println("Course created: " + course.getCourseId());
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi tạo course: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Lấy tất cả môn học
     * @return List danh sách courses
     */
    public List<Course> findAll() {
        List<Course> courses = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_ALL_COURSES);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                courses.add(mapResultSetToCourse(rs));
            }
            System.out.println("Found " + courses.size() + " courses");
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy danh sách course: " + e.getMessage());
            e.printStackTrace();
        }
        return courses;
    }

    /**
     * Tìm môn học theo course ID
     * @param courseId Course ID cần tìm
     * @return Course nếu tìm thấy, null nếu không
     */
    public Course findById(String courseId) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_COURSE_BY_ID)) {
            
            stmt.setString(1, courseId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                System.out.println("Course found: " + courseId);
                return mapResultSetToCourse(rs);
            } else {
                System.out.println("Course not found: " + courseId);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi tìm course by ID: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Tìm các môn học theo khoa
     * @param facultyId Faculty ID
     * @return List danh sách courses của khoa
     */
    public List<Course> findByFaculty(String facultyId) {
        List<Course> courses = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_COURSES_BY_FACULTY)) {
            
            stmt.setString(1, facultyId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                courses.add(mapResultSetToCourse(rs));
            }
            System.out.println("Found " + courses.size() + " courses for faculty: " + facultyId);
        } catch (SQLException e) {
            System.err.println("Lỗi khi tìm course by faculty: " + e.getMessage());
            e.printStackTrace();
        }
        return courses;
    }

    /**
     * Tìm các môn học theo số tín chỉ
     * @param credits Số tín chỉ
     * @return List danh sách courses có số tín chỉ đó
     */
    public List<Course> findByCredits(int credits) {
        List<Course> courses = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_COURSES_BY_CREDITS)) {
            
            stmt.setInt(1, credits);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                courses.add(mapResultSetToCourse(rs));
            }
            System.out.println("Found " + courses.size() + " courses with " + credits + " credits");
        } catch (SQLException e) {
            System.err.println("Lỗi khi tìm course by credits: " + e.getMessage());
            e.printStackTrace();
        }
        return courses;
    }

    /**
     * Cập nhật thông tin môn học
     * @param course Course object với thông tin mới
     * @param facultyId Faculty ID mới
     * @return true nếu update thành công
     */
    public boolean updateCourse(Course course, String facultyId) {
        try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(UPDATE_COURSE)) {
            
            stmt.setString(1, course.getCourseName());
            stmt.setInt(2, course.getCredits());
            stmt.setString(3, facultyId);
            stmt.setString(4, course.getCourseId());
            
            int rows = stmt.executeUpdate();
            if (rows > 0) {
                System.out.println("Course updated: " + course.getCourseId());
                return true;
            } else {
                System.out.println("No course found to update: " + course.getCourseId());
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi update course: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Xóa môn học
     * @param courseId ID của môn học cần xóa
     * @return true nếu xóa thành công
     */
    public boolean deleteCourse(String courseId) {
        try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(DELETE_COURSE)) {
            
            stmt.setString(1, courseId);
            int rows = stmt.executeUpdate();
            
            if (rows > 0) {
                System.out.println("Course deleted: " + courseId);
                return true;
            } else {
                System.out.println("No course found to delete: " + courseId);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi xóa course: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Kiểm tra course ID đã tồn tại chưa
     * @param courseId Course ID cần kiểm tra
     * @return true nếu đã tồn tại
     */
    public boolean existsById(String courseId) {
        Course course = findById(courseId);
        return course != null;
    }

    /**
     * Map ResultSet thành Course object
     */
    private Course mapResultSetToCourse(ResultSet rs) throws SQLException {
        String courseId = rs.getString("course_id");
        String courseName = rs.getString("course_name");
        int credits = rs.getInt("credits");
        // Note: faculty_id không có trong model Course, chỉ lưu trong database
        
        return new Course(courseId, courseName, credits, null);
    }
}
