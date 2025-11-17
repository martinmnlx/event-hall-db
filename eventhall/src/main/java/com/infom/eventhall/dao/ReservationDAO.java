package com.infom.eventhall.dao;

import com.infom.eventhall.model.Reservation;
import com.infom.eventhall.model.User;
import lombok.Getter;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ReservationDAO {

    @Getter
    private final Connection connection;

    public ReservationDAO(Connection connection) {
        this.connection = connection;
    }

    public boolean createReservation(Reservation reservation) {
        String sql = "INSERT INTO Reservations (user_id, hall_id, staff_id, created_on, event_date, event_type, guest_count, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        // sets all the ? parameters
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, reservation.getUserId());
            stmt.setInt(2, reservation.getHallId());
            stmt.setInt(3, reservation.getStaffId());
            stmt.setTimestamp(4, Timestamp.valueOf(reservation.getCreatedOn()));
            stmt.setDate(5, Date.valueOf(reservation.getEventDate()));
            stmt.setString(6, reservation.getEventType());
            stmt.setInt(7, reservation.getGuestCount());
            stmt.setString(8, reservation.getStatus().name());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) return false;

            // get the generated ID
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    reservation.setReservationId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Creating reservation failed, no ID obtained.");
                }
            }

            return true;
        } catch(SQLException e){
            e.printStackTrace();
            return false;
        }
    }

    public Reservation getReservationById(int reservationId) {
        String sql = "SELECT * FROM Reservations WHERE reservation_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, reservationId);

            try (ResultSet rs = stmt.executeQuery()){
                if (rs.next()) {
                    return mapRowToReservation(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Reservation> getReservationByUserId(int userId) {
        List<Reservation> reservationsList = new ArrayList<>();
        String sql = "SELECT * FROM Reservations WHERE user_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);

            try (ResultSet rs = stmt.executeQuery()){
                while (rs.next()) {
                    reservationsList.add(mapRowToReservation(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reservationsList;
    }

    public List<Reservation> getReservationByHallId(int hallId) {
        List<Reservation> reservationsList = new ArrayList<>();
        String sql = "SELECT * FROM Reservations WHERE hall_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, hallId);

            try (ResultSet rs = stmt.executeQuery()){
                while (rs.next()) {
                    reservationsList.add(mapRowToReservation(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reservationsList;
    }

    public List<Reservation> getReservationByStaffId(int staffId) {
        List<Reservation> reservationsList = new ArrayList<>();
        String sql = "SELECT * FROM Reservations WHERE staff_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, staffId);

            try (ResultSet rs = stmt.executeQuery()){
                while (rs.next()) {
                    reservationsList.add(mapRowToReservation(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reservationsList;
    }

    public List<Reservation> getAllReservations() {
        List<Reservation> reservationsList = new ArrayList<>();
        String sql = "SELECT * FROM Reservations";

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()){

            while(rs.next()){
                reservationsList.add(mapRowToReservation(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reservationsList;
    }

    public boolean updateReservation(Reservation reservation) {
        String sql = "UPDATE Reservations SET user_id = ?, hall_id = ?, staff_id = ?, event_date = ?, event_type = ?, guest_count = ?, status = ? WHERE reservation_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)){

            stmt.setInt(1, reservation.getUserId());
            stmt.setInt(2, reservation.getHallId());
            stmt.setInt(3, reservation.getStaffId());
            stmt.setDate(4, java.sql.Date.valueOf(reservation.getEventDate()));
            stmt.setString(5, reservation.getEventType());
            stmt.setInt(6, reservation.getGuestCount());
            stmt.setString(7, reservation.getStatus().name());
            stmt.setInt(8, reservation.getReservationId());

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        }
        catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteReservation(int reservationId) {
        String sql = "DELETE FROM Reservations WHERE reservation_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)){
            stmt.setInt(1, reservationId);

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        }
        catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Integer> findConflictingHallIds(LocalDate eventDate) {
        List<Integer> conflictingHallIds = new ArrayList<>();

        // This query finds any booking that overlaps with the requested date
        String sql = "SELECT r.hall_id FROM Reservations r " +
                "WHERE r.status IN ('Confirmed', 'Pending') " +
                "AND r.event_date = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setDate(1, Date.valueOf(eventDate));

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    conflictingHallIds.add(rs.getInt("hall_id"));
                }
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return conflictingHallIds;
    }

    private Reservation mapRowToReservation(ResultSet rs) throws SQLException {
        Reservation reservation = new Reservation();
        reservation.setReservationId(rs.getInt("reservation_id"));
        reservation.setUserId(rs.getInt("user_id"));
        reservation.setHallId(rs.getInt("hall_id"));
        reservation.setStaffId(rs.getInt("staff_id"));
        reservation.setCreatedOn(rs.getTimestamp("created_on").toLocalDateTime());
        reservation.setEventDate(rs.getDate("event_date").toLocalDate());
        reservation.setEventType(rs.getString("event_type"));
        reservation.setGuestCount(rs.getInt("guest_count"));
        reservation.setStatus(Reservation.ReservationStatus.valueOf(rs.getString("status")));
        return reservation;
    }
}
