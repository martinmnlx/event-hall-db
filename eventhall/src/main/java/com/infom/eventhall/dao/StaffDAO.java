package com.infom.eventhall.dao;

import com.infom.eventhall.model.Staff;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StaffDAO {

    private final Connection connection;

    public StaffDAO(Connection connection) {
        this.connection = connection;
    }

    public boolean createStaff(Staff staff) {
        String sql = "INSERT INTO Staff (name, role, phoneNumber) VALUES (?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)){
            stmt.setString(1, staff.getName());
            stmt.setString(2, staff.getRole());
            stmt.setString(3, staff.getPhoneNumber());

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        }
        catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Staff getStaffById(int staffId) {
        String sql = "SELECT * FROM Staff WHERE staffId = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)){
            stmt.setInt(1, staffId);

            try (ResultSet rs = stmt.executeQuery()){
                if (rs.next()){
                    return mapRowToStaff(rs);
                }
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Staff> getAllStaff() {
        List<Staff> staffList = new ArrayList<>();
        String sql = "SELECT * FROM Staff";

        try (PreparedStatement stmt = connection.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery()){

            while (rs.next()){
                staffList.add(mapRowToStaff(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return staffList;
    }

    public boolean updateStaff(Staff staff) {
        String sql = "UPDATE Staff SET name = ?, role = ?, phone = ? WHERE staff_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)){
            stmt.setString(1, staff.getName());
            stmt.setString(2, staff.getRole());
            stmt.setString(3, staff.getPhoneNumber());
            stmt.setInt(4, staff.getStaffId());

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        }
        catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteStaff(int staffId) {
        String sql = "DELETE FROM Staff WHERE staffId = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, staffId);

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private Staff mapRowToStaff(ResultSet rs) throws SQLException {
        Staff staff = new Staff();
        staff.setStaffId(rs.getInt("staff_id"));
        staff.setName(rs.getString("name"));
        staff.setRole(rs.getString("role"));
        staff.setPhoneNumber(rs.getString("phone"));
        return staff;
    }
}
