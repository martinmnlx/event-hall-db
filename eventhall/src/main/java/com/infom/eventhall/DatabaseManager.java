package com.infom.eventhall;

import java.sql.*;

public class DatabaseManager {

    // Your manual connection details
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/event_hall_reservation";
    private static final String JDBC_USER = "root"; // <-- SET YOUR USERNAME
    private static final String JDBC_PASS = "REPOgoodVALOnoob48$$"; // <-- SET YOUR PASSWORD

    // A single, static method to get a connection from anywhere in your code
    public static Connection getConnection() throws SQLException {
        try {
            // Manually load the MySQL driver
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL JDBC Driver not found", e);
        }
        // Return the new connection
        return DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASS);
    }
}