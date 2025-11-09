package main.java.repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import main.java.config.DatabaseConnection;
import main.java.model.Schedule;

/**
 * ScheduleRepository - Quản lý CRUD cho bảng schedules (Lịch học)
 */
public class ScheduleRepository {
    
    private static final String INSERT_SCHEDULE =
        "INSERT INTO schedules (schedule_id, day_of_week, start_time, end_time) " +
        "VALUES (?, ?, ?, ?)";

    private static final String SELECT_ALL_SCHEDULES =
        "SELECT * FROM schedules ORDER BY day_of_week, start_time";
    
    private static final String SELECT_SCHEDULE_BY_ID =
        "SELECT * FROM schedules WHERE schedule_id = ?";
    
    private static final String SELECT_SCHEDULES_BY_DAY =
        "SELECT * FROM schedules WHERE day_of_week = ? ORDER BY start_time";
    
    private static final String SELECT_SCHEDULES_BY_TIME_RANGE =
        "SELECT * FROM schedules WHERE start_time >= ? AND end_time <= ? ORDER BY day_of_week, start_time";
    
    private static final String UPDATE_SCHEDULE =
        "UPDATE schedules SET day_of_week = ?, start_time = ?, end_time = ? WHERE schedule_id = ?";
    
    private static final String DELETE_SCHEDULE =
        "DELETE FROM schedules WHERE schedule_id = ?";
    
    private static final String SELECT_SCHEDULES_BY_COURSE_OFFERING =
        "SELECT s.* FROM schedules s " +
        "INNER JOIN course_offerings_schedules cos ON s.schedule_id = cos.schedule_id " +
        "WHERE cos.course_offering_id = ? " +
        "ORDER BY s.day_of_week, s.start_time";

    /**
     * Tạo lịch học mới
     * @param schedule Schedule object cần tạo
     * @return true nếu tạo thành công
     */
    public boolean createSchedule(Schedule schedule) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(INSERT_SCHEDULE)) {

            stmt.setString(1, schedule.getScheduleId());
            stmt.setInt(2, schedule.getDayOfWeek());
            stmt.setTime(3, Time.valueOf(schedule.getStartTime()));
            stmt.setTime(4, Time.valueOf(schedule.getEndTime()));

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                System.out.println("Schedule created: " + schedule.getScheduleId());
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi tạo schedule: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Lấy tất cả lịch học
     * @return List danh sách schedules
     */
    public List<Schedule> findAll() {
        List<Schedule> schedules = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_ALL_SCHEDULES);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                schedules.add(mapResultSetToSchedule(rs));
            }
            System.out.println("Found " + schedules.size() + " schedules");
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy danh sách schedule: " + e.getMessage());
            e.printStackTrace();
        }
        return schedules;
    }

    /**
     * Tìm lịch học theo schedule ID
     * @param scheduleId Schedule ID cần tìm
     * @return Schedule nếu tìm thấy, null nếu không
     */
    public Schedule findById(String scheduleId) {
        try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(SELECT_SCHEDULE_BY_ID)) {
            
            stmt.setString(1, scheduleId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                System.out.println("Schedule found: " + scheduleId);
                return mapResultSetToSchedule(rs);
            } else {
                System.out.println("Schedule not found: " + scheduleId);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi tìm schedule by ID: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Tìm lịch học theo ngày trong tuần
     * @param dayOfWeek Ngày trong tuần (2: T2, 3: T3, ..., 8: CN)
     * @return List danh sách schedules trong ngày đó
     */
    public List<Schedule> findByDay(int dayOfWeek) {
        List<Schedule> schedules = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_SCHEDULES_BY_DAY)) {
            
            stmt.setInt(1, dayOfWeek);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                schedules.add(mapResultSetToSchedule(rs));
            }
            System.out.println("Found " + schedules.size() + " schedules on day: " + dayOfWeek);
        } catch (SQLException e) {
            System.err.println("Lỗi khi tìm schedule by day: " + e.getMessage());
            e.printStackTrace();
        }
        return schedules;
    }

    /**
     * Tìm lịch học trong khoảng thời gian
     * @param startTime Thời gian bắt đầu
     * @param endTime Thời gian kết thúc
     * @return List danh sách schedules trong khoảng thời gian đó
     */
    public List<Schedule> findByTimeRange(java.time.LocalTime startTime, java.time.LocalTime endTime) {
        List<Schedule> schedules = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_SCHEDULES_BY_TIME_RANGE)) {
            
            stmt.setTime(1, Time.valueOf(startTime));
            stmt.setTime(2, Time.valueOf(endTime));
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                schedules.add(mapResultSetToSchedule(rs));
            }
            System.out.println("Found " + schedules.size() + " schedules in time range: " + startTime + " - " + endTime);
        } catch (SQLException e) {
            System.err.println("Lỗi khi tìm schedule by time range: " + e.getMessage());
            e.printStackTrace();
        }
        return schedules;
    }

    /**
     * Cập nhật thông tin lịch học
     * @param schedule Schedule object với thông tin mới
     * @return true nếu update thành công
     */
    public boolean updateSchedule(Schedule schedule) {
        try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(UPDATE_SCHEDULE)) {
            
            stmt.setInt(1, schedule.getDayOfWeek());
            stmt.setTime(2, Time.valueOf(schedule.getStartTime()));
            stmt.setTime(3, Time.valueOf(schedule.getEndTime()));
            stmt.setString(4, schedule.getScheduleId());
            
            int rows = stmt.executeUpdate();
            if (rows > 0) {
                System.out.println("Schedule updated: " + schedule.getScheduleId());
                return true;
            } else {
                System.out.println("No schedule found to update: " + schedule.getScheduleId());
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi update schedule: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Xóa lịch học
     * @param scheduleId ID của lịch học cần xóa
     * @return true nếu xóa thành công
     */
    public boolean deleteSchedule(String scheduleId) {
        try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(DELETE_SCHEDULE)) {
            
            stmt.setString(1, scheduleId);
            int rows = stmt.executeUpdate();
            
            if (rows > 0) {
                System.out.println("Schedule deleted: " + scheduleId);
                return true;
            } else {
                System.out.println("No schedule found to delete: " + scheduleId);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi xóa schedule: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Kiểm tra schedule ID đã tồn tại chưa
     * @param scheduleId Schedule ID cần kiểm tra
     * @return true nếu đã tồn tại
     */
    public boolean existsById(String scheduleId) {
        Schedule schedule = findById(scheduleId);
        return schedule != null;
    }
    
    /**
     * Lấy danh sách lịch học theo course offering ID
     * @param courseOfferingId ID của course offering
     * @return List<Schedule> danh sách lịch học
     */
    public List<Schedule> findByCourseOfferingId(String courseOfferingId) {
        List<Schedule> schedules = new ArrayList<>();
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_SCHEDULES_BY_COURSE_OFFERING)) {
            
            stmt.setString(1, courseOfferingId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                schedules.add(mapResultSetToSchedule(rs));
            }
            
            return schedules;
            
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy schedules theo course offering: " + e.getMessage());
            e.printStackTrace();
            return schedules;
        }
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
