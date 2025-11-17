package com.infom.eventhall.service;

import com.infom.eventhall.model.*;
import com.infom.eventhall.dao.*;
import com.infom.eventhall.DatabaseManager;

import java.sql.Connection;
import java.sql.SQLException;
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

    public boolean createReservation(Reservation reservation, List<EquipmentAllocation> allocations) {

        try {
            // so that it only saves when we complete the whole process
            connection.setAutoCommit(false);

            //Checks for availableHalls
            List<EventHall> availableEventHalls = eventHallService.findAllAvailableEventHalls(reservation.getEventDate());

            boolean isAvailable = false;
            for (EventHall eventHall : availableEventHalls) {
                if (eventHall.getHallId().equals(reservation.getHallId())) {
                    isAvailable = true;
                    break;
                }
            }
            if (!isAvailable) {
                throw new IllegalStateException("Event hall is not available");
            }

            // now checks for equipment availability
            for (EquipmentAllocation allocation : allocations) {
                Equipment equipment = equipmentDAO.getEquipmentByID(allocation.getEquipmentId());
                if (equipment == null) {
                    throw new IllegalStateException("Equipment not found");
                }

                // checks how much equipment is booked for the day
                int reservedEquipment = allocationDAO.getReservedQuantityForTheDay(allocation.getEquipmentId(), reservation.getEventDate());

                // calculate how much stock is available left
                int remainingStock = equipment.getQuantityTotal() - reservedEquipment;

                // should now check if there is enough stock
                if (allocation.getQuantityUsed() > remainingStock) {
                    throw new IllegalStateException("Not enough equipment quantity");
                }
            }

            // set the time reservation is created
            reservation.setCreatedOn(LocalDateTime.now());
            boolean reservationCreated = reservationDAO.createReservation(reservation);

            if (!reservationCreated) {
                throw new IllegalStateException("Unable to create reservation");
            }

            // saving the allocation of equipment
            for  (EquipmentAllocation allocation : allocations) {
                allocation.setReservationId(reservation.getReservationId());
                if (!allocationDAO.createAllocation(allocation)) {
                    throw new IllegalStateException("Unable to create allocation");
                }
            }
            // this will commit the changes to create the booking
            connection.commit();
            return true;
    } catch (Exception e) {
            e.printStackTrace();
            // if any error occurs we roll back all changes
            try {
                connection.rollback();
            }
            catch (SQLException se) {
                se.printStackTrace();
            }
            return false;
        } finally {
            // always set auto-commit back to true for other operations
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
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



    public Reservation getReservationById(int reservationId){
        return reservationDAO.getReservationById(reservationId);
    }

    public List<Reservation> getReservationByUserId(int userId) {
        return reservationDAO.getReservationByUserId(userId);
    }

    public List<Reservation> getReservationByHallId(int hallId) {
        return reservationDAO.getReservationByHallId(hallId);
    }
}
