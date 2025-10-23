package main.java.service.impl;

import java.util.List;
import java.util.stream.Collectors;
import main.java.model.CourseOffering;
import main.java.repository.CourseOfferingRepository;
import main.java.repository.CourseRepository;
import main.java.repository.RoomRepository;
import main.java.repository.SemesterRepository;
import main.java.service.CourseOfferingService;

/**
 * CourseOfferingServiceImpl - Implementation của CourseOfferingService
 */
public class CourseOfferingServiceImpl implements CourseOfferingService {
    
    private final CourseOfferingRepository courseOfferingRepository;
    private final CourseRepository courseRepository;
    private final SemesterRepository semesterRepository;
    private final RoomRepository roomRepository;
    
    public CourseOfferingServiceImpl() {
        this.courseOfferingRepository = new CourseOfferingRepository();
        this.courseRepository = new CourseRepository();
        this.semesterRepository = new SemesterRepository();
        this.roomRepository = new RoomRepository();
    }
    
    public CourseOfferingServiceImpl(CourseOfferingRepository courseOfferingRepository,
                                    CourseRepository courseRepository,
                                    SemesterRepository semesterRepository,
                                    RoomRepository roomRepository) {
        this.courseOfferingRepository = courseOfferingRepository;
        this.courseRepository = courseRepository;
        this.semesterRepository = semesterRepository;
        this.roomRepository = roomRepository;
    }
    
    @Override
    public CourseOffering createCourseOffering(CourseOffering courseOffering, String courseId,
                                              String semesterId, String roomId) {
        // Validate courseOffering
        validateCourseOffering(courseOffering);
        
        // Validate course ID
        if (courseId == null || courseId.trim().isEmpty()) {
            throw new IllegalArgumentException("Course ID không được để trống");
        }
        if (courseRepository.findById(courseId) == null) {
            throw new IllegalArgumentException("Môn học không tồn tại: " + courseId);
        }
        
        // Validate semester ID
        if (semesterId == null || semesterId.trim().isEmpty()) {
            throw new IllegalArgumentException("Semester ID không được để trống");
        }
        if (semesterRepository.findById(semesterId) == null) {
            throw new IllegalArgumentException("Học kỳ không tồn tại: " + semesterId);
        }
        
        // Validate room ID
        if (roomId == null || roomId.trim().isEmpty()) {
            throw new IllegalArgumentException("Room ID không được để trống");
        }
        if (roomRepository.findById(roomId) == null) {
            throw new IllegalArgumentException("Phòng học không tồn tại: " + roomId);
        }
        
        // Kiểm tra course offering ID đã tồn tại chưa
        if (courseOfferingRepository.findById(courseOffering.getCourseOfferingId()) != null) {
            throw new IllegalArgumentException("Course Offering ID đã tồn tại: " + 
                                             courseOffering.getCourseOfferingId());
        }
        
        // Set các field cần thiết
        courseOffering.setCourseId(courseId);
        courseOffering.setSemesterId(semesterId);
        courseOffering.setRoomId(roomId);
        
        // Set current capacity = 0 nếu chưa set
        if (courseOffering.getCurrentCapacity() == null || 
            courseOffering.getCurrentCapacity().trim().isEmpty()) {
            courseOffering.setCurrentCapacity("0");
        }
        
        // Tạo course offering
        boolean created = courseOfferingRepository.createCourseOffering(courseOffering);
        
        if (created) {
            System.out.println("Tạo lớp mở thành công: " + courseOffering.getCourseOfferingId());
            return courseOffering;
        }
        
        throw new RuntimeException("Không thể tạo lớp mở");
    }
    
    @Override
    public CourseOffering getCourseOfferingById(String courseOfferingId) {
        if (courseOfferingId == null || courseOfferingId.trim().isEmpty()) {
            throw new IllegalArgumentException("Course Offering ID không được để trống");
        }
        
        return courseOfferingRepository.findById(courseOfferingId);
    }
    
    @Override
    public List<CourseOffering> getAllCourseOfferings() {
        return courseOfferingRepository.findAll();
    }
    
    @Override
    public List<CourseOffering> getCourseOfferingsByCourse(String courseId) {
        if (courseId == null || courseId.trim().isEmpty()) {
            throw new IllegalArgumentException("Course ID không được để trống");
        }
        
        return courseOfferingRepository.findByCourse(courseId);
    }
    
