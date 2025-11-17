package com.infom.eventhall;

import java.sql.*;

public class TestDBQuery {

    public static void main(String[] args) {

        String url = "jdbc:mysql://localhost:3306/event_hall_reservation";
        String user = "root"; // your MySQL username
        String password = "REPOgoodVALOnoob48$$";

        try {
            // Load the MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Connect to the database
            Connection conn = DriverManager.getConnection(url, user, password);

            // Create a statement
            Statement stmt = conn.createStatement();

            System.out.println("-- Users Table --------------------");

            // Execute a query
            String sql = "SELECT * FROM Users";
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                System.out.printf("%-5d %-10s %-20s %-30s %-15s %-20s%n",
                        rs.getInt("user_id"),
                        rs.getString("type"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("phone"),
                        rs.getTimestamp("created_on"));
            }

            System.out.println();
            System.out.println("-- Event Halls Table --------------------");

            // Execute a query
            sql = "SELECT * FROM Event_Halls";
            rs = stmt.executeQuery(sql);

            // Print the results
            while (rs.next()) {
                System.out.printf("%-5d %-30s %-10d %-15s%n",
                        rs.getInt("hall_id"),
                        rs.getString("hall_name"),
                        rs.getInt("capacity"),
                        rs.getString("location"));
            }

            // Close resources
            rs.close();
            stmt.close();
            conn.close();

        } catch (ClassNotFoundException e) {
            System.out.println("JDBC Driver not found.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("Database error.");
            e.printStackTrace();
        }
    }
}
