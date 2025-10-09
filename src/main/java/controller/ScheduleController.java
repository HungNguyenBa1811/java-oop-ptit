package main.java.controller;

/**
 * ScheduleController - Xử lý các request liên quan đến Schedule
 * Chịu trách nhiệm: Schedule management
 */
public class ScheduleController {

    /**
     * Tạo lịch học mới
     */
    public void createSchedule(String dayOfWeek, String startTime, String endTime, String roomId) {
        // TODO: Implement create schedule
    }

    /**
     * Cập nhật lịch học
     */
    public void updateSchedule(String scheduleId, String dayOfWeek, String startTime, String endTime, String roomId) {
        // TODO: Implement update schedule
    }

    /**
     * Xóa lịch học
     */
    public void deleteSchedule(String scheduleId) {
        // TODO: Implement delete schedule
    }

    /**
     * Lấy thông tin lịch học
     */
    public void getScheduleInfo(String scheduleId) {
        // TODO: Implement get schedule info
    }

    /**
     * Lấy lịch học theo phòng
     */
    public void getSchedulesByRoom(String roomId) {
        // TODO: Implement get schedules by room
    }

    /**
     * Lấy lịch học theo ngày trong tuần
     */
    public void getSchedulesByDay(String dayOfWeek) {
        // TODO: Implement get schedules by day
    }

    /**
     * Kiểm tra trùng lịch
     */
    public void checkScheduleConflict(String scheduleId1, String scheduleId2) {
        // TODO: Implement check schedule conflict
    }
}
