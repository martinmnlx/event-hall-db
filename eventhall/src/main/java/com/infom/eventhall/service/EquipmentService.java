package com.infom.eventhall.service;

import com.infom.eventhall.model.Equipment;
import com.infom.eventhall.dao.EquipmentDAO;
import com.infom.eventhall.DatabaseManager;

import java.sql.Connection;
import java.util.List;

public class EquipmentService {

    private EquipmentDAO equipmentDAO;

    public EquipmentService(DatabaseManager db) {
        this.equipmentDAO = db.getEquipmentDAO();
    }

    public Equipment getEquipmentById(int equipmentId) {
        return equipmentDAO.getEquipmentById(equipmentId);
    }

    public List<Equipment> getEquipmentByName(String equipmentName) {
        return equipmentDAO.getEquipmentByName(equipmentName);
    }

    public List<Equipment> getEquipmentByQuantity(int quantity) {
        return equipmentDAO.getEquipmentByQuantity(quantity);
    }

    public List<Equipment> getEquipmentByStatus(Equipment.EquipmentStatus status) {
        return equipmentDAO.getEquipmentByStatus(status);
    }

    public List<Equipment> getAllEquipments() {
        return equipmentDAO.getAllEquipments();
    }

    // Admin ability
    public boolean addEquipment(Equipment equipment) {
        return equipmentDAO.createEquipment(equipment);
    }

    public boolean updateEquipment(Equipment equipment) {
        return equipmentDAO.updateEquipment(equipment);
    }

    public boolean deleteEquipment(int equipmentId) {
        return equipmentDAO.deleteEquipment(equipmentId);
    }

}
