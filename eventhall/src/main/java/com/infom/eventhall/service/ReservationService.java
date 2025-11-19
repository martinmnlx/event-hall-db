package com.infom.eventhall.service;

import com.infom.eventhall.model.*;
import com.infom.eventhall.dao.*;
import com.infom.eventhall.DatabaseManager;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


public class ReservationService {

    private ReservationDAO reservationDAO;
    private EquipmentAllocationDAO allocationDAO;
    private EquipmentDAO equipmentDAO;
    private EventHallDAO eventHallDAO;
    private EventHallService eventHallService;
    private Connection connection;

    public ReservationService(DatabaseManager db) {
        this.reservationDAO = db.getReservationDAO();
        this.allocationDAO = db.getEquipmentAllocationDAO();
        this.equipmentDAO = db.getEquipmentDAO();
        this.eventHallDAO = db.getEventHallDAO();
        this.eventHallService = new EventHallService(db);
        this.connection = reservationDAO.getConnection();
    }

    public ReservationResult createReservation(Reservation reservation, List<EquipmentAllocation> allocations) {

        try {
            connection.setAutoCommit(false);

            //Check hall availability
            List<EventHall> availableEventHalls = eventHallService.findAllAvailableEventHalls(reservation.getEventDate());
            boolean isAvailable = availableEventHalls.stream()
                    .anyMatch(h -> h.getHallId().equals(reservation.getHallId()));

            if (!isAvailable) {
                throw new IllegalStateException("Event hall has already been reserved on the chosen date.");
            }

            //Check equipment availability
            for (EquipmentAllocation allocation : allocations) {
                Equipment equipment = equipmentDAO.getEquipmentById(allocation.getEquipmentId());
                if (equipment == null) {
                    throw new IllegalStateException("Equipment not found: " + allocation.getEquipmentId());
                }

                int reservedEquipment = allocationDAO.getReservedQuantityForTheDay(allocation.getEquipmentId(), reservation.getEventDate());
                int remainingStock = equipment.getQuantityTotal() - reservedEquipment;

                if (allocation.getQuantityUsed() > remainingStock) {
                    throw new IllegalStateException("All " + equipmentDAO.getEquipmentById(allocation.getEquipmentId()).getEquipmentName() + " quantity have been reserved on the chosen date.");
                }
            }

            reservation.setCreatedOn(LocalDateTime.now());
            if (!reservationDAO.createReservation(reservation)) {
                throw new IllegalStateException("Unable to create reservation");
            }

            for (EquipmentAllocation allocation : allocations) {
                allocation.setReservationId(reservation.getReservationId());
                if (!allocationDAO.createAllocation(allocation)) {
                    throw new IllegalStateException("Unable to create allocation");
                }
            }

            connection.commit();
            return new ReservationResult(true, "Reservation created successfully");

        } catch (Exception e) {

            // Rollback
            try { connection.rollback(); } catch (SQLException ignore) {}

            return new ReservationResult(false, e.getMessage());

        } finally {
            try { connection.setAutoCommit(true); } catch (SQLException ignore) {}
        }
    }

    public boolean cancelReservation(int reservationId) {
        try{
            // locate the reservation record
            Reservation reservation = reservationDAO.getReservationById(reservationId);

            if (reservation == null) {
                throw new IllegalStateException("Reservation not found");
            }
            // update the reservation status
            reservation.setStatus(Reservation.ReservationStatus.Canceled);
            // returns the changes to our database
            return reservationDAO.updateReservation(reservation);
        }
        catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateReservation(Reservation reservation) {
        return reservationDAO.updateReservation(reservation);
    }

    public boolean markPastReservationsAsCompleted() {
        return reservationDAO.markPastReservationsAsCompleted();
    }

    public boolean deleteReservation(int reservationId) {
        return reservationDAO.deleteReservation(reservationId);
    }

    public List<Reservation> getAllReservations() {
        return reservationDAO.getAllReservations();
    }

    public Reservation getReservationById(int reservationId){
        return reservationDAO.getReservationById(reservationId);
    }

    public List<Reservation> getReservationByUserId(int userId) {
        return reservationDAO.getReservationByUserId(userId);
    }

    public List<Reservation> getReservationByStaffId(int staffId) {
        return reservationDAO.getReservationByStaffId(staffId);
    }

    public boolean isStaffBusyOnDate(int staffId, LocalDate date) {
        List<Reservation> reservations = getReservationByStaffId(staffId);
        for (Reservation r : reservations) {
            if (r.getEventDate().isEqual(date) && (r.getStatus() != Reservation.ReservationStatus.Canceled)) {
                return true;
            }
        }
        return false;
    }

    public List<Reservation> getReservationByHallId(int hallId) {
        return reservationDAO.getReservationByHallId(hallId);
    }

    public class ReservationResult {
        private boolean success;
        private String message;

        public ReservationResult(boolean success, String message) {
            this.success = success;
            this.message = message;
        }

        public boolean isSuccess() { return success; }
        public String getMessage() { return message; }
    }
}
