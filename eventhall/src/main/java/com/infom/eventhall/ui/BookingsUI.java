package com.infom.eventhall.ui;

import javax.swing.*;
import java.awt.*;
import java.util.List;

import com.infom.eventhall.model.EventHall;
import com.infom.eventhall.model.Reservation;
import com.infom.eventhall.model.Staff;
import com.infom.eventhall.model.User;
import com.infom.eventhall.service.EventHallService;
import com.infom.eventhall.service.ReservationService;
import com.infom.eventhall.service.StaffService;
import com.infom.eventhall.service.UserService;
import lombok.Getter;

public class BookingsUI extends JPanel {

    private final AppFrame app;
    private final ReservationService reservationService;
    private final EventHallService eventHallService;
    private final UserService userService;
    private final StaffService staffService;

    private final JPanel bookingsPanel;
    private final JComboBox<String> typeDropdown;
    private final JComboBox<String> statusDropdown;
    private final JLabel numResults;

    private List<Reservation> reservations;

    @Getter
    private int chosenHallID = 0;

    public BookingsUI(AppFrame app, ReservationService r, EventHallService h, UserService u, StaffService s) {
        this.app = app;
        this.reservationService = r;
        this.eventHallService = h;
        this.userService = u;
        this.staffService = s;

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        JLabel titleLabel = app.createLabel("Reservation History", Color.BLUE, 60f, 3);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel filterPanel = new JPanel();
        filterPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 8, 0));

        typeDropdown = new JComboBox<>(new String[]{"All", "Birthday", "Wedding", "Meeting", "Conference", "Other"});
        typeDropdown.setFont(app.getRegularFont().deriveFont(14f));
        typeDropdown.setBackground(Color.WHITE);
        ((JLabel) typeDropdown.getRenderer()).setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));

        statusDropdown = new JComboBox<>(new String[]{"All", "Pending", "Confirmed", "Canceled", "Completed"});
        statusDropdown.setFont(app.getRegularFont().deriveFont(14f));
        statusDropdown.setBackground(Color.WHITE);
        ((JLabel) statusDropdown.getRenderer()).setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));

        filterPanel.add(app.createLabel("Event Type: ", Color.BLACK, 16f, 2));
        filterPanel.add(typeDropdown);
        filterPanel.add(Box.createRigidArea(new Dimension(20, 0)));
        filterPanel.add(app.createLabel("Status: ", Color.BLACK, 16f, 2));
        filterPanel.add(statusDropdown);
        filterPanel.add(Box.createRigidArea(new Dimension(20, 0)));
        filterPanel.add(numResults = app.createLabel("0 Results", Color.BLUE, 16f, 3));

        // --- Halls panel inside scroll pane ---
        bookingsPanel = new JPanel();
        bookingsPanel.setLayout(new BoxLayout(bookingsPanel, BoxLayout.Y_AXIS));

        JScrollPane scrollPane = new JScrollPane(bookingsPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        JButton dashboardButton = app.createButton("Return to Dashboard", Color.BLUE, 16f, false);
        dashboardButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        add(Box.createVerticalStrut(40));
        add(titleLabel);
        add(Box.createVerticalStrut(40));
        add(filterPanel);
        add(Box.createVerticalStrut(40));
        add(scrollPane);
        add(Box.createVerticalStrut(40));
        add(dashboardButton);
        add(Box.createVerticalStrut(40));

        typeDropdown.addActionListener(e -> refresh());

        statusDropdown.addActionListener(e -> refresh());

        dashboardButton.addActionListener(e -> app.showScreen("dashboard"));

        refresh();
    }

    // Refresh hall cards dynamically
    public void refresh() {
        if (app.getUser() != null) reservations = reservationService.getReservationByUserId(app.getUser().getUserId());
        else return;

        bookingsPanel.removeAll();

        int reservationCount = 0;

        String selectedType = (String) typeDropdown.getSelectedItem();
        String selectedStatus = (String) statusDropdown.getSelectedItem();

        for (Reservation reservation : reservations) {
            boolean matchesType = selectedType.equals("All") || reservation.getEventType().equals(selectedType);
            boolean matchesStatus = selectedStatus.equals("All") || reservation.getStatus().name().equals(selectedStatus);

            if (matchesType && matchesStatus) {
                // --- Card panel ---
                JPanel hallCard = new JPanel();
                hallCard.setLayout(new BoxLayout(hallCard, BoxLayout.X_AXIS));
                hallCard.setAlignmentX(Component.CENTER_ALIGNMENT);
                hallCard.setBorder(
                        BorderFactory.createCompoundBorder(
                                BorderFactory.createLineBorder(Color.GRAY, 1),
                                BorderFactory.createEmptyBorder(20, 20, 20, 20)
                        )
                );
                hallCard.setMinimumSize(new Dimension(760 , 260));
                hallCard.setMaximumSize(new Dimension(760 , 260));

                JPanel main = new JPanel();
                main.setLayout(new BoxLayout(main, BoxLayout.Y_AXIS));

                JPanel side = new JPanel();
                side.setLayout(new BoxLayout(side, BoxLayout.Y_AXIS));

                JPanel status = new JPanel();
                status.setLayout(new BoxLayout(status, BoxLayout.X_AXIS));

                EventHall hall = eventHallService.getHallById(reservation.getHallId());
                User user = userService.getUserById(reservation.getUserId());
                Staff staff = staffService.getStaffById(reservation.getStaffId());

                JLabel nameLabel = app.createLabel(hall.getHallName(), Color.BLUE, 32f, 3);
                JLabel dateLabel = app.createLabel("Date: " + reservation.getEventDate(), Color.BLACK, 16f, 2);
                JLabel typeLabel = app.createLabel("Type: " + reservation.getEventType(), Color.BLACK, 16f, 2);
                JLabel capacityLabel = app.createLabel("Staff-In-Charge: " + staff.getName(), Color.BLACK, 16f, 2);
                JLabel locationLabel = app.createLabel("Contact Number: " + staff.getPhoneNumber(), Color.BLACK, 16f, 2);
                JLabel guestsLabel = app.createLabel("Guest Count: " + reservation.getGuestCount(), Color.BLACK, 16f, 2);
                JLabel statusLabel = app.createLabel("Status: ", Color.BLACK, 16f, 2);

                Color availColor = Color.BLACK;
                if (reservation.getStatus().name().equals("Confirmed")) availColor = Color.decode("#5BB450");
                if (reservation.getStatus().name().equals("Canceled")) availColor = Color.RED;
                if (reservation.getStatus().name().equals("Completed")) availColor = Color.BLUE;

                JLabel availLabel = app.createLabel(reservation.getStatus().name(), availColor, 16f, 3);

                JButton cancelButton = app.createButton("Cancel", Color.decode("#F94449"), 20f, false);

                if (reservation.getStatus() == Reservation.ReservationStatus.Canceled) {
                    cancelButton.setEnabled(false);
                    cancelButton.setBackground(Color.LIGHT_GRAY);
                    cancelButton.setForeground(Color.WHITE);
                    cancelButton.setText("Canceled");
                } else {
                    cancelButton.addActionListener(e -> {
                        reservationService.cancelReservation(reservation.getReservationId());
                        refresh();
                    });
                }

                status.add(statusLabel);
                status.add(availLabel);
                status.setAlignmentX(Component.LEFT_ALIGNMENT);

                side.add(Box.createVerticalGlue());
                side.add(cancelButton);

                main.add(nameLabel);
                main.add(Box.createVerticalStrut(16));
                main.add(typeLabel);
                main.add(Box.createVerticalStrut(4));
                main.add(dateLabel);
                main.add(Box.createVerticalStrut(4));
                main.add(capacityLabel);
                main.add(Box.createVerticalStrut(4));
                main.add(locationLabel);
                main.add(Box.createVerticalStrut(4));
                main.add(guestsLabel);
                main.add(Box.createVerticalStrut(4));
                main.add(status);

                hallCard.add(main);
                hallCard.add(side);

                reservationCount++;

                bookingsPanel.add(hallCard);
                bookingsPanel.add(Box.createRigidArea(new Dimension(0, 12)));
            }
        }

        numResults.setText(reservationCount + " Results");

        bookingsPanel.revalidate();
        bookingsPanel.repaint();
    }

}
