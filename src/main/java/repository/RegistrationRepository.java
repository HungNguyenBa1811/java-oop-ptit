package main.java.repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import main.java.config.DatabaseConnection;
import main.java.model.Registration;

/**
 * RegistrationRepository - Quản lý CRUD cho bảng registrations (Đăng ký môn học)
 */
public class RegistrationRepository {
    
    private static final String INSERT_REGISTRATION =
        "INSERT INTO registrations (registration_id, student_id, course_offering_id, registered_at, status, note) " +
        "VALUES (?, ?, ?, ?, ?, ?)";

    private static final String SELECT_ALL_REGISTRATIONS =
        "SELECT * FROM registrations ORDER BY registered_at DESC";
    
    private static final String SELECT_REGISTRATION_BY_ID =
        "SELECT * FROM registrations WHERE registration_id = ?";
    
    private static final String SELECT_REGISTRATIONS_BY_STUDENT =
        "SELECT * FROM registrations WHERE student_id = ? ORDER BY registered_at DESC";
    
    private static final String SELECT_REGISTRATIONS_BY_COURSE_OFFERING =
        "SELECT * FROM registrations WHERE course_offering_id = ? ORDER BY registered_at DESC";
    
    private static final String SELECT_REGISTRATIONS_BY_STATUS =
        "SELECT * FROM registrations WHERE status = ? ORDER BY registered_at DESC";
    
    private static final String SELECT_REGISTRATION_BY_STUDENT_AND_COURSE =
        "SELECT * FROM registrations WHERE student_id = ? AND course_offering_id = ?";
    
    private static final String UPDATE_REGISTRATION =
        "UPDATE registrations SET status = ?, note = ? WHERE registration_id = ?";
    
    private static final String UPDATE_REGISTRATION_STATUS =
        "UPDATE registrations SET status = ? WHERE registration_id = ?";
    
    private static final String DELETE_REGISTRATION =
        "DELETE FROM registrations WHERE registration_id = ?";
    
    private static final String COUNT_REGISTRATIONS_BY_COURSE_OFFERING =
        "SELECT COUNT(*) as count FROM registrations WHERE course_offering_id = ? AND status = 'Thành công'";

