package main.java.service;

import main.java.model.Admin;
import main.java.model.Student;
import main.java.model.User;

/**
 * AuthService Interface - Quản lý Authentication và Authorization
 * Service này chuyên biệt cho việc đăng nhập/đăng xuất và quản lý session
 */
public interface AuthService {
    
    /**
     * Đăng nhập với username và password
     * @param username Tên đăng nhập
     * @param password Mật khẩu
     * @return User nếu đăng nhập thành công, null nếu thất bại
     */
    User login(String username, String password);
    
    /**
     * Đăng nhập với email và password
     * @param email Email
     * @param password Mật khẩu
     * @return User nếu đăng nhập thành công, null nếu thất bại
     */
    User loginWithEmail(String email, String password);
    
    /**
     * Đăng xuất user hiện tại
     * @param userId User ID
     * @return true nếu đăng xuất thành công
     */
    boolean logout(String userId);
    
    /**
     * Lấy thông tin user hiện đang đăng nhập
     * @return User hiện tại, null nếu chưa đăng nhập
     */
    User getCurrentUser();
    
    /**
     * Lấy thông tin Student nếu user hiện tại là sinh viên
     * @return Student object, null nếu không phải sinh viên hoặc chưa đăng nhập
     */
    Student getCurrentStudent();
    
    /**
     * Lấy thông tin Admin nếu user hiện tại là admin
     * @return Admin object, null nếu không phải admin hoặc chưa đăng nhập
     */
    Admin getCurrentAdmin();
    
    /**
     * Kiểm tra user hiện tại có đang đăng nhập không
     * @return true nếu đã đăng nhập
     */
    boolean isAuthenticated();
    
    /**
     * Kiểm tra user hiện tại có phải admin không
     * @return true nếu là admin
     */
    boolean isAdmin();
    
    /**
     * Kiểm tra user hiện tại có phải sinh viên không
     * @return true nếu là sinh viên
     */
    boolean isStudent();
    
    /**
     * Đổi mật khẩu cho user hiện tại
     * @param oldPassword Mật khẩu cũ
     * @param newPassword Mật khẩu mới
     * @return true nếu đổi thành công
     */
    boolean changePassword(String oldPassword, String newPassword);
    
    /**
     * Verify password của user
     * @param userId User ID
     * @param password Password cần verify
     * @return true nếu password đúng
     */
    boolean verifyPassword(String userId, String password);
    
    /**
     * Refresh session (cập nhật thông tin user từ database)
     * @return User với thông tin mới nhất
     */
    User refreshSession();
    
    /**
     * Kiểm tra session có còn valid không (cho auto-logout)
     * @return true nếu session còn valid
     */
    boolean isSessionValid();
    
    /**
     * Clear tất cả session data (dùng khi đăng xuất)
     */
    void clearSession();
}
