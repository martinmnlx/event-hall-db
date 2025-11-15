package com.sam.eventhallproject.model;

import lombok.Data;
import java.time.LocalDateTime;

@Data // provides getters, setters, and constructors

public class Reservation {

    private Integer reservationId;
    private Integer userId; // Foreign Key
    private Integer hallId; // Foreign Key
    private Integer staffId; // Foreign Key
    private LocalDateTime createdOn;
    private LocalDateTime startsOn;
    private LocalDateTime endsOn;
    private String eventType;
    private Integer guessCount;
    private ReservationStatus status;

    public enum ReservationStatus {
        Pending, Confirmed, Cancelled
    }
}
