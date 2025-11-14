package main.java.repository;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import main.java.config.DatabaseConnection;
import main.java.model.User;


/**
 * MajorRepository - Quản lý CRUD cho bảng users (Người dùng)
 */
public class UserRepository {
    private static final String INSERT_USER =
        "INSERT INTO users (user_id, username, password, email, full_name, role, created_at)" +
        "VALUES (?, ?, ?, ?, ?, ?, ?)";

    private static final String SELECT_ALL_USER =
        "SELECT * FROM users ORDER BY created_at DESC";
    
    private static final String SELECT_USER_BY_ID =
        "SELECT * FROM users WHERE user_id = ?";

    private static final String SELECT_USER_BY_USERNAME =
        "SELECT * FROM users WHERE username = ?";
    
    private static final String SELECT_USER_BY_EMAIL = 
        "SELECT * FROM users WHERE email = ?";
    
    private static final String UPDATE_USER = 
        "UPDATE users SET username = ?, email = ?, full_name = ?, role = ? WHERE user_id = ?";

    private static final String UPDATE_PASSWORD = 
        "UPDATE users SET password = ?, updated_at = ? WHERE user_id = ?";

    private static final String DELETE_USER = 
        "DELETE FROM users WHERE user_id = ?";

    /*
    1. Connection conn = Kết nối đến database
    2. PreparedStatement stmt = Chuẩn bị câu lệnh SQL
    3. stmt.setXxx() = Điền tham số vào câu lệnh
    4. stmt.executeXxx() = Thực thi câu lệnh
    5. Close stmt và conn = Đóng kết nối
    */    
    
    /*
     * Tạo user mới
     * @param User object cần tạo
     * @return true nếu tạo thành công, false nếu thất bại
     */
    public boolean createUser(User user){
         try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(INSERT_USER, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, user.getUserId());
            stmt.setString(2, user.getUsername());
            stmt.setString(3, user.getPassword());
            stmt.setString(4, user.getEmail());
            stmt.setString(5, user.getFullName());
            stmt.setInt(6, user.getRole());
            stmt.setTimestamp(7, Timestamp.valueOf(LocalDateTime.now()));

            int rows = stmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi khi tạo user: " + e.getMessage());
            e.printStackTrace();
        }
        return false;

    }

    public List<User> findAll(){
        List<User> users = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement smtm = conn.prepareStatement(SELECT_ALL_USER);
                ResultSet rs = smtm.executeQuery()) {
                    
                    while(rs.next()){
                        users.add(mapResultSetToUser(rs));
                    }
                } catch (SQLException e) {
                    System.err.println("Lỗi khi lấy danh sách user: " + e.getMessage());
                    e.printStackTrace();
                }
        return users;
    }
   
    /**
     * Tìm user theo ID
     * @param userId User ID cần tìm
     * @return User nếu tìm thấy, null nếu không
     */
    public User findById(String userId) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_USER_BY_ID)) {
            
            stmt.setString(1, userId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                System.out.println("User found: " + userId);
                return mapResultSetToUser(rs);
            } else {
                System.out.println("User not found: " + userId);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi tìm user by ID: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Tìm user theo username
     * @param username Username cần tìm
     * @return User nếu tìm thấy, null nếu không
     */
    public User findByUsername(String username) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_USER_BY_USERNAME)) {
            
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                System.out.println("User found by username: " + username);
                return mapResultSetToUser(rs);
            } else {
                System.out.println("User not found with username: " + username);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi tìm user by username: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Tìm user theo email
     * @param email Email cần tìm
     * @return User nếu tìm thấy, null nếu không
     */
    public User findByEmail(String email) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_USER_BY_EMAIL)) {
            
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                System.out.println("User found by email: " + email);
                return mapResultSetToUser(rs);
            } else {
                System.out.println("User not found with email: " + email);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi tìm user by email: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Cập nhật thông tin user
     * @param user User object với thông tin mới
     * @return true nếu update thành công
     */
    public boolean updateUser(User user) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(UPDATE_USER)) {
            
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getFullName());
            stmt.setInt(4, user.getRole());
            stmt.setString(5, user.getUserId());
            
            int rows = stmt.executeUpdate();
            if (rows > 0) {
                System.out.println("User updated: " + user.getUsername());
                return true;
            } else {
                System.out.println("No user found to update: " + user.getUserId());
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi update user: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Cập nhật mật khẩu user
     * @param userId ID của user
     * @param newPassword Mật khẩu mới
     * @return true nếu update thành công
     */
    public boolean updatePassword(String userId, String newPassword) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(UPDATE_PASSWORD)) {
            
            stmt.setString(1, newPassword);
            stmt.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
            stmt.setString(3, userId);
            
            int rows = stmt.executeUpdate();
            if (rows > 0) {
                System.out.println("Password updated for user: " + userId);
                return true;
            } else {
                System.out.println("No user found to update password: " + userId);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi update password: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Xóa user
     * @param userId ID của user cần xóa
     * @return true nếu xóa thành công
     */
    public boolean deleteUser(String userId) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(DELETE_USER)) {
            
            stmt.setString(1, userId);
            int rows = stmt.executeUpdate();
            
            if (rows > 0) {
                System.out.println("User deleted: " + userId);
                return true;
            } else {
                System.out.println("No user found to delete: " + userId);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi xóa user: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Kiểm tra username đã tồn tại chưa
     * @param username Username cần kiểm tra
     * @return true nếu đã tồn tại
     */
    public boolean existsByUsername(String username) {
        User user = findByUsername(username);
        return user != null;
    }

    /**
     * Kiểm tra email đã tồn tại chưa
     * @param email Email cần kiểm tra
     * @return true nếu đã tồn tại
     */
    public boolean existsByEmail(String email) {
        User user = findByEmail(email);
        return user != null;
    }

    /**
     * Map ResultSet thành User object
     */
    private User mapResultSetToUser(ResultSet rs) throws SQLException {
        User user = new User();
        user.setUserId(rs.getString("user_id"));
        user.setUsername(rs.getString("username"));
        user.setPassword(rs.getString("password"));
        user.setEmail(rs.getString("email"));
        user.setFullName(rs.getString("full_name"));
        user.setRole(rs.getInt("role"));
        return user;
    }

}
