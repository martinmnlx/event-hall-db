package com.infom.eventhall.dao;


import java.sql.*;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;

public class ReportDAO {

    private final Connection connection;

    public ReportDAO(Connection connection) {
        this.connection = connection;
    }

    // counts all of the confirmed reservations for a specific month and year
    // returns a list of { "hallName": "Emerald Hall", "reservationCount": 5 }
    public List<Map<String, Object>> getMonthlyReservationReportPerHall(int month, int year) {
        List<Map<String, Object>> reportOnMonthlyReservationPerHall = new ArrayList<>();
        String sql = "SELECT h.hall_name, COUNT(r.reservation_id) as reservation_count " +
                     "FROM Reservations r " +
                     "JOIN Event_Halls h ON r.hall_id = h.hall_id " +
                     "WHERE MONTH(r.event_date) = ? AND YEAR(r.event_date) = ? " +
                     "AND r.status = 'Confirmed' " +
                     "GROUP BY h.hall_name";
        // sets all the ? parameters
        try (PreparedStatement stmt = connection.prepareStatement(sql)){
            stmt.setInt(1, month);
            stmt.setInt(2, year);

            try (ResultSet rs = stmt.executeQuery()){
                while (rs.next()) {
                    Map<String, Object> row = new HashMap<>();
                    row.put("hallName", rs.getString("hall_name"));
                    row.put("reservationCount", rs.getInt("reservation_count"));
                    reportOnMonthlyReservationPerHall.add(row);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reportOnMonthlyReservationPerHall;
    }

    // counts the number of days a hall was booked on a specific date range
    // returns a list of { "hallName": "Emerald Hall", "daysBooked": 5 }
    public List<Map<String, Object>> getHallUtilizationReport(LocalDate startingDate, LocalDate endingDate) {
        List<Map<String, Object>> reportOnHallUtilization = new ArrayList<>();
        String sql = "SELECT h.hall_name, COUNT(DISTINCT r.event_date) as days_booked " +
                     "FROM Reservations r " +
                     "JOIN Event_Halls h ON r.hall_id = h.hall_id " +
                     "WHERE r.event_date BETWEEN ? AND ? " +
                     "AND r.status = 'Confirmed' " +
                     "GROUP BY h.hall_name";

        // sets all the ? parameters
        try (PreparedStatement stmt = connection.prepareStatement(sql)){
            stmt.setDate(1, Date.valueOf(startingDate));
            stmt.setDate(2, Date.valueOf(endingDate));

            try (ResultSet rs = stmt.executeQuery()){
                while (rs.next()) {
                    Map<String, Object> row = new HashMap<>();
                    row.put("hallName", rs.getString("hall_name"));
                    row.put("daysBooked", rs.getInt("days_booked"));
                    reportOnHallUtilization.add(row);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reportOnHallUtilization;
    }

    // counts the number of times equipment was used and total quantity allocated on a specific date rang
    // returns a list of { "equipmentName": "Projector", "reservationCount": 5, "quantityRented": 20 }
    public List<Map<String, Object>> getEquipmentUtilizationReport(LocalDate startingDate, LocalDate endingDate) {
        List<Map<String, Object>> reportOnHallUtilization = new ArrayList<>();
        String sql = "SELECT e.equipment_name, " +
                     "COUNT(ea.allocation_id) as reservation_count, " +
                     "SUM(ea.quantity_used) as quantity_rented " +
                     "JOIN reservations r ON ea.reservation_id = r.reservation_id " +
                     "JOIN equipment e ON ea.equipment_id = e.equipment_id " +
                     "WHERE r.event_date BETWEEN ? AND ? " +
                     "AND r.status = 'Confirmed' " +
                     "GROUP BY e.equipment_name";

        // sets all the ? parameters
        try (PreparedStatement stmt = connection.prepareStatement(sql)){
            stmt.setDate(1, Date.valueOf(startingDate));
            stmt.setDate(2, Date.valueOf(endingDate));

            try (ResultSet rs = stmt.executeQuery()){
                while (rs.next()) {
                    Map<String, Object> row = new HashMap<>();
                    row.put("equipmentName", rs.getString("equipment_name"));
                    row.put("reservationCount", rs.getInt("reservation_count"));
                    row.put("quantityRented", rs.getInt("quantity_rented"));

                    reportOnHallUtilization.add(row);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reportOnHallUtilization;
    }

    // counts the confirmed reservations with its event type
    // returns a list of { "eventType": "Wedding", "count": 5 }
    public List<Map<String, Object>> getEventTypeReport() {
        List<Map<String, Object>> reportOnEventType = new ArrayList<>();
        String sql = "SELECT event_type, COUNT(*) as event_type_count FROM Reservations " +
                     "WHERE status = 'Confirmed' " +
                     "GROUP BY event_type";

        try (PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery()) {

                while (rs.next()) {
                    Map<String, Object> row = new HashMap<>();
                    row.put("eventType", rs.getString("event_type"));
                    row.put("count", rs.getInt("event_type_count"));
                    reportOnEventType.add(row);
                }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reportOnEventType;
    }
}
