package main.java.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import main.java.model.CourseOffering;
import main.java.model.Registration;
import main.java.repository.CourseOfferingRepository;
import main.java.repository.RegistrationRepository;
import main.java.repository.StudentRepository;
import main.java.service.RegistrationService;

/**
 * RegistrationServiceImpl - Implementation của RegistrationService
 */
public class RegistrationServiceImpl implements RegistrationService {
    
    private final RegistrationRepository registrationRepository;
    private final StudentRepository studentRepository;
    private final CourseOfferingRepository courseOfferingRepository;
    
    public RegistrationServiceImpl() {
        this.registrationRepository = new RegistrationRepository();
        this.studentRepository = new StudentRepository();
        this.courseOfferingRepository = new CourseOfferingRepository();
    }
    
    public RegistrationServiceImpl(RegistrationRepository registrationRepository, 
                                   StudentRepository studentRepository,
                                   CourseOfferingRepository courseOfferingRepository) {
        this.registrationRepository = registrationRepository;
        this.studentRepository = studentRepository;
        this.courseOfferingRepository = courseOfferingRepository;
    }
    
    @Override
    public Registration registerCourse(String studentId, String courseOfferingId) {
        // Validate input
        if (studentId == null || studentId.trim().isEmpty()) {
            throw new IllegalArgumentException("Student ID không được để trống");
        }
        
        if (courseOfferingId == null || courseOfferingId.trim().isEmpty()) {
            throw new IllegalArgumentException("Course Offering ID không được để trống");
        }
        
        // Kiểm tra student tồn tại
        if (studentRepository.findById(studentId) == null) {
            throw new IllegalArgumentException("Sinh viên không tồn tại: " + studentId);
        }
        
        // Kiểm tra course offering tồn tại
        CourseOffering courseOffering = courseOfferingRepository.findById(courseOfferingId);
        if (courseOffering == null) {
            throw new IllegalArgumentException("Lớp mở không tồn tại: " + courseOfferingId);
        }
        
        // Kiểm tra đã đăng ký chưa
        if (registrationRepository.existsByStudentAndCourseOffering(studentId, courseOfferingId)) {
            throw new IllegalArgumentException("Sinh viên đã đăng ký lớp mở này rồi");
        }
        
        // Kiểm tra còn chỗ không
        int currentCapacity = Integer.parseInt(courseOffering.getCurrentCapacity());
        int maxCapacity = Integer.parseInt(courseOffering.getMaxCapacity());
        
        if (currentCapacity >= maxCapacity) {
            throw new IllegalArgumentException("Lớp mở đã đầy");
        }
        
        // Tạo registration
        String registrationId = generateRegistrationId();
        Registration registration = new Registration();
        registration.setRegistrationId(registrationId);
        registration.setStudentId(studentId);
        registration.setCourseOfferingId(courseOfferingId);
        registration.setRegisteredAt(LocalDateTime.now());
        registration.setStatus("Thành công");
        registration.setNote("Đăng ký thành công");
        
        boolean created = registrationRepository.createRegistration(registration);
        
        if (created) {
            // Tăng current capacity
            courseOfferingRepository.incrementCapacity(courseOfferingId);
            System.out.println("Đăng ký thành công: " + registrationId);
            return registration;
        }
        
        throw new RuntimeException("Không thể tạo đăng ký");
    }
    
    @Override
    public boolean cancelRegistration(String registrationId) {
        if (registrationId == null || registrationId.trim().isEmpty()) {
            throw new IllegalArgumentException("Registration ID không được để trống");
        }
        
        // Kiểm tra registration tồn tại
        Registration registration = registrationRepository.findById(registrationId);
        if (registration == null) {
            throw new IllegalArgumentException("Đăng ký không tồn tại: " + registrationId);
        }
        
        // Chỉ cho phép hủy nếu trạng thái là "Thành công"
        if (!"Thành công".equals(registration.getStatus())) {
            throw new IllegalArgumentException("Không thể hủy đăng ký có trạng thái: " + registration.getStatus());
        }
        
        // Xóa registration
        boolean deleted = registrationRepository.deleteRegistration(registrationId);
        
        if (deleted) {
            // Giảm current capacity
            courseOfferingRepository.decrementCapacity(registration.getCourseOfferingId());
            System.out.println("Hủy đăng ký thành công: " + registrationId);
            return true;
        }
        
        return false;
    }
    
