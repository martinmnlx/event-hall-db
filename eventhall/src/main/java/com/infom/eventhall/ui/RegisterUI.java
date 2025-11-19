package com.infom.eventhall.ui;

import com.infom.eventhall.model.User;
import com.infom.eventhall.service.UserService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class RegisterUI extends JPanel {

    private final AppFrame app;
    private final UserService userService;

    private final JTextField nameField;
    private final JTextField emailField;
    private final JTextField phoneField;
    private final JPasswordField passwordField;
    private final JButton registerButton;
    private final JLabel warningLabel;

    public RegisterUI(AppFrame app, UserService userService) {
        this.app = app;
        this.userService = userService;

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.GRAY, 1),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        card.setMaximumSize(new Dimension(350, 600));
        card.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel titleLabel = app.createLabel("Create an Account", Color.BLUE, 60f, 3);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel nameLabel = app.createLabel("Name", Color.BLACK, 20f, 2);
        nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel emailLabel = app.createLabel("Email", Color.BLACK, 20f, 2);
        emailLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel phoneLabel = app.createLabel("Phone Number", Color.BLACK, 20f, 2);
        phoneLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel passwordLabel = app.createLabel("Password", Color.BLACK, 20f, 2);
        passwordLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        warningLabel = app.createLabel(" ", Color.RED, 16f, 2);
        warningLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        nameField = app.createTextField();

        emailField = app.createTextField();

        phoneField = app.createTextField();

        passwordField = app.createPasswordField();

        registerButton = app.createButton("Create Account", Color.BLUE, 20f, true);

        JButton cancelButton = app.createButton("Cancel", Color.decode("#F94449"), 20f, true);

        card.add(Box.createVerticalGlue());
        card.add(nameLabel);
        card.add(Box.createVerticalStrut(8));
        card.add(nameField);
        card.add(Box.createVerticalStrut(12));
        card.add(emailLabel);
        card.add(Box.createVerticalStrut(8));
        card.add(emailField);
        card.add(Box.createVerticalStrut(12));
        card.add(phoneLabel);
        card.add(Box.createVerticalStrut(8));
        card.add(phoneField);
        card.add(Box.createVerticalStrut(12));
        card.add(passwordLabel);
        card.add(Box.createVerticalStrut(8));
        card.add(passwordField);
        card.add(Box.createVerticalStrut(20));
        card.add(warningLabel);
        card.add(Box.createVerticalStrut(20));
        card.add(registerButton);
        card.add(Box.createVerticalStrut(16));
        card.add(cancelButton);
        card.add(Box.createVerticalGlue());

        add(Box.createVerticalGlue());
        add(titleLabel);
        add(Box.createVerticalStrut(60));
        add(card);
        add(Box.createVerticalGlue());

        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                app.getRootPane().setDefaultButton(registerButton);
            }
        });

        registerButton.addActionListener(e -> handleRegistration());
        cancelButton.addActionListener(e -> app.showScreen("login"));
    }

    private void handleRegistration() {
        String name = nameField.getText();
        String email = emailField.getText();
        String phone = phoneField.getText();
        String password = new String(passwordField.getPassword());

        if (name.isEmpty() || email.isEmpty() || phone.isEmpty() || password.isEmpty()) {
            warningLabel.setText("Fields cannot be left blank!");
        } else {
            if (userService.getUserByEmail(emailField.getText()) != null) {
                warningLabel.setText("Email already in use!");
            } else {
                userService.registerUser(name, email, phone, password);

                JOptionPane.showMessageDialog(
                        this,
                        "Account registered successfully! You may login now.",
                        "Registration Successful",
                        JOptionPane.INFORMATION_MESSAGE
                );

                app.showScreen("login");
            }
        }
    }

}