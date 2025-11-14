package main.java.service.impl;

import java.time.LocalDate;
import java.util.List;
import main.java.model.Semester;
import main.java.repository.SemesterRepository;
import main.java.service.SemesterService;

/**
 * SemesterServiceImpl - Implementation của SemesterService
 */
public class SemesterServiceImpl implements SemesterService {
    
    private final SemesterRepository repository;
    
    public SemesterServiceImpl() {
        this.repository = new SemesterRepository();
    }
    
    public SemesterServiceImpl(SemesterRepository repository) {
        this.repository = repository;
    }
    
    @Override
    public boolean createSemester(Semester semester, String term, String academicYear) {
        // Validate
        if (semester == null) {
            System.err.println("Semester không được null");
            return false;
        }
        
        if (semester.getSemesterId() == null || semester.getSemesterId().trim().isEmpty()) {
            System.err.println("Semester ID không được rỗng");
            return false;
        }
        
        if (term == null || term.trim().isEmpty()) {
            System.err.println("Term không được rỗng");
            return false;
        }
        
        if (academicYear == null || academicYear.trim().isEmpty()) {
            System.err.println("Academic year không được rỗng");
            return false;
        }
        
        // Validate date range
        if (!isValidDateRange(semester.getStartDate(), semester.getEndDate())) {
            System.err.println("Khoảng thời gian không hợp lệ");
            return false;
        }
        
        // Kiểm tra trùng ID
        if (existsById(semester.getSemesterId())) {
            System.err.println("Semester ID đã tồn tại: " + semester.getSemesterId());
            return false;
        }
        
        return repository.createSemester(semester, term, academicYear);
    }
    
    @Override
    public List<Semester> getAllSemesters() {
        return repository.findAll();
    }
    
    @Override
    public Semester getSemesterById(String semesterId) {
        if (semesterId == null || semesterId.trim().isEmpty()) {
            System.err.println("Semester ID không được rỗng");
            return null;
        }
        return repository.findById(semesterId);
    }
    
    @Override
    public List<Semester> getSemestersByYear(String academicYear) {
        if (academicYear == null || academicYear.trim().isEmpty()) {
            System.err.println("Academic year không được rỗng");
            return List.of();
        }
        return repository.findByAcademicYear(academicYear);
    }
    
    @Override
    public Semester getCurrentSemester() {
        return repository.findCurrentSemester();
    }
    
    @Override
    public boolean updateSemester(Semester semester, String term, String academicYear) {
        // Validate
        if (semester == null) {
            System.err.println("Semester không được null");
            return false;
        }
        
        if (semester.getSemesterId() == null || semester.getSemesterId().trim().isEmpty()) {
            System.err.println("Semester ID không được rỗng");
            return false;
        }
        
        if (term == null || term.trim().isEmpty()) {
            System.err.println("Term không được rỗng");
            return false;
        }
        
        if (academicYear == null || academicYear.trim().isEmpty()) {
            System.err.println("Academic year không được rỗng");
            return false;
        }
        
        // Validate date range
        if (!isValidDateRange(semester.getStartDate(), semester.getEndDate())) {
            System.err.println("Khoảng thời gian không hợp lệ");
            return false;
        }
        
        // Kiểm tra tồn tại
        if (!existsById(semester.getSemesterId())) {
            System.err.println("Semester không tồn tại: " + semester.getSemesterId());
            return false;
        }
        
        return repository.updateSemester(semester, term, academicYear);
    }
    
    @Override
    public boolean deleteSemester(String semesterId) {
        if (semesterId == null || semesterId.trim().isEmpty()) {
            System.err.println("Semester ID không được rỗng");
            return false;
        }
        
        if (!existsById(semesterId)) {
            System.err.println("Semester không tồn tại: " + semesterId);
            return false;
        }
        
        return repository.deleteSemester(semesterId);
    }
    
    @Override
    public boolean existsById(String semesterId) {
        Semester semester = repository.findById(semesterId);
        return semester != null;
    }
    
    @Override
    public boolean isValidDateRange(LocalDate startDate, LocalDate endDate) {
        if (startDate == null || endDate == null) {
            return false;
        }
        
        // Start date phải trước end date
        if (!startDate.isBefore(endDate)) {
            return false;
        }
        
        // Học kỳ phải dài ít nhất 1 tháng (30 ngày)
        if (startDate.plusDays(30).isAfter(endDate)) {
            System.err.println("Học kỳ phải dài ít nhất 30 ngày");
            return false;
        }
        
        return true;
    }
}
