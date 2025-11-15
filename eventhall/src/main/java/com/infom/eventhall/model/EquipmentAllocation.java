package com.infom.eventhall.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data // provides getters, setters, and constructors

public class EquipmentAllocation {

    private Integer allocationId;
    private Integer reservationId; // foreign key to reservation Table
    private Integer equipmentId; // foreign key to equipment Table
    private Integer quantityUsed;
}
