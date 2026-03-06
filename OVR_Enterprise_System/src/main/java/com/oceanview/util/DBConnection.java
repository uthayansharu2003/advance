package com.oceanview.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    // Forward-thinking: Variables extracted to constants for easy environment switching
    private static final String URL = "jdbc:mysql://localhost:3306/ovr_enterprise_db?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = ""; // Update this to your local MySQL password
    
    // Volatile keyword ensures thread-safe Singleton
    private static volatile Connection connection = null;

    // Private constructor prevents instantiation
    private DBConnection() {}

    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                synchronized (DBConnection.class) {
                    if (connection == null || connection.isClosed()) {
                        // Register JDBC driver implicitly required in some Tomcat environments
                        Class.forName("com.mysql.cj.jdbc.Driver");
                        connection = DriverManager.getConnection(URL, USER, PASSWORD);
                        System.out.println("[SYSTEM] Database Connection Established Successfully.");
                    }
                }
            }
        } catch (ClassNotFoundException e) {
            System.err.println("[ERROR] MySQL Driver not found. Check WEB-INF/lib.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("[ERROR] Database connection failed.");
            e.printStackTrace();
        }
        return connection;
    }
}