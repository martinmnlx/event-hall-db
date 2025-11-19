package com.infom.eventhall.ui;

import com.infom.eventhall.model.User;
import com.infom.eventhall.service.UserService;
import lombok.Getter;

import javax.swing.*;
import java.awt.*;

public class LoginUI extends JPanel {

    private final AppFrame app;
    private final UserService userService;

    private final JLabel titleLabel;
    private final JLabel emailLabel;
    private final JLabel passwordLabel;
    private final JTextField emailField;
    private final JPasswordField passwordField;
    private final JButton loginButton;
    private final JButton registerButton;
    @Getter
    private JLabel warningLabel;

    public LoginUI(AppFrame app, UserService userService) {
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

        titleLabel = app.createLabel("EventBuddy", Color.BLUE, 80f, 3);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        emailLabel = app.createLabel("Email", Color.BLACK, 20f, 2);
        emailLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        passwordLabel = app.createLabel("Password", Color.BLACK, 20f, 2);
        passwordLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        warningLabel = app.createLabel(" ", Color.RED, 16f, 2);
        warningLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        emailField = app.createTextField();

        passwordField = app.createPasswordField();

        loginButton = app.createButton("Login", Color.BLUE, 20f, true);

        registerButton = app.createButton("Sign Up", Color.WHITE, 20f, true);
        registerButton.setForeground(Color.BLUE);
        registerButton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.BLUE, 1),
                BorderFactory.createEmptyBorder(8, 8, 8, 8)
        ));

        card.add(Box.createVerticalGlue());
        card.add(emailLabel);
        card.add(Box.createVerticalStrut(8));
        card.add(emailField);
        card.add(Box.createVerticalStrut(12));
        card.add(passwordLabel);
        card.add(Box.createVerticalStrut(8));
        card.add(passwordField);
        card.add(Box.createVerticalStrut(20));
        card.add(warningLabel);
        card.add(Box.createVerticalStrut(20));
        card.add(loginButton);
        card.add(Box.createVerticalStrut(16));
        card.add(registerButton);
        card.add(Box.createVerticalGlue());

        add(Box.createVerticalGlue());
        add(titleLabel);
        add(Box.createVerticalStrut(60));
        add(card);
        add(Box.createVerticalGlue());

        app.getRootPane().setDefaultButton(loginButton);

        loginButton.addActionListener(e -> handleLogin());
        registerButton.addActionListener(e -> app.showScreen("register"));
    }

    public void refresh() {
        emailField.setText("");
        passwordField.setText("");
        warningLabel.setText("");
    }

    private void handleLogin() {
        String email = emailField.getText();
        String password = new String(passwordField.getPassword());

        if (emailField.getText().isEmpty()) {
            warningLabel.setText("Email cannot be left blank!");
            return;
        }

        if (userService.getUserByEmail(email) == null) {
            warningLabel.setText("User with email " + email + " doesn't exist!");
            return;
        }

        if (userService.authenticateUser(email, password) == null) {
            warningLabel.setText("Incorrect password!");
            return;
        }

        app.setUser(userService.authenticateUser(email, password));
        System.out.println("Username: " + app.getUser().getName());
        if (app.getUser().getType().equals(User.UserType.Customer)) app.showScreen("dashboard");
        else app.showScreen("admin");
    }

}

