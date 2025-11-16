package com.infom.eventhall.service;

import com.infom.eventhall.model.EventHall;
import com.infom.eventhall.dao.EventHallDAO;
import com.infom.eventhall.dao.ReservationDAO;
import com.infom.eventhall.DatabaseManager;

import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class EventHallService {

    private final EventHallDAO eventHallDAO;
    private final ReservationDAO reservationDAO;

    public EventHallService(DatabaseManager db ) {
        this.eventHallDAO = db.getEventHallDAO();
        this.reservationDAO = db.getReservationDAO();
    }

    public List<EventHall> findAllAvailableEventHalls(LocalDateTime startsOn, LocalDateTime endsOn) {

        List<Integer> conflictingHallIds = reservationDAO.findConflictingHallIds(startsOn, endsOn);
        List<EventHall> allEventHalls = eventHallDAO.getAllEventHalls();
        List<EventHall> availableEventHalls = new ArrayList<>();

        for (EventHall eventHall : allEventHalls) {
            if (!conflictingHallIds.contains(eventHall.getHallId()) && eventHall.getStatus() == EventHall.HallStatus.Available) {
                availableEventHalls.add(eventHall);
            }
        }
        return availableEventHalls;
    }

    public EventHall getHallByName(String name) {
        return eventHallDAO.getHallByName(name);
    }

    public List<EventHall> getHallByCapacity(int capacity) {
        return eventHallDAO.getHallByCapacity(capacity);
    }

    public List<EventHall> getHallByLocation(String location) {
        return eventHallDAO.getHallByLocation(location);
    }

    public List<EventHall> getAllEventHalls() {
        return eventHallDAO.getAllEventHalls();
    }

    // Admin ability

    public boolean createEventHall(EventHall eventHall) {
        return eventHallDAO.createEventHall(eventHall);
    }

    public boolean updateEventHall(EventHall eventHall) {
        return eventHallDAO.updateEventHall(eventHall);
    }

    public boolean deleteEventHall(int hallId) {
        return eventHallDAO.deleteEventHall(hallId);
    }
}
