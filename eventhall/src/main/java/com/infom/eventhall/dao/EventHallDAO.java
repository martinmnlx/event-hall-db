package com.infom.eventhall.dao;

import com.infom.eventhall.model.EventHall;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EventHallDAO {

    private final Connection connection;

    public EventHallDAO(Connection connection) {
        this.connection = connection;
    }

    public boolean createEventHall(EventHall eventHall) {
        String sql = "INSERT INTO Event_Halls (hall_name, capacity, location, status) VALUES (?, ?, ?, ?)";

        // sets all the ? parameters
        try (PreparedStatement stmt = connection.prepareStatement(sql)){
            stmt.setString(1, eventHall.getHallName());
            stmt.setInt(2, eventHall.getCapacity());
            stmt.setString(3, eventHall.getLocation());
            stmt.setString(4, eventHall.getStatus().name());

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        }
        catch(SQLException e){
            e.printStackTrace();
            return false;
        }
    }

    public EventHall getHallById(int hallId) {
        String sql = "SELECT * FROM Event_Halls WHERE hall_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, hallId);

            try (ResultSet rs = stmt.executeQuery()){
                if (rs.next()) {
                    return mapRowToEventHall(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public EventHall getHallByName(String eventHallName) {
        String sql = "SELECT * FROM Event_Halls WHERE hall_name = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, eventHallName);

            try (ResultSet rs = stmt.executeQuery()){
                if (rs.next()) {
                    return mapRowToEventHall(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<EventHall> getHallByCapacity(int capacity) {
        List<EventHall> EventHallList = new ArrayList<>();
        String sql = "SELECT * FROM Event_Halls WHERE capacity >= ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, capacity);

            try (ResultSet rs = stmt.executeQuery()){
                while (rs.next()) {
                    EventHallList.add(mapRowToEventHall(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return EventHallList;
    }

    public List<EventHall> getHallByLocation(String location) {
        List<EventHall> EventHallList = new ArrayList<>();
        String sql = "SELECT * FROM Event_Halls WHERE location = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, location);

            try (ResultSet rs = stmt.executeQuery()){
                while (rs.next()) {
                    EventHallList.add(mapRowToEventHall(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return EventHallList;
    }

    public List<EventHall> getAllEventHalls() {
        List<EventHall> hallList = new ArrayList<>();
        String sql = "SELECT * FROM Event_Halls";
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()){

            while(rs.next()){
                hallList.add(mapRowToEventHall(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return hallList;
    }

    public boolean updateEventHall(EventHall eventHall) {
        String sql = "UPDATE Event_Halls SET hall_name = ?, capacity = ?, location = ?, status = ? WHERE hall_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)){

            stmt.setString(1, eventHall.getHallName());
            stmt.setInt(2, eventHall.getCapacity());
            stmt.setString(3, eventHall.getLocation());
            stmt.setString(4, eventHall.getStatus().name());
            stmt.setInt(5, eventHall.getHallId());

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        }
        catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteEventHall(int hallId) {
        String sql = "DELETE FROM Event_Halls WHERE hall_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)){
            stmt.setInt(1, hallId);

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        }
        catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private EventHall mapRowToEventHall(ResultSet rs) throws SQLException {
        EventHall eventHall = new EventHall();
        eventHall.setHallId(rs.getInt("hall_id"));
        eventHall.setHallName(rs.getString("hall_name"));
        eventHall.setCapacity(rs.getInt("capacity"));
        eventHall.setLocation(rs.getString("location"));
        eventHall.setStatus(EventHall.HallStatus.valueOf(rs.getString("status")));
        return eventHall;
    }
}
