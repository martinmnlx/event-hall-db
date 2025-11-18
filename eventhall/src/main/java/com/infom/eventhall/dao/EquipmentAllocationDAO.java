package com.infom.eventhall.dao;

import com.infom.eventhall.model.Equipment;
import com.infom.eventhall.model.EquipmentAllocation;
import com.infom.eventhall.model.User;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EquipmentAllocationDAO {

    private final Connection connection;

    public EquipmentAllocationDAO(Connection connection) {
        this.connection = connection;
    }

    public boolean createAllocation(EquipmentAllocation allocation) {
        String sql = "INSERT INTO Equipment_Allocations (reservation_id, equipment_id, quantity_used) VALUES (?, ?, ?)";

        // sets all the ? parameters
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, allocation.getReservationId());
            stmt.setInt(2, allocation.getEquipmentId());
            stmt.setInt(3, allocation.getQuantityUsed());

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        }
        catch(SQLException e){
            e.printStackTrace();
            return false;
        }
    }

    public EquipmentAllocation getAllocationById(int allocationId) {
        String sql = "SELECT * FROM Equipment_Allocations WHERE allocation_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, allocationId);

            try (ResultSet rs = stmt.executeQuery()){
                if (rs.next()) {
                    return mapRowToAllocation(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<EquipmentAllocation> getAllAllocationsByReservationId(int reservationId) {
        List<EquipmentAllocation> allocationList = new ArrayList<>();
        String sql = "SELECT * FROM Equipment_Allocations WHERE reservation_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, reservationId);

            try (ResultSet rs = stmt.executeQuery()){
                while (rs.next()) {
                    allocationList.add(mapRowToAllocation(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return allocationList;
    }

    public List<EquipmentAllocation> getAllAllocationsByEquipmentId(int equipmentId) {
        List<EquipmentAllocation> allocationList = new ArrayList<>();
        String sql = "SELECT * FROM Equipment_Allocations WHERE equipment_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, equipmentId);

            try (ResultSet rs = stmt.executeQuery()){
                while (rs.next()) {
                    allocationList.add(mapRowToAllocation(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return allocationList;
    }

    public List<EquipmentAllocation> getAllAllocations() {
        List<EquipmentAllocation> allocationList = new ArrayList<>();
        String sql = "SELECT * FROM Equipment_Allocations";

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                allocationList.add(mapRowToAllocation(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return allocationList;
    }

    public int getReservedQuantityForTheDay(int equipmentId, LocalDate eventDate) {
        // to prevent giving garbage values (For IFNULL)
        String sql = "SELECT IFNULL(SUM(ea.quantity_used), 0) FROM equipment_allocations ea " +
                "JOIN reservations r ON ea.reservation_id = r.reservation_id " +
                "WHERE ea.equipment_id = ? AND r.status IN ('Confirmed', 'Pending') AND r.event_date = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, equipmentId);
            stmt.setDate(2, Date.valueOf(eventDate));

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public boolean updateAllocation(EquipmentAllocation allocation) {
        String sql = "UPDATE Equipment_Allocations SET reservation_id = ?, equipment_id = ? WHERE allocation_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)){

            stmt.setInt(1, allocation.getReservationId());
            stmt.setInt(2, allocation.getEquipmentId());
            stmt.setInt(4, allocation.getAllocationId());

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        }
        catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteAllocation(int allocationId) {
        String sql = "DELETE FROM Equipment_Allocations WHERE allocation_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)){
            stmt.setInt(1, allocationId);

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        }
        catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private EquipmentAllocation mapRowToAllocation(ResultSet rs) throws SQLException {
        EquipmentAllocation allocation = new EquipmentAllocation();
        allocation.setAllocationId(rs.getInt("allocation_id"));
        allocation.setReservationId(rs.getInt("reservation_id"));
        allocation.setEquipmentId(rs.getInt("equipment_id"));
        allocation.setQuantityUsed(rs.getInt("quantity_used"));
        return allocation;
    }
}
