package com.infom.eventhall.dao;

import com.infom.eventhall.model.Equipment;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EquipmentDAO {

    private final Connection connection;

    public EquipmentDAO(Connection connection) {
        this.connection = connection;
    }

    public boolean createEquipment(Equipment equipment) {
        String sql = "INSERT INTO Equipment (equipment_name, quantity_total, status) VALUES (?, ?, ?)";

        // sets all the ? parameters
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, equipment.getEquipmentName());
            stmt.setInt(2, equipment.getQuantityTotal());
            stmt.setString(3, equipment.getEquipment().name());

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        }
        catch(SQLException e){
            e.printStackTrace();
            return false;
        }
    }

    public Equipment getEquipmentByID(int equipmentId) {
        String sql = "SELECT * FROM Equipment WHERE equipment_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, equipmentId);

            try (ResultSet rs = stmt.executeQuery()){
                if (rs.next()) {
                    return mapRowToEquipment(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Equipment> getEquipmentByName(String equipmentName) {
        List<Equipment> equipmentList = new ArrayList<>();
        String sql = "SELECT * FROM Equipment WHERE equipment_name = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, equipmentName);

            try (ResultSet rs = stmt.executeQuery()){
                while (rs.next()) {
                    equipmentList.add(mapRowToEquipment(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return equipmentList;
    }

    public List<Equipment> getEquipmentByQuantity(int quantity) {
        List<Equipment> equipmentList = new ArrayList<>();
        String sql = "SELECT * FROM Equipment WHERE quantity_total >= ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, quantity);

            try (ResultSet rs = stmt.executeQuery()){
                while (rs.next()) {
                    equipmentList.add(mapRowToEquipment(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return equipmentList;
    }

    public List<Equipment> getEquipmentByStatus(Equipment.EquipmentStatus status) {
        List<Equipment> equipmentList = new ArrayList<>();
        String sql = "SELECT * FROM Equipment WHERE status = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, status.name());

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    equipmentList.add(mapRowToEquipment(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return equipmentList;
    }

    public List<Equipment> getAllEquipments() {
        List<Equipment> equipmentList = new ArrayList<>();
        String sql = "SELECT * FROM Equipment";

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()){

            while(rs.next()){
                equipmentList.add(mapRowToEquipment(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return equipmentList;
    }

    public boolean updateEquipment(Equipment equipment) {
        String sql = "UPDATE Equipment SET equipment_name = ?, quantity_total = ?, status = ? WHERE equipment_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)){

            stmt.setString(1, equipment.getEquipmentName());
            stmt.setInt(2, equipment.getQuantityTotal());
            stmt.setString(3, equipment.getEquipment().name());
            stmt.setInt(4, equipment.getEquipmentId());

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        }
        catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteEquipment(int equipmentId) {
        String sql = "DELETE FROM Equipment WHERE equipment_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)){
            stmt.setInt(1, equipmentId);

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        }
        catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private Equipment mapRowToEquipment(ResultSet rs) throws SQLException {
        Equipment equipment = new Equipment();
        equipment.setEquipmentId(rs.getInt("equipment_id"));
        equipment.setEquipmentName(rs.getString("equipment_name"));
        equipment.setQuantityTotal(rs.getInt("quantity_total"));
        equipment.setEquipment(Equipment.EquipmentStatus.valueOf(rs.getString("status")));
        return equipment;
    }
}
