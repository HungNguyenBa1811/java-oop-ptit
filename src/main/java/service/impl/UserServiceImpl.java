package main.java.service.impl;

import java.util.List;
import main.java.utils.PasswordUtils;
import main.java.model.User;
import main.java.repository.UserRepository;
import main.java.service.UserService;

/**
 * UserServiceImpl - Implementation của UserService
 */
public class UserServiceImpl implements UserService {
    
    protected final UserRepository userRepository;
    
    public UserServiceImpl() {
        this.userRepository = new UserRepository();
    }
    
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    
    @Override
    public User register(User user, String password) {
        // Validate user
        validateUser(user);
        
        // Kiểm tra username đã tồn tại
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new IllegalArgumentException("Username đã tồn tại: " + user.getUsername());
        }
        
        // Kiểm tra email đã tồn tại
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("Email đã tồn tại: " + user.getEmail());
        }
        
        // Validate password
        if (password == null || password.length() < 6) {
            throw new IllegalArgumentException("Mật khẩu phải có ít nhất 6 ký tự");
        }
        
        // Hash password trước khi lưu
        user.setPassword(PasswordUtils.md5Hex(password));
        
        // Tạo user
        boolean created = userRepository.createUser(user);
        if (created) {
            System.out.println("User đăng ký thành công: " + user.getUsername());
            return user;
        }
        
        return null;
    }
    
    @Override
    public User login(String username, String password) {
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
        
        // Kiểm tra password (so sánh hash)
        if (!PasswordUtils.matchesMd5(password, user.getPassword())) {
            System.out.println("Đăng nhập thất bại: Mật khẩu không đúng");
            return null;
        }
        
        System.out.println("Đăng nhập thành công: " + username);
        return user;
    }
    
    @Override
    public User getUserById(String userId) {
        if (userId == null || userId.trim().isEmpty()) {
            throw new IllegalArgumentException("User ID không được để trống");
        }
        
        return userRepository.findById(userId);
    }
    
    @Override
    public User getUserByUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username không được để trống");
        }
        
        return userRepository.findByUsername(username);
    }
    
    @Override
    public User getUserByEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email không được để trống");
        }
        
        return userRepository.findByEmail(email);
    }
    
    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    
    @Override
    public boolean updateUser(User user) {
        // Validate user
        validateUser(user);
        
        // Kiểm tra user tồn tại
        User existingUser = userRepository.findById(user.getUserId());
        if (existingUser == null) {
            throw new IllegalArgumentException("User không tồn tại: " + user.getUserId());
        }
        
        // Kiểm tra username trùng (nếu thay đổi)
        if (!existingUser.getUsername().equals(user.getUsername())) {
            if (userRepository.existsByUsername(user.getUsername())) {
                throw new IllegalArgumentException("Username đã tồn tại: " + user.getUsername());
            }
        }
        
        // Kiểm tra email trùng (nếu thay đổi)
        if (!existingUser.getEmail().equals(user.getEmail())) {
            if (userRepository.existsByEmail(user.getEmail())) {
                throw new IllegalArgumentException("Email đã tồn tại: " + user.getEmail());
            }
        }
        
        return userRepository.updateUser(user);
    }
    
    @Override
    public boolean changePassword(String userId, String oldPassword, String newPassword) {
        if (userId == null || userId.trim().isEmpty()) {
            throw new IllegalArgumentException("User ID không được để trống");
        }
        
        if (oldPassword == null || oldPassword.trim().isEmpty()) {
            throw new IllegalArgumentException("Mật khẩu cũ không được để trống");
        }
        
        if (newPassword == null || newPassword.length() < 6) {
            throw new IllegalArgumentException("Mật khẩu mới phải có ít nhất 6 ký tự");
        }
        
        // Kiểm tra user tồn tại
        User user = userRepository.findById(userId);
        if (user == null) {
            throw new IllegalArgumentException("User không tồn tại");
        }
        
        // Kiểm tra mật khẩu cũ (so sánh hash)
        if (!PasswordUtils.matchesMd5(oldPassword, user.getPassword())) {
            throw new IllegalArgumentException("Mật khẩu cũ không đúng");
        }
        
        // Hash password mới trước khi lưu
        return userRepository.updatePassword(userId, PasswordUtils.md5Hex(newPassword));
    }
    
    @Override
    public boolean deleteUser(String userId) {
        if (userId == null || userId.trim().isEmpty()) {
            throw new IllegalArgumentException("User ID không được để trống");
        }
        
        // Kiểm tra user tồn tại
        User user = userRepository.findById(userId);
        if (user == null) {
            throw new IllegalArgumentException("User không tồn tại: " + userId);
        }
        
        return userRepository.deleteUser(userId);
    }
    
    @Override
    public boolean isUsernameExists(String username) {
        if (username == null || username.trim().isEmpty()) {
            return false;
        }
        
        return userRepository.existsByUsername(username);
    }
    
    @Override
    public boolean isEmailExists(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        
        return userRepository.existsByEmail(email);
    }
    
    @Override
    public boolean validateUser(User user) throws IllegalArgumentException {
        if (user == null) {
            throw new IllegalArgumentException("User không được null");
        }
        
        if (user.getUserId() == null || user.getUserId().trim().isEmpty()) {
            throw new IllegalArgumentException("User ID không được để trống");
        }
        
        if (user.getUsername() == null || user.getUsername().trim().isEmpty()) {
            throw new IllegalArgumentException("Username không được để trống");
        }
        
        if (user.getUsername().length() < 3) {
            throw new IllegalArgumentException("Username phải có ít nhất 3 ký tự");
        }
        
        if (user.getEmail() == null || user.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("Email không được để trống");
        }
        
        // Validate email format
        if (!user.getEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new IllegalArgumentException("Email không hợp lệ");
        }
        
        if (user.getFullName() == null || user.getFullName().trim().isEmpty()) {
            throw new IllegalArgumentException("Họ tên không được để trống");
        }
        
        return true;
    }
}
