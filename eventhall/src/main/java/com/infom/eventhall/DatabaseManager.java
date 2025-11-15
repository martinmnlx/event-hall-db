package com.infom.eventhall;

import java.sql.*;

public class DatabaseManager {

    private static final String URL = "jdbc:mysql://localhost:3306/event_hall_reservation";
    private static final String USER = "root";
    private static final String PASSWORD = "REPOgoodVALOnoob48$$";

    private static Connection connection;

    public static Connection getConnection() {
        if (connection == null) {
            try {
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println("Database connected!");
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Failed to connect to database.");
            }
        }
        return connection;
    }
}