package model;

/**
 * Admin entity - Kế thừa từ User
 * Admin có thể truy cập password (override getPassword() thành public)
 */
public class Admin extends User {
    private String adminId;

    // Constructor mặc định
    public Admin() {
        super();
    }

    // Constructor đầy đủ
    public Admin(String userId, String username, String password, String fullName,
                 String email, String adminId, String department) {
        super(userId, username, password, fullName, email, true); // role = true (admin)
        this.adminId = adminId;
        this.department = department;
    }

    // Constructor không có User fields
    public Admin(String adminId) {
        this.adminId = adminId;
    }

    // Getters
    public String getAdminId() {
        return adminId;
    }


    // Override: Admin có thể truy cập password
    @Override
    public String getPassword() {
        return super.getPassword();
    }

    @Override
    public String toString() {
        return "Admin{" +
                "userId='" + userId + '\'' +
                ", username='" + username + '\'' +
                ", fullName='" + fullName + '\'' +
                ", email='" + email + '\'' +
                ", adminId='" + adminId + '\'' +
                ", department='" + department + '\'' +
                '}';
    }
}
