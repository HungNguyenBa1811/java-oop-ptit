package main.java.service.impl;

import java.util.List;
import main.java.model.Student;
import main.java.model.User;
import main.java.repository.MajorRepository;
import main.java.repository.StudentRepository;
import main.java.repository.UserRepository;
import main.java.service.StudentService;

/**
 * StudentServiceImpl - Implementation của StudentService
 */
public class StudentServiceImpl implements StudentService {
    
    private final StudentRepository studentRepository;
    private final UserRepository userRepository;
    private final MajorRepository majorRepository;
    
    public StudentServiceImpl() {
        this.studentRepository = new StudentRepository();
        this.userRepository = new UserRepository();
        this.majorRepository = new MajorRepository();
    }
    
    public StudentServiceImpl(StudentRepository studentRepository, UserRepository userRepository, MajorRepository majorRepository) {
        this.studentRepository = studentRepository;
        this.userRepository = userRepository;
        this.majorRepository = majorRepository;
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
    public List<Student> getStudentsByMajor(String majorId) {
        if (majorId == null || majorId.trim().isEmpty()) {
            throw new IllegalArgumentException("Major ID không được để trống");
        }
        
        // Kiểm tra major tồn tại
        if (!majorRepository.existsById(majorId)) {
            throw new IllegalArgumentException("Ngành không tồn tại: " + majorId);
        }
        
        return studentRepository.findByMajor(majorId);
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
        
        // Kiểm tra major tồn tại (nếu thay đổi)
        if (!student.getMajorId().equals(existingStudent.getMajorId())) {
            if (!majorRepository.existsById(student.getMajorId())) {
                throw new IllegalArgumentException("Ngành không tồn tại: " + student.getMajorId());
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
        
        if (student.getMajorId() == null || student.getMajorId().trim().isEmpty()) {
            throw new IllegalArgumentException("Major ID không được để trống");
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
