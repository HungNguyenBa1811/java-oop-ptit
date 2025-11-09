package main.java.test;

import java.time.LocalTime;
import java.util.List;
import main.java.model.Schedule;
import main.java.service.ScheduleService;
import main.java.service.impl.ScheduleServiceImpl;

/**
 * Test getFullSchedule() method
 */
public class ScheduleFormatTest {
    
    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("TEST: getFullSchedule() Method");
        System.out.println("========================================\n");
        
        // Test 1: Tạo schedule thủ công
        System.out.println(">>> TEST 1: Schedule thủ công");
        Schedule schedule1 = new Schedule();
        schedule1.setScheduleId("SCH001");
        schedule1.setDayOfWeek(2); // Thứ 2
        schedule1.setStartTime(LocalTime.of(7, 30));
        schedule1.setEndTime(LocalTime.of(9, 30));
        
        System.out.println("Schedule ID: " + schedule1.getScheduleId());
        System.out.println("Full Schedule: " + schedule1.getFullSchedule());
        System.out.println();
        
        // Test 2: Schedule khác
        System.out.println(">>> TEST 2: Schedule khác");
        Schedule schedule2 = new Schedule();
        schedule2.setScheduleId("SCH002");
        schedule2.setDayOfWeek(6); // Thứ 6
        schedule2.setStartTime(LocalTime.of(13, 0));
        schedule2.setEndTime(LocalTime.of(15, 30));
        
        System.out.println("Schedule ID: " + schedule2.getScheduleId());
        System.out.println("Full Schedule: " + schedule2.getFullSchedule());
        System.out.println();
        
        // Test 3: Chủ nhật
        System.out.println(">>> TEST 3: Schedule Chủ nhật");
        Schedule schedule3 = new Schedule();
        schedule3.setScheduleId("SCH003");
        schedule3.setDayOfWeek(8); // Chủ nhật
        schedule3.setStartTime(LocalTime.of(8, 0));
        schedule3.setEndTime(LocalTime.of(11, 0));
        
        System.out.println("Schedule ID: " + schedule3.getScheduleId());
        System.out.println("Full Schedule: " + schedule3.getFullSchedule());
        System.out.println();
        
        // Test 4: Lấy từ database và hiển thị
        System.out.println(">>> TEST 4: Lấy từ database (Course Offering: CNTT_1_241)");
        try {
            ScheduleService scheduleService = new ScheduleServiceImpl();
            List<Schedule> schedules = scheduleService.getSchedulesByCourseOfferingId("CNTT_1_241");
            
            if (schedules != null && !schedules.isEmpty()) {
                System.out.println("✅ Tìm thấy " + schedules.size() + " lịch học:");
                for (Schedule schedule : schedules) {
                    System.out.println("   • " + schedule.getFullSchedule());
                }
            } else {
                System.out.println("⚠️  Không tìm thấy lịch học");
            }
        } catch (Exception e) {
            System.err.println("❌ Lỗi: " + e.getMessage());
        }
        
        System.out.println("\n========================================");
        System.out.println("✅ TEST HOÀN THÀNH!");
        System.out.println("========================================");
    }
}
