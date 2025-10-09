package controller;

/**
 * CourseOfferingController - Xử lý các request liên quan đến CourseOffering
 * Chịu trách nhiệm: Course offering operations
 */
public class CourseOfferingController {

    /**
     * Lấy thông tin lớp học phần
     */
    public void getCourseOfferingInfo(String courseOfferingId) {
        // TODO: Implement get course offering info
    }

    /**
     * Lấy danh sách lớp học phần theo học kỳ
     */
    public void getCourseOfferingsBySemester(String semesterId) {
        // TODO: Implement get course offerings by semester
    }

    /**
     * Lấy danh sách lớp học phần theo môn học
     */
    public void getCourseOfferingsByCourse(String courseId) {
        // TODO: Implement get course offerings by course
    }

    /**
     * Lấy danh sách lớp học phần theo giảng viên
     */
    public void getCourseOfferingsByInstructor(String instructor) {
        // TODO: Implement get course offerings by instructor
    }

    /**
     * Lấy danh sách lớp học phần còn chỗ trống
     */
    public void getAvailableCourseOfferings() {
        // TODO: Implement get available course offerings
    }

    /**
     * Cập nhật số lượng đăng ký hiện tại
     */
    public void updateCurrentEnrollment(String courseOfferingId, int newEnrollment) {
        // TODO: Implement update current enrollment
    }

    /**
     * Cập nhật trạng thái lớp học phần
     */
    public void updateCourseOfferingStatus(String courseOfferingId, String status) {
        // TODO: Implement update status
    }
}
