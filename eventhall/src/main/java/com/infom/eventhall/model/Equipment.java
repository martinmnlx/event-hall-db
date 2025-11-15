package com.infom.eventhall.model;

import lombok.Data;

@Data // provides getters, setters, and constructors

public class Equipment {

    private Integer equipmentId;
    private String EquipmentName;
    private Integer quantityTotal;
    private EquipmentStatus equipment;

    public enum EquipmentStatus {
        Available, InUse, Maintenance
    }

}
