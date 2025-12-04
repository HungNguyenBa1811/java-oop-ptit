package main.java.service.impl;

import java.time.LocalDateTime;
import main.java.model.Admin;
import main.java.model.Student;
import main.java.model.User;
import main.java.repository.StudentRepository;
import main.java.repository.UserRepository;
import main.java.service.AuthService;
import main.java.utils.PasswordUtils;

/**
 * AuthServiceImpl - Implementation của AuthService
 * Quản lý session và authentication cho desktop app
 */
public class AuthServiceImpl implements AuthService {
    
    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    
    // Session management - lưu user hiện tại (singleton pattern cho desktop app)
    private User currentUser = null;
    private Student currentStudent = null;
    private Admin currentAdmin = null;
    private LocalDateTime loginTime = null;
    
    // Session timeout (30 phút = 1800000 ms)
    private static final long SESSION_TIMEOUT = 30 * 60 * 1000;
    
    public AuthServiceImpl() {
        this.userRepository = new UserRepository();
        this.studentRepository = new StudentRepository();
    }
    
    public AuthServiceImpl(UserRepository userRepository, StudentRepository studentRepository) {
        this.userRepository = userRepository;
        this.studentRepository = studentRepository;
    }
    
    @Override
    public User login(String username, String password) {
        // Validate input
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username không được để trống");
        }
        
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Password không được để trống");
        }
        
        // Tìm user theo username
        User user = userRepository.findByUsername(username);
        
        if (user == null) {
            System.out.println("Đăng nhập thất bại: Username không tồn tại");
            return null;
        }
        
        // Verify password (compare hashed)
        if (!PasswordUtils.matchesMd5(password, user.getPassword())) {
            System.out.println("Đăng nhập thất bại: Password không đúng");
            return null;
        }
        
        // Đăng nhập thành công - lưu session
        this.currentUser = user;
        this.loginTime = LocalDateTime.now();
        
        // Load thêm thông tin tùy theo role
        if (user.getRole() == 0) {
            // Sinh viên
            this.currentStudent = studentRepository.findById(user.getUserId());
            this.currentAdmin = null;
        } else if (user.getRole() == 1) {
            // Admin
            Admin admin = new Admin();
            admin.setUserId(user.getUserId());
            admin.setUsername(user.getUsername());
            admin.setFullName(user.getFullName());
            admin.setEmail(user.getEmail());
            admin.setRole(user.getRole());
            this.currentAdmin = admin;
            this.currentStudent = null;
        }
        
        System.out.println("Đăng nhập thành công: " + user.getUsername() + " (" + 
                          (user.getRole() == 1 ? "Admin" : "Sinh viên") + ")");
        
        return user;
    }
    
    @Override
    public User loginWithEmail(String email, String password) {
        // Validate input
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email không được để trống");
        }
        
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Password không được để trống");
        }
        
        // Tìm user theo email
        User user = userRepository.findByEmail(email);
        
        if (user == null) {
            System.out.println("Đăng nhập thất bại: Email không tồn tại");
            return null;
        }
        
        // Verify password (compare hashed)
        if (!PasswordUtils.matchesMd5(password, user.getPassword())) {
            System.out.println("Đăng nhập thất bại: Password không đúng");
            return null;
        }
        
        // Đăng nhập thành công - lưu session
        this.currentUser = user;
        this.loginTime = LocalDateTime.now();
        
        // Load thêm thông tin tùy theo role
        if (user.getRole() == 0) {
            // Sinh viên
            this.currentStudent = studentRepository.findById(user.getUserId());
            this.currentAdmin = null;
        } else if (user.getRole() == 1) {
            // Admin
            Admin admin = new Admin();
            admin.setUserId(user.getUserId());
            admin.setUsername(user.getUsername());
            admin.setFullName(user.getFullName());
            admin.setEmail(user.getEmail());
            admin.setRole(user.getRole());
            this.currentAdmin = admin;
            this.currentStudent = null;
        }
        
        System.out.println("Đăng nhập thành công (email): " + user.getUsername() + " (" + 
                          (user.getRole() == 1 ? "Admin" : "Sinh viên") + ")");
        
        return user;
    }
    
    @Override
    public boolean logout(String userId) {
        if (currentUser == null) {
            System.out.println("Không có user nào đang đăng nhập");
            return false;
        }
        
        if (!currentUser.getUserId().equals(userId)) {
            System.out.println("User ID không khớp với user hiện tại");
            return false;
        }
        
        System.out.println("Đăng xuất: " + currentUser.getUsername());
        clearSession();
        return true;
    }
    
    @Override
    public User getCurrentUser() {
        if (!isSessionValid()) {
            clearSession();
            return null;
        }
        return currentUser;
    }
    
    @Override
    public Student getCurrentStudent() {
        if (!isSessionValid()) {
            clearSession();
            return null;
        }
        
        if (currentUser != null && currentUser.getRole() == 0) {
            return currentStudent;
        }
        
        return null;
    }
    
    @Override
    public Admin getCurrentAdmin() {
        if (!isSessionValid()) {
            clearSession();
            return null;
        }
        
        if (currentUser != null && currentUser.getRole() == 1) {
            return currentAdmin;
        }
        
        return null;
    }
    
    @Override
    public boolean isAuthenticated() {
        return currentUser != null && isSessionValid();
    }
    
    @Override
    public boolean isAdmin() {
        return currentUser != null && currentUser.getRole() == 1 && isSessionValid();
    }
    
    @Override
    public boolean isStudent() {
        return currentUser != null && currentUser.getRole() == 0 && isSessionValid();
    }
    
    @Override
    public boolean changePassword(String oldPassword, String newPassword) {
        if (!isAuthenticated()) {
            throw new IllegalArgumentException("Chưa đăng nhập");
        }
        
        if (oldPassword == null || oldPassword.trim().isEmpty()) {
            throw new IllegalArgumentException("Password cũ không được để trống");
        }
        
        if (newPassword == null || newPassword.trim().isEmpty()) {
            throw new IllegalArgumentException("Password mới không được để trống");
        }
        
        if (newPassword.length() < 6) {
            throw new IllegalArgumentException("Password mới phải có ít nhất 6 ký tự");
        }
        
        // Verify old password (compare hashed)
        if (!PasswordUtils.matchesMd5(oldPassword, currentUser.getPassword())) {
            throw new IllegalArgumentException("Password cũ không đúng");
        }
        
        // Update password (hash before saving)
        boolean updated = userRepository.updatePassword(currentUser.getUserId(), PasswordUtils.md5Hex(newPassword));

        if (updated) {
            currentUser.setPassword(PasswordUtils.md5Hex(newPassword));
            System.out.println("Đổi password thành công");
        }
        
        return updated;
    }
    
    @Override
    public boolean verifyPassword(String userId, String password) {
        if (userId == null || userId.trim().isEmpty()) {
            return false;
        }
        
        if (password == null || password.trim().isEmpty()) {
            return false;
        }
        
        User user = userRepository.findById(userId);
        if (user == null) {
            return false;
        }
        
        // Compare provided raw password with stored hash
        return PasswordUtils.matchesMd5(password, user.getPassword());
    }
    
    @Override
    public User refreshSession() {
        if (!isAuthenticated()) {
            return null;
        }
        
        // Reload user từ database
        User updatedUser = userRepository.findById(currentUser.getUserId());
        
        if (updatedUser == null) {
            // User đã bị xóa
            clearSession();
            return null;
        }
        
        // Update current user
        this.currentUser = updatedUser;
        
        // Reload Student/Admin info
        if (updatedUser.getRole() == 0) {
            this.currentStudent = studentRepository.findById(updatedUser.getUserId());
        } else if (updatedUser.getRole() == 1) {
            Admin admin = new Admin();
            admin.setUserId(updatedUser.getUserId());
            admin.setUsername(updatedUser.getUsername());
            admin.setFullName(updatedUser.getFullName());
            admin.setEmail(updatedUser.getEmail());
            admin.setRole(updatedUser.getRole());
            this.currentAdmin = admin;
        }
        
        System.out.println("Session refreshed: " + updatedUser.getUsername());
        return updatedUser;
    }
    
    @Override
    public boolean isSessionValid() {
        if (currentUser == null || loginTime == null) {
            return false;
        }
        
        // Kiểm tra session timeout
        long currentTime = System.currentTimeMillis();
        long loginTimeMillis = java.sql.Timestamp.valueOf(loginTime).getTime();
        
        if (currentTime - loginTimeMillis > SESSION_TIMEOUT) {
            System.out.println("Session timeout - tự động đăng xuất");
            return false;
        }
        
        return true;
    }
    
    @Override
    public void clearSession() {
        this.currentUser = null;
        this.currentStudent = null;
        this.currentAdmin = null;
        this.loginTime = null;
        System.out.println("Session cleared");
    }
}
