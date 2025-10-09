package controller;

/**
 * UserController - Xử lý các request liên quan đến User
 * Chịu trách nhiệm: Authentication, User management
 */
public class UserController {

    /**
     * Đăng nhập
     */
    public void login(String username, String password) {
        // TODO: Implement login logic
    }

    /**
     * Đăng xuất
     */
    public void logout(String userId) {
        // TODO: Implement logout logic
    }

    /**
     * Lấy thông tin user
     */
    public void getUserInfo(String userId) {
        // TODO: Implement get user info
    }

    /**
     * Cập nhật thông tin user
     */
    public void updateUserInfo(String userId, String fullName, String email) {
        // TODO: Implement update user info
    }

    /**
     * Đổi mật khẩu
     */
    public void changePassword(String userId, String oldPassword, String newPassword) {
        // TODO: Implement change password
    }
}
