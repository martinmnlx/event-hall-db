package com.infom.eventhall.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

import com.infom.eventhall.model.EventHall;

public class HallsUI extends JPanel {

    private final AppFrame app;

    private JPanel hallsPanel;

    public HallsUI(AppFrame app) {
        this.app = app;

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        JLabel titleLabel = app.createLabel("Event Halls", Color.BLUE, 60f, 3);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        hallsPanel = new JPanel();
        hallsPanel.setLayout(new BoxLayout(hallsPanel, BoxLayout.Y_AXIS));

        JScrollPane scrollPane = new JScrollPane(hallsPanel);
        scrollPane.setBorder(null);

        add(Box.createVerticalStrut(40));
        add(titleLabel);
        add(Box.createVerticalStrut(40));
        add(scrollPane);

        refreshHalls();
    }

    // Refresh hall cards dynamically
    public void refreshHalls() {
        hallsPanel.removeAll();
        List<EventHall> halls = app.getDb().getEventHallDAO().getAllEventHalls();

        for (EventHall hall : halls) {
            JPanel card = new JPanel();
            card.setLayout(new BoxLayout(card, BoxLayout.X_AXIS));
            card.setAlignmentX(Component.CENTER_ALIGNMENT);
            card.setBorder(
                    BorderFactory.createCompoundBorder(
                            BorderFactory.createLineBorder(Color.GRAY, 1),  // outer border
                            BorderFactory.createEmptyBorder(20, 20, 20, 20) // inner padding
                    )
            );
            card.setMaximumSize(new Dimension(800 , 200));

            JPanel main = new JPanel();
            main.setLayout(new BoxLayout(main, BoxLayout.Y_AXIS));

            JPanel side = new JPanel();
            side.setLayout(new BoxLayout(side, BoxLayout.Y_AXIS));

            JPanel status = new JPanel();
            status.setLayout(new BoxLayout(status, BoxLayout.X_AXIS));

            JLabel nameLabel = app.createLabel(hall.getHallName(), Color.BLUE, 32f, 3);
            JLabel capacityLabel = app.createLabel("Capacity: " + hall.getCapacity(), Color.BLACK, 16f, 2);
            JLabel locationLabel = app.createLabel("Location: " + hall.getLocation(), Color.BLACK, 16f, 2);
            JLabel statusLabel = app.createLabel("Status: ", Color.BLACK, 16f, 2);
            JLabel availLabel = app.createLabel(hall.getStatus().name(), Color.decode("#5BB450"), 16f, 3);

            JButton bookButton = app.createButton("Book", 20f);

            if (hall.getStatus().name().equals("Maintenance")) {
                availLabel.setForeground(Color.decode("#F94449"));
                bookButton.setEnabled(false);
                bookButton.setBackground(Color.LIGHT_GRAY);
                bookButton.setForeground(Color.WHITE);
                bookButton.setText("Under Maintenance");
            }

            status.add(statusLabel);
            status.add(availLabel);
            status.setAlignmentX(Component.LEFT_ALIGNMENT);

            side.add(Box.createVerticalGlue());
            side.add(bookButton);

            main.add(nameLabel);
            main.add(Box.createVerticalStrut(8));
            main.add(capacityLabel);
            main.add(Box.createVerticalStrut(4));
            main.add(locationLabel);
            main.add(Box.createVerticalStrut(4));
            main.add(status);

            card.add(main);
            card.add(side);

            hallsPanel.add(card);
            hallsPanel.add(Box.createRigidArea(new Dimension(0, 12))); // spacing
        }

        hallsPanel.revalidate();
        hallsPanel.repaint();
    }
}
