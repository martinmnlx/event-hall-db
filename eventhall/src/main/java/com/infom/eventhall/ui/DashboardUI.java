package com.infom.eventhall.ui;

import com.infom.eventhall.dao.DashboardDAO;

import javax.swing.*;
import java.awt.*;

public class DashboardUI extends JPanel {

    private final AppFrame app;
    private final DashboardDAO dao;

    public DashboardUI(AppFrame app, DashboardDAO dao) {
        this.app = app;
        this.dao = dao;
    }

}
