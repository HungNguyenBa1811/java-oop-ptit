package main.java.service;

import java.util.List;
import main.java.model.Student;

/**
 * StudentService Interface - Business logic cho Student
 */
public interface StudentService {
    
    /**
     * Lấy sinh viên theo ID
     * @param studentId Student ID
     * @return Student nếu tìm thấy
     */
    Student getStudentById(String studentId);
    
    /**
     * Lấy sinh viên theo User ID
     * @param userId User ID
     * @return Student nếu tìm thấy
     */
    Student getStudentByUserId(String userId);
    
    /**
     * Lấy tất cả sinh viên
     * @return List danh sách students
     */
    List<Student> getAllStudents();
    
    /**
     * Lấy sinh viên theo khoa
     * @param facultyId Faculty ID
     * @return List danh sách students
     */
    List<Student> getStudentsByFaculty(String facultyId);
    
    /**
     * Lấy sinh viên theo lớp
     * @param studentClass Tên lớp
     * @return List danh sách students
     */
    List<Student> getStudentsByClass(String studentClass);
    
    /**
     * Cập nhật thông tin sinh viên
     * @param student Student với thông tin mới
     * @return true nếu thành công
     */
    boolean updateStudent(Student student);
    
    /**
     * Validate thông tin sinh viên
     * @param student Student cần validate
     * @return true nếu hợp lệ
     */
    boolean validateStudent(Student student) throws IllegalArgumentException;
}
