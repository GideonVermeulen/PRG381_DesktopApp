package view;

import main.model.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.table.DefaultTableModel;
import main.dao.StaffDAO;
import main.dao.AppointmentDAO;
import main.dao.FeedbackDAO;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import javax.swing.filechooser.FileNameExtensionFilter;

public class Dashboard extends JFrame {
    private final Color purple = new Color(120, 80, 255);
    private final Font headerFont = new Font("Segoe UI", Font.BOLD, 28);
    private final Font cardTitleFont = new Font("Segoe UI", Font.BOLD, 18);
    private final Font cardDescFont = new Font("Segoe UI", Font.PLAIN, 14);
    private final Font buttonFont = new Font("Segoe UI", Font.BOLD, 14);

    // Shared in-memory lists for demo
    private java.util.List<Appointment> appointments = new ArrayList<>();
    private java.util.List<Feedback> feedbackList = new ArrayList<>();

    // In-memory staff lists for demo
    private StaffDAO staffDAO = new StaffDAO();
    private AppointmentDAO appointmentDAO = new AppointmentDAO();
    private FeedbackDAO feedbackDAO = new FeedbackDAO();

    private JPanel contentPanel;

    public Dashboard(User user) {
        setTitle("Wellness System - " + user.getRole() + " Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(Color.WHITE);

        // Demo data
        appointments.add(new Appointment(1, "Alice", "Dr. Smith", "2024-06-01", "09:00", "Scheduled", ""));
        appointments.add(new Appointment(2, "Bob", "Dr. Jones", "2024-06-02", "10:00", "Completed", "Follow-up needed"));
        appointments.add(new Appointment(3, "Charlie", "Dr. Smith", "2024-06-03", "11:00", "Scheduled", ""));
        feedbackList.add(new Feedback(1, "Alice", "Dr. Smith", "2024-06-01", 5, "Great session!"));
        feedbackList.add(new Feedback(2, "Bob", "Dr. Jones", "2024-06-02", 4, "Helpful advice."));
        feedbackList.add(new Feedback(3, "Charlie", "Dr. Smith", "2024-06-03", 3, "Okay experience."));

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        JLabel welcome = new JLabel("Welcome, " + user.getName() + " (" + user.getRole() + ")");
        welcome.setFont(headerFont);
        welcome.setForeground(purple);
        welcome.setBorder(BorderFactory.createEmptyBorder(20, 30, 10, 0));
        headerPanel.add(welcome, BorderLayout.WEST);
        JButton logoutBtn = new JButton("Logout");
        logoutBtn.setFont(buttonFont);
        logoutBtn.setBackground(purple);
        logoutBtn.setForeground(Color.WHITE);
        logoutBtn.setFocusPainted(false);
        logoutBtn.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        logoutBtn.addActionListener(e -> {
            dispose();
            main.App.main(null);
        });
        JPanel logoutPanel = new JPanel();
        logoutPanel.setBackground(Color.WHITE);
        logoutPanel.add(logoutBtn);
        headerPanel.add(logoutPanel, BorderLayout.EAST);
        add(headerPanel, BorderLayout.NORTH);

        // Main content area
        contentPanel = new JPanel(new BorderLayout());
        add(contentPanel, BorderLayout.CENTER);

        // Feature cards grid
        JPanel grid = new JPanel(new GridLayout(2, 3, 30, 30));
        grid.setBackground(Color.WHITE);
        grid.setBorder(BorderFactory.createEmptyBorder(20, 30, 30, 30));

        // Add feature cards based on permissions
        if (user.canManageCounselors()) grid.add(featureCard("Staff Management", "Manage all staff members", "Staff Management", () -> showStaffPanel(user)));
        if (user.canManageAppointments() || user.canBookAppointments()) grid.add(featureCard("Appointments", "Manage all appointments", "Manage Appointments", () -> showAppointmentPanel(user)));
        if (user.canManageCounselors()) grid.add(featureCard("Counselors", "Manage counselor information", "Manage Counselors", () -> showCounselorPanel(user)));
        if (user.canManageFeedback() || user.getRole().equals("Receptionist") || user.getRole().equals("Counselor")) grid.add(featureCard("Feedback", "Manage all feedback", "Manage Feedback", () -> showFeedbackPanel(user)));
        if (user.canViewAllData()) grid.add(featureCard("Reports", "Generate system reports", "Generate Reports", () -> showReportsPanel(user)));
        if (user.getRole().equals("Counselor")) grid.add(featureCard("Generate Report", "View your statistics report", "Generate Report", () -> showReportsPanel(user)));
        if (user.canViewSchedule()) grid.add(featureCard("Schedule", "View all schedules", "View Schedule", () -> showSchedulePanel(user)));

        add(grid, BorderLayout.SOUTH);
    }

    private JPanel featureCard(String title, String desc, String btnText, Runnable onClick) {
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createLineBorder(purple, 2));
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(cardTitleFont);
        titleLabel.setForeground(purple);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));
        card.add(titleLabel, BorderLayout.NORTH);
        JLabel descLabel = new JLabel(desc);
        descLabel.setFont(cardDescFont);
        descLabel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
        card.add(descLabel, BorderLayout.CENTER);
        JButton btn = new JButton(btnText);
        btn.setFont(buttonFont);
        btn.setBackground(purple);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        btn.addActionListener(e -> onClick.run());
        JPanel btnPanel = new JPanel();
        btnPanel.setBackground(Color.WHITE);
        btnPanel.add(btn);
        card.add(btnPanel, BorderLayout.SOUTH);
        return card;
    }

    private void showAppointmentPanel(User user) {
        // Get dynamic counselor names
        java.util.List<String> counselorNames = new ArrayList<>();
        for (User u : staffDAO.getStaffByRole("Counselor")) {
            counselorNames.add(u.getName());
        }
        contentPanel.removeAll();
        contentPanel.add(new AppointmentPanel(user, counselorNames), BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private void showFeedbackPanel(User user) {
        // Get dynamic counselor names
        java.util.List<String> counselorNames = new ArrayList<>();
        for (User u : staffDAO.getStaffByRole("Counselor")) {
            counselorNames.add(u.getName());
        }
        contentPanel.removeAll();
        contentPanel.add(new FeedbackPanel(user, counselorNames), BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private void showCounselorPanel(User user) {
        contentPanel.removeAll();
        if (user.getRole().equals("Admin")) {
            JPanel panel = new JPanel(new BorderLayout());
            JButton updateBtn = new JButton("Update Counselor Availability");
            updateBtn.addActionListener(e -> {
                java.time.LocalDateTime now = java.time.LocalDateTime.now();
                for (Appointment a : appointments) {
                    try {
                        java.time.LocalDateTime start = java.time.LocalDateTime.parse(a.getDate() + "T" + a.getTime());
                        java.time.LocalDateTime end = start.plusHours(1);
                        if (now.isAfter(end)) {
                            a.setStatus("Completed");
                        } else {
                            a.setStatus("Scheduled");
                        }
                    } catch (Exception ex) { /* ignore parse errors */ }
                }
                for (User u : staffDAO.getStaffByRole("Counselor")) {
                    Counselor c = (Counselor) u;
                    boolean hasOngoing = false;
                    for (Appointment a : appointments) {
                        if (a.getCounselorName().equals(c.getName())) {
                            try {
                                java.time.LocalDateTime start = java.time.LocalDateTime.parse(a.getDate() + "T" + a.getTime());
                                java.time.LocalDateTime end = start.plusHours(1);
                                if (!now.isBefore(start) && now.isBefore(end)) {
                                    hasOngoing = true;
                                    break;
                                }
                            } catch (Exception ex) { /* ignore parse errors */ }
                        }
                    }
                    c.setAvailable(!hasOngoing);
                }
                // Refresh panel
                showCounselorPanel(user);
            });
            panel.add(updateBtn, BorderLayout.NORTH);
            panel.add(new CounselorPanel(user, appointments, feedbackList, staffDAO.getStaffByRole("Counselor")), BorderLayout.CENTER);
            contentPanel.add(panel, BorderLayout.CENTER);
        } else {
            contentPanel.add(new CounselorPanel(user, appointments, feedbackList, staffDAO.getStaffByRole("Counselor")), BorderLayout.CENTER);
        }
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    // Placeholder for Staff, Reports, Schedule panels
    private void showStaffPanel(User user) {
        if (!user.getRole().equals("Admin")) {
            contentPanel.removeAll();
            contentPanel.add(new JLabel("Access denied: Only Admins can manage staff."), BorderLayout.CENTER);
            contentPanel.revalidate();
            contentPanel.repaint();
            return;
        }
        contentPanel.removeAll();
        JPanel staffPanel = new JPanel(new BorderLayout());
        JTabbedPane tabs = new JTabbedPane();
        // Admins Table
        tabs.addTab("Admins", createStaffTable("Admin"));
        // Receptionists Table
        tabs.addTab("Receptionists", createStaffTable("Receptionist"));
        // Counselors Table
        tabs.addTab("Counselors", createStaffTable("Counselor"));
        staffPanel.add(tabs, BorderLayout.CENTER);
        contentPanel.add(staffPanel, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }
    private void showReportsPanel(User user) {
        contentPanel.removeAll();
        JPanel reportPanel = new JPanel(new BorderLayout());
        String[] columns = {"Counselor", "Total Appointments", "Completed", "Upcoming", "Avg. Rating", "Feedback Count"};
        javax.swing.table.DefaultTableModel model = new javax.swing.table.DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable table = new JTable(model);
        if (user.getRole().equals("Admin")) {
            java.util.List<User> counselors = staffDAO.getStaffByRole("Counselor");
            for (User u : counselors) {
                String name = u.getName();
                int total = appointmentDAO.countAppointmentsForCounselor(name);
                int completed = appointmentDAO.countCompletedAppointmentsForCounselor(name);
                int upcoming = appointmentDAO.countUpcomingAppointmentsForCounselor(name);
                double avgRating = feedbackDAO.averageRatingForCounselor(name);
                int feedbackCount = feedbackDAO.countFeedbackForCounselor(name);
                model.addRow(new Object[]{name, total, completed, upcoming, String.format("%.2f", avgRating), feedbackCount});
            }
            reportPanel.add(new JLabel("Counselor Statistics Report"), BorderLayout.NORTH);
            reportPanel.add(new JScrollPane(table), BorderLayout.CENTER);
            JButton pdfBtn = new JButton("Save as PDF");
            pdfBtn.addActionListener(e -> saveReportAsPDF(model));
            JPanel btnPanel = new JPanel();
            btnPanel.add(pdfBtn);
            reportPanel.add(btnPanel, BorderLayout.SOUTH);
        } else if (user.getRole().equals("Counselor")) {
            String name = user.getName();
            int total = appointmentDAO.countAppointmentsForCounselor(name);
            int completed = appointmentDAO.countCompletedAppointmentsForCounselor(name);
            int upcoming = appointmentDAO.countUpcomingAppointmentsForCounselor(name);
            double avgRating = feedbackDAO.averageRatingForCounselor(name);
            int feedbackCount = feedbackDAO.countFeedbackForCounselor(name);
            model.addRow(new Object[]{name, total, completed, upcoming, String.format("%.2f", avgRating), feedbackCount});
            reportPanel.add(new JLabel("My Statistics Report"), BorderLayout.NORTH);
            reportPanel.add(new JScrollPane(table), BorderLayout.CENTER);
        } else {
            reportPanel.add(new JLabel("Reports not available for this role."), BorderLayout.CENTER);
        }
        contentPanel.add(reportPanel, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    // Add a stub for PDF export (to be implemented with PDFBox/iText)
    private void saveReportAsPDF(javax.swing.table.DefaultTableModel model) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save Report as PDF");
        fileChooser.setFileFilter(new FileNameExtensionFilter("PDF Files", "pdf"));
        int userSelection = fileChooser.showSaveDialog(this);
        if (userSelection != JFileChooser.APPROVE_OPTION) return;
        String filePath = fileChooser.getSelectedFile().getAbsolutePath();
        if (!filePath.toLowerCase().endsWith(".pdf")) filePath += ".pdf";
        try (PDDocument doc = new PDDocument()) {
            PDPage page = new PDPage(PDRectangle.LETTER);
            doc.addPage(page);
            PDPageContentStream content = new PDPageContentStream(doc, page);
            float margin = 50;
            float yStart = 730;
            float tableWidth = page.getMediaBox().getWidth() - 2 * margin;
            int cols = model.getColumnCount();
            int rows = model.getRowCount() + 1; // +1 for header
            float rowHeight = 25;
            float tableHeight = rowHeight * rows;
            float colWidth = tableWidth / cols;
            float cellMargin = 5f;
            float y = yStart;

            // Title
            content.setFont(PDType1Font.HELVETICA_BOLD, 16);
            content.beginText();
            content.newLineAtOffset(margin, y);
            content.showText("Counselor Statistics Report");
            content.endText();
            y -= 30;

            // Draw table grid
            // Horizontal lines
            for (int i = 0; i <= rows; i++) {
                content.moveTo(margin, y - i * rowHeight);
                content.lineTo(margin + tableWidth, y - i * rowHeight);
            }
            // Vertical lines
            for (int i = 0; i <= cols; i++) {
                content.moveTo(margin + i * colWidth, y);
                content.lineTo(margin + i * colWidth, y - tableHeight);
            }
            content.setStrokingColor(0, 0, 0);
            content.stroke();

            // Write header row
            content.setFont(PDType1Font.HELVETICA_BOLD, 12);
            float textY = y - 18;
            for (int col = 0; col < cols; col++) {
                String text = model.getColumnName(col);
                float textX = margin + col * colWidth + cellMargin;
                content.beginText();
                content.newLineAtOffset(textX, textY);
                content.showText(text);
                content.endText();
            }

            // Write data rows
            content.setFont(PDType1Font.HELVETICA, 12);
            for (int row = 0; row < model.getRowCount(); row++) {
                textY = y - rowHeight * (row + 2) + 7;
                for (int col = 0; col < cols; col++) {
                    Object val = model.getValueAt(row, col);
                    String text = val != null ? val.toString() : "";
                    float textX = margin + col * colWidth + cellMargin;
                    content.beginText();
                    content.newLineAtOffset(textX, textY);
                    content.showText(text);
                    content.endText();
                }
            }
            content.close();
            doc.save(filePath);
            JOptionPane.showMessageDialog(this, "PDF saved to: " + filePath, "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to save PDF: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showSchedulePanel(User user) {
        contentPanel.removeAll();
        JPanel panel = new JPanel(new BorderLayout());
        java.util.List<Appointment> allAppointments = appointmentDAO.getAllAppointments();
        if (user.getRole().equals("Counselor")) {
            // Show only this counselor's scheduled appointments
            java.util.List<Appointment> myAppointments = new ArrayList<>();
            for (Appointment a : allAppointments) {
                if (a.getCounselorName().equals(user.getName()) && !"Completed".equalsIgnoreCase(a.getStatus())) {
                    myAppointments.add(a);
                }
            }
            panel.add(new JLabel("My Schedule (Upcoming Appointments)"), BorderLayout.NORTH);
            String[] cols = {"ID", "Student", "Date", "Time", "Status", "Comments"};
            javax.swing.table.DefaultTableModel model = new javax.swing.table.DefaultTableModel(cols, 0) {
                public boolean isCellEditable(int r, int c) { return false; }
            };
            for (Appointment a : myAppointments) {
                model.addRow(new Object[]{a.getId(), a.getStudentName(), a.getDate(), a.getTime(), a.getStatus(), a.getComments()});
            }
            JTable table = new JTable(model);
            panel.add(new JScrollPane(table), BorderLayout.CENTER);
        } else if (user.getRole().equals("Admin") || user.getRole().equals("Receptionist")) {
            // Dropdown to select counselor
            java.util.List<String> counselorNames = new ArrayList<>();
            for (User u : staffDAO.getStaffByRole("Counselor")) {
                counselorNames.add(u.getName());
            }
            JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            JLabel label = new JLabel("Select Counselor: ");
            JComboBox<String> counselorDropdown = new JComboBox<>(counselorNames.toArray(new String[0]));
            topPanel.add(label);
            topPanel.add(counselorDropdown);
            panel.add(topPanel, BorderLayout.NORTH);
            String[] cols = {"ID", "Student", "Date", "Time", "Status", "Comments"};
            DefaultTableModel model = new DefaultTableModel(cols, 0) {
                public boolean isCellEditable(int r, int c) { return false; }
            };
            JTable table = new JTable(model);
            JScrollPane scrollPane = new JScrollPane(table);
            panel.add(scrollPane, BorderLayout.CENTER);
            Runnable refreshTable = () -> {
                model.setRowCount(0);
                String selectedCounselor = (String) counselorDropdown.getSelectedItem();
                for (Appointment a : allAppointments) {
                    if (a.getCounselorName().equals(selectedCounselor) && !"Completed".equalsIgnoreCase(a.getStatus())) {
                        model.addRow(new Object[]{a.getId(), a.getStudentName(), a.getDate(), a.getTime(), a.getStatus(), a.getComments()});
                    }
                }
            };
            counselorDropdown.addActionListener(e -> refreshTable.run());
            if (!counselorNames.isEmpty()) {
                counselorDropdown.setSelectedIndex(0);
                refreshTable.run();
            }
        } else {
            panel.add(new JLabel("Schedule view not available for this role."), BorderLayout.CENTER);
        }
        contentPanel.add(panel, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private JPanel createStaffTable(String role) {
        String[] columns;
        if (role.equals("Counselor")) {
            columns = new String[]{"ID", "Name", "Password", "Specialization", "Available"};
        } else {
            columns = new String[]{"ID", "Name", "Password"};
        }
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int row, int col) { return false; }
        };
        JTable table = new JTable(model);
        refreshStaffTable(model, role);
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        JPanel btnPanel = new JPanel();
        JButton addBtn = new JButton("Add");
        JButton editBtn = new JButton("Edit");
        JButton delBtn = new JButton("Delete");
        btnPanel.add(addBtn);
        btnPanel.add(editBtn);
        btnPanel.add(delBtn);
        panel.add(btnPanel, BorderLayout.SOUTH);
        // Add
        addBtn.addActionListener(e -> {
            User newUser = showStaffForm(null, role);
            if (newUser != null) {
                staffDAO.addStaff(newUser);
                refreshStaffTable(model, role);
            }
        });
        // Edit
        editBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) return;
            int id = (int) model.getValueAt(row, 0);
            User orig = staffDAO.getStaffByRole(role).stream().filter(u -> u.getId() == id).findFirst().orElse(null);
            if (orig == null) return;
            User updated = showStaffForm(orig, role);
            if (updated != null) {
                staffDAO.updateStaff(updated);
                refreshStaffTable(model, role);
            }
        });
        // Delete
        delBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) return;
            int id = (int) model.getValueAt(row, 0);
            staffDAO.deleteStaff(id);
            refreshStaffTable(model, role);
        });
        return panel;
    }

    private void refreshStaffTable(DefaultTableModel model, String role) {
        model.setRowCount(0);
        for (User u : staffDAO.getStaffByRole(role)) {
            if (role.equals("Counselor")) {
                Counselor c = (Counselor) u;
                model.addRow(new Object[]{c.getId(), c.getName(), c.getPassword(), c.getSpecialization(), c.isAvailable()});
            } else {
                model.addRow(new Object[]{u.getId(), u.getName(), u.getPassword()});
            }
        }
    }

    private User showStaffForm(User orig, String role) {
        JPanel form = new JPanel(new GridLayout(role.equals("Counselor") ? 5 : 3, 2));
        JTextField idField = new JTextField(orig != null ? String.valueOf(orig.getId()) : "");
        JTextField nameField = new JTextField(orig != null ? orig.getName() : "");
        JTextField passField = new JTextField(orig != null ? orig.getPassword() : "");
        form.add(new JLabel("ID:")); form.add(idField);
        form.add(new JLabel("Name:")); form.add(nameField);
        form.add(new JLabel("Password:")); form.add(passField);
        JTextField specField = null;
        JCheckBox availBox = null;
        if (role.equals("Counselor")) {
            specField = new JTextField(orig != null ? ((Counselor)orig).getSpecialization() : "");
            availBox = new JCheckBox("Available", orig != null && ((Counselor)orig).isAvailable());
            form.add(new JLabel("Specialization:")); form.add(specField);
            form.add(new JLabel("Available:")); form.add(availBox);
        }
        int res = JOptionPane.showConfirmDialog(contentPanel, form, (orig == null ? "Add " : "Edit ") + role, JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (res == JOptionPane.OK_OPTION) {
            try {
                int id = Integer.parseInt(idField.getText());
                String name = nameField.getText();
                String pass = passField.getText();
                if (role.equals("Admin")) return new Admin(id, name, pass);
                if (role.equals("Receptionist")) return new Receptionist(id, name, pass);
                if (role.equals("Counselor")) return new Counselor(id, name, pass, specField.getText(), availBox.isSelected());
            } catch (Exception ex) { JOptionPane.showMessageDialog(contentPanel, "Invalid input."); }
        }
        return null;
    }
} 