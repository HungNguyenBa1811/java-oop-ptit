package main.java.test;

import main.java.model.Admin;
import main.java.model.Course;
import main.java.model.User;
import main.java.service.AdminService;
import main.java.service.AuthService;
import main.java.service.impl.AdminServiceImpl;
import main.java.service.impl.AuthServiceImpl;

/**
 * AdminLoginTest - Test đăng nhập admin và tạo môn học
 */
public class AdminLoginTest {
    
    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("TEST: Đăng nhập Admin và Tạo môn học");
        System.out.println("========================================\n");
        
        // 1. Khởi tạo services
        AuthService authService = new AuthServiceImpl();
        AdminService adminService = new AdminServiceImpl();
        
        try {
            // 2. Đăng nhập với admin/admin123
            System.out.println(">>> BƯỚC 1: Đăng nhập");
            System.out.println("Username: admin");
            System.out.println("Password: admin123");
            
            User loggedInUser = authService.login("admin", "admin123");
            
            if (loggedInUser == null) {
                System.out.println("❌ Đăng nhập thất bại!");
                System.out.println("Lỗi: Username hoặc password không đúng");
                return;
            }
            
            System.out.println("✅ Đăng nhập thành công!");
            System.out.println("   User ID: " + loggedInUser.getUserId());
            System.out.println("   Username: " + loggedInUser.getUsername());
            System.out.println("   Full Name: " + loggedInUser.getFullName());
            System.out.println("   Email: " + loggedInUser.getEmail());
            System.out.println("   Role: " + (loggedInUser.getRole() == 1 ? "Admin" : "Student"));
            System.out.println();
            
            // 3. Kiểm tra quyền admin
            System.out.println(">>> BƯỚC 2: Kiểm tra quyền Admin");
            if (!authService.isAdmin()) {
                System.out.println("❌ User này không phải admin!");
                System.out.println("Chỉ admin mới có quyền tạo môn học");
                return;
            }
            System.out.println("✅ User có quyền Admin");
            System.out.println();
            
            // 4. Lấy thông tin admin
            Admin currentAdmin = authService.getCurrentAdmin();
            if (currentAdmin != null) {
                System.out.println(">>> Thông tin Admin hiện tại:");
                System.out.println("   Admin ID: " + currentAdmin.getUserId());
                System.out.println("   Tên: " + currentAdmin.getFullName());
                System.out.println();
            }
            
            // 5. Tạo môn học mới
            System.out.println(">>> BƯỚC 3: Tạo môn học mới");
            
            // Tạo object Course với constructor
            String courseId = "TEST001";
            String courseName = "Lập trình Java Nâng cao";
            int credits = 4;
            String description = "Môn học về lập trình Java nâng cao, bao gồm: OOP, Collections, Threads, Database";
            
            Course newCourse = new Course(courseId, courseName, credits, description);
            
            System.out.println("Thông tin môn học:");
            System.out.println("   Course ID: " + newCourse.getCourseId());
            System.out.println("   Tên môn học: " + newCourse.getCourseName());
            System.out.println("   Số tín chỉ: " + newCourse.getCredits());
            System.out.println("   Mô tả: " + newCourse.getDescription());
            System.out.println("   Khoa: FAC001 (Khoa Công nghệ Thông tin)");
            System.out.println();
            
            // Admin tạo môn học
            System.out.println("Đang tạo môn học...");
            Course createdCourse = adminService.createCourse(newCourse, "FAC001");
            
            if (createdCourse != null) {
                System.out.println("✅ Tạo môn học thành công!");
                System.out.println("   Môn học đã được thêm vào hệ thống");
                System.out.println();
            } else {
                System.out.println("❌ Tạo môn học thất bại!");
                return;
            }
            
            // 6. Xác nhận lại bằng cách lấy môn học vừa tạo
            System.out.println(">>> BƯỚC 4: Xác nhận môn học đã được tạo");
            Course verifyiedCourse = adminService.getAllCourses().stream()
                    .filter(c -> c.getCourseId().equals("TEST001"))
                    .findFirst()
                    .orElse(null);
            
            if (verifyiedCourse != null) {
                System.out.println("✅ Xác nhận thành công!");
                System.out.println("   Course ID: " + verifyiedCourse.getCourseId());
                System.out.println("   Tên: " + verifyiedCourse.getCourseName());
                System.out.println("   Tín chỉ: " + verifyiedCourse.getCredits());
                System.out.println();
            } else {
                System.out.println("⚠️  Không tìm thấy môn học vừa tạo trong danh sách");
                System.out.println();
            }
            
            // 7. Hiển thị tất cả môn học
            System.out.println(">>> BƯỚC 5: Danh sách tất cả môn học");
            var allCourses = adminService.getAllCourses();
            System.out.println("Tổng số môn học trong hệ thống: " + allCourses.size());
            System.out.println();
            
            if (!allCourses.isEmpty()) {
                System.out.println("Danh sách 5 môn học đầu tiên:");
                allCourses.stream()
                        .limit(5)
                        .forEach(course -> {
                            System.out.println("  - " + course.getCourseId() + 
                                             " | " + course.getCourseName() + 
                                             " | " + course.getCredits() + " tín chỉ");
                        });
                System.out.println();
            }
            
            // 8. Đăng xuất
            System.out.println(">>> BƯỚC 6: Đăng xuất");
            boolean loggedOut = authService.logout(loggedInUser.getUserId());
            if (loggedOut) {
                System.out.println("✅ Đăng xuất thành công");
            }
            
            System.out.println();
            System.out.println("========================================");
            System.out.println("✅ TEST HOÀN THÀNH THÀNH CÔNG!");
            System.out.println("========================================");
            
        } catch (IllegalArgumentException e) {
            System.out.println("\n❌ LỖI: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("\n❌ LỖI KHÔNG MONG MUỐN:");
            e.printStackTrace();
        }
    }
}