    @Override
    public Registration getRegistrationById(String registrationId) {
        if (registrationId == null || registrationId.trim().isEmpty()) {
            throw new IllegalArgumentException("Registration ID không được để trống");
        }
        
        return registrationRepository.findById(registrationId);
    }
    
    @Override
    public List<Registration> getRegistrationsByStudent(String studentId) {
        if (studentId == null || studentId.trim().isEmpty()) {
            throw new IllegalArgumentException("Student ID không được để trống");
        }
        
        // Kiểm tra student tồn tại
        if (studentRepository.findById(studentId) == null) {
            throw new IllegalArgumentException("Sinh viên không tồn tại: " + studentId);
        }
        
        return registrationRepository.findByStudent(studentId);
    }
    
    @Override
    public List<Registration> getRegistrationsByCourseOffering(String courseOfferingId) {
        if (courseOfferingId == null || courseOfferingId.trim().isEmpty()) {
            throw new IllegalArgumentException("Course Offering ID không được để trống");
        }
        
        // Kiểm tra course offering tồn tại
        if (courseOfferingRepository.findById(courseOfferingId) == null) {
            throw new IllegalArgumentException("Lớp mở không tồn tại: " + courseOfferingId);
        }
        
        return registrationRepository.findByCourseOffering(courseOfferingId);
    }
    
    @Override
    public List<Registration> getRegistrationsByStatus(String status) {
        if (status == null || status.trim().isEmpty()) {
            throw new IllegalArgumentException("Status không được để trống");
        }
        
        // Validate status
        if (!"Thành công".equals(status) && !"Thất bại".equals(status)) {
            throw new IllegalArgumentException("Status phải là 'Thành công' hoặc 'Thất bại'");
        }
        
        return registrationRepository.findByStatus(status);
    }
    
    @Override
    public boolean hasRegistered(String studentId, String courseOfferingId) {
        if (studentId == null || studentId.trim().isEmpty()) {
            return false;
        }
        
        if (courseOfferingId == null || courseOfferingId.trim().isEmpty()) {
            return false;
        }
        
        return registrationRepository.existsByStudentAndCourseOffering(studentId, courseOfferingId);
    }
    
    @Override
    public boolean hasAvailableSlot(String courseOfferingId) {
        if (courseOfferingId == null || courseOfferingId.trim().isEmpty()) {
            return false;
        }
        
        CourseOffering courseOffering = courseOfferingRepository.findById(courseOfferingId);
        if (courseOffering == null) {
            return false;
        }
        
        int currentCapacity = Integer.parseInt(courseOffering.getCurrentCapacity());
        int maxCapacity = Integer.parseInt(courseOffering.getMaxCapacity());
        
        return currentCapacity < maxCapacity;
    }
    
    @Override
    public boolean updateRegistrationStatus(String registrationId, String status) {
        if (registrationId == null || registrationId.trim().isEmpty()) {
            throw new IllegalArgumentException("Registration ID không được để trống");
        }
        
        if (status == null || status.trim().isEmpty()) {
            throw new IllegalArgumentException("Status không được để trống");
        }
        
        // Validate status
        if (!"Thành công".equals(status) && !"Thất bại".equals(status)) {
            throw new IllegalArgumentException("Status phải là 'Thành công' hoặc 'Thất bại'");
        }
        
        // Kiểm tra registration tồn tại
        if (registrationRepository.findById(registrationId) == null) {
            throw new IllegalArgumentException("Đăng ký không tồn tại: " + registrationId);
        }
        
        return registrationRepository.updateStatus(registrationId, status);
    }
    
    /**
     * Tạo Registration ID tự động
     */
    private String generateRegistrationId() {
        return "REG_" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}
