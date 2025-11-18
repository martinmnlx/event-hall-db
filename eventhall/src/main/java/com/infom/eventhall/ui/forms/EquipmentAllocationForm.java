package com.infom.eventhall.ui.forms;

import com.infom.eventhall.model.Equipment;
import com.infom.eventhall.model.EquipmentAllocation;
import com.infom.eventhall.model.Reservation;
import com.infom.eventhall.service.EquipmentAllocationService;
import com.infom.eventhall.service.EquipmentService;
import com.infom.eventhall.service.EventHallService;
import com.infom.eventhall.service.ReservationService;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;

public class EquipmentAllocationForm extends JDialog {

    private final EquipmentAllocationService allocService;
    private final ReservationService reservationService;
    private final EquipmentService equipmentService;
    private final EventHallService hallService;
    private EquipmentAllocation alloc;

    // Defined with specific types for safety
    private JComboBox<ReservationItem> reservationDropdown;
    private JComboBox<EquipmentItem> equipmentDropdown;
    private JSpinner quantityPicker;

    private JButton saveButton;
    private JButton cancelButton;

    public EquipmentAllocationForm(Frame owner, EquipmentAllocationService allocService, EquipmentAllocation alloc, ReservationService reservationService, EquipmentService equipmentService, EventHallService hallService) {
        super(owner, true);
        this.allocService = allocService;
        this.alloc = alloc;
        this.reservationService = reservationService;
        this.equipmentService = equipmentService;
        this.hallService = hallService;

        setTitle(alloc == null ? "Allocate Equipment" : "Edit Allocation");
        setSize(450, 300);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout());

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // 1. Reservation Dropdown
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Reservation:"), gbc);

        reservationDropdown = new JComboBox<>();
        for (Reservation reservation : reservationService.getAllReservations()) {
            reservationDropdown.addItem(new ReservationItem(reservation.getReservationId(), hallService.getHallById(reservation.getHallId()).getHallName(), reservation.getEventDate()));
        }

        gbc.gridx = 1;
        formPanel.add(reservationDropdown, gbc);

        // 2. Equipment Dropdown
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("Equipment:"), gbc);

        equipmentDropdown = new JComboBox<>();
        for (Equipment equipment : equipmentService.getAllEquipments()) {
            equipmentDropdown.addItem(new EquipmentItem(equipment.getEquipmentId(), equipment.getEquipmentName()));
        }

        gbc.gridx = 1;
        formPanel.add(equipmentDropdown, gbc);

        // 3. Quantity Used
        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(new JLabel("Quantity Used:"), gbc);
        // Increased max value to 1000, default 1
        quantityPicker = new JSpinner(new SpinnerNumberModel(1, 1, 5, 1));
        gbc.gridx = 1;
        formPanel.add(quantityPicker, gbc);

        // IMPORTANT: Add the form panel to the Dialog
        add(formPanel, BorderLayout.CENTER);

        // Buttons
        JPanel buttonPanel = new JPanel();
        saveButton = new JButton(alloc == null ? "Allocate" : "Update");
        cancelButton = new JButton("Cancel");

        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        add(buttonPanel, BorderLayout.SOUTH);

        // Populate fields if editing
        if (alloc != null) {
            selectComboBoxItem(reservationDropdown, alloc.getReservationId());
            selectComboBoxItem(equipmentDropdown, alloc.getEquipmentId());
            quantityPicker.setValue(alloc.getQuantityUsed());

            // Lock fields that shouldn't change during edit if necessary (Optional)
            // reservationDropdown.setEnabled(false);
        }

        // Button actions
        saveButton.addActionListener(e -> saveAllocation());
        cancelButton.addActionListener(e -> dispose());
    }

    // Fixed Generic Method Signature
    private <T extends Identifiable> void selectComboBoxItem(JComboBox<T> combo, int id) {
        for (int i = 0; i < combo.getItemCount(); i++) {
            if (combo.getItemAt(i).getId() == id) {
                combo.setSelectedIndex(i);
                return;
            }
        }
    }

    private void saveAllocation() {
        ReservationItem selectedRes = (ReservationItem) reservationDropdown.getSelectedItem();
        EquipmentItem selectedEq = (EquipmentItem) equipmentDropdown.getSelectedItem();
        int quantity = (Integer) quantityPicker.getValue();

        // Basic Validation
        if (selectedRes == null || selectedEq == null) {
            JOptionPane.showMessageDialog(this, "Please select a reservation and equipment.", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (quantity <= 0) {
            JOptionPane.showMessageDialog(this, "Quantity must be greater than 0.", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            if (alloc == null) {
                // Create NEW Allocation
                EquipmentAllocation newAlloc = new EquipmentAllocation();
                newAlloc.setReservationId(selectedRes.getId());
                newAlloc.setEquipmentId(selectedEq.getId());
                newAlloc.setQuantityUsed(quantity);

                allocService.createAllocation(newAlloc);
                JOptionPane.showMessageDialog(this, "Equipment allocated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                // Update EXISTING Allocation
                alloc.setReservationId(selectedRes.getId());
                alloc.setEquipmentId(selectedEq.getId());
                alloc.setQuantityUsed(quantity);

                allocService.updateAllocation(alloc);
                JOptionPane.showMessageDialog(this, "Allocation updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            }
            dispose(); // Close dialog
        } catch (Exception ex) {
            ex.printStackTrace(); // Good for debugging
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Interface for Dropdown Items
    interface Identifiable { int getId(); }

    // Helper class for Reservation Dropdown
    private static class ReservationItem implements Identifiable {
        private final int id;
        private String name;
        private final LocalDate date;

        ReservationItem(int id, String name, LocalDate date) { this.id = id; this.name = name; this.date = date; }
        public int getId() { return id; }

        @Override public String toString() { return id + " (" + date + ") " + name; }
    }

    // Helper class for Equipment Dropdown
    private static class EquipmentItem implements Identifiable {
        private final int id;
        private final String name; // Fixed variable name from 'email' to 'name'

        EquipmentItem(int id, String name) { this.id = id; this.name = name; }
        public int getId() { return id; }

        @Override public String toString() { return name; }
    }
}