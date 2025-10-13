package main.java.test;

import io.github.cdimascio.dotenv.Dotenv;

/**
 * Test riêng thư viện Dotenv - kiểm tra xem .env có được load đúng không
 */
public class DotenvTest {
    public static void main(String[] args) {
        System.out.println("=== Dotenv Test ===");
        System.out.println("Working directory: " + System.getProperty("user.dir"));
        System.out.println();

        Dotenv dotenv = Dotenv.configure()
                .directory("./")
                .ignoreIfMissing()
                .load();

        System.out.println("Dotenv loaded successfully!");
        System.out.println();

        System.out.println("Environment variables from .env:");
        System.out.println("--------------------------------");
        
        String dbUrl = dotenv.get("DB_URL");
        String dbUsername = dotenv.get("DB_USERNAME");
        String dbPassword = dotenv.get("DB_PASSWORD");
        String dbDriver = dotenv.get("DB_DRIVER");

        System.out.println("DB_URL      = " + (dbUrl != null ? dbUrl : "(not set)"));
        System.out.println("DB_USERNAME = " + (dbUsername != null ? dbUsername : "(not set)"));
        System.out.println("DB_PASSWORD = " + (dbPassword != null ? dbPassword : "(not set)"));
        System.out.println("DB_DRIVER   = " + (dbDriver != null ? dbDriver : "(not set)"));
        System.out.println();

        java.io.File envFile = new java.io.File(".env");
        System.out.println(".env file exists: " + envFile.exists());
        System.out.println(".env file path: " + envFile.getAbsolutePath());
        
        if (envFile.exists()) {
            System.out.println(".env file size: " + envFile.length() + " bytes");
        }
        
        System.out.println();
        System.out.println("=== Test completed ===");
    }
}
