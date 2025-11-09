package main.java.test;

import java.util.List;
import main.java.model.Schedule;
import main.java.service.ScheduleService;
import main.java.service.impl.ScheduleServiceImpl;

/**
 * ScheduleServiceTest - Test lấy lịch học theo course offering ID
 */
public class ScheduleServiceTest {
    
    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("TEST: Lấy lịch học theo Course Offering");
        System.out.println("========================================\n");
        
        ScheduleService scheduleService = new ScheduleServiceImpl();
        
        // Test case 1: Lấy lịch học của course offering CNTT_1_241
        System.out.println(">>> TEST 1: Lấy lịch học của CNTT_1_241");
        String courseOfferingId = "CNTT_1_241";
        
        try {
            List<Schedule> schedules = scheduleService.getSchedulesByCourseOfferingId(courseOfferingId);
            
            if (schedules != null && !schedules.isEmpty()) {
                System.out.println("✅ Tìm thấy " + schedules.size() + " lịch học:");
                for (Schedule schedule : schedules) {
                    String dayName = getDayName(schedule.getDayOfWeek());
                    System.out.println("   - " + dayName + ": " + 
                                     schedule.getStartTime() + " - " + 
                                     schedule.getEndTime() +
                                     " (ID: " + schedule.getScheduleId() + ")");
                }
            } else {
                System.out.println("⚠️  Không tìm thấy lịch học cho course offering: " + courseOfferingId);
            }
            
        } catch (Exception e) {
            System.err.println("❌ Lỗi: " + e.getMessage());
            e.printStackTrace();
        }
        
        System.out.println();
        
        // Test case 2: Lấy tất cả lịch học
        System.out.println(">>> TEST 2: Lấy tất cả lịch học trong hệ thống");
        try {
            List<Schedule> allSchedules = scheduleService.getAllSchedules();
            System.out.println("✅ Tổng số lịch học: " + allSchedules.size());
            
            // Group by day
            for (int day = 2; day <= 8; day++) {
                List<Schedule> schedulesByDay = scheduleService.getSchedulesByDay(day);
                if (!schedulesByDay.isEmpty()) {
                    System.out.println("   " + getDayName(day) + ": " + schedulesByDay.size() + " lịch");
                }
            }
            
        } catch (Exception e) {
            System.err.println("❌ Lỗi: " + e.getMessage());
        }
        
        System.out.println("\n========================================");
        System.out.println("✅ TEST HOÀN THÀNH!");
        System.out.println("========================================");
    }
    
    /**
     * Convert day of week number to Vietnamese name
     */
    private static String getDayName(int dayOfWeek) {
        switch (dayOfWeek) {
            case 2: return "Thứ 2";
            case 3: return "Thứ 3";
            case 4: return "Thứ 4";
            case 5: return "Thứ 5";
            case 6: return "Thứ 6";
            case 7: return "Thứ 7";
            case 8: return "Chủ nhật";
            default: return "Unknown";
        }
    }
}
