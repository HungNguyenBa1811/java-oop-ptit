package controller;

/**
 * CourseController - Xử lý các request liên quan đến Course
 * Chịu trách nhiệm: Course management (CRUD)
 */
public class CourseController {

    /**
     * Tạo môn học mới
     */
    public void createCourse(String courseId, String courseName, int credits, String description) {
        // TODO: Implement create course
    }

    /**
     * Cập nhật thông tin môn học
     */
    public void updateCourse(String courseId, String courseName, int credits, String description) {
        // TODO: Implement update course
    }

    /**
     * Xóa môn học
     */
    public void deleteCourse(String courseId) {
        // TODO: Implement delete course
    }

    /**
     * Lấy thông tin môn học
     */
    public void getCourseInfo(String courseId) {
        // TODO: Implement get course info
    }

    /**
     * Lấy danh sách tất cả môn học
     */
    public void getAllCourses() {
        // TODO: Implement get all courses
    }

    /**
     * Tìm kiếm môn học
     */
    public void searchCourses(String keyword) {
        // TODO: Implement search courses by name or id
    }

    /**
     * Lấy môn học theo số tín chỉ
     */
    public void getCoursesByCredits(int credits) {
        // TODO: Implement get courses by credits
    }
}
