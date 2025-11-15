package com.infom.eventhall.service;

import com.infom.eventhall.model.Equipment;
import com.infom.eventhall.dao.EquipmentDAO;
import com.infom.eventhall.DatabaseManager;

import java.sql.Connection;
import java.util.List;

public class EquipmentService {

    private EquipmentDAO equipmentDAO;

    public EquipmentService(DatabaseManager db) {
        //this.equipmentDAO = db.getEquipmentDAO();
    }

    public List<Equipment> getAllEquipments() {
        return equipmentDAO.getAllEquipments();
    }

    public boolean addEquipment(Equipment equipment) {
        return equipmentDAO.createEquipment(equipment);
    }

    public boolean deleteEquipment(int equipmentId) {
        return equipmentDAO.deleteEquipment(equipmentId);
    }

}
