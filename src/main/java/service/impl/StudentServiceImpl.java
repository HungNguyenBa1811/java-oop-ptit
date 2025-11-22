package main.java.service.impl;

import java.util.List;
import main.java.model.Student;
import main.java.model.User;
import main.java.repository.FacultyRepository;
import main.java.repository.StudentRepository;
import main.java.repository.UserRepository;
import main.java.service.StudentService;

/**
 * StudentServiceImpl - Implementation của StudentService
 */
public class StudentServiceImpl implements StudentService {
    
    private final StudentRepository studentRepository;
    private final UserRepository userRepository;
    private final FacultyRepository facultyRepository;
    
    public StudentServiceImpl() {
        this.studentRepository = new StudentRepository();
        this.userRepository = new UserRepository();
        this.facultyRepository = new FacultyRepository();
    }
    
    public StudentServiceImpl(StudentRepository studentRepository, UserRepository userRepository, FacultyRepository facultyRepository) {
        this.studentRepository = studentRepository;
        this.userRepository = userRepository;
        this.facultyRepository = facultyRepository;
    }
    @Override
    public Student getStudentById(String studentId) {
        if (studentId == null || studentId.trim().isEmpty()) {
            throw new IllegalArgumentException("Student ID không được để trống");
        }
        
        return studentRepository.findById(studentId);
    }
    
    @Override
    public Student getStudentByUserId(String userId) {
        if (userId == null || userId.trim().isEmpty()) {
            throw new IllegalArgumentException("User ID không được để trống");
        }
        
        return studentRepository.findByUserId(userId);
    }
    
    @Override
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }
    
    @Override
    public List<Student> getStudentsByFaculty(String facultyId) {
        if (facultyId == null || facultyId.trim().isEmpty()) {
            throw new IllegalArgumentException("Faculty ID không được để trống");
        }
        
        // Kiểm tra faculty tồn tại
        if (!facultyRepository.existsById(facultyId)) {
            throw new IllegalArgumentException("Khoa không tồn tại: " + facultyId);
        }
        
        return studentRepository.findByFaculty(facultyId);
    }
    
    @Override
    public List<Student> getStudentsByClass(String studentClass) {
        if (studentClass == null || studentClass.trim().isEmpty()) {
            throw new IllegalArgumentException("Lớp không được để trống");
        }
        
        return studentRepository.findByClass(studentClass);
    }
    
    @Override
    public boolean updateStudent(Student student) {
        // Validate student
        validateStudent(student);
        
        // Kiểm tra student tồn tại
        Student existingStudent = studentRepository.findById(student.getStudentId());
        if (existingStudent == null) {
            throw new IllegalArgumentException("Sinh viên không tồn tại: " + student.getStudentId());
        }
        
        // Kiểm tra faculty tồn tại (nếu thay đổi)
        if (!student.getFacultyId().equals(existingStudent.getFacultyId())) {
            if (!facultyRepository.existsById(student.getFacultyId())) {
                throw new IllegalArgumentException("Khoa không tồn tại: " + student.getFacultyId());
            }
        }
        
        // Cập nhật student
        boolean studentUpdated = studentRepository.updateStudent(student);
        
        // Cập nhật thông tin user nếu cần
        if (studentUpdated) {
            User user = userRepository.findById(student.getStudentId());
            if (user != null) {
                user.setFullName(student.getFullName());
                user.setEmail(student.getEmail());
                userRepository.updateUser(user);
            }
        }
        
        return studentUpdated;
    }
    
    @Override
    public boolean validateStudent(Student student) throws IllegalArgumentException {
        if (student == null) {
            throw new IllegalArgumentException("Student không được null");
        }
        
        if (student.getStudentId() == null || student.getStudentId().trim().isEmpty()) {
            throw new IllegalArgumentException("Student ID không được để trống");
        }
        
        if (student.getStudentClass() == null || student.getStudentClass().trim().isEmpty()) {
            throw new IllegalArgumentException("Lớp không được để trống");
        }
        
        if (student.getFacultyId() == null || student.getFacultyId().trim().isEmpty()) {
            throw new IllegalArgumentException("Faculty ID không được để trống");
        }
        
        if (student.getStatus() == null || student.getStatus().trim().isEmpty()) {
            throw new IllegalArgumentException("Trạng thái không được để trống");
        }
        
        // Validate status value
        if (!student.getStatus().equals("Đang học") && !student.getStatus().equals("Nghỉ học")) {
            throw new IllegalArgumentException("Trạng thái phải là 'Đang học' hoặc 'Nghỉ học'");
        }
        
        // Validate thông tin user (kế thừa)
        if (student.getUsername() == null || student.getUsername().trim().isEmpty()) {
            throw new IllegalArgumentException("Username không được để trống");
        }
        
        if (student.getEmail() == null || student.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("Email không được để trống");
        }
        
        if (!student.getEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new IllegalArgumentException("Email không hợp lệ");
        }
        
        if (student.getFullName() == null || student.getFullName().trim().isEmpty()) {
            throw new IllegalArgumentException("Họ tên không được để trống");
        }
        
        return true;
    }
}
