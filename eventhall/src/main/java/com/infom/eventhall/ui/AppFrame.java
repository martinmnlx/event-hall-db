package com.infom.eventhall.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.io.*;

import com.infom.eventhall.DatabaseManager;
import com.infom.eventhall.model.User;

import com.infom.eventhall.service.UserService;
import lombok.Data;

@Data

public class AppFrame extends JFrame {

    private User user;

    private final DatabaseManager db;

    private final CardLayout cardLayout;
    private final JPanel mainPanel;
    private Font thinFont, regularFont, boldFont;

    private final UserService userService;

    private final LoginUI loginUI;
    private final RegisterUI registerUI;
    private final DashboardUI dashboardUI;
    private final HallsUI hallsUI;

    public AppFrame() {
        db = new DatabaseManager();

        userService = new UserService(db);

        loadFonts();

        setTitle("Event Hall Reservation");
        setSize(1200, 900);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        mainPanel.add(loginUI = new LoginUI(this, userService), "login");
        mainPanel.add(registerUI = new RegisterUI(this, userService), "register");
        mainPanel.add(dashboardUI = new DashboardUI(this), "dashboard");
        mainPanel.add(hallsUI = new HallsUI(this), "halls");

        add(mainPanel);

        setVisible(true);

        showScreen("login");
    }

    public void showScreen(String name) {
        cardLayout.show(mainPanel, name);

        switch (name) {
            case "dashboard" -> dashboardUI.refresh();
        }

        System.out.println("Panel changed: " + name);
    }

    private void loadFonts() {
        try {
            thinFont = Font.createFont(Font.TRUETYPE_FONT, new File("src/main/resources/fonts/outfit-thin.ttf"));
            regularFont = Font.createFont(Font.TRUETYPE_FONT, new File("src/main/resources/fonts/outfit-regular.ttf"));
            boldFont = Font.createFont(Font.TRUETYPE_FONT, new File("src/main/resources/fonts/outfit-bold.ttf"));
        }
        catch(FontFormatException | IOException e) {
            thinFont = regularFont = boldFont = new Font("Times New Roman", Font.BOLD, 16);
        }
    }

    public JLabel createLabel(String text, Color color, float size, int weight) {
        JLabel t = new JLabel(text);
        Font font = regularFont;

        switch (weight) {
            case 1 -> font = thinFont;
            case 3 -> font = boldFont;
        }

        t.setForeground(color);
        t.setFont(font.deriveFont(size));

        return t;
    }

    public JButton createButton(String text, float size) {
        JButton b = new JButton(text);

        b.setFont(getRegularFont().deriveFont(size));
        b.setFocusPainted(false);
        b.setContentAreaFilled(true);
        b.setOpaque(true);
        b.setBackground(Color.BLUE);
        b.setForeground(Color.WHITE);

        b.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                b.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                b.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        });

        return b;
    }

}
