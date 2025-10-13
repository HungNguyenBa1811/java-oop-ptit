package main.java.config;

import io.github.cdimascio.dotenv.Dotenv;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


/**
 * DatabaseConnection - Singleton pattern
 * Quản lý kết nối database dùng Dotenv để load config, VSCode báo đỏ nhưng dùng bình thường.
 */
public class DatabaseConnection {
    private static Connection connection = null;
    private static final Dotenv dotenv = Dotenv.configure()
            .directory("./")  // Tìm file .env ở root project
            .ignoreIfMissing()  // Không báo lỗi nếu không có .env
            .load();

    // Private constructor - Singleton
    private DatabaseConnection() {
    }

    /**
     * Get database connection (Singleton)
     */
    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                // Load config từ .env
                String url = dotenv.get("DB_URL", "jdbc:mysql://localhost:3306/course_registration");
                String username = dotenv.get("DB_USERNAME");
                String password = dotenv.get("DB_PASSWORD");
                String driver = dotenv.get("DB_DRIVER", "com.mysql.cj.jdbc.Driver");

                // Load JDBC Driver
                Class.forName(driver);

                // Create connection
                connection = DriverManager.getConnection(url, username, password);
                System.out.println("✅ Database connected successfully!");
            }
        } catch (ClassNotFoundException e) {
            System.err.println("❌ JDBC Driver not found: " + e.getMessage());
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("❌ Database connection failed: " + e.getMessage());
            e.printStackTrace();
        }
        return connection;
    }

    /**
     * Close connection
     */
    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("✅ Database connection closed.");
            }
        } catch (SQLException e) {
            System.err.println("❌ Error closing connection: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Test connection
     */
    public static boolean testConnection() {
        try {
            Connection conn = getConnection();
            return conn != null && !conn.isClosed();
        } catch (SQLException e) {
            return false;
        }
    }
}