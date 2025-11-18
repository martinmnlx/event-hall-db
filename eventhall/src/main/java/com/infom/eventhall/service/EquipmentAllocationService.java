package com.infom.eventhall.service;

import com.infom.eventhall.DatabaseManager;
import com.infom.eventhall.dao.EquipmentAllocationDAO;
import com.infom.eventhall.model.EquipmentAllocation;

import java.util.List;

public class EquipmentAllocationService {

    private EquipmentAllocationDAO equipmentAllocationDAO;

    public EquipmentAllocationService(DatabaseManager db) {
        this.equipmentAllocationDAO = db.getEquipmentAllocationDAO();
    }

    public List<EquipmentAllocation> getAllAllocations() {
        return equipmentAllocationDAO.getAllAllocations();
    }

}
