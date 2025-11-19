package com.infom.eventhall.ui.forms;

import com.infom.eventhall.model.Equipment;
import com.infom.eventhall.service.EquipmentService;

import javax.swing.*;
import java.awt.*;

public class EquipmentForm extends JDialog {

    private final EquipmentService equipmentService;
    private final Equipment equipment;

    private final JTextField nameField;
    private final JTextField quantityField;
    private final JComboBox<Equipment.EquipmentStatus> statusDropdown;

    public EquipmentForm(Frame owner, EquipmentService equipmentService, Equipment equipment) {
        super(owner, true);
        this.equipmentService = equipmentService;
        this.equipment = equipment;

        setTitle(equipment == null ? "Add Equipment" : "Edit Equipment");
        setSize(400, 300);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout());

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Name
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Equipment Name:"), gbc);
        nameField = new JTextField(20);
        gbc.gridx = 1;
        formPanel.add(nameField, gbc);

        // Quantity
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("Total Quantity:"), gbc);
        quantityField = new JTextField(20);
        gbc.gridx = 1;
        formPanel.add(quantityField, gbc);

        // Status
        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(new JLabel("Status:"), gbc);
        statusDropdown = new JComboBox<>(Equipment.EquipmentStatus.values());
        gbc.gridx = 1;
        formPanel.add(statusDropdown, gbc);

        add(formPanel, BorderLayout.CENTER);

        // Buttons
        JPanel buttonPanel = new JPanel();
        JButton saveButton = new JButton(equipment == null ? "Create" : "Update");
        JButton cancelButton = new JButton("Cancel");

        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        add(buttonPanel, BorderLayout.SOUTH);

        if (equipment != null) {
            nameField.setText(equipment.getEquipmentName());
            quantityField.setText(equipment.getQuantityTotal().toString());
            statusDropdown.setSelectedItem(equipment.getEquipment());
        }

        saveButton.addActionListener(e -> saveEquipment());
        cancelButton.addActionListener(e -> dispose());
    }

    private void saveEquipment() {
        String name = nameField.getText().trim();
        String quantity = quantityField.getText().trim();
        Equipment.EquipmentStatus status = (Equipment.EquipmentStatus) statusDropdown.getSelectedItem();

        if (name.isEmpty() || quantity.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields must be filled!", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            if (equipment == null) {
                Equipment newEquipment = new Equipment();
                newEquipment.setEquipmentName(name);
                newEquipment.setQuantityTotal(Integer.valueOf(quantity));
                newEquipment.setEquipment(status);
                equipmentService.addEquipment(newEquipment);
                JOptionPane.showMessageDialog(this, "Equipment added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                equipment.setEquipmentName(name);
                equipment.setQuantityTotal(Integer.valueOf(quantity));
                equipment.setEquipment(status);
                equipmentService.updateEquipment(equipment);
                JOptionPane.showMessageDialog(this, "Equipment updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            }
            dispose();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

}