    @Override
    public List<CourseOffering> getCourseOfferingsBySemester(String semesterId) {
        if (semesterId == null || semesterId.trim().isEmpty()) {
            throw new IllegalArgumentException("Semester ID không được để trống");
        }
        
        return courseOfferingRepository.findBySemester(semesterId);
    }
    
    @Override
    public List<CourseOffering> getCourseOfferingsByRoom(String roomId) {
        if (roomId == null || roomId.trim().isEmpty()) {
            throw new IllegalArgumentException("Room ID không được để trống");
        }
        
        return courseOfferingRepository.findByRoom(roomId);
    }
    
    @Override
    public List<CourseOffering> getCourseOfferingsByCourseAndSemester(String courseId, String semesterId) {
        if (courseId == null || courseId.trim().isEmpty()) {
            throw new IllegalArgumentException("Course ID không được để trống");
        }
        
        if (semesterId == null || semesterId.trim().isEmpty()) {
            throw new IllegalArgumentException("Semester ID không được để trống");
        }
        
        // Filter kết quả từ findByCourse
        return courseOfferingRepository.findByCourse(courseId).stream()
                .filter(co -> co.getSemesterId().equals(semesterId))
                .collect(Collectors.toList());
    }
    
    @Override
    public List<CourseOffering> getAvailableCourseOfferings() {
        return courseOfferingRepository.findAll().stream()
                .filter(co -> {
                    int current = Integer.parseInt(co.getCurrentCapacity());
                    int max = Integer.parseInt(co.getMaxCapacity());
                    return current < max;
                })
                .collect(Collectors.toList());
    }
    
    @Override
    public List<CourseOffering> getAvailableCourseOfferingsBySemester(String semesterId) {
        if (semesterId == null || semesterId.trim().isEmpty()) {
            throw new IllegalArgumentException("Semester ID không được để trống");
        }
        
        return courseOfferingRepository.findBySemester(semesterId).stream()
                .filter(co -> {
                    int current = Integer.parseInt(co.getCurrentCapacity());
                    int max = Integer.parseInt(co.getMaxCapacity());
                    return current < max;
                })
                .collect(Collectors.toList());
    }
    
    @Override
    public boolean updateCourseOffering(CourseOffering courseOffering) {
        // Validate courseOffering
        validateCourseOffering(courseOffering);
        
        // Kiểm tra course offering tồn tại
        if (courseOfferingRepository.findById(courseOffering.getCourseOfferingId()) == null) {
            throw new IllegalArgumentException("Lớp mở không tồn tại: " + 
                                             courseOffering.getCourseOfferingId());
        }
        
        boolean updated = courseOfferingRepository.updateCourseOffering(courseOffering);
        
        if (updated) {
            System.out.println("Cập nhật lớp mở thành công: " + courseOffering.getCourseOfferingId());
        }
        
        return updated;
    }
    
    @Override
    public boolean deleteCourseOffering(String courseOfferingId) {
        if (courseOfferingId == null || courseOfferingId.trim().isEmpty()) {
            throw new IllegalArgumentException("Course Offering ID không được để trống");
        }
        
        // Kiểm tra course offering tồn tại
        CourseOffering courseOffering = courseOfferingRepository.findById(courseOfferingId);
        if (courseOffering == null) {
            throw new IllegalArgumentException("Lớp mở không tồn tại: " + courseOfferingId);
        }
        
        // Kiểm tra xem có sinh viên đã đăng ký chưa
        int currentCapacity = Integer.parseInt(courseOffering.getCurrentCapacity());
        if (currentCapacity > 0) {
            throw new IllegalArgumentException("Không thể xóa lớp mở đã có sinh viên đăng ký");
        }
        
        boolean deleted = courseOfferingRepository.deleteCourseOffering(courseOfferingId);
        
        if (deleted) {
            System.out.println("Xóa lớp mở thành công: " + courseOfferingId);
        }
        
        return deleted;
    }
    
