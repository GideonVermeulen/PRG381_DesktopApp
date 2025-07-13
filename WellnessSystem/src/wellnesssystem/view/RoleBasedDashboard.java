package wellnesssystem.view;

import wellnesssystem.model.Staff;
import wellnesssystem.model.Admin;
import wellnesssystem.model.CounselorStaff;
import wellnesssystem.model.Receptionist;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * Role-based dashboard that adapts to the logged-in staff member's permissions
 * Demonstrates MVC architecture and role-based access control
 */
public class RoleBasedDashboard extends JFrame {
    private Staff currentStaff;
    private JPanel mainPanel;
    private JLabel welcomeLabel;
    private JPanel featuresPanel;
    private JButton logoutButton;
    
    // Feature buttons
    private JButton appointmentsButton;
    private JButton counselorsButton;
    private JButton feedbackButton;
    private JButton reportsButton;
    private JButton scheduleButton;
    private JButton staffManagementButton;

    public RoleBasedDashboard(Staff staff) {
        this.currentStaff = staff;
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        configureForRole();
    }

    private void initializeComponents() {
        setTitle("Wellness System - " + currentStaff.getRole() + " Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setResizable(true);

        Color bgMain = new Color(248, 250, 252); // Light background
        Color bgPanel = new Color(255, 255, 255); // White panels
        Color accent = new Color(130, 90, 255); // Purple accent
        Color textMain = new Color(50, 50, 50); // Dark text
        Color textSecondary = new Color(100, 100, 100); // Secondary text

        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(bgMain);
        welcomeLabel = new JLabel();
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        welcomeLabel.setForeground(accent);
        welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        welcomeLabel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        featuresPanel = new JPanel(new GridBagLayout());
        featuresPanel.setBackground(bgPanel);
        logoutButton = new JButton("Logout");
        logoutButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        logoutButton.setPreferredSize(new Dimension(100, 35));
        logoutButton.setBackground(accent);
        logoutButton.setForeground(Color.WHITE);

        // Initialize feature buttons
        appointmentsButton = new JButton("Manage Appointments");
        counselorsButton = new JButton("Manage Counselors");
        feedbackButton = new JButton("Manage Feedback");
        reportsButton = new JButton("Generate Reports");
        scheduleButton = new JButton("View Schedule");
        staffManagementButton = new JButton("Staff Management");
        // Set consistent button styling
        JButton[] buttons = {appointmentsButton, counselorsButton, feedbackButton, 
                           reportsButton, scheduleButton, staffManagementButton};
        for (JButton btn : buttons) {
            btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
            btn.setPreferredSize(new Dimension(150, 40));
            btn.setBackground(accent);
            btn.setForeground(Color.WHITE);
            btn.setFocusPainted(false);
            btn.setBorder(BorderFactory.createLineBorder(bgPanel, 2));
        }
    }

    private void setupLayout() {
        // Welcome section with better spacing
        JPanel welcomePanel = new JPanel(new BorderLayout());
        welcomePanel.setBackground(new Color(255, 255, 255));
        welcomePanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        welcomePanel.add(welcomeLabel, BorderLayout.CENTER);

        // Logout button in top-right with padding
        JPanel logoutPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        logoutPanel.setBackground(new Color(255, 255, 255));
        logoutPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 30));
        logoutPanel.add(logoutButton);

