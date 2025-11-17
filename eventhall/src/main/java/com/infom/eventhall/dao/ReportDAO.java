package com.infom.eventhall.dao;


import java.sql.*;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class ReportDAO {

    private final Connection connection;

    public ReportDAO(Connection connection) {
        this.connection = connection;
    }

    // counts all of the confirmed reservations for a specific month and year
    // format will be like (Hall 1 -> 5 bookings, Hall 2 -> 8 bookings)
    public Map<Integer, Integer> getMonthlyReservationReportPerHall(int month, int year) {
        Map<Integer, Integer> reportOnMonthlyReservationPerHall = new HashMap<>();
        String sql = "SELECT hall_id, COUNT(*) as reservation_count FROM Reservations " +
                     "WHERE MONTH(event_date) = ? AND YEAR(event_date) = ? " +
                     "AND status = 'Confirmed' " +
                     "GROUP BY hall_id";

        // sets all the ? parameters
        try (PreparedStatement stmt = connection.prepareStatement(sql)){
            stmt.setInt(1, month);
            stmt.setInt(2, year);

            try (ResultSet rs = stmt.executeQuery()){
                while (rs.next()) {
                    // report will now be added as (hall_id, count) to the map
                    reportOnMonthlyReservationPerHall.put(rs.getInt("hall_id"), rs.getInt("reservation_count"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reportOnMonthlyReservationPerHall;
    }

    // counts the number of days a hall was booked on a specific date range
    // format will be like (Hall 1 -> 5 days, Hall 2 -> 8 days)
    public Map<Integer, Integer> getHallUtilizationReport(LocalDate startingDate, LocalDate endingDate) {
        Map<Integer, Integer> reportOnHallUtilization = new HashMap<>();
        String sql = "SELECT hall_id, COUNT(DISTINCT event_date) as days_booked FROM Reservations " +
                "WHERE event_date BETWEEN ? AND ? " +
                "AND status = 'Confirmed' " +
                "GROUP BY hall_id";

        // sets all the ? parameters
        try (PreparedStatement stmt = connection.prepareStatement(sql)){
            stmt.setDate(1, Date.valueOf(startingDate));
            stmt.setDate(2, Date.valueOf(endingDate));

            try (ResultSet rs = stmt.executeQuery()){
                while (rs.next()) {
                    // report will now be added as (hall_id, days_booked) to the map
                    reportOnHallUtilization.put(rs.getInt("hall_id"), rs.getInt("days_booked"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reportOnHallUtilization;
    }

    // counts the confirmed reservations with its event type
    // format will be like (Wedding 1 -> 5 bookings, Birthdays 2 -> 8 bookings)
    public Map<String, Integer> getEventTypeReport() {
        Map<String, Integer> reportOnEventType = new HashMap<>();
        String sql = "SELECT event_type, COUNT(*) as event_type_count FROM Reservations " +
                "WHERE status = 'Confirmed' " +
                "GROUP BY event_type";

        try (PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery()) {

                while (rs.next()) {
                    // report will now be added as (hall_id, days_booked) to the map
                    reportOnEventType.put(rs.getString("event_type"), rs.getInt("event_type_count"));
                }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reportOnEventType;
    }
}
