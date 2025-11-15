package com.infom.eventhall.ui;

import com.infom.eventhall.dao.UserDAO;
import com.infom.eventhall.model.User;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class LoginUI extends JPanel {

    private final AppFrame app;
    private final UserDAO dao;

    private final JLabel titleLabel;
    private final JLabel emailLabel;
    private final JLabel passwordLabel;
    private final JTextField emailField;
    private final JPasswordField passwordField;
    private final JButton loginButton;
    private JLabel warningLabel = new JLabel("Warning");

    public LoginUI(AppFrame app, UserDAO dao) {
        this.app = app;
        this.dao = dao;

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
        emailField.setMargin(new Insets(8, 4, 8, 4));
        emailField.setFont(app.getRegularFont().deriveFont(16f));
        emailField.setAlignmentX(Component.LEFT_ALIGNMENT);

        passwordField = new JPasswordField();
        passwordField.setMaximumSize(new Dimension(300, 30));
        passwordField.setMargin(new Insets(8, 4, 8, 4));
        passwordField.setFont(app.getRegularFont().deriveFont(16f));
        passwordField.setAlignmentX(Component.LEFT_ALIGNMENT);

        loginButton = app.createButton("Login", 20f);
        loginButton.setAlignmentX(Component.LEFT_ALIGNMENT);

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
        card.add(Box.createVerticalGlue());

        add(Box.createVerticalGlue());
        add(titleLabel);
        add(Box.createVerticalStrut(60));
        add(card);
        add(Box.createVerticalGlue());

        loginButton.addActionListener(e -> handleLogin());
    }


    private void handleLogin() {
        if (emailField.getText().isEmpty()) {
            warningLabel.setText("Email cannot be left blank!");
        } else {
            User user = dao.getUserByEmail(emailField.getText());
            if (user != null) {
                System.out.println("User with email exists!");

                if (Objects.equals(new String(passwordField.getPassword()), user.getPassword())) {
                    System.out.println("Login successful!");
                    warningLabel.setText("Login successful!");
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

