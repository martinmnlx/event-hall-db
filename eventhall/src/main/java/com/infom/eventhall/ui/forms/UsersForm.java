package com.infom.eventhall.ui.forms;

import com.infom.eventhall.model.User;
import com.infom.eventhall.service.UserService;

import javax.swing.*;
import java.awt.*;

public class UsersForm extends JDialog {

    private final UserService userService;
    private User user;

    private JComboBox<User.UserType> typeDropdown;
    private JTextField nameField;
    private JTextField emailField;
    private JTextField phoneField;
    private JTextField passwordField;

    private JButton saveButton;
    private JButton cancelButton;

    public UsersForm(Frame owner, UserService userService, User user) {
        super(owner, true); // modal
        this.userService = userService;
        this.user = user;

        setTitle(user == null ? "Add User" : "Edit User");
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
        formPanel.add(new JLabel("Type:"), gbc);
        typeDropdown = new JComboBox<>(User.UserType.values());
        gbc.gridx = 1;
        formPanel.add(typeDropdown, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("Name:"), gbc);
        nameField = new JTextField(20);
        gbc.gridx = 1;
        formPanel.add(nameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(new JLabel("Email:"), gbc);
        emailField = new JTextField(20);
        gbc.gridx = 1;
        formPanel.add(emailField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(new JLabel("Phone:"), gbc);
        phoneField = new JTextField(20);
        gbc.gridx = 1;
        formPanel.add(phoneField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        formPanel.add(new JLabel("Password:"), gbc);
        passwordField = new JTextField(20);
        gbc.gridx = 1;
        formPanel.add(passwordField, gbc);

        add(formPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        saveButton = new JButton(user == null ? "Create" : "Update");
        cancelButton = new JButton("Cancel");

        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        add(buttonPanel, BorderLayout.SOUTH);

        if (user != null) {
            typeDropdown.setSelectedItem(user.getType());
            typeDropdown.setEnabled(false);
            nameField.setText(user.getName());
            emailField.setText(user.getEmail());
            phoneField.setText(user.getPhoneNumber());
            passwordField.setText(user.getPassword());
        }

        saveButton.addActionListener(e -> saveUser());
        cancelButton.addActionListener(e -> dispose());
    }

    private void saveUser() {
        User.UserType type = (User.UserType) typeDropdown.getSelectedItem();
        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String phone = phoneField.getText().trim();
        String password = passwordField.getText().trim();

        if (name.isEmpty() || email.isEmpty() || phone.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields must be filled!", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            if (user == null) {
                // Create new user
                User newUser = new User();
                newUser.setType(type);
                newUser.setName(name);
                newUser.setEmail(email);
                newUser.setPhoneNumber(phone);
                newUser.setPassword(password);
                userService.createUser(newUser);
                JOptionPane.showMessageDialog(this, "User added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                // Update existing user
                user.setType(type);
                System.out.println(user.getType().name());
                user.setName(name);
                user.setEmail(email);
                user.setPhoneNumber(phone);
                user.setPassword(password);
                userService.updateUser(user);
                JOptionPane.showMessageDialog(this, "User updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            }
            dispose();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
