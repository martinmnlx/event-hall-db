package com.infom.eventhall.service;

import com.infom.eventhall.dao.ReportDAO;
import com.infom.eventhall.DatabaseManager;
import java.time.LocalDate;
import java.util.Map;

public class ReportService {

    private ReportDAO reportDAO;

    public ReportService(DatabaseManager db) {
        // this.reportDAO = db.getReportDAO();
    }

    public Map<Integer, Integer> getMonthlyReservationPerHall(int month, int year) {
        return  reportDAO.getMonthlyReservationReportPerHall(month, year);
    }

    public Map<Integer, Integer> getHallUtilization(LocalDate startingDate, LocalDate endingDate) {
        return reportDAO.getHallUtilizationReport(startingDate, endingDate);
    }

    public Map<String, Integer> getHallUtilizationReport() {
        return reportDAO.getEventTypeReport();
    }
}
