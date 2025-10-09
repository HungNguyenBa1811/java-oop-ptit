package main.java.controller;

/**
 * MajorController - Xử lý các request liên quan đến Major
 * Chịu trách nhiệm: Major management (CRUD)
 */
public class MajorController {

    /**
     * Tạo ngành học mới
     */
    public void createMajor(String majorId, String majorName, String facultyId, int totalCredits) {
        // TODO: Implement create major
    }

    /**
     * Cập nhật thông tin ngành học
     */
    public void updateMajor(String majorId, String majorName, String facultyId, int totalCredits) {
        // TODO: Implement update major
    }

    /**
     * Xóa ngành học
     */
    public void deleteMajor(String majorId) {
        // TODO: Implement delete major
    }

    /**
     * Lấy thông tin ngành học
     */
    public void getMajorInfo(String majorId) {
        // TODO: Implement get major info
    }

    /**
     * Lấy danh sách tất cả ngành học
     */
    public void getAllMajors() {
        // TODO: Implement get all majors
    }

    /**
     * Lấy danh sách ngành học theo khoa
     */
    public void getMajorsByFaculty(String facultyId) {
        // TODO: Implement get majors by faculty
    }

    /**
     * Lấy danh sách sinh viên theo ngành
     */
    public void getStudentsByMajor(String majorId) {
        // TODO: Implement get students by major
    }
}
