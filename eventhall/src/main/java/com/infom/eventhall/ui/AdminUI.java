package com.infom.eventhall.ui;

import javax.swing.*;
import java.awt.*;

public class AdminUI extends JPanel {

    private final AppFrame app;

    private final JLabel nameLabel;

    public AdminUI(AppFrame app) {
        this.app = app;

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        JLabel titleLabel = app.createLabel("Admin Dashboard", Color.BLUE, 80f, 3);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel welcomeLayout = new JPanel();
        welcomeLayout.setLayout(new BoxLayout(welcomeLayout, BoxLayout.X_AXIS));

        JLabel welcomeLabel = app.createLabel("Welcome, ", Color.BLACK, 20f, 2);
        welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        nameLabel = app.createLabel("(NO USER)", Color.BLACK, 20f, 3);
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel exclamLabel = app.createLabel("!", Color.BLACK, 20f, 2);
        exclamLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        welcomeLayout.add(welcomeLabel);
        welcomeLayout.add(nameLabel);
        welcomeLayout.add(exclamLabel);

        JButton recordsButton = app.createButton("View Records Tables", Color.BLUE, 24f, false);
        recordsButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton reportsButton = app.createButton("View Generated Reports", Color.BLUE, 24f, false);
        reportsButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton logoutButton = app.createButton("Logout", Color.decode("#F94449"), 24f, false);
        logoutButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        add(Box.createVerticalGlue());
        add(titleLabel);
        add(Box.createVerticalStrut(20));
        add(welcomeLayout);
        add(Box.createVerticalStrut(100));
        add(recordsButton);
        add(Box.createVerticalStrut(20));
        add(reportsButton);
        add(Box.createVerticalStrut(40));
        add(logoutButton);
        add(Box.createVerticalGlue());

        recordsButton.addActionListener(e -> app.showScreen("records"));

        reportsButton.addActionListener(e -> app.showScreen("reports"));

        logoutButton.addActionListener(e -> { app.setUser(null); app.showScreen("login"); });
    }

    public void refresh() {
        nameLabel.setText(app.getUser().getName());
        revalidate();
        repaint();
    }

}
