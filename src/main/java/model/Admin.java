package main.java.model;

/**
 * Admin entity - Kế thừa từ User
 * Admin có thể truy cập password (override getPassword() thành public)
 */
public class Admin extends User {
    // Constructor mặc định
    public Admin() {
        super();
    }

    // Constructor đầy đủ
    public Admin(String userId, String username, String password, String fullName,
                 String email, String adminId, String department) {
        super(userId, username, password, fullName, email, true); // role = true (admin)
    }


    // Override: Admin có thể truy cập password
    @Override
    public String getPassword() {
        return super.getPassword();
    }

    @Override
    public String toString() {
        return "Admin{" +
                "userId='" + getUserId() + '\'' +
                ", username='" + getUsername() + '\'' +
                ", fullName='" + getFullName() + '\'' +
                ", email='" + getEmail() + '\'' +
                '}';
    }
}
