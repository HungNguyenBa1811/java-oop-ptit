package main.java.service.impl;

import java.util.List;
import main.java.model.Faculty;
import main.java.repository.FacultyRepository;
import main.java.service.FacultyService;

/**
 * FacultyServiceImpl - Implementation của FacultyService
 */
public class FacultyServiceImpl implements FacultyService {
    
    private final FacultyRepository repository;
    
    public FacultyServiceImpl() {
        this.repository = new FacultyRepository();
    }
    
    public FacultyServiceImpl(FacultyRepository repository) {
        this.repository = repository;
    }
    
    @Override
    public boolean createFaculty(Faculty faculty) {
        // Validate
        if (faculty == null) {
            System.err.println("Faculty không được null");
            return false;
        }
        
        if (faculty.getFacultyId() == null || faculty.getFacultyId().trim().isEmpty()) {
            System.err.println("Faculty ID không được rỗng");
            return false;
        }
        
        if (faculty.getFacultyName() == null || faculty.getFacultyName().trim().isEmpty()) {
            System.err.println("Faculty name không được rỗng");
            return false;
        }
        
        // Kiểm tra trùng ID
        if (existsById(faculty.getFacultyId())) {
            System.err.println("Faculty ID đã tồn tại: " + faculty.getFacultyId());
            return false;
        }
        
        return repository.createFaculty(faculty);
    }
    
    @Override
    public List<Faculty> getAllFaculties() {
        return repository.findAll();
    }
    
    @Override
    public Faculty getFacultyById(String facultyId) {
        if (facultyId == null || facultyId.trim().isEmpty()) {
            System.err.println("Faculty ID không được rỗng");
            return null;
        }
        return repository.findById(facultyId);
    }
    
    @Override
    public boolean updateFaculty(Faculty faculty) {
        // Validate
        if (faculty == null) {
            System.err.println("Faculty không được null");
            return false;
        }
        
        if (faculty.getFacultyId() == null || faculty.getFacultyId().trim().isEmpty()) {
            System.err.println("Faculty ID không được rỗng");
            return false;
        }
        
        // Kiểm tra tồn tại
        if (!existsById(faculty.getFacultyId())) {
            System.err.println("Faculty không tồn tại: " + faculty.getFacultyId());
            return false;
        }
        
        return repository.updateFaculty(faculty);
    }
    
    @Override
    public boolean deleteFaculty(String facultyId) {
        if (facultyId == null || facultyId.trim().isEmpty()) {
            System.err.println("Faculty ID không được rỗng");
            return false;
        }
        
        if (!existsById(facultyId)) {
            System.err.println("Faculty không tồn tại: " + facultyId);
            return false;
        }
        
        return repository.deleteFaculty(facultyId);
    }
    
    @Override
    public boolean existsById(String facultyId) {
        Faculty faculty = repository.findById(facultyId);
        return faculty != null;
    }
}
