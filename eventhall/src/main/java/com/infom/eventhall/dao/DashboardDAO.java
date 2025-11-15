package com.infom.eventhall.dao;

import java.sql.*;

public class DashboardDAO {

    Connection connection;

    public DashboardDAO(Connection connection) {
        this.connection = connection;
    }
}
