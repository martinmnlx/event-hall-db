package com.infom.eventhall.ui;

import com.infom.eventhall.model.User;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class LoginUI extends JPanel {

    private final AppFrame app;

    private final JLabel titleLabel;
    private final JLabel emailLabel;
    private final JLabel passwordLabel;
    private final JTextField emailField;
    private final JPasswordField passwordField;
    private final JButton loginButton;
    private final JButton registerButton;
    private JLabel warningLabel;

    public LoginUI(AppFrame app) {
        this.app = app;

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.GRAY, 1),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        card.setMaximumSize(new Dimension(350, 600));
        card.setAlignmentX(Component.CENTER_ALIGNMENT);

        titleLabel = app.createLabel("EventBuddy", Color.BLUE, 60f, 3);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        emailLabel = app.createLabel("Email", Color.BLACK, 20f, 2);
        emailLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        passwordLabel = app.createLabel("Password", Color.BLACK, 20f, 2);
        passwordLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        warningLabel = app.createLabel(" ", Color.RED, 16f, 2);
        warningLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        emailField = new JTextField();
        emailField.setMaximumSize(new Dimension(300, 30));
        emailField.setMargin(new Insets(8, 8, 8, 8));
        emailField.setFont(app.getRegularFont().deriveFont(16f));
        emailField.setAlignmentX(Component.LEFT_ALIGNMENT);

        passwordField = new JPasswordField();
        passwordField.setMaximumSize(new Dimension(300, 30));
        passwordField.setMargin(new Insets(8, 8, 8, 8));
        passwordField.setFont(app.getRegularFont().deriveFont(16f));
        passwordField.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel bottom = new JPanel();
        bottom.setLayout(new BoxLayout(bottom, BoxLayout.X_AXIS));

        loginButton = app.createButton("Login", 20f);
        loginButton.setAlignmentX(Component.LEFT_ALIGNMENT);

        registerButton = app.createButton("Create Account", 16f);
        registerButton.setForeground(Color.BLUE);
        registerButton.setBorderPainted(false);
        registerButton.setContentAreaFilled(false);
        registerButton.setFocusPainted(false);
        registerButton.setOpaque(false);
        registerButton.setAlignmentX(Component.RIGHT_ALIGNMENT);

        bottom.add(loginButton);
        bottom.add(Box.createHorizontalStrut(4));
        bottom.add(registerButton);
        bottom.setAlignmentX(Component.LEFT_ALIGNMENT);

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
        card.add(bottom);
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


    private void handleLogin() {
        if (emailField.getText().isEmpty()) {
            warningLabel.setText("Email cannot be left blank!");
        } else {
            User user = app.getDb().getUserDAO().getUserByEmail(emailField.getText());
            if (user != null) {
                System.out.println("User with email exists!");

                if (Objects.equals(new String(passwordField.getPassword()), user.getPassword())) {
                    System.out.println("Login successful!");
                    warningLabel.setText("Login successful!");
                    app.setUser(user);
                    System.out.println("Username: " + app.getUser().getName());
                    app.showScreen("dashboard");
                } else {
                    System.out.println("Incorrect password!");
                    warningLabel.setText("Incorrect password!");
                }
            } else {
                System.out.println("User with email doesn't exist!");
                warningLabel.setText("User with email " + emailField.getText() + " doesn't exist!");
            }
        }
    }
}

