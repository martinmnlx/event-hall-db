package com.infom.eventhall.ui.forms;

import com.infom.eventhall.model.Staff;
import com.infom.eventhall.service.StaffService;

import javax.swing.*;
import java.awt.*;

public class StaffForm extends JDialog {

    private final StaffService staffService;
    private final Staff staff;

    private final JTextField nameField;
    private final JTextField phoneField;
    private final JTextField roleField;

    public StaffForm(Frame owner, StaffService staffService, Staff staff) {
        super(owner, true);
        this.staffService = staffService;
        this.staff = staff;

        setTitle(staff == null ? "Add Staff" : "Edit Staff");
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
        formPanel.add(new JLabel("Name:"), gbc);
        nameField = new JTextField(20);
        gbc.gridx = 1;
        formPanel.add(nameField, gbc);

        // Phone
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("Phone:"), gbc);
        phoneField = new JTextField(20);
        gbc.gridx = 1;
        formPanel.add(phoneField, gbc);

        // Role
        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(new JLabel("Role:"), gbc);
        roleField = new JTextField(20);
        gbc.gridx = 1;
        formPanel.add(roleField, gbc);

        add(formPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        JButton saveButton = new JButton(staff == null ? "Create" : "Update");
        JButton cancelButton = new JButton("Cancel");

        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        add(buttonPanel, BorderLayout.SOUTH);

        if (staff != null) {
            nameField.setText(staff.getName());
            phoneField.setText(staff.getPhoneNumber());
            roleField.setText(staff.getRole());
        }

        saveButton.addActionListener(e -> saveStaff());
        cancelButton.addActionListener(e -> dispose());
    }

    private void saveStaff() {
        String name = nameField.getText().trim();
        String phone = phoneField.getText().trim();
        String role = roleField.getText().trim();

        if (name.isEmpty() || phone.isEmpty() || role.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields must be filled!", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            if (staff == null) {
                Staff newStaff = new Staff();
                newStaff.setName(name);
                newStaff.setPhoneNumber(phone);
                newStaff.setRole(role);
                staffService.addStaff(newStaff);
                JOptionPane.showMessageDialog(this, "Staff added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                staff.setName(name);
                staff.setPhoneNumber(phone);
                staff.setRole(role);
                staffService.updateStaff(staff);
                JOptionPane.showMessageDialog(this, "Staff updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            }
            dispose();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

}
