package com.infom.eventhall;

import com.infom.eventhall.dao.*;

import java.sql.*;

import lombok.Data;

@Data

public class DatabaseManager {

    private static Connection connection;

    private UserDAO userDAO;
    private DashboardDAO dashboardDAO;
    private EventHallDAO eventHallDAO;

    public DatabaseManager() {
        connectToDatabase();
        initializeDAOs();
    }

    public void connectToDatabase() {
        try {
            String url = "jdbc:mysql://localhost:3306/event_hall_reservation";
            String username = "root";
            String password = "REPOgoodVALOnoob48$$";
            connection = DriverManager.getConnection(url, username, password);
            System.out.println("Database connected!");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Failed to connect to database.");
        }
    }

    private void initializeDAOs() {
        userDAO = new UserDAO(connection);
        dashboardDAO = new DashboardDAO(connection);
        eventHallDAO = new EventHallDAO(connection);
    }
}