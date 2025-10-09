package model;

/**
 * CourseOfferingSchedule entity - Liên kết giữa CourseOffering và Schedule
 * Một lớp học phần có thể có nhiều lịch học (nhiều buổi trong tuần)
 */
public class CourseOfferingSchedule {
    private String courseOfferingScheduleId;
    private String courseOfferingId;
    private String scheduleId;

    // Constructor mặc định
    public CourseOfferingSchedule() {
    }

    // Constructor đầy đủ
    public CourseOfferingSchedule(String courseOfferingScheduleId, String courseOfferingId, String scheduleId) {
        this.courseOfferingScheduleId = courseOfferingScheduleId;
        this.courseOfferingId = courseOfferingId;
        this.scheduleId = scheduleId;
    }

    // Getters
    public String getCourseOfferingScheduleId() {
        return courseOfferingScheduleId;
    }

    public String getCourseOfferingId() {
        return courseOfferingId;
    }

    public String getScheduleId() {
        return scheduleId;
    }

    @Override
    public String toString() {
        return "CourseOfferingSchedule{" +
                "courseOfferingScheduleId='" + courseOfferingScheduleId + '\'' +
                ", courseOfferingId='" + courseOfferingId + '\'' +
                ", scheduleId='" + scheduleId + '\'' +
                '}';
    }
}
