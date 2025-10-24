package main.java.test;

import main.java.config.DatabaseConnection;

public class DBTest {
    public static void main(String[] args) {
        System.out.println("Starting DB test...");
        boolean ok = DatabaseConnection.testConnection();
        System.out.println("DB connection test result: " + ok);
        DatabaseConnection.closeConnection();
    }
}
