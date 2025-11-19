package com.infom.eventhall.ui;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.time.format.DateTimeFormatter;

import com.infom.eventhall.model.*;
import com.infom.eventhall.service.*;
import lombok.Getter;

public class BookingsUI extends JPanel {

    private final AppFrame app;
    private final ReservationService reservationService;
    private final EventHallService eventHallService;
    private final UserService userService;
    private final StaffService staffService;
    private final EquipmentService equipmentService;
    private final EquipmentAllocationService allocationService;

    private final JPanel bookingsPanel;
    private final JComboBox<String> typeDropdown;
    private final JComboBox<String> statusDropdown;
    private final JComboBox<String> sortDropdown;
    private final JLabel numResults;

    private List<Reservation> reservations;

    @Getter
    private int chosenHallID = 0;

    public BookingsUI(AppFrame app, ReservationService r, EventHallService h, UserService u, StaffService s, EquipmentService eq, EquipmentAllocationService a) {
        this.app = app;
        this.reservationService = r;
        this.eventHallService = h;
        this.userService = u;
        this.staffService = s;
        this.equipmentService = eq;
        this.allocationService = a;

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        JLabel titleLabel = app.createLabel("Reservation History", Color.BLUE, 60f, 3);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel filterPanel = new JPanel();
        filterPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 8, 0));

        typeDropdown = new JComboBox<>(new String[]{"All", "Birthday", "Wedding", "Meeting", "Conference", "Other"});
        typeDropdown.setFont(app.getRegularFont().deriveFont(14f));
        typeDropdown.setBackground(Color.WHITE);
        ((JLabel) typeDropdown.getRenderer()).setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));

        statusDropdown = new JComboBox<>(new String[]{"Confirmed", "Pending", "Canceled", "Completed", "All"});
        statusDropdown.setFont(app.getRegularFont().deriveFont(14f));
        statusDropdown.setBackground(Color.WHITE);
        ((JLabel) statusDropdown.getRenderer()).setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));

        sortDropdown = new JComboBox<>(new String[]{"Event Date: Earliest", "Event Date: Latest"});
        sortDropdown.setFont(app.getRegularFont().deriveFont(14f));
        sortDropdown.setBackground(Color.WHITE);
        ((JLabel) sortDropdown.getRenderer()).setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));

        filterPanel.add(app.createLabel("Event Type: ", Color.BLACK, 16f, 2));
        filterPanel.add(typeDropdown);
        filterPanel.add(Box.createRigidArea(new Dimension(20, 0)));
        filterPanel.add(app.createLabel("Sort By: ", Color.BLACK, 16f, 2));
        filterPanel.add(sortDropdown);
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
        sortDropdown.addActionListener(e -> refresh());
        statusDropdown.addActionListener(e -> refresh());

        dashboardButton.addActionListener(e -> app.showScreen("dashboard"));

        refresh();
    }

    // Refresh hall cards dynamically
    public void refresh() {
        if (app.getUser() != null) reservations = reservationService.getReservationByUserId(app.getUser().getUserId());
        else return;

        if (sortDropdown.getSelectedItem().equals("Event Date: Earliest")) reservations.sort(Comparator.comparing(Reservation::getEventDate));
        if (sortDropdown.getSelectedItem().equals("Event Date: Latest")) reservations.sort(Comparator.comparing(Reservation::getEventDate).reversed());

        bookingsPanel.removeAll();

        int reservationCount = 0;

        String selectedType = (String) typeDropdown.getSelectedItem();
        String selectedStatus = (String) statusDropdown.getSelectedItem();

        for (Reservation reservation : reservations) {
            boolean matchesType = selectedType.equals("All") || reservation.getEventType().equals(selectedType);
            boolean matchesStatus = selectedStatus.equals("All") || reservation.getStatus().name().equals(selectedStatus);

            if (matchesType && matchesStatus) {
                List<EquipmentAllocation> allocs = allocationService.getAllAllocationsByReservationId(reservation.getReservationId());
                StringBuilder equipments = new StringBuilder();
                int count = 0;

                for (EquipmentAllocation alloc : allocs) {
                    if (count != 0) equipments.append(", ");
                    equipments.append(equipmentService.getEquipmentById(alloc.getEquipmentId()).getEquipmentName());
                    count ++;
                }

                JPanel hallCard = new JPanel();
                hallCard.setLayout(new BoxLayout(hallCard, BoxLayout.X_AXIS));
                hallCard.setAlignmentX(Component.CENTER_ALIGNMENT);
                hallCard.setBorder(
                        BorderFactory.createCompoundBorder(
                                BorderFactory.createLineBorder(Color.GRAY, 1),
                                BorderFactory.createEmptyBorder(20, 20, 20, 20)
                        )
                );
                hallCard.setMinimumSize(new Dimension(760 , 300));
                hallCard.setMaximumSize(new Dimension(760 , 300));

                JPanel main = new JPanel();
                main.setLayout(new BoxLayout(main, BoxLayout.Y_AXIS));

                JPanel side = new JPanel();
                side.setLayout(new BoxLayout(side, BoxLayout.Y_AXIS));

                EventHall hall = eventHallService.getHallById(reservation.getHallId());
                User user = userService.getUserById(reservation.getUserId());
                Staff staff = staffService.getStaffById(reservation.getStaffId());
                String staffName = (staff != null ? staff.getName() : "");
                String staffPhone = (staff != null ? staff.getPhoneNumber() : "");

                JLabel nameLabel = app.createLabel(hall.getHallName(), Color.BLUE, 32f, 3);

                JPanel typePanel = new JPanel();
                typePanel.setLayout(new BoxLayout(typePanel, BoxLayout.X_AXIS));
                JLabel typeLabel1 = app.createLabel("Type: ", Color.BLACK, 16f, 2);
                JLabel typeLabel2 = app.createLabel("" + reservation.getEventType(), Color.BLACK, 16f, 3);
                typePanel.add(typeLabel1);
                typePanel.add(typeLabel2);
                typePanel.setAlignmentX(Component.LEFT_ALIGNMENT);

                JPanel datePanel = new JPanel();
                datePanel.setLayout(new BoxLayout(datePanel, BoxLayout.X_AXIS));
                JLabel dateLabel1 = app.createLabel("Date: ", Color.BLACK, 16f, 2);
                JLabel dateLabel2 = app.createLabel(reservation.getEventDate().format(DateTimeFormatter.ofPattern("MMMM d, yyyy")), Color.BLACK, 16f, 3);
                datePanel.add(dateLabel1);
                datePanel.add(dateLabel2);
                datePanel.setAlignmentX(Component.LEFT_ALIGNMENT);

                JPanel locationPanel = new JPanel();
                locationPanel.setLayout(new BoxLayout(locationPanel, BoxLayout.X_AXIS));
                JLabel locationLabel1 = app.createLabel("Location: ", Color.BLACK, 16f, 2);
                JLabel locationLabel2 = app.createLabel(hall.getLocation(), Color.BLACK, 16f, 3);
                locationPanel.add(locationLabel1);
                locationPanel.add(locationLabel2);
                locationPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

                JPanel guestsPanel = new JPanel();
                guestsPanel.setLayout(new BoxLayout(guestsPanel, BoxLayout.X_AXIS));
                JLabel guestsLabel1 = app.createLabel("Guest Count: ", Color.BLACK, 16f, 2);
                JLabel guestsLabel2 = app.createLabel("" + reservation.getGuestCount(), Color.BLACK, 16f, 3);
                guestsPanel.add(guestsLabel1);
                guestsPanel.add(guestsLabel2);
                guestsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

                JPanel staffPanel = new JPanel();
                staffPanel.setLayout(new BoxLayout(staffPanel, BoxLayout.X_AXIS));
                JLabel staffLabel1 = app.createLabel("Staff In-Charge: ", Color.BLACK, 16f, 2);
                JLabel staffLabel2 = app.createLabel(staffName, Color.BLACK, 16f, 3);
                staffPanel.add(staffLabel1);
                staffPanel.add(staffLabel2);
                staffPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

                JPanel contactPanel = new JPanel();
                contactPanel.setLayout(new BoxLayout(contactPanel, BoxLayout.X_AXIS));
                JLabel contactLabel1 = app.createLabel("Contact Number: ", Color.BLACK, 16f, 2);
                JLabel contactLabel2 = app.createLabel(staffPhone, Color.BLACK, 16f, 3);
                contactPanel.add(contactLabel1);
                contactPanel.add(contactLabel2);
                contactPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

                JPanel equipmentPanel = new JPanel();
                equipmentPanel.setLayout(new BoxLayout(equipmentPanel, BoxLayout.X_AXIS));
                JLabel equipmentLabel1 = app.createLabel("Equipment: ", Color.BLACK, 16f, 2);
                JLabel equipmentLabel2 = app.createLabel(equipments.toString(), Color.BLACK, 16f, 3);
                equipmentPanel.add(equipmentLabel1);
                equipmentPanel.add(equipmentLabel2);
                equipmentPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

                Color availColor = Color.BLACK;
                if (reservation.getStatus().name().equals("Pending")) availColor = Color.ORANGE;
                if (reservation.getStatus().name().equals("Confirmed")) availColor = Color.decode("#5BB450");
                if (reservation.getStatus().name().equals("Canceled")) availColor = Color.decode("#F94449");
                if (reservation.getStatus().name().equals("Completed")) availColor = Color.GRAY;

                JPanel statusPanel = new JPanel();
                statusPanel.setLayout(new BoxLayout(statusPanel, BoxLayout.X_AXIS));
                JLabel statusLabel = app.createLabel("Status: ", Color.BLACK, 16f, 2);
                JLabel availLabel = app.createLabel(reservation.getStatus().name(), availColor, 16f, 3);
                statusPanel.add(statusLabel);
                statusPanel.add(availLabel);
                statusPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

                JButton cancelButton = app.createButton("Cancel", Color.decode("#F94449"), 20f, false);

                cancelButton.addActionListener(e -> {
                    int confirm = JOptionPane.showConfirmDialog(
                            null, // parent component, can use your frame or panel
                            "Are you sure you want to cancel this reservation?",
                            "Confirm Cancel",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.WARNING_MESSAGE
                    );

                    if (confirm == JOptionPane.YES_OPTION) {
                        reservationService.cancelReservation(reservation.getReservationId());
                        refresh();
                    }
                });

                if (reservation.getStatus() == Reservation.ReservationStatus.Canceled) {
                    cancelButton.setEnabled(false);
                    cancelButton.setBackground(Color.LIGHT_GRAY);
                    cancelButton.setForeground(Color.WHITE);
                    cancelButton.setText("Canceled");
                } else if (reservation.getStatus() == Reservation.ReservationStatus.Completed) {
                    cancelButton.setEnabled(false);
                    cancelButton.setBackground(Color.LIGHT_GRAY);
                    cancelButton.setForeground(Color.WHITE);
                    cancelButton.setText("Completed");
                }

                side.add(Box.createVerticalGlue());
                side.add(cancelButton);

                main.add(nameLabel);
                main.add(Box.createVerticalStrut(16));
                main.add(typePanel);
                main.add(Box.createVerticalStrut(4));
                main.add(datePanel);
                main.add(Box.createVerticalStrut(4));
                main.add(locationPanel);
                main.add(Box.createVerticalStrut(4));
                main.add(guestsPanel);
                main.add(Box.createVerticalStrut(4));
                main.add(staffPanel);
                main.add(Box.createVerticalStrut(4));
                main.add(contactPanel);
                main.add(Box.createVerticalStrut(4));
                main.add(equipmentPanel);
                main.add(Box.createVerticalStrut(4));
                main.add(statusPanel);

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