    @Override
    public boolean existsCourseOffering(String courseOfferingId) {
        if (courseOfferingId == null || courseOfferingId.trim().isEmpty()) {
            return false;
        }
        
        return courseOfferingRepository.findById(courseOfferingId) != null;
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
        
        int current = Integer.parseInt(courseOffering.getCurrentCapacity());
        int max = Integer.parseInt(courseOffering.getMaxCapacity());
        
        return current < max;
    }
    
    @Override
    public int getAvailableSlots(String courseOfferingId) {
        if (courseOfferingId == null || courseOfferingId.trim().isEmpty()) {
            throw new IllegalArgumentException("Course Offering ID không được để trống");
        }
        
        CourseOffering courseOffering = courseOfferingRepository.findById(courseOfferingId);
        if (courseOffering == null) {
            throw new IllegalArgumentException("Lớp mở không tồn tại: " + courseOfferingId);
        }
        
        int current = Integer.parseInt(courseOffering.getCurrentCapacity());
        int max = Integer.parseInt(courseOffering.getMaxCapacity());
        
        return max - current;
    }
    
    @Override
    public boolean incrementCurrentCapacity(String courseOfferingId) {
        if (courseOfferingId == null || courseOfferingId.trim().isEmpty()) {
            throw new IllegalArgumentException("Course Offering ID không được để trống");
        }
        
        // Kiểm tra còn chỗ không
        if (!hasAvailableSlot(courseOfferingId)) {
            throw new IllegalArgumentException("Lớp mở đã đầy");
        }
        
        boolean updated = courseOfferingRepository.incrementCapacity(courseOfferingId);
        
        if (updated) {
            System.out.println("Tăng current capacity cho lớp: " + courseOfferingId);
        }
        
        return updated;
    }
    
    @Override
    public boolean decrementCurrentCapacity(String courseOfferingId) {
        if (courseOfferingId == null || courseOfferingId.trim().isEmpty()) {
            throw new IllegalArgumentException("Course Offering ID không được để trống");
        }
        
        CourseOffering courseOffering = courseOfferingRepository.findById(courseOfferingId);
        if (courseOffering == null) {
            throw new IllegalArgumentException("Lớp mở không tồn tại: " + courseOfferingId);
        }
        
        int current = Integer.parseInt(courseOffering.getCurrentCapacity());
        if (current <= 0) {
            throw new IllegalArgumentException("Current capacity đã là 0");
        }
        
        boolean updated = courseOfferingRepository.decrementCapacity(courseOfferingId);
        
        if (updated) {
            System.out.println("Giảm current capacity cho lớp: " + courseOfferingId);
        }
        
        return updated;
    }
    
    @Override
    public boolean validateCourseOffering(CourseOffering courseOffering) throws IllegalArgumentException {
        if (courseOffering == null) {
            throw new IllegalArgumentException("Course Offering không được null");
        }
        
        // Validate course offering ID
        if (courseOffering.getCourseOfferingId() == null || 
            courseOffering.getCourseOfferingId().trim().isEmpty()) {
            throw new IllegalArgumentException("Course Offering ID không được để trống");
        }
        
        // Validate max capacity
        if (courseOffering.getMaxCapacity() == null || 
            courseOffering.getMaxCapacity().trim().isEmpty()) {
            throw new IllegalArgumentException("Max capacity không được để trống");
        }
        
        try {
            int maxCapacity = Integer.parseInt(courseOffering.getMaxCapacity());
            if (maxCapacity <= 0) {
                throw new IllegalArgumentException("Max capacity phải lớn hơn 0");
            }
            if (maxCapacity > 200) {
                throw new IllegalArgumentException("Max capacity không được vượt quá 200");
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Max capacity phải là số nguyên");
        }
        
        // Validate current capacity nếu có
        if (courseOffering.getCurrentCapacity() != null && 
            !courseOffering.getCurrentCapacity().trim().isEmpty()) {
            try {
                int currentCapacity = Integer.parseInt(courseOffering.getCurrentCapacity());
                int maxCapacity = Integer.parseInt(courseOffering.getMaxCapacity());
                
                if (currentCapacity < 0) {
                    throw new IllegalArgumentException("Current capacity không được âm");
                }
                
                if (currentCapacity > maxCapacity) {
                    throw new IllegalArgumentException("Current capacity không được vượt quá max capacity");
                }
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Current capacity phải là số nguyên");
            }
        }
        
        return true;
    }
}
