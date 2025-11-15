package com.sam.eventhallproject.model;

import lombok.Data;

@Data // provides getters, setters, and constructors

public class Staff {

    private Integer staffId;
    private String name;
    private String role;
    private String phoneNumber;
}
