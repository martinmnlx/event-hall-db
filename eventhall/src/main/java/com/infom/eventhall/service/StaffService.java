package com.infom.eventhall.service;


import com.infom.eventhall.model.Staff;
import com.infom.eventhall.dao.StaffDAO;
import com.infom.eventhall.DatabaseManager;
import java.util.List;

public class StaffService {

    private StaffDAO staffDAO;

    public StaffService(DatabaseManager db) {
        //this.staffDAO = db.getStaffDAO();
    }

    public Staff getStaffById(int staffId){
        return staffDAO.getStaffById(staffId);
    }

    public List<Staff> getStaffByName(String staffName) {
        return staffDAO.getStaffByName(staffName);
    }

    public List<Staff> getStaffByRole(String role) {
        return staffDAO.getStaffByRole(role);
    }

    public List<Staff> getAllStaff() {
        return staffDAO.getAllStaff();
    }

    public boolean addStaff(Staff staff) {
        return staffDAO.createStaff(staff);
    }

    public boolean updateStaff(Staff staff) {
        return staffDAO.updateStaff(staff);
    }

    public boolean deleteStaff(int staffId) {
        return staffDAO.deleteStaff(staffId);
    }
}
