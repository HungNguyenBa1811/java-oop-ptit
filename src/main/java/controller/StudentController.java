package main.java.controller;

/**
 * StudentController - Xử lý các request liên quan đến Student
 * Chịu trách nhiệm: Student operations, course registration
 */
public class StudentController {

    /**
     * Đăng ký môn học
     */
    public void registerCourse(String studentId, String courseOfferingId) {
        // TODO: Implement register course logic
        // - Kiểm tra trùng môn học
        // - Kiểm tra trùng lịch
        // - Kiểm tra lớp đã đầy
        // - Tạo registration record
    }

    /**
     * Hủy đăng ký môn học
     */
    public void cancelRegistration(String registrationId) {
        // TODO: Implement cancel registration logic
    }

    /**
     * Xem danh sách môn học đã đăng ký
     */
    public void getRegisteredCourses(String studentId) {
        // TODO: Implement get registered courses
    }

    /**
     * Xem lịch học
     */
    public void getSchedule(String studentId) {
        // TODO: Implement get schedule
        // - Lấy tất cả course offerings đã đăng ký
        // - Lấy schedule cho mỗi offering
        // - Hiển thị lịch theo thứ
    }

    /**
     * Xem thông tin cá nhân
     */
    public void getStudentInfo(String studentId) {
        // TODO: Implement get student info
    }

    /**
     * Xem danh sách lớp học phần khả dụng
     */
    public void getAvailableCourseOfferings(String semesterId) {
        // TODO: Implement get available course offerings
    }

    /**
     * Kiểm tra điều kiện đăng ký môn học
     */
    public void checkRegistrationEligibility(String studentId, String courseOfferingId) {
        // TODO: Implement check eligibility
        // - Check duplicate course
        // - Check schedule conflict
        // - Check capacity
    }
}
