package com.sam.eventhallproject.model;

import lombok.Data;
import java.time.LocalDateTime;

@Data // provides getters, setters, and constructors

public class User {

    private Integer userId;
    private UserType type; // either gives a value of Customer, Admin, Staff
    private String name;
    private String email;
    private String phoneNumber;
    private String password;
    private LocalDateTime createdOn;

    public enum UserType {
        Customer, Admin, Staff
    }
}
