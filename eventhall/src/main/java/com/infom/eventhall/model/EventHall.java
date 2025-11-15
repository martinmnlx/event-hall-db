package com.sam.eventhallproject.model;

import lombok.Data;

@Data // provides getters, setters, and constructors

public class EventHall {

    private Integer hallId;
    private String hallName;
    private Integer capacity;
    private String location; // location will be just cities
    private HallStatus status; // either gives a value of Available, Booked, UnderMaintenance

    public enum HallStatus{
        Available, Booked, UnderMaintenance
    }

}
