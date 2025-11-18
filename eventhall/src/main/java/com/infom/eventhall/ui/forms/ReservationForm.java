package com.infom.eventhall.ui.forms;

import com.infom.eventhall.model.*;
import com.infom.eventhall.service.*;

import javax.swing.*;
import java.awt.*;

public class ReservationForm extends JDialog {

    private final ReservationService reservationService;
    private final EventHallService eventHallService;
    private final UserService userService;
    private final StaffService staffService;
    private Reservation reservation;

    private JComboBox<HallItem> hallDropdown;
    private JComboBox<UserItem> userDropdown;
    private JComboBox<StaffItem> staffDropdown;
    private JComboBox<String> typeDropdown;
    private JSpinner guestSpinner;
    private JComboBox<Reservation.ReservationStatus> statusDropdown;

    private JButton saveButton;
    private JButton cancelButton;

    public ReservationForm(Frame owner, ReservationService reservationService, Reservation reservation, EventHallService eventHallService, UserService userService, StaffService staffService) {
        super(owner, true);
        this.reservationService = reservationService;
        this.reservation = reservation;
        this.eventHallService = eventHallService;
        this.userService = userService;
        this.staffService = staffService;

        setTitle(reservation == null ? "Add Reservation" : "Edit Reservation");
        setSize(400, 350);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // --- Hall Dropdown ---
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Event Hall:"), gbc);

        hallDropdown = new JComboBox<>();
        for (EventHall hall : eventHallService.getAllEventHalls())
            hallDropdown.addItem(new HallItem(hall.getHallId(), hall.getHallName()));

        gbc.gridx = 1;
        formPanel.add(hallDropdown, gbc);

        // --- User Dropdown ---
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("Customer:"), gbc);

        userDropdown = new JComboBox<>();
        for (User user : userService.getAllUsers())
            userDropdown.addItem(new UserItem(user.getUserId(), user.getEmail()));

        gbc.gridx = 1;
        formPanel.add(userDropdown, gbc);

        // --- Staff Dropdown ---
        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(new JLabel("Staff In-Charge:"), gbc);

        staffDropdown = new JComboBox<>();
        for (Staff staff : staffService.getAllStaff())
            staffDropdown.addItem(new StaffItem(staff.getStaffId(), staff.getName()));

        gbc.gridx = 1;
        formPanel.add(staffDropdown, gbc);

        // --- Type Dropdown ---
        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(new JLabel("Event Type:"), gbc);

        typeDropdown = new JComboBox<>(new String[]{"Birthday", "Wedding", "Meeting", "Conference", "Other"});

        gbc.gridx = 1;
        formPanel.add(typeDropdown, gbc);

        // --- Guest Count ---
        gbc.gridx = 0;
        gbc.gridy = 4;
        formPanel.add(new JLabel("Guest Count:"), gbc);

        guestSpinner = new JSpinner(new SpinnerNumberModel(0, 0, eventHallService.getHallById(reservation.getHallId()).getCapacity().intValue(), 10));

        gbc.gridx = 1;
        formPanel.add(guestSpinner, gbc);

        // --- Status Dropdown ---
        gbc.gridx = 0;
        gbc.gridy = 5;
        formPanel.add(new JLabel("Status:"), gbc);

        statusDropdown = new JComboBox<>(Reservation.ReservationStatus.values());
        gbc.gridx = 1;
        formPanel.add(statusDropdown, gbc);

        add(formPanel, BorderLayout.CENTER);

        // Buttons
        JPanel buttonPanel = new JPanel();
        saveButton = new JButton(reservation == null ? "Create" : "Update");
        cancelButton = new JButton("Cancel");
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // Populate if editing
        if (reservation != null) {
            selectComboBoxItem(hallDropdown, reservation.getHallId());
            selectComboBoxItem(userDropdown, reservation.getUserId());
            selectComboBoxItem(staffDropdown, reservation.getStaffId());
            guestSpinner.setValue(reservation.getGuestCount());
            statusDropdown.setSelectedItem(reservation.getStatus());
        }

        // Button actions
        saveButton.addActionListener(e -> saveReservation());
        cancelButton.addActionListener(e -> dispose());
    }

    private <T extends Identifiable> void selectComboBoxItem(JComboBox<T> combo, int id) {
        for (int i = 0; i < combo.getItemCount(); i++) {
            if (combo.getItemAt(i).getId() == id) {
                combo.setSelectedIndex(i);
                return;
            }
        }
    }

    private void saveReservation() {
        HallItem hall = (HallItem) hallDropdown.getSelectedItem();
        UserItem user = (UserItem) userDropdown.getSelectedItem();
        StaffItem staff = (StaffItem) staffDropdown.getSelectedItem();
        String type = (String) typeDropdown.getSelectedItem();
        String guests = guestSpinner.getValue().toString();
        Reservation.ReservationStatus status = (Reservation.ReservationStatus) statusDropdown.getSelectedItem();

        if (hall == null || user == null || staff == null || status == null) {
            JOptionPane.showMessageDialog(this, "All fields must be selected!", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            if (reservation == null) {
                Reservation newRes = new Reservation();
                newRes.setHallId(hall.getId());
                newRes.setUserId(user.getId());
                newRes.setStaffId(staff.getId());
                newRes.setEventType(type);
                newRes.setGuestCount(Integer.valueOf(guests));
                newRes.setStatus(status);
                reservationService.createReservation(newRes, null);
                JOptionPane.showMessageDialog(this, "Reservation added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                reservation.setHallId(hall.getId());
                reservation.setUserId(user.getId());
                reservation.setStaffId(staff.getId());
                reservation.setEventType(type);
                reservation.setStatus(status);
                reservation.setGuestCount(Integer.valueOf(guests));
                reservationService.updateReservation(reservation);
                JOptionPane.showMessageDialog(this, "Reservation updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            }
            dispose();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // --- Helper classes ---
    interface Identifiable { int getId(); }

    private static class HallItem implements Identifiable {
        private final int id;
        private final String name;
        HallItem(int id, String name) { this.id = id; this.name = name; }
        public int getId() { return id; }
        @Override public String toString() { return id + " " + name; }
    }

    private static class UserItem implements Identifiable {
        private final int id;
        private final String email;
        UserItem(int id, String email) { this.id = id; this.email = email; }
        public int getId() { return id; }
        @Override public String toString() { return id + " " + email; }
    }

    private static class StaffItem implements Identifiable {
        private final int id;
        private final String name;
        StaffItem(int id, String name) { this.id = id; this.name = name; }
        public int getId() { return id; }
        @Override public String toString() { return id + " " + name; }
    }
}
