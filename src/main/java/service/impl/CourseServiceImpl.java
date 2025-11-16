package main.java.service.impl;

import java.util.List;
import java.util.stream.Collectors;
import main.java.model.Course;
import main.java.model.CourseOffering;
import main.java.repository.CourseOfferingRepository;
import main.java.repository.CourseRepository;
import main.java.repository.FacultyRepository;
import main.java.service.CourseService;

/**
 * CourseServiceImpl - Implementation của CourseService
 */
public class CourseServiceImpl implements CourseService {
    
    private final CourseRepository courseRepository;
    private final FacultyRepository facultyRepository;
    private final CourseOfferingRepository courseOfferingRepository;
    
    public CourseServiceImpl() {
        this.courseRepository = new CourseRepository();
        this.facultyRepository = new FacultyRepository();
        this.courseOfferingRepository = new CourseOfferingRepository();
    }
    
    public CourseServiceImpl(CourseRepository courseRepository, FacultyRepository facultyRepository, CourseOfferingRepository courseOfferingRepository) {
        this.courseRepository = courseRepository;
        this.facultyRepository = facultyRepository;
        this.courseOfferingRepository = courseOfferingRepository;
    }
    
    @Override
    public Course createCourse(Course course, String facultyId) {
        // Validate course
        validateCourse(course);
        
        // Validate faculty ID
        if (facultyId == null || facultyId.trim().isEmpty()) {
            throw new IllegalArgumentException("Faculty ID không được để trống");
        }
        
        // Kiểm tra faculty tồn tại
        if (facultyRepository.findById(facultyId) == null) {
            throw new IllegalArgumentException("Khoa không tồn tại: " + facultyId);
        }
        
        // Kiểm tra course ID đã tồn tại chưa
        if (courseRepository.findById(course.getCourseId()) != null) {
            throw new IllegalArgumentException("Course ID đã tồn tại: " + course.getCourseId());
        }
        
        // Tạo course
        boolean created = courseRepository.createCourse(course, facultyId);
        
        if (created) {
            System.out.println("Tạo môn học thành công: " + course.getCourseId());
            return course;
        }
        
        throw new RuntimeException("Không thể tạo môn học");
    }
    
    @Override
    public Course getCourseById(String courseId) {
        if (courseId == null || courseId.trim().isEmpty()) {
            throw new IllegalArgumentException("Course ID không được để trống");
        }
        
        return courseRepository.findById(courseId);
    }
    
    @Override
    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }
    
    @Override
    public List<Course> getCoursesByFaculty(String facultyId) {
        if (facultyId == null || facultyId.trim().isEmpty()) {
            throw new IllegalArgumentException("Faculty ID không được để trống");
        }
        
        // Kiểm tra faculty tồn tại
        if (facultyRepository.findById(facultyId) == null) {
            throw new IllegalArgumentException("Khoa không tồn tại: " + facultyId);
        }
        
        return courseRepository.findByFaculty(facultyId);
    }
    
    @Override
    public List<Course> getCoursesByCredits(String credits) {
        if (credits == null || credits.trim().isEmpty()) {
            throw new IllegalArgumentException("Credits không được để trống");
        }
        
        int creditValue;
        try {
            creditValue = Integer.parseInt(credits);
            if (creditValue <= 0) {
                throw new IllegalArgumentException("Số tín chỉ phải lớn hơn 0");
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Credits phải là số nguyên");
        }
        
        return courseRepository.findByCredits(creditValue);
    }
    
    @Override
    public List<Course> searchCoursesByName(String courseName) {
        if (courseName == null || courseName.trim().isEmpty()) {
            throw new IllegalArgumentException("Course name không được để trống");
        }
        
        // Tìm kiếm không phân biệt hoa thường
        String searchTerm = courseName.toLowerCase();
        
        return courseRepository.findAll().stream()
                .filter(course -> course.getCourseName().toLowerCase().contains(searchTerm))
                .collect(Collectors.toList());
    }
    
    @Override
    public boolean updateCourse(Course course) {
        // Validate course
        validateCourse(course);
        
        // Kiểm tra course tồn tại
        Course existingCourse = courseRepository.findById(course.getCourseId());
        if (existingCourse == null) {
            throw new IllegalArgumentException("Môn học không tồn tại: " + course.getCourseId());
        }
        
        // Lấy facultyId từ course object, nếu null thì giữ nguyên faculty cũ
        String facultyId = course.getFacultyId();
        if (facultyId == null || facultyId.trim().isEmpty()) {
            facultyId = existingCourse.getFacultyId();
        }
        
        // Validate faculty tồn tại
        if (facultyRepository.findById(facultyId) == null) {
            throw new IllegalArgumentException("Khoa không tồn tại: " + facultyId);
        }
        
        boolean updated = courseRepository.updateCourse(course, facultyId);
        
        if (updated) {
            System.out.println("Cập nhật môn học thành công: " + course.getCourseId());
        }
        
        return updated;
    }
    
    @Override
    public boolean deleteCourse(String courseId) {
        if (courseId == null || courseId.trim().isEmpty()) {
            throw new IllegalArgumentException("Course ID không được để trống");
        }
        
        // Kiểm tra course tồn tại
        if (courseRepository.findById(courseId) == null) {
            throw new IllegalArgumentException("Môn học không tồn tại: " + courseId);
        }
        
        // Kiểm tra xem môn học có đang được sử dụng trong CourseOffering không
        List<CourseOffering> relatedOfferings = courseOfferingRepository.findByCourse(courseId);
        if (!relatedOfferings.isEmpty()) {
            throw new IllegalStateException(
                "Không thể xóa môn học. Có " + relatedOfferings.size() + 
                " lớp học đang sử dụng môn học này. " +
                "Vui lòng xóa các lớp học liên quan trước."
            );
        }
        
        boolean deleted = courseRepository.deleteCourse(courseId);
        
        if (deleted) {
            System.out.println("Xóa môn học thành công: " + courseId);
        }
        
        return deleted;
    }
    
    @Override
    public boolean existsCourse(String courseId) {
        if (courseId == null || courseId.trim().isEmpty()) {
            return false;
        }
        
        return courseRepository.findById(courseId) != null;
    }
    
    @Override
    public boolean validateCourse(Course course) throws IllegalArgumentException {
        if (course == null) {
            throw new IllegalArgumentException("Course không được null");
        }
        
        // Validate course ID
        if (course.getCourseId() == null || course.getCourseId().trim().isEmpty()) {
            throw new IllegalArgumentException("Course ID không được để trống");
        }
        
        // Validate course name
        if (course.getCourseName() == null || course.getCourseName().trim().isEmpty()) {
            throw new IllegalArgumentException("Tên môn học không được để trống");
        }
        
        // Validate credits
        int credits = course.getCredits();
        if (credits <= 0 || credits > 10) {
            throw new IllegalArgumentException("Số tín chỉ phải từ 1 đến 10");
        }
        
        // Note: facultyId validation được thực hiện trong createCourse/updateCourse
        // vì cần kiểm tra với database
        
        return true;
    }
}
