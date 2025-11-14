package main.java.service.impl;

import java.util.List;
import main.java.model.Major;
import main.java.repository.MajorRepository;
import main.java.service.MajorService;

/**
 * MajorServiceImpl - Implementation của MajorService
 */
public class MajorServiceImpl implements MajorService {
    
    private final MajorRepository repository;
    
    public MajorServiceImpl() {
        this.repository = new MajorRepository();
    }
    
    public MajorServiceImpl(MajorRepository repository) {
        this.repository = repository;
    }
    
    @Override
    public boolean createMajor(Major major) {
        // Validate
        if (major == null) {
            System.err.println("Major không được null");
            return false;
        }
        
        if (major.getMajorId() == null || major.getMajorId().trim().isEmpty()) {
            System.err.println("Major ID không được rỗng");
            return false;
        }
        
        if (major.getMajorName() == null || major.getMajorName().trim().isEmpty()) {
            System.err.println("Major name không được rỗng");
            return false;
        }
        
        // Kiểm tra trùng ID
        if (existsById(major.getMajorId())) {
            System.err.println("Major ID đã tồn tại: " + major.getMajorId());
            return false;
        }
        
        return repository.createMajor(major);
    }
    
    @Override
    public List<Major> getAllMajors() {
        return repository.findAll();
    }
    
    @Override
    public Major getMajorById(String majorId) {
        if (majorId == null || majorId.trim().isEmpty()) {
            System.err.println("Major ID không được rỗng");
            return null;
        }
        return repository.findById(majorId);
    }
    
    @Override
    public List<Major> getMajorsByFaculty(String facultyId) {
        if (facultyId == null || facultyId.trim().isEmpty()) {
            System.err.println("Faculty ID không được rỗng");
            return List.of();
        }
        return repository.findByFaculty(facultyId);
    }
    
    @Override
    public boolean updateMajor(Major major) {
        // Validate
        if (major == null) {
            System.err.println("Major không được null");
            return false;
        }
        
        if (major.getMajorId() == null || major.getMajorId().trim().isEmpty()) {
            System.err.println("Major ID không được rỗng");
            return false;
        }
        
        // Kiểm tra tồn tại
        if (!existsById(major.getMajorId())) {
            System.err.println("Major không tồn tại: " + major.getMajorId());
            return false;
        }
        
        return repository.updateMajor(major);
    }
    
    @Override
    public boolean deleteMajor(String majorId) {
        if (majorId == null || majorId.trim().isEmpty()) {
            System.err.println("Major ID không được rỗng");
            return false;
        }
        
        if (!existsById(majorId)) {
            System.err.println("Major không tồn tại: " + majorId);
            return false;
        }
        
        return repository.deleteMajor(majorId);
    }
    
    @Override
    public boolean existsById(String majorId) {
        Major major = repository.findById(majorId);
        return major != null;
    }
    
    @Override
    public int countMajors() {
        return repository.findAll().size();
    }
}
