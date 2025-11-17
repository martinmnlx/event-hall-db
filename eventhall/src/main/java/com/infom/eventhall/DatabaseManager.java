package com.infom.eventhall;

import com.infom.eventhall.dao.*;

import java.sql.*;

import com.infom.eventhall.model.EquipmentAllocation;
import lombok.Data;

@Data

public class DatabaseManager {

    private static Connection connection;

    private UserDAO userDAO;
    private StaffDAO staffDAO;
    private EventHallDAO eventHallDAO;
    private ReservationDAO reservationDAO;
    private EquipmentDAO equipmentDAO;
    private EquipmentAllocationDAO equipmentAllocationDAO;

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
        staffDAO = new StaffDAO(connection);
        eventHallDAO = new EventHallDAO(connection);
        reservationDAO = new ReservationDAO(connection);
        equipmentDAO = new EquipmentDAO(connection);
        equipmentAllocationDAO = new EquipmentAllocationDAO(connection);
    }
}