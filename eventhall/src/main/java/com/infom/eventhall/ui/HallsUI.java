package com.infom.eventhall.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

import com.infom.eventhall.model.EventHall;

public class HallsUI extends JPanel {

    private AppFrame app;

    private JPanel hallsPanel;

    private JLabel titleLabel;

    public HallsUI(AppFrame app) {
        this.app = app;

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        titleLabel = app.createLabel("Event Halls", Color.BLUE, 60f, 3);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        hallsPanel = new JPanel();
        hallsPanel.setLayout(new BoxLayout(hallsPanel, BoxLayout.Y_AXIS));

        JScrollPane scrollPane = new JScrollPane(hallsPanel);
        add(titleLabel);
        add(scrollPane);

        refreshHalls();
    }

    // Refresh hall cards dynamically
    public void refreshHalls() {
        hallsPanel.removeAll();
        List<EventHall> halls = app.getDb().getEventHallDAO().getAllEventHalls();

        for (EventHall hall : halls) {
            JPanel card = new JPanel();
            card.setLayout(new GridLayout(0, 1));
            card.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
            card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));

            JLabel nameLabel = app.createLabel(hall.getHallName(), Color.BLUE, 32f, 3);
            JLabel capacityLabel = new JLabel("Capacity: " + hall.getCapacity());
            JLabel locationLabel = new JLabel("Location: " + hall.getLocation());
            JLabel statusLabel = new JLabel("Status: " + hall.getStatus());

            card.add(nameLabel);
            card.add(capacityLabel);
            card.add(locationLabel);
            card.add(statusLabel);

            hallsPanel.add(card);
            hallsPanel.add(Box.createRigidArea(new Dimension(0, 5))); // spacing
        }

        hallsPanel.revalidate();
        hallsPanel.repaint();
    }
}
