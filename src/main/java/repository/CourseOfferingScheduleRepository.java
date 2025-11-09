package main.java.repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import main.java.config.DatabaseConnection;
import main.java.model.CourseOfferingSchedule;
import main.java.model.Schedule;

/**
 * CourseOfferingScheduleRepository - Quản lý CRUD cho bảng course_offerings_schedules
 */
public class CourseOfferingScheduleRepository {
    
    private static final String INSERT_COURSE_OFFERING_SCHEDULE =
        "INSERT INTO course_offerings_schedules (course_offering_id, schedule_id, start_date, end_date) " +
        "VALUES (?, ?, ?, ?)";
    
    private static final String SELECT_ALL =
        "SELECT * FROM course_offerings_schedules";
    
    private static final String SELECT_BY_ID =
        "SELECT * FROM course_offerings_schedules WHERE id = ?";
    
    private static final String SELECT_BY_COURSE_OFFERING =
        "SELECT * FROM course_offerings_schedules WHERE course_offering_id = ?";
    
    private static final String SELECT_BY_SCHEDULE =
        "SELECT * FROM course_offerings_schedules WHERE schedule_id = ?";
    
    private static final String SELECT_SCHEDULES_BY_COURSE_OFFERING =
        "SELECT s.*, cos.start_date, cos.end_date FROM schedules s " +
        "INNER JOIN course_offerings_schedules cos ON s.schedule_id = cos.schedule_id " +
        "WHERE cos.course_offering_id = ? " +
        "ORDER BY s.day_of_week, s.start_time";
    
    private static final String SELECT_WITH_SCHEDULE_BY_COURSE_OFFERING =
        "SELECT cos.id, cos.course_offering_id, cos.schedule_id, cos.start_date, cos.end_date, " +
        "s.day_of_week, s.start_time, s.end_time " +
        "FROM course_offerings_schedules cos " +
        "INNER JOIN schedules s ON cos.schedule_id = s.schedule_id " +
        "WHERE cos.course_offering_id = ? " +
        "ORDER BY s.day_of_week, s.start_time";
    
    private static final String DELETE_BY_ID =
        "DELETE FROM course_offerings_schedules WHERE id = ?";
    
    private static final String DELETE_BY_COURSE_OFFERING =
        "DELETE FROM course_offerings_schedules WHERE course_offering_id = ?";
    
    private static final String DELETE_BY_SCHEDULE =
        "DELETE FROM course_offerings_schedules WHERE schedule_id = ?";
    
