package model;

import java.time.LocalTime;

/**
 * Schedule entity - Lịch học cố định
 */
public class Schedule {
    private String scheduleId;
    private String dayOfWeek; // MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY
    private LocalTime startTime;
    private LocalTime endTime;
    private String roomId;

    // Constructor mặc định
    public Schedule() {
    }

    // Constructor đầy đủ
    public Schedule(String scheduleId, String dayOfWeek, LocalTime startTime, LocalTime endTime, String roomId) {
        this.scheduleId = scheduleId;
        this.dayOfWeek = dayOfWeek;
        this.startTime = startTime;
        this.endTime = endTime;
        this.roomId = roomId;
    }

    // Getters
    public String getScheduleId() {
        return scheduleId;
    }

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public String getRoomId() {
        return roomId;
    }

    // Helper method: Check trùng lịch
    public boolean isConflictWith(Schedule other) {
        if (!this.dayOfWeek.equals(other.dayOfWeek)) {
            return false; // Khác ngày -> không trùng
        }
        // Kiểm tra trùng giờ
        return !(this.endTime.isBefore(other.startTime) || this.startTime.isAfter(other.endTime));
    }

    @Override
    public String toString() {
        return "Schedule{" +
                "scheduleId='" + scheduleId + '\'' +
                ", dayOfWeek='" + dayOfWeek + '\'' +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", roomId='" + roomId + '\'' +
                '}';
    }
}
