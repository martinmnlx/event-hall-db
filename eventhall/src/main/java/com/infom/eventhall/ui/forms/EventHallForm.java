package com.infom.eventhall.ui.forms;

import com.infom.eventhall.model.EventHall;
import com.infom.eventhall.service.EventHallService;

import javax.swing.*;
import java.awt.*;

public class EventHallForm extends JDialog {

    private final EventHallService eventHallService;
    private final EventHall eventHall;

    private final JTextField nameField;
    private final JTextField capacityField;
    private final JTextField locationField;
    private final JComboBox<EventHall.HallStatus> statusDropdown;

    public EventHallForm(Frame owner, EventHallService eventHallService, EventHall eventHall) {
        super(owner, true);
        this.eventHallService = eventHallService;
        this.eventHall = eventHall;

        setTitle(eventHall == null ? "Add User" : "Edit User");
        setSize(400, 300);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout());

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Hall Name:"), gbc);
        nameField = new JTextField(20);
        gbc.gridx = 1;
        formPanel.add(nameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("Capacity:"), gbc);
        capacityField = new JTextField(20);
        gbc.gridx = 1;
        formPanel.add(capacityField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(new JLabel("Location:"), gbc);
        locationField = new JTextField(20);
        gbc.gridx = 1;
        formPanel.add(locationField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(new JLabel("Type:"), gbc);
        statusDropdown = new JComboBox<>(EventHall.HallStatus.values());
        gbc.gridx = 1;
        formPanel.add(statusDropdown, gbc);

        add(formPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        JButton saveButton = new JButton(eventHall == null ? "Create" : "Update");
        JButton cancelButton = new JButton("Cancel");

        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        add(buttonPanel, BorderLayout.SOUTH);

        if (eventHall != null) {
            nameField.setText(eventHall.getHallName());
            capacityField.setText(eventHall.getCapacity().toString());
            locationField.setText(eventHall.getLocation());
            statusDropdown.setSelectedItem(eventHall.getStatus());
        }

        saveButton.addActionListener(e -> saveEventHall());
        cancelButton.addActionListener(e -> dispose());
    }

    private void saveEventHall() {
        String name = nameField.getText().trim();
        String capacity = capacityField.getText().trim();
        String location = locationField.getText().trim();
        EventHall.HallStatus status = (EventHall.HallStatus) statusDropdown.getSelectedItem();

        if (name.isEmpty() || capacity.isEmpty() || location.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields must be filled!", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            if (eventHall == null) {
                EventHall newHall = new EventHall();
                newHall.setHallName(name);
                newHall.setCapacity(Integer.valueOf(capacity));
                newHall.setLocation(location);
                newHall.setStatus(status);
                eventHallService.createEventHall(newHall);
                JOptionPane.showMessageDialog(this, "Event Hall added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                eventHall.setHallName(name);
                eventHall.setCapacity(Integer.valueOf(capacity));
                eventHall.setLocation(location);
                eventHall.setStatus(status);
                eventHallService.updateEventHall(eventHall);
                JOptionPane.showMessageDialog(this, "Event Hall updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            }
            dispose();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

}
