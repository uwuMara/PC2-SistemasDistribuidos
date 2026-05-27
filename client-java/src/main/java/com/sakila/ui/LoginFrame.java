package com.sakila.ui;

import com.sakila.api.ApiClient;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class LoginFrame extends JFrame {

    private final JTextField usernameField;
    private final JPasswordField passwordField;
    private final ApiClient api;

    public LoginFrame() {

        api = new ApiClient();

        setTitle("Login - Sakila M4");
        setSize(480, 420);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        Color background = new Color(255, 244, 248);
        Color card = Color.WHITE;
        Color darkPink = new Color(173, 20, 87);
        Color borderPink = new Color(255, 202, 212);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(background);
        mainPanel.setBorder(new EmptyBorder(36, 40, 36, 40));

        JPanel cardPanel = new JPanel();
        cardPanel.setLayout(new BoxLayout(cardPanel, BoxLayout.Y_AXIS));
        cardPanel.setBackground(card);
        cardPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(borderPink, 2),
                new EmptyBorder(24, 24, 24, 24)
        ));

        JLabel title = new JLabel("Acceso Staff");
        title.setFont(new Font("Dialog", Font.BOLD, 24));
        title.setForeground(darkPink);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitle = new JLabel("Sistema Distribuido Sakila");
        subtitle.setFont(new Font("Dialog", Font.PLAIN, 13));
        subtitle.setForeground(new Color(120, 80, 100));
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        usernameField = createTextField();
        passwordField = new JPasswordField();
        passwordField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        passwordField.setFont(new Font("Dialog", Font.PLAIN, 14));
        passwordField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(borderPink),
                new EmptyBorder(7, 10, 7, 10)
        ));

        JButton loginButton = new JButton("Ingresar");
        loginButton.setBackground(darkPink);
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);
        loginButton.setFont(new Font("Dialog", Font.BOLD, 14));
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        loginButton.addActionListener(e -> login());

        cardPanel.add(title);
        cardPanel.add(Box.createVerticalStrut(4));
        cardPanel.add(subtitle);
        cardPanel.add(Box.createVerticalStrut(28));

        cardPanel.add(createLabel("Usuario"));
        cardPanel.add(usernameField);
        cardPanel.add(Box.createVerticalStrut(12));

        cardPanel.add(createLabel("Contraseña"));
        cardPanel.add(passwordField);
        cardPanel.add(Box.createVerticalStrut(20));

        cardPanel.add(loginButton);

        mainPanel.add(cardPanel, BorderLayout.CENTER);

        add(mainPanel);
        setVisible(true);
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text, SwingConstants.CENTER);
        label.setFont(new Font("Dialog", Font.BOLD, 13));
        label.setForeground(new Color(60, 45, 55));
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        label.setMaximumSize(new Dimension(Integer.MAX_VALUE, 24));
        return label;
    }

    private JTextField createTextField() {
        JTextField field = new JTextField();
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        field.setFont(new Font("Dialog", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(255, 202, 212)),
                new EmptyBorder(7, 10, 7, 10)
        ));
        field.setAlignmentX(Component.CENTER_ALIGNMENT);
        return field;
    }

    private void login() {

        try {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            String json = String.format("""
                    {
                        "username": "%s",
                        "password": "%s"
                    }
                    """, username, password);

            String response = api.post("/auth/login", json);

            int staffId = Integer.parseInt(
                    api.extractNumber(response, "staff_id")
            );

            String firstName = api.extractString(response, "first_name");
            String lastName = api.extractString(response, "last_name");

            JOptionPane.showMessageDialog(
                    this,
                    "Bienvenida/o " + firstName + " " + lastName,
                    "Login correcto",
                    JOptionPane.INFORMATION_MESSAGE
            );

            dispose();

            new MainFrame(
                    staffId,
                    firstName + " " + lastName
            );

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Credenciales inválidas o error de conexión.",
                    "Error de login",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}