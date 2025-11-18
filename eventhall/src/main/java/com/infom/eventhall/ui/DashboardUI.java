package com.infom.eventhall.ui;

import javax.swing.*;
import java.awt.*;

public class DashboardUI extends JPanel {

    private final AppFrame app;

    private final JLabel titleLabel;
    private final JLabel welcomeLabel;
    private final JLabel nameLabel;
    private final JLabel exclamLabel;
    private final JButton bookingsButton;
    private final JButton hallsButton;
    private final JButton profileButton;
    private final JButton logoutButton;

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

        bookingsButton = app.createButton("View Your Booking History", Color.BLUE, 24f, false);
        bookingsButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        hallsButton = app.createButton("Browse Event Halls", Color.BLUE, 24f, false);
        hallsButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        profileButton = app.createButton("View/Edit Your Profile", Color.BLUE, 24f, false);
        profileButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        logoutButton = app.createButton("Logout", Color.decode("#F94449"), 24f, false);
        logoutButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        add(Box.createVerticalGlue());
        add(titleLabel);
        add(Box.createVerticalStrut(20));
        add(welcomeLayout);
        add(Box.createVerticalStrut(100));
        add(bookingsButton);
        add(Box.createVerticalStrut(20));
        add(hallsButton);
        add(Box.createVerticalStrut(20));
        add(profileButton);
        add(Box.createVerticalStrut(40));
        add(logoutButton);
        add(Box.createVerticalGlue());

        bookingsButton.addActionListener(e -> app.showScreen("bookings"));

        hallsButton.addActionListener(e -> app.showScreen("halls"));

        logoutButton.addActionListener(e -> {
            app.setUser(null);
            app.showScreen("login");
        });
    }

    public void refresh() {
        nameLabel.setText(app.getUser().getName());
        revalidate();
        repaint();
    }

}