    /**
     * Tạo liên kết mới giữa course offering và schedule
     */
    public boolean create(String courseOfferingId, String scheduleId, Date startDate, Date endDate) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(INSERT_COURSE_OFFERING_SCHEDULE)) {
            
            stmt.setString(1, courseOfferingId);
            stmt.setString(2, scheduleId);
            stmt.setDate(3, startDate);
            stmt.setDate(4, endDate);
            
            int rows = stmt.executeUpdate();
            return rows > 0;
            
        } catch (SQLException e) {
            System.err.println("Lỗi khi tạo course offering schedule: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Lấy tất cả liên kết
     */
    public List<CourseOfferingSchedule> findAll() {
        List<CourseOfferingSchedule> result = new ArrayList<>();
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(SELECT_ALL)) {
            
            while (rs.next()) {
                result.add(mapResultSetToEntity(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy tất cả course offering schedules: " + e.getMessage());
            e.printStackTrace();
        }
        
        return result;
    }
    
    /**
     * Lấy theo ID
     */
    public CourseOfferingSchedule findById(int id) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_BY_ID)) {
            
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToEntity(rs);
            }
            
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy course offering schedule theo ID: " + e.getMessage());
            e.printStackTrace();
        }
        
        return null;
    }
    
    /**
     * Lấy tất cả liên kết theo course offering ID
     */
    public List<CourseOfferingSchedule> findByCourseOfferingId(String courseOfferingId) {
        List<CourseOfferingSchedule> result = new ArrayList<>();
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_BY_COURSE_OFFERING)) {
            
            stmt.setString(1, courseOfferingId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                result.add(mapResultSetToEntity(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy course offering schedules theo course offering ID: " + e.getMessage());
            e.printStackTrace();
        }
        
        return result;
    }
    
    /**
     * Lấy danh sách Schedule objects theo course offering ID
     * JOIN với bảng schedules để lấy đầy đủ thông tin
     */
    public List<Schedule> findSchedulesByCourseOfferingId(String courseOfferingId) {
        List<Schedule> schedules = new ArrayList<>();
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_SCHEDULES_BY_COURSE_OFFERING)) {
            
            stmt.setString(1, courseOfferingId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                schedules.add(mapResultSetToSchedule(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy schedules theo course offering ID: " + e.getMessage());
            e.printStackTrace();
        }
        
        return schedules;
    }
    
    /**
     * Lấy danh sách CourseOfferingSchedule với đầy đủ thông tin Schedule object
     * JOIN với bảng schedules để có thể dùng getFullSchedule()
     */
    public List<CourseOfferingSchedule> findWithSchedulesByCourseOfferingId(String courseOfferingId) {
        List<CourseOfferingSchedule> result = new ArrayList<>();
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_WITH_SCHEDULE_BY_COURSE_OFFERING)) {
            
            stmt.setString(1, courseOfferingId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                result.add(mapResultSetToEntityWithSchedule(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy course offering schedules với schedule: " + e.getMessage());
            e.printStackTrace();
        }
        
        return result;
    }
    
    /**
     * Lấy tất cả liên kết theo schedule ID
     */
    public List<CourseOfferingSchedule> findByScheduleId(String scheduleId) {
        List<CourseOfferingSchedule> result = new ArrayList<>();
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_BY_SCHEDULE)) {
            
            stmt.setString(1, scheduleId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                result.add(mapResultSetToEntity(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy course offering schedules theo schedule ID: " + e.getMessage());
            e.printStackTrace();
        }
        
        return result;
    }
    
    /**
     * Xóa theo ID
     */
    public boolean deleteById(int id) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(DELETE_BY_ID)) {
            
            stmt.setInt(1, id);
            int rows = stmt.executeUpdate();
            return rows > 0;
            
        } catch (SQLException e) {
            System.err.println("Lỗi khi xóa course offering schedule: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Xóa tất cả liên kết của một course offering
     */
    public boolean deleteByCourseOfferingId(String courseOfferingId) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(DELETE_BY_COURSE_OFFERING)) {
            
            stmt.setString(1, courseOfferingId);
            int rows = stmt.executeUpdate();
            return rows > 0;
            
        } catch (SQLException e) {
            System.err.println("Lỗi khi xóa course offering schedules theo course offering ID: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Xóa tất cả liên kết của một schedule
     */
    public boolean deleteByScheduleId(String scheduleId) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(DELETE_BY_SCHEDULE)) {
            
            stmt.setString(1, scheduleId);
            int rows = stmt.executeUpdate();
            return rows > 0;
            
        } catch (SQLException e) {
            System.err.println("Lỗi khi xóa course offering schedules theo schedule ID: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Map ResultSet thành CourseOfferingSchedule object (chỉ các cột cơ bản)
     */
    private CourseOfferingSchedule mapResultSetToEntity(ResultSet rs) throws SQLException {
        CourseOfferingSchedule entity = new CourseOfferingSchedule();
        entity.setCourseOfferingScheduleId(String.valueOf(rs.getInt("id")));
        entity.setCourseOfferingId(rs.getString("course_offering_id"));
        entity.setScheduleId(rs.getString("schedule_id"));
        
        // Map start_date và end_date nếu có
        Date startDate = rs.getDate("start_date");
        if (startDate != null) {
            entity.setStartDate(startDate.toLocalDate());
        }
        
        Date endDate = rs.getDate("end_date");
        if (endDate != null) {
            entity.setEndDate(endDate.toLocalDate());
        }
        
        return entity;
    }
    
    /**
     * Map ResultSet thành CourseOfferingSchedule object với đầy đủ Schedule object
     * Dùng cho query JOIN có cả thông tin schedule
     */
    private CourseOfferingSchedule mapResultSetToEntityWithSchedule(ResultSet rs) throws SQLException {
        CourseOfferingSchedule entity = new CourseOfferingSchedule();
        entity.setCourseOfferingScheduleId(String.valueOf(rs.getInt("id")));
        entity.setCourseOfferingId(rs.getString("course_offering_id"));
        entity.setScheduleId(rs.getString("schedule_id"));
        
        // Map dates
        Date startDate = rs.getDate("start_date");
        if (startDate != null) {
            entity.setStartDate(startDate.toLocalDate());
        }
        
        Date endDate = rs.getDate("end_date");
        if (endDate != null) {
            entity.setEndDate(endDate.toLocalDate());
        }
        
        // Map Schedule object
        Schedule schedule = new Schedule();
        schedule.setScheduleId(rs.getString("schedule_id"));
        schedule.setDayOfWeek(rs.getInt("day_of_week"));
        
        Time startTime = rs.getTime("start_time");
        if (startTime != null) {
            schedule.setStartTime(startTime.toLocalTime());
        }
        
        Time endTime = rs.getTime("end_time");
        if (endTime != null) {
            schedule.setEndTime(endTime.toLocalTime());
        }
        
        // Note: CourseOfferingSchedule only stores scheduleId, not the full Schedule object
        // If you need the full schedule details, use findSchedulesByCourseOfferingId() instead
        
        return entity;
    }
    
    /**
     * Map ResultSet thành Schedule object
     */
    private Schedule mapResultSetToSchedule(ResultSet rs) throws SQLException {
        Schedule schedule = new Schedule();
        schedule.setScheduleId(rs.getString("schedule_id"));
        schedule.setDayOfWeek(rs.getInt("day_of_week"));
        
        Time startTime = rs.getTime("start_time");
        if (startTime != null) {
            schedule.setStartTime(startTime.toLocalTime());
        }
        
        Time endTime = rs.getTime("end_time");
        if (endTime != null) {
            schedule.setEndTime(endTime.toLocalTime());
        }
        
        return schedule;
    }
}
