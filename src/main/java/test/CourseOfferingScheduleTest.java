package main.java.test;

import java.util.List;
import main.java.model.CourseOfferingSchedule;
import main.java.model.Schedule;
import main.java.repository.CourseOfferingScheduleRepository;
import main.java.repository.ScheduleRepository;

/**
 * Test lấy course offering schedules từ database
 */
public class CourseOfferingScheduleTest {
    
    public static void main(String[] args) {
        System.out.println("=== Test CourseOfferingSchedule Repository ===\n");
        
        CourseOfferingScheduleRepository cosRepository = new CourseOfferingScheduleRepository();
        ScheduleRepository scheduleRepository = new ScheduleRepository();
        
        // Test: Lấy danh sách course offering schedules
        System.out.println("Test: findByCourseOfferingId");
        List<CourseOfferingSchedule> schedules = cosRepository.findByCourseOfferingId("OFFER001");
        
        if (schedules.isEmpty()) {
            System.out.println("Không tìm thấy dữ liệu cho OFFER001");
        } else {
            System.out.println("Tìm thấy " + schedules.size() + " lịch học cho OFFER001:\n");
            
            for (CourseOfferingSchedule cos : schedules) {
                System.out.println("- Schedule ID: " + cos.getScheduleId());
                System.out.println("  Start Date: " + cos.getStartDate());
                System.out.println("  End Date: " + cos.getEndDate());
                
                // Query schedule riêng khi cần thông tin chi tiết
                Schedule schedule = scheduleRepository.findById(cos.getScheduleId());
                
                if (schedule != null) {
                    System.out.println("  Schedule Detail: " + schedule.getFullSchedule());
                } else {
                    System.out.println("  Schedule Detail: Không tìm thấy");
                }
                
                System.out.println();
            }
        }
        
        System.out.println("\n=== Test Complete ===");
    }
}
