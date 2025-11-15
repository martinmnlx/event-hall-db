package com.infom.eventhall.service;

import com.infom.eventhall.dao.UserDAO;
import com.infom.eventhall.model.Staff;
import com.infom.eventhall.dao.StaffDAO;
import com.infom.eventhall.DatabaseManager;

import java.sql.Connection;
import java.util.List;

public class StaffService {

    private StaffDAO staffDAO;

    public StaffService(DatabaseManager db) {
        //this.staffDAO = db.getStaffDAO();
    }

    public List<Staff> getAllStaff() {
        return staffDAO.getAllStaff();
    }

    public boolean addStaff(Staff staff) {
        return staffDAO.createStaff(staff);
    }

    public boolean deleteStaff(int staffId) {
        return staffDAO.deleteStaff(staffId);
    }
}
