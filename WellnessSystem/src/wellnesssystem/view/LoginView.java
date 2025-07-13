package wellnesssystem.view;

import wellnesssystem.model.*;
import wellnesssystem.WellnessSystem;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Login form for staff authentication
 * Demonstrates role-based access control
 */
public class LoginView extends JFrame {
    private JTextField idField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton clearButton;
    private JLabel statusLabel;
    
    private StaffRepository staffRepo;
    private Staff currentStaff;

    public LoginView() {
        staffRepo = WellnessSystem.staffRepo;
        initializeComponents();
        setupLayout();
        setupEventHandlers();
    }

    private void initializeComponents() {
        setTitle("Wellness System - Staff Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(420, 340);
        setLocationRelativeTo(null);
        setResizable(false);

        idField = new JTextField(20);
        idField.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        idField.setPreferredSize(new Dimension(220, 44));
        idField.setMinimumSize(new Dimension(220, 44));
        idField.setMaximumSize(new Dimension(220, 44));
        idField.setForeground(new Color(50, 50, 50));
        idField.setBackground(Color.WHITE);
        idField.setCaretColor(new Color(130, 90, 255));
        idField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(130, 90, 255)),
            BorderFactory.createEmptyBorder(6, 10, 6, 10)
        ));
        passwordField = new JPasswordField(20);
        passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        passwordField.setPreferredSize(new Dimension(220, 44));
        passwordField.setMinimumSize(new Dimension(220, 44));
        passwordField.setMaximumSize(new Dimension(220, 44));
        passwordField.setForeground(new Color(50, 50, 50));
        passwordField.setBackground(Color.WHITE);
        passwordField.setCaretColor(new Color(130, 90, 255));
        passwordField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(130, 90, 255)),
            BorderFactory.createEmptyBorder(6, 10, 6, 10)
        ));
        loginButton = new JButton("Login");
        clearButton = new JButton("Clear");
        statusLabel = new JLabel("Please enter your credentials");
        statusLabel.setForeground(new Color(130, 90, 255)); // purple accent
        // Modern button style
        loginButton.setBackground(new Color(130, 90, 255));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        clearButton.setBackground(new Color(240, 240, 240));
        clearButton.setForeground(new Color(130, 90, 255));
        clearButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
    }

    private void setupLayout() {
        setLayout(new BorderLayout());
        // Main panel with GridBagLayout
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(new Color(248, 250, 252));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        // Title
        JLabel titleLabel = new JLabel("Staff Login");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(new Color(130, 90, 255));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        mainPanel.add(titleLabel, gbc);

        // ID Label and Field
        JLabel idLabel = new JLabel("Staff ID (6 digits):");
        idLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        idLabel.setForeground(new Color(70, 70, 70));
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.EAST;
        mainPanel.add(idLabel, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        mainPanel.add(idField, gbc);

        // Password Label and Field
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        passwordLabel.setForeground(new Color(70, 70, 70));
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.EAST;
        mainPanel.add(passwordLabel, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        mainPanel.add(passwordField, gbc);

        // Buttons Panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(new Color(248, 250, 252));
        buttonPanel.add(loginButton);
        buttonPanel.add(clearButton);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        mainPanel.add(buttonPanel, gbc);

        // Status Label
        gbc.gridy = 4;
        mainPanel.add(statusLabel, gbc);

        add(mainPanel, BorderLayout.CENTER);

        // Add some sample staff info
        JPanel infoPanel = new JPanel();
        infoPanel.setBackground(new Color(255, 255, 255));
        infoPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(130, 90, 255)), "Sample Staff IDs", 0, 0, new Font("Segoe UI", Font.BOLD, 14), new Color(130, 90, 255)));
        JTextArea infoArea = new JTextArea(
            "Admin: 100001 (password: admin123)\n" +
            "Receptionist: 200001 (password: reception789)\n" +
            "Counselor: 300001 (password: counselor123)"
        );
        infoArea.setEditable(false);
        infoArea.setBackground(new Color(255, 255, 255));
        infoArea.setForeground(new Color(70, 70, 70));
        infoArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        infoPanel.add(infoArea);
        add(infoPanel, BorderLayout.SOUTH);
    }

    private void setupEventHandlers() {
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleLogin();
            }
        });

        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearFields();
            }
        });

        // Enter key in password field triggers login
        passwordField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleLogin();
            }
        });
    }

    private void handleLogin() {
        try {
            // Get input values
            String idText = idField.getText().trim();
            String password = new String(passwordField.getPassword());



            // Validate input
            if (idText.isEmpty() || password.isEmpty()) {
                showStatus("Please enter both ID and password", Color.RED);
                return;
            }

            int staffId;
            try {
                staffId = Integer.parseInt(idText);
            } catch (NumberFormatException e) {
                showStatus("Invalid staff ID format", Color.RED);
                return;
            }

            if (!Staff.isValidId(staffId)) {
                showStatus("Staff ID must be 6 digits", Color.RED);
                return;
            }



            // Authenticate staff
            currentStaff = staffRepo.authenticate(staffId, password);

            if (currentStaff != null) {
                showStatus("Login successful! Welcome " + currentStaff.getName(), Color.GREEN);
                
                // Show role-based welcome message
                showRoleBasedWelcome();
                
                // Open appropriate dashboard based on role
                openRoleBasedDashboard();
                
            } else {
                showStatus("Invalid ID or password", Color.RED);
                passwordField.setText("");
            }

        } catch (Exception e) {
            showStatus("Login error: " + e.getMessage(), Color.RED);
            e.printStackTrace();
        }
    }

    private void showRoleBasedWelcome() {
        String welcomeMessage = "Welcome " + currentStaff.getName() + "!\n";
        welcomeMessage += "Role: " + currentStaff.getRole() + "\n\n";
        welcomeMessage += "You have successfully logged into the Wellness System.";

        JOptionPane.showMessageDialog(this, welcomeMessage, "Login Successful", JOptionPane.INFORMATION_MESSAGE);
    }

    private void openRoleBasedDashboard() {
        // Open the appropriate role-based dashboard
        SwingUtilities.invokeLater(() -> {
            this.dispose(); // Close the login form
            new RoleBasedDashboard(currentStaff).setVisible(true);
        });
    }

    private void clearFields() {
        idField.setText("");
        passwordField.setText("");
        statusLabel.setText("Please enter your credentials");
        statusLabel.setForeground(Color.BLUE);
        idField.requestFocus();
    }

    private void showStatus(String message, Color color) {
        statusLabel.setText(message);
        statusLabel.setForeground(color);
    }

    public static void main(String[] args) {
        // Set look and feel to system default
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new LoginView().setVisible(true);
            }
        });
    }
} 