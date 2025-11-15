package com.infom.eventhall.ui;

import com.infom.eventhall.model.User;
import com.infom.eventhall.service.UserService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class RegisterUI extends JPanel {

    private final AppFrame app;
    private final UserService userService;

    private final JLabel titleLabel;
    private final JLabel nameLabel;
    private final JLabel emailLabel;
    private final JLabel phoneLabel;
    private final JLabel passwordLabel;
    private final JTextField nameField;
    private final JTextField emailField;
    private final JTextField phoneField;
    private final JPasswordField passwordField;
    private final JButton registerButton;
    private JLabel warningLabel = new JLabel("Warning");

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

        titleLabel = app.createLabel("Create an Account", Color.BLUE, 40f, 3);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        nameLabel = app.createLabel("Name", Color.BLACK, 20f, 2);
        nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        emailLabel = app.createLabel("Email", Color.BLACK, 20f, 2);
        emailLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        phoneLabel = app.createLabel("Phone Number", Color.BLACK, 20f, 2);
        phoneLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        passwordLabel = app.createLabel("Password", Color.BLACK, 20f, 2);
        passwordLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        warningLabel = app.createLabel(" ", Color.RED, 16f, 2);
        warningLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        nameField = new JTextField();
        nameField.setMaximumSize(new Dimension(300, 30));
        nameField.setMargin(new Insets(8, 8, 8, 8));
        nameField.setFont(app.getRegularFont().deriveFont(16f));
        nameField.setAlignmentX(Component.LEFT_ALIGNMENT);

        emailField = new JTextField();
        emailField.setMaximumSize(new Dimension(300, 30));
        emailField.setMargin(new Insets(8, 8, 8, 8));
        emailField.setFont(app.getRegularFont().deriveFont(16f));
        emailField.setAlignmentX(Component.LEFT_ALIGNMENT);

        phoneField = new JTextField();
        phoneField.setMaximumSize(new Dimension(300, 30));
        phoneField.setMargin(new Insets(8, 8, 8, 8));
        phoneField.setFont(app.getRegularFont().deriveFont(16f));
        phoneField.setAlignmentX(Component.LEFT_ALIGNMENT);

        passwordField = new JPasswordField();
        passwordField.setMaximumSize(new Dimension(300, 30));
        passwordField.setMargin(new Insets(8, 8, 8, 8));
        passwordField.setFont(app.getRegularFont().deriveFont(16f));
        passwordField.setAlignmentX(Component.LEFT_ALIGNMENT);

        registerButton = app.createButton("Register", 20f);
        registerButton.setAlignmentX(Component.LEFT_ALIGNMENT);

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
    }


    private void handleRegistration() {
        String name = nameField.getText();
        String email = emailField.getText();
        String phone = phoneField.getText();
        String password = new String(passwordField.getPassword());

        if (name.isEmpty() || email.isEmpty() || phone.isEmpty() || password.isEmpty()) {
            warningLabel.setText("Fields cannot be left blank!");
        } else {
            if (app.getDb().getUserDAO().getUserByEmail(emailField.getText()) != null) {
                warningLabel.setText("Email already in use!");
            } else {
                userService.registerUser(name, email, phone, password);
                app.showScreen("login");
            }
        }
    }
}

