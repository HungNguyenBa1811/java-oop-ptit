package controller;

/**
 * RegistrationController - Xử lý các request liên quan đến Registration
 * Chịu trách nhiệm: Registration operations
 */
public class RegistrationController {

    /**
     * Lấy thông tin đăng ký
     */
    public void getRegistrationInfo(String registrationId) {
        // TODO: Implement get registration info
    }

    /**
     * Lấy danh sách đăng ký theo sinh viên
     */
    public void getRegistrationsByStudent(String studentId) {
        // TODO: Implement get registrations by student
    }

    /**
     * Lấy danh sách đăng ký theo lớp học phần
     */
    public void getRegistrationsByCourseOffering(String courseOfferingId) {
        // TODO: Implement get registrations by course offering
    }

    /**
     * Lấy danh sách đăng ký theo học kỳ
     */
    public void getRegistrationsBySemester(String semesterId) {
        // TODO: Implement get registrations by semester
    }

    /**
     * Cập nhật điểm cho đăng ký
     */
    public void updateGrade(String registrationId, String grade) {
        // TODO: Implement update grade
    }

    /**
     * Cập nhật trạng thái đăng ký
     */
    public void updateRegistrationStatus(String registrationId, String status) {
        // TODO: Implement update registration status
    }

    /**
     * Lấy danh sách đăng ký theo trạng thái
     */
    public void getRegistrationsByStatus(String status) {
        // TODO: Implement get registrations by status
    }
}
