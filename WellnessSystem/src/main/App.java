package main;

import main.model.*;
import main.dao.UserDAO;
import main.db.DBConnection;
import view.Dashboard;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class App {
    private static UserDAO userDAO = new UserDAO();

    public static void main(String[] args) {
        // Ensure tables are created before login
        new main.dao.StaffDAO();
        SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
    }

    // Custom login JFrame
    static class LoginFrame extends JFrame {
        private JTextField idField = new JTextField(16);
        private JPasswordField passwordField = new JPasswordField(16);
        private JLabel statusLabel = new JLabel(" ");

        public LoginFrame() {
            setTitle("Wellness System - Staff Login");
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setSize(400, 350);
            setLocationRelativeTo(null);
            setResizable(false);
            getContentPane().setBackground(Color.WHITE);
            setLayout(null);

            Color purple = new Color(120, 80, 255);
            Font labelFont = new Font("Segoe UI", Font.PLAIN, 16);
            Font headerFont = new Font("Segoe UI", Font.BOLD, 18);

            JLabel idLabel = new JLabel("Staff ID (6 digits):");
            idLabel.setFont(labelFont);
            idLabel.setBounds(40, 30, 180, 30);
            add(idLabel);

            idField.setBounds(200, 30, 140, 30);
            idField.setBorder(BorderFactory.createLineBorder(purple, 2));
            add(idField);

            JLabel passLabel = new JLabel("Password:");
            passLabel.setFont(labelFont);
            passLabel.setBounds(40, 80, 180, 30);
            add(passLabel);

            passwordField.setBounds(200, 80, 140, 30);
            passwordField.setBorder(BorderFactory.createLineBorder(purple, 2));
            add(passwordField);

            JButton loginBtn = new JButton("Login");
            loginBtn.setBackground(purple);
            loginBtn.setForeground(Color.WHITE);
            loginBtn.setFont(labelFont);
            loginBtn.setFocusPainted(false);
            loginBtn.setBounds(110, 130, 80, 35);
            loginBtn.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
            add(loginBtn);

            JButton clearBtn = new JButton("Clear");
            clearBtn.setFont(labelFont);
            clearBtn.setBounds(210, 130, 80, 35);
            clearBtn.setBackground(Color.WHITE);
            clearBtn.setForeground(purple);
            clearBtn.setBorder(BorderFactory.createLineBorder(purple, 2));
            add(clearBtn);

            statusLabel.setBounds(40, 170, 320, 20);
            statusLabel.setForeground(purple);
            add(statusLabel);

            JLabel sampleLabel = new JLabel("Sample Staff IDs");
            sampleLabel.setFont(headerFont);
            sampleLabel.setForeground(purple);
            sampleLabel.setBounds(40, 190, 320, 25);
            add(sampleLabel);

            JTextArea sampleBox = new JTextArea(
                "Admin: 100001 (password: admin123)\n" +
                "Receptionist: 200001 (password: reception789)\n" +
                "Counselor: 300001 (password: counselor123)\n" +
                "Counselor: 300002 (password: counselor456)"
            );
            sampleBox.setEditable(false);
            sampleBox.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            sampleBox.setForeground(purple);
            sampleBox.setBackground(new Color(245,245,255));
            sampleBox.setBorder(BorderFactory.createLineBorder(purple, 1));
            sampleBox.setBounds(40, 220, 300, 60);
            add(sampleBox);

            loginBtn.addActionListener(e -> doLogin());
            clearBtn.addActionListener(e -> {
                idField.setText("");
                passwordField.setText("");
                statusLabel.setText(" ");
            });
        }

        private void doLogin() {
            String idStr = idField.getText().trim();
            String password = new String(passwordField.getPassword());
            if (idStr.isEmpty() || password.isEmpty()) {
                statusLabel.setText("Please enter both ID and password.");
                return;
            }
            int id;
            try {
                id = Integer.parseInt(idStr);
            } catch (NumberFormatException e) {
                statusLabel.setText("ID must be a number.");
                return;
            }
            
            // Authenticate using database
            User user = userDAO.authenticateUser(id, password);
            if (user != null) {
                dispose();
                SwingUtilities.invokeLater(() -> new Dashboard(user).setVisible(true));
            } else {
                statusLabel.setText("Login failed. Try again.");
            }
        }
    }
} 