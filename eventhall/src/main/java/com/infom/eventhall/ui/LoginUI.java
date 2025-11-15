package com.infom.eventhall.ui;


import javax.swing.*;
import java.awt.*;

public class LoginUI extends JPanel {
    private JTextField emailField;
    private JPasswordField passwordField;
    private JButton loginButton;

    private CardLayout cardLayout;
    private JPanel mainPanel;

    public LoginUI(CardLayout cardLayout, JPanel mainPanel) {
        this.cardLayout = cardLayout;
        this.mainPanel = mainPanel;

        emailField = new JTextField(20);
        passwordField = new JPasswordField(20);
        loginButton = new JButton("Login");

        add(new JLabel("Email:"));
        add(emailField);
        add(new JLabel("Password:"));
        add(passwordField);
        add(loginButton);

        // loginButton.addActionListener(e -> handleLogin());
    }

    /*
    private void handleLogin() {
        String email = emailField.getText();
        String password = new String(passwordField.getPassword());

        User user = UserDAO.checkUser(email, password); // DAO does the DB check

        if (user != null) {
            // switch to dashboard panel
            cardLayout.show(mainPanel, "dashboard");
        } else {
            JOptionPane.showMessageDialog(this, "Invalid login");
        }
    }
    */
}

