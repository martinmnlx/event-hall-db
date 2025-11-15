package com.infom.eventhall;

import javax.swing.*;
import java.awt.*;

import com.infom.eventhall.ui.*;

public class Main {
    static void main() {
        // Create JFrame
        JFrame frame = new JFrame("Event Hall Reservation");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);

        // Create main panel with CardLayout
        CardLayout cardLayout = new CardLayout();
        JPanel mainPanel = new JPanel(cardLayout);

        // Create UI panels
        LoginUI loginPanel = new LoginUI(cardLayout, mainPanel);
        JPanel dashboardPanel = new JPanel();
        dashboardPanel.add(new JLabel("Welcome to Dashboard"));

        // Add panels to CardLayout
        mainPanel.add(loginPanel, "login");
        mainPanel.add(dashboardPanel, "dashboard");

        // Show login first
        cardLayout.show(mainPanel, "login");

        // Add mainPanel to frame and display
        frame.add(mainPanel);
        frame.setLocationRelativeTo(null); // center on screen
        frame.setVisible(true);
    }
}
