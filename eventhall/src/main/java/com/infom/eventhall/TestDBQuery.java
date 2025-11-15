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

            // Execute a query
            String sql = "SELECT user_id, name, email FROM Users";
            ResultSet rs = stmt.executeQuery(sql);

            // Print the results
            while (rs.next()) {
                int id = rs.getInt("user_id");
                String name = rs.getString("name");
                String email = rs.getString("email");

                System.out.println("ID: " + id + ", Name: " + name + ", Email: " + email);
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
