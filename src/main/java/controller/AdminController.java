package main.java.controller;

/**
 * AdminController - Xử lý các request liên quan đến Admin
 * Chịu trách nhiệm: Course offering management, registration management
 */
public class AdminController {

    /**
     * Tạo lớp học phần mới
     */
    public void createCourseOffering(String courseId, String semesterId, String instructor,
                                     int maxCapacity, String startDate, String endDate) {
        // TODO: Implement create course offering
    }

    /**
     * Cập nhật lớp học phần
     */
    public void updateCourseOffering(String courseOfferingId, String instructor, int maxCapacity, String status) {
        // TODO: Implement update course offering
    }

    /**
     * Xóa lớp học phần
     */
    public void deleteCourseOffering(String courseOfferingId) {
        // TODO: Implement delete course offering
    }

    /**
     * Xem tất cả đăng ký
     */
    public void getAllRegistrations() {
        // TODO: Implement get all registrations
    }

    /**
     * Xem đăng ký theo lớp học phần
     */
    public void getRegistrationsByCourseOffering(String courseOfferingId) {
        // TODO: Implement get registrations by course offering
    }

    /**
     * Xem đăng ký theo sinh viên
     */
    public void getRegistrationsByStudent(String studentId) {
        // TODO: Implement get registrations by student
    }

    /**
     * Cập nhật thông tin đăng ký
     */
    public void updateRegistration(String registrationId, String status, String grade, String note) {
        // TODO: Implement update registration
    }

    /**
     * Xóa đăng ký (hủy đăng ký cho sinh viên)
     */
    public void deleteRegistration(String registrationId) {
        // TODO: Implement delete registration
    }

    /**
     * Kiểm tra sĩ số lớp học phần
     */
    public void checkCourseOfferingCapacity(String courseOfferingId) {
        // TODO: Implement check capacity
    }

    /**
     * Đăng ký môn học cho sinh viên (admin thao tác)
     */
    public void registerCourseForStudent(String studentId, String courseOfferingId) {
        // TODO: Implement register course for student
    }

    /**
     * Xem lịch học của lớp học phần
     */
    public void getCourseOfferingSchedule(String courseOfferingId) {
        // TODO: Implement get course offering schedule
    }

    /**
     * Thêm lịch học cho lớp học phần
     */
    public void addScheduleToCourseOffering(String courseOfferingId, String scheduleId) {
        // TODO: Implement add schedule to course offering
    }
}