    /**
     * Tạo đăng ký mới
     * @param registration Registration object cần tạo
     * @return true nếu tạo thành công
     */
    public boolean createRegistration(Registration registration) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(INSERT_REGISTRATION)) {

            stmt.setString(1, registration.getRegistrationId());
            stmt.setString(2, registration.getStudentId());
            stmt.setString(3, registration.getCourseOfferingId());
            stmt.setTimestamp(4, registration.getRegisteredAt() != null ? 
                Timestamp.valueOf(registration.getRegisteredAt()) : 
                Timestamp.valueOf(java.time.LocalDateTime.now()));
            stmt.setString(5, registration.getStatus());
            stmt.setString(6, registration.getNote());

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                System.out.println("Registration created: " + registration.getRegistrationId());
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi tạo registration: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Đăng ký môn học cho sinh viên bằng userId
     * Không validation - để service layer xử lý
     * @param userId User ID của sinh viên (userId = studentId trong schema)
     * @param courseOfferingId Course Offering ID
     * @param registrationId Registration ID
     * @param status Trạng thái đăng ký
     * @return true nếu tạo thành công
     */
    public boolean registerByUserId(String userId, String courseOfferingId, String registrationId, String status) {
        Registration registration = new Registration();
        registration.setRegistrationId(registrationId);
        registration.setStudentId(userId); // userId = studentId
        registration.setCourseOfferingId(courseOfferingId);
        registration.setRegisteredAt(java.time.LocalDateTime.now());
        registration.setStatus(status);
        registration.setNote(null);
        
        return createRegistration(registration);
    }

    /**
     * Lấy tất cả đăng ký
     * @return List danh sách registrations
     */
    public List<Registration> findAll() {
        List<Registration> registrations = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_ALL_REGISTRATIONS);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                registrations.add(mapResultSetToRegistration(rs));
            }
            System.out.println("Found " + registrations.size() + " registrations");
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy danh sách registration: " + e.getMessage());
            e.printStackTrace();
        }
        return registrations;
    }

    /**
     * Tìm đăng ký theo registration ID
     * @param registrationId Registration ID cần tìm
     * @return Registration nếu tìm thấy, null nếu không
     */
    public Registration findById(String registrationId) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_REGISTRATION_BY_ID)) {
            
            stmt.setString(1, registrationId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                System.out.println("Registration found: " + registrationId);
                return mapResultSetToRegistration(rs);
            } else {
                System.out.println("Registration not found: " + registrationId);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi tìm registration by ID: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Tìm tất cả đăng ký của một sinh viên
     * @param studentId Student ID
     * @return List danh sách registrations của sinh viên
     */
    public List<Registration> findByStudent(String studentId) {
        List<Registration> registrations = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_REGISTRATIONS_BY_STUDENT)) {
            
            stmt.setString(1, studentId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                registrations.add(mapResultSetToRegistration(rs));
            }
            System.out.println("Found " + registrations.size() + " registrations for student: " + studentId);
        } catch (SQLException e) {
            System.err.println("Lỗi khi tìm registration by student: " + e.getMessage());
            e.printStackTrace();
        }
        return registrations;
    }

    /**
     * Tìm tất cả đăng ký cho một lớp mở môn học
     * @param courseOfferingId Course Offering ID
     * @return List danh sách registrations
     */
    public List<Registration> findByCourseOffering(String courseOfferingId) {
        List<Registration> registrations = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_REGISTRATIONS_BY_COURSE_OFFERING)) {
            
            stmt.setString(1, courseOfferingId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                registrations.add(mapResultSetToRegistration(rs));
            }
            System.out.println("Found " + registrations.size() + " registrations for course offering: " + courseOfferingId);
        } catch (SQLException e) {
            System.err.println("Lỗi khi tìm registration by course offering: " + e.getMessage());
            e.printStackTrace();
        }
        return registrations;
    }

    /**
     * Tìm đăng ký theo trạng thái
     * @param status Trạng thái ('Thành công' hoặc 'Thất bại')
     * @return List danh sách registrations với trạng thái đó
     */
    public List<Registration> findByStatus(String status) {
        List<Registration> registrations = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_REGISTRATIONS_BY_STATUS)) {
            
            stmt.setString(1, status);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                registrations.add(mapResultSetToRegistration(rs));
            }
            System.out.println("Found " + registrations.size() + " registrations with status: " + status);
        } catch (SQLException e) {
            System.err.println("Lỗi khi tìm registration by status: " + e.getMessage());
            e.printStackTrace();
        }
        return registrations;
    }

    /**
     * Tìm đăng ký cụ thể của sinh viên cho một lớp mở
     * @param studentId Student ID
     * @param courseOfferingId Course Offering ID
     * @return Registration nếu tìm thấy, null nếu không
     */
    public Registration findByStudentAndCourseOffering(String studentId, String courseOfferingId) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_REGISTRATION_BY_STUDENT_AND_COURSE)) {
            
            stmt.setString(1, studentId);
            stmt.setString(2, courseOfferingId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                System.out.println("Registration found for student: " + studentId + " and course offering: " + courseOfferingId);
                return mapResultSetToRegistration(rs);
            } else {
                System.out.println("Registration not found for student: " + studentId + " and course offering: " + courseOfferingId);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi tìm registration by student and course offering: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Cập nhật thông tin đăng ký
     * @param registration Registration object với thông tin mới
     * @return true nếu update thành công
     */
    public boolean updateRegistration(Registration registration) {
        try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(UPDATE_REGISTRATION)) {
            
            stmt.setString(1, registration.getStatus());
            stmt.setString(2, registration.getNote());
            stmt.setString(3, registration.getRegistrationId());
            
            int rows = stmt.executeUpdate();
            if (rows > 0) {
                System.out.println("Registration updated: " + registration.getRegistrationId());
                return true;
            } else {
                System.out.println("No registration found to update: " + registration.getRegistrationId());
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi update registration: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Cập nhật trạng thái đăng ký
     * @param registrationId Registration ID
     * @param status Trạng thái mới
     * @return true nếu update thành công
     */
    public boolean updateStatus(String registrationId, String status) {
        try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(UPDATE_REGISTRATION_STATUS)) {
            
            stmt.setString(1, status);
            stmt.setString(2, registrationId);
            
            int rows = stmt.executeUpdate();
            if (rows > 0) {
                System.out.println("Registration status updated: " + registrationId + " -> " + status);
                return true;
            } else {
                System.out.println("No registration found to update status: " + registrationId);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi update registration status: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Xóa đăng ký
     * @param registrationId ID của đăng ký cần xóa
     * @return true nếu xóa thành công
     */
    public boolean deleteRegistration(String registrationId) {
        try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(DELETE_REGISTRATION)) {
            
            stmt.setString(1, registrationId);
            int rows = stmt.executeUpdate();
            
            if (rows > 0) {
                System.out.println("Registration deleted: " + registrationId);
                return true;
            } else {
                System.out.println("No registration found to delete: " + registrationId);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi xóa registration: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Đếm số lượng đăng ký thành công cho một lớp mở
     * @param courseOfferingId Course Offering ID
     * @return Số lượng đăng ký thành công
     */
    public int countSuccessfulRegistrations(String courseOfferingId) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(COUNT_REGISTRATIONS_BY_COURSE_OFFERING)) {
            
            stmt.setString(1, courseOfferingId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                int count = rs.getInt("count");
                System.out.println("Successful registrations for course offering " + courseOfferingId + ": " + count);
                return count;
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi đếm registration: " + e.getMessage());
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * Kiểm tra registration ID đã tồn tại chưa
     * @param registrationId Registration ID cần kiểm tra
     * @return true nếu đã tồn tại
     */
    public boolean existsById(String registrationId) {
        Registration registration = findById(registrationId);
        return registration != null;
    }

    /**
     * Kiểm tra sinh viên đã đăng ký lớp mở này chưa
     * @param studentId Student ID
     * @param courseOfferingId Course Offering ID
     * @return true nếu đã đăng ký
     */
    public boolean existsByStudentAndCourseOffering(String studentId, String courseOfferingId) {
        Registration registration = findByStudentAndCourseOffering(studentId, courseOfferingId);
        return registration != null;
    }

    /**
     * Map ResultSet thành Registration object
     */
    private Registration mapResultSetToRegistration(ResultSet rs) throws SQLException {
        Registration registration = new Registration();
        
        registration.setRegistrationId(rs.getString("registration_id"));
        registration.setStudentId(rs.getString("student_id"));
        registration.setCourseOfferingId(rs.getString("course_offering_id"));
        
        Timestamp registeredAt = rs.getTimestamp("registered_at");
        if (registeredAt != null) {
            registration.setRegisteredAt(registeredAt.toLocalDateTime());
        }
        
        registration.setStatus(rs.getString("status"));
        registration.setNote(rs.getString("note"));
        
        return registration;
    }
}
