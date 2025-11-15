package com.infom.eventhall.service;

import com.infom.eventhall.model.User;
import com.infom.eventhall.dao.UserDAO;
import com.infom.eventhall.DatabaseManager;

import java.sql.Connection;
import java.time.LocalDateTime;


public class UserService {

    private UserDAO userDAO;

    public UserService() {
        Connection connection = DatabaseManager.getConnection();
        this.userDAO = new UserDAO(connection);
    }

    public boolean registerUser(String name, String email, String phone, String password) {
        if (userDAO.getUserByEmail(email) != null) {
            return false;
        }
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPhoneNumber(phone);
        user.setPassword(password);
        user.setType(User.UserType.Customer);
        user.setCreatedOn(LocalDateTime.now());
        return userDAO.createUser(user);
    }

    public User authenticateUser(String email, String password) {
        User user = userDAO.getUserByEmail(email);

        if (user != null && user.getPassword().equals(password)) {
            return user;
        }
        return null;
    }
}
