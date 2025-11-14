package main.java.service;

import java.util.List;
import main.java.model.Major;

/**
 * MajorService - Interface cho business logic của Major
 */
public interface MajorService {
    
    /**
     * Tạo ngành học mới
     * @param major Major object cần tạo
     * @return true nếu tạo thành công
     */
    boolean createMajor(Major major);
    
    /**
     * Lấy tất cả ngành học
     * @return List<Major> danh sách các ngành học
     */
    List<Major> getAllMajors();
    
    /**
     * Lấy ngành học theo ID
     * @param majorId Major ID cần tìm
     * @return Major nếu tìm thấy, null nếu không
     */
    Major getMajorById(String majorId);
    
    /**
     * Lấy danh sách ngành học theo khoa
     * @param facultyId Faculty ID
     * @return List<Major> danh sách ngành học của khoa
     */
    List<Major> getMajorsByFaculty(String facultyId);
    
    /**
     * Cập nhật thông tin ngành học
     * @param major Major object cần cập nhật
     * @return true nếu cập nhật thành công
     */
    boolean updateMajor(Major major);
    
    /**
     * Xóa ngành học
     * @param majorId Major ID cần xóa
     * @return true nếu xóa thành công
     */
    boolean deleteMajor(String majorId);
    
    /**
     * Kiểm tra major ID đã tồn tại chưa
     * @param majorId Major ID cần kiểm tra
     * @return true nếu đã tồn tại
     */
    boolean existsById(String majorId);
    
    /**
     * Đếm tổng số ngành học
     * @return số lượng ngành học
     */
    int countMajors();
}
