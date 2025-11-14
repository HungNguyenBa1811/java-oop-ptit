package main.java.service;

import java.time.LocalDate;
import java.util.List;
import main.java.model.Semester;

/**
 * SemesterService - Interface cho business logic của Semester
 */
public interface SemesterService {
    
    /**
     * Tạo học kỳ mới
     * @param semester Semester object cần tạo
     * @param term Tên học kỳ (Fall, Spring, Summer)
     * @param academicYear Năm học (VD: 2025-2026)
     * @return true nếu tạo thành công
     */
    boolean createSemester(Semester semester, String term, String academicYear);
    
    /**
     * Lấy tất cả học kỳ
     * @return List<Semester> danh sách các học kỳ
     */
    List<Semester> getAllSemesters();
    
    /**
     * Lấy học kỳ theo ID
     * @param semesterId Semester ID cần tìm
     * @return Semester nếu tìm thấy, null nếu không
     */
    Semester getSemesterById(String semesterId);
    
    /**
     * Lấy danh sách học kỳ theo năm học
     * @param academicYear Năm học (VD: 2025-2026)
     * @return List<Semester> danh sách học kỳ trong năm đó
     */
    List<Semester> getSemestersByYear(String academicYear);
    
    /**
     * Lấy học kỳ hiện tại
     * @return Semester đang diễn ra, null nếu không có
     */
    Semester getCurrentSemester();
    
    /**
     * Cập nhật thông tin học kỳ
     * @param semester Semester object cần cập nhật
     * @param term Tên học kỳ mới
     * @param academicYear Năm học mới
     * @return true nếu cập nhật thành công
     */
    boolean updateSemester(Semester semester, String term, String academicYear);
    
    /**
     * Xóa học kỳ
     * @param semesterId Semester ID cần xóa
     * @return true nếu xóa thành công
     */
    boolean deleteSemester(String semesterId);
    
    /**
     * Kiểm tra semester ID đã tồn tại chưa
     * @param semesterId Semester ID cần kiểm tra
     * @return true nếu đã tồn tại
     */
    boolean existsById(String semesterId);
    
    /**
     * Validate khoảng thời gian học kỳ
     * @param startDate Ngày bắt đầu
     * @param endDate Ngày kết thúc
     * @return true nếu hợp lệ
     */
    boolean isValidDateRange(LocalDate startDate, LocalDate endDate);
}
