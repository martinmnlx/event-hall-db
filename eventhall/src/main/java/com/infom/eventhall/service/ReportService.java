package com.infom.eventhall.service;

import com.infom.eventhall.dao.ReportDAO;
import com.infom.eventhall.DatabaseManager;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class ReportService {

    private ReportDAO reportDAO;

    public ReportService(DatabaseManager db) {
        this.reportDAO = db.getReportDAO();
    }

    public List<Map<String, Object>> getMonthlyReservationPerHall(int month, int year) {
        return  reportDAO.getMonthlyReservationReportPerHall(month, year);
    }

    public List<Map<String, Object>> getHallUtilizationReport(LocalDate startingDate, LocalDate endingDate) {
        return reportDAO.getHallUtilizationReport(startingDate, endingDate);
    }

    public List<Map<String, Object>> getEquipmentUtilizationReport(LocalDate startingDate, LocalDate endingDate) {
        return reportDAO.getEquipmentUtilizationReport(startingDate, endingDate);
    }

    public List<Map<String, Object>> getEventTypeReport() {
        return reportDAO.getEventTypeReport();
    }
}
