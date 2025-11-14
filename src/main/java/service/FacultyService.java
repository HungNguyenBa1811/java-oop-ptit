package main.java.service;

import java.util.List;
import main.java.model.Faculty;

/**
 * FacultyService - Interface cho business logic của Faculty
 */
public interface FacultyService {
    
    /**
     * Tạo khoa mới
     * @param faculty Faculty object cần tạo
     * @return true nếu tạo thành công
     */
    boolean createFaculty(Faculty faculty);
    
    /**
     * Lấy tất cả khoa
     * @return List<Faculty> danh sách các khoa
     */
    List<Faculty> getAllFaculties();
    
    /**
     * Lấy khoa theo ID
     * @param facultyId Faculty ID cần tìm
     * @return Faculty nếu tìm thấy, null nếu không
     */
    Faculty getFacultyById(String facultyId);
    
    /**
     * Cập nhật thông tin khoa
     * @param faculty Faculty object cần cập nhật
     * @return true nếu cập nhật thành công
     */
    boolean updateFaculty(Faculty faculty);
    
    /**
     * Xóa khoa
     * @param facultyId Faculty ID cần xóa
     * @return true nếu xóa thành công
     */
    boolean deleteFaculty(String facultyId);
    
    /**
     * Kiểm tra faculty ID đã tồn tại chưa
     * @param facultyId Faculty ID cần kiểm tra
     * @return true nếu đã tồn tại
     */
    boolean existsById(String facultyId);
}
