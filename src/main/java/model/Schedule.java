package main.java.model;

import java.time.LocalTime;

/**
 * Schedule entity - Lịch học cố định
 */
public class Schedule {
    private String scheduleId;
    private int dayOfWeek; // MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY
    private LocalTime startTime;
    private LocalTime endTime;

    // Constructor mặc định
    public Schedule() {
    }

    // Constructor đầy đủ
    public Schedule(String scheduleId, int dayOfWeek, LocalTime startTime, LocalTime endTime, String roomId) {
        this.scheduleId = scheduleId;
        this.dayOfWeek = dayOfWeek;
        this.startTime = startTime;
        this.endTime = endTime;
    }


    

    @Override
    public String toString() {
        return "Schedule{" +
                "scheduleId='" + scheduleId + '\'' +
                ", dayOfWeek='" + dayOfWeek + '\'' +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                '}';
    }

    public String getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(String scheduleId) {
        this.scheduleId = scheduleId;
    }

    public int getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(int dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    /**
     * Lấy thông tin lịch học đầy đủ dưới dạng String
     * Format: "Thứ X: HH:mm - HH:mm"
     * @return String mô tả lịch học đầy đủ
     */
    public String getFullSchedule() {
        String dayName = getDayName(this.dayOfWeek);
        String startTimeStr = this.startTime != null ? this.startTime.toString() : "??:??";
        String endTimeStr = this.endTime != null ? this.endTime.toString() : "??:??";
        
        return dayName + ": " + startTimeStr + " - " + endTimeStr;
    }
    
    private String getDayName(int dayOfWeek) {
        switch (dayOfWeek) {
            case 2: return "Thứ 2";
            case 3: return "Thứ 3";
            case 4: return "Thứ 4";
            case 5: return "Thứ 5";
            case 6: return "Thứ 6";
            case 7: return "Thứ 7";
            case 8: return "Chủ nhật";
            default: return "Không xác định";
        }
    }
    
}
