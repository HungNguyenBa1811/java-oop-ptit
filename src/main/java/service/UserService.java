package main.java.service;

import java.util.List;
import main.java.model.User;

/**
 * UserService Interface - Định nghĩa các business logic cho User
 */
public interface UserService {
    
    /**
     * Đăng ký user mới
     * @param user User object
     * @param password Mật khẩu (sẽ được hash)
     * @return User đã tạo, null nếu thất bại
     */
    User register(User user, String password);
    
    /**
     * Đăng nhập
     * @param username Tên đăng nhập
     * @param password Mật khẩu
     * @return User nếu đăng nhập thành công, null nếu thất bại
     */
    User login(String username, String password);
    
    /**
     * Lấy user theo ID
     * @param userId User ID
     * @return User nếu tìm thấy
     */
    User getUserById(String userId);
    
    /**
     * Lấy user theo username
     * @param username Username
     * @return User nếu tìm thấy
     */
    User getUserByUsername(String username);
    
    /**
     * Lấy user theo email
     * @param email Email
     * @return User nếu tìm thấy
     */
    User getUserByEmail(String email);
    
    /**
     * Lấy tất cả users
     * @return List danh sách users
     */
    List<User> getAllUsers();
    
    /**
     * Cập nhật thông tin user
     * @param user User với thông tin mới
     * @return true nếu thành công
     */
    boolean updateUser(User user);
    
    /**
     * Đổi mật khẩu
     * @param userId User ID
     * @param oldPassword Mật khẩu cũ
     * @param newPassword Mật khẩu mới
     * @return true nếu thành công
     */
    boolean changePassword(String userId, String oldPassword, String newPassword);
    
    /**
     * Xóa user
     * @param userId User ID
     * @return true nếu thành công
     */
    boolean deleteUser(String userId);
    
    /**
     * Kiểm tra username đã tồn tại chưa
     * @param username Username
     * @return true nếu đã tồn tại
     */
    boolean isUsernameExists(String username);
    
    /**
     * Kiểm tra email đã tồn tại chưa
     * @param email Email
     * @return true nếu đã tồn tại
     */
    boolean isEmailExists(String email);
    
    /**
     * Validate thông tin user
     * @param user User cần validate
     * @return true nếu hợp lệ
     * @throws IllegalArgumentException nếu không hợp lệ
     */
    boolean validateUser(User user) throws IllegalArgumentException;
}
