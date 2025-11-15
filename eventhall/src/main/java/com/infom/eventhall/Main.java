package com.infom.eventhall;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

import com.infom.eventhall.dao.*;
import com.infom.eventhall.ui.*;

public class Main {

    public static void main(String[] args) {
        Connection connection = DatabaseManager.getConnection();

        UserDAO userDAO = new UserDAO(connection);
        DashboardDAO dashboardDAO = new DashboardDAO(connection);

        SwingUtilities.invokeLater(() -> {
            AppFrame app = new AppFrame(userDAO, dashboardDAO);
            app.setVisible(true);
        });
    }
}