        // Top panel with better layout
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(255, 255, 255));
        topPanel.setBorder(BorderFactory.createEtchedBorder());
        topPanel.add(welcomePanel, BorderLayout.CENTER);
        topPanel.add(logoutPanel, BorderLayout.EAST);

        // Features section with better spacing
        JScrollPane scrollPane = new JScrollPane(featuresPanel);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Available Features"));
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder("Available Features"),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        add(mainPanel);
    }

    private void setupEventHandlers() {
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int choice = JOptionPane.showConfirmDialog(
                    RoleBasedDashboard.this,
                    "Are you sure you want to logout?",
                    "Logout Confirmation",
                    JOptionPane.YES_NO_OPTION
                );
                if (choice == JOptionPane.YES_OPTION) {
                    dispose();
                    new LoginView().setVisible(true);
                }
            }
        });

        // Add action listeners for feature buttons
        appointmentsButton.addActionListener(e -> openAppointments());
        counselorsButton.addActionListener(e -> openCounselors());
        feedbackButton.addActionListener(e -> openFeedback());
        reportsButton.addActionListener(e -> openReports());
        scheduleButton.addActionListener(e -> openSchedule());
        staffManagementButton.addActionListener(e -> openStaffManagement());
    }

    private void configureForRole() {
        // Set welcome message
        String welcomeMessage = "Welcome, " + currentStaff.getName() + " (" + currentStaff.getRole() + ")";
        welcomeLabel.setText(welcomeMessage);

        // Clear existing components
        featuresPanel.removeAll();

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 25, 20, 25);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 0.5;
        gbc.weighty = 0.0;

        int row = 0;

        // Add role-specific features
        switch (currentStaff.getRole()) {
            case "Admin":
                addAdminFeatures(gbc, row);
                break;
            case "Receptionist":
                addReceptionistFeatures(gbc, row);
                break;
            case "Counselor":
                addCounselorFeatures(gbc, row);
                break;
        }

        // Refresh the panel
        featuresPanel.revalidate();
        featuresPanel.repaint();
    }

    private void addAdminFeatures(GridBagConstraints gbc, int row) {
        // Admin has access to everything - better spacing with 3 columns
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 1;
        gbc.weightx = 0.33;
        gbc.fill = GridBagConstraints.BOTH;
        featuresPanel.add(createFeaturePanel("Staff Management", "Manage all staff members", staffManagementButton), gbc);

        gbc.gridx = 1;
        gbc.gridy = row;
        gbc.weightx = 0.33;
        featuresPanel.add(createFeaturePanel("Appointments", "Manage all appointments", appointmentsButton), gbc);

        gbc.gridx = 2;
        gbc.gridy = row++;
        gbc.weightx = 0.33;
        featuresPanel.add(createFeaturePanel("Counselors", "Manage counselor information", counselorsButton), gbc);

        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0.33;
        featuresPanel.add(createFeaturePanel("Feedback", "Manage all feedback", feedbackButton), gbc);

        gbc.gridx = 1;
        gbc.gridy = row;
        gbc.weightx = 0.33;
        featuresPanel.add(createFeaturePanel("Reports", "Generate system reports", reportsButton), gbc);

        gbc.gridx = 2;
        gbc.gridy = row++;
        gbc.weightx = 0.33;
        featuresPanel.add(createFeaturePanel("Schedule", "View all schedules", scheduleButton), gbc);
    }

    private void addReceptionistFeatures(GridBagConstraints gbc, int row) {
        // Receptionist can book appointments and view schedules
        gbc.gridx = 0;
        gbc.gridy = row++;
        featuresPanel.add(createFeaturePanel("Book Appointments", "Book appointments for students", appointmentsButton), gbc);

        gbc.gridx = 1;
        gbc.gridy = row - 1;
        featuresPanel.add(createFeaturePanel("View Schedule", "View appointment schedules", scheduleButton), gbc);

        gbc.gridx = 0;
        gbc.gridy = row++;
        featuresPanel.add(createFeaturePanel("Check In/Out", "Check students in and out", new JButton("Check In/Out")), gbc);

        gbc.gridx = 1;
        gbc.gridy = row - 1;
        featuresPanel.add(createFeaturePanel("Walk-in Appointments", "Handle walk-in appointments", new JButton("Walk-ins")), gbc);
    }

    private void addCounselorFeatures(GridBagConstraints gbc, int row) {
        // Counselor can view their schedule and feedback
        gbc.gridx = 0;
        gbc.gridy = row++;
        featuresPanel.add(createFeaturePanel("My Schedule", "View my appointment schedule", scheduleButton), gbc);

        gbc.gridx = 1;
        gbc.gridy = row - 1;
        featuresPanel.add(createFeaturePanel("My Feedback", "View feedback for my sessions", feedbackButton), gbc);

        gbc.gridx = 0;
        gbc.gridy = row++;
        featuresPanel.add(createFeaturePanel("Update Availability", "Update my availability status", new JButton("Availability")), gbc);

        gbc.gridx = 1;
        gbc.gridy = row - 1;
        featuresPanel.add(createFeaturePanel("Session Notes", "Manage session notes", new JButton("Session Notes")), gbc);
    }

    private JPanel createFeaturePanel(String title, String description, JButton button) {
        Color bgPanel = new Color(255, 255, 255);
        Color accent = new Color(130, 90, 255);
        Color textMain = new Color(50, 50, 50);
        Color textSecondary = new Color(100, 100, 100);
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(accent, 2),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        panel.setBackground(bgPanel);

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(accent);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        JLabel descLabel = new JLabel(description);
        descLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        descLabel.setForeground(textSecondary);
        descLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        JPanel textPanel = new JPanel(new BorderLayout());
        textPanel.setBackground(bgPanel);
        textPanel.add(titleLabel, BorderLayout.NORTH);
        textPanel.add(descLabel, BorderLayout.CENTER);

        // Button panel with padding
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(bgPanel);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 5, 0));
        buttonPanel.add(button);

        panel.add(textPanel, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    // Feature action methods
    private void openAppointments() {
        if (currentStaff.canManageAppointments() || currentStaff.canBookAppointments()) {
            SwingUtilities.invokeLater(() -> {
                new AppointmentsInterface(currentStaff).setVisible(true);
            });
        } else {
            JOptionPane.showMessageDialog(this, 
                "You don't have permission to access appointments.",
                "Access Denied", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void openCounselors() {
        if (currentStaff.canManageCounselors()) {
            SwingUtilities.invokeLater(() -> {
                new StaffManagementInterface(currentStaff, true).setVisible(true);
            });
        } else {
            JOptionPane.showMessageDialog(this, 
                "You don't have permission to manage counselors.",
                "Access Denied", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void openFeedback() {
        if (currentStaff.canManageFeedback() || currentStaff.getRole().equals("Counselor")) {
            SwingUtilities.invokeLater(() -> {
                new FeedbackInterface(currentStaff).setVisible(true);
            });
        } else {
            JOptionPane.showMessageDialog(this, 
                "You don't have permission to access feedback.",
                "Access Denied", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void openReports() {
        if (currentStaff.canViewAllData()) {
            JOptionPane.showMessageDialog(this, 
                "Opening Reports Generation...\n" +
                "This would show the reports generation interface.",
                "Reports", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, 
                "You don't have permission to generate reports.",
                "Access Denied", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void openSchedule() {
        if (currentStaff.canViewSchedule()) {
            // For now, show appointments interface as schedule view
            SwingUtilities.invokeLater(() -> {
                new AppointmentsInterface(currentStaff).setVisible(true);
            });
        }
    }

    private void openStaffManagement() {
        if (currentStaff.canManageCounselors()) {
            SwingUtilities.invokeLater(() -> {
                new StaffManagementInterface(currentStaff).setVisible(true);
            });
        } else {
            JOptionPane.showMessageDialog(this, 
                "You don't have permission to manage staff.",
                "Access Denied", JOptionPane.WARNING_MESSAGE);
        }
    }

    public static void main(String[] args) {
        // For testing purposes
        SwingUtilities.invokeLater(() -> {
            // Create a test admin
            Admin testAdmin = new Admin(100001, "Test Admin", "password");
            new RoleBasedDashboard(testAdmin).setVisible(true);
        });
    }
} 