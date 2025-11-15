package com.infom.eventhall.ui;

import com.infom.eventhall.dao.DashboardDAO;

import javax.swing.*;
import java.awt.*;

public class DashboardUI extends JPanel {

    private final AppFrame app;

    private final JLabel titleLabel;
    private final JLabel welcomeLabel;
    private final JLabel nameLabel;
    private final JLabel exclamLabel;
    private final JButton reservationsButton;
    private final JButton hallsButton;
    private final JButton profileButton;

    public DashboardUI(AppFrame app) {
        this.app = app;

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        titleLabel = app.createLabel("Dashboard", Color.BLUE, 80f, 3);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel welcomeLayout = new JPanel();
        welcomeLayout.setLayout(new BoxLayout(welcomeLayout, BoxLayout.X_AXIS));

        welcomeLabel = app.createLabel("Welcome, ", Color.BLACK, 20f, 2);
        welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        nameLabel = app.createLabel("(NO USER)", Color.BLACK, 20f, 3);
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        exclamLabel = app.createLabel("!", Color.BLACK, 20f, 2);
        exclamLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        welcomeLayout.add(welcomeLabel);
        welcomeLayout.add(nameLabel);
        welcomeLayout.add(exclamLabel);

        reservationsButton = app.createButton("View Your Current Bookings", 24f);
        reservationsButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        hallsButton = app.createButton("Browse Event Halls", 24f);
        hallsButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        profileButton = app.createButton("View/Edit Your Profile", 24f);
        profileButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        add(Box.createVerticalGlue());
        add(titleLabel);
        add(Box.createVerticalStrut(20));
        add(welcomeLayout);
        add(Box.createVerticalStrut(60));
        add(reservationsButton);
        add(Box.createVerticalStrut(20));
        add(hallsButton);
        add(Box.createVerticalStrut(20));
        add(profileButton);
        add(Box.createVerticalGlue());

        hallsButton.addActionListener(e -> app.showScreen("halls"));
    }

    public void refresh() {
        nameLabel.setText(app.getUser().getName());
        revalidate();
        repaint();
    }

}
