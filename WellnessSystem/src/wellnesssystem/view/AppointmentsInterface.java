package wellnesssystem.view;

import wellnesssystem.WellnessSystem;
import wellnesssystem.model.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Vector;
import java.sql.SQLException;

/**
 * Appointments Management Interface
 * Database-only implementation
 */
public class AppointmentsInterface extends JFrame {
    private Staff currentStaff;
    private JTable appointmentsTable;
    private DefaultTableModel tableModel;
    private JTextField studentNameField;
    private JComboBox<CounselorStaff> counselorComboBox;
    private JTextField dateField;
    private JTextField timeField;
    private JComboBox<String> statusComboBox;
    private JTextArea commentsArea;
    
    // Buttons
    private JButton addButton;
    private JButton updateButton;
    private JButton deleteButton;
    private JButton clearButton;
    private JButton refreshButton;

    public AppointmentsInterface(Staff staff) {
        this.currentStaff = staff;
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        refreshTable();
    }

    private void initializeComponents() {
        setTitle("Appointments Management - " + currentStaff.getName());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setResizable(true);

        // Initialize form fields
        studentNameField = new JTextField(20);
        studentNameField.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        studentNameField.setForeground(new Color(50, 50, 50));
        studentNameField.setBackground(Color.WHITE);
        studentNameField.setCaretColor(new Color(130, 90, 255));
        counselorComboBox = new JComboBox<>();
        counselorComboBox.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        dateField = new JTextField(10);
        dateField.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        dateField.setForeground(new Color(50, 50, 50));
        dateField.setBackground(Color.WHITE);
        dateField.setCaretColor(new Color(130, 90, 255));
        timeField = new JTextField(10);
        timeField.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        timeField.setForeground(new Color(50, 50, 50));
        timeField.setBackground(Color.WHITE);
        timeField.setCaretColor(new Color(130, 90, 255));
        statusComboBox = new JComboBox<>(new String[]{"Scheduled", "Completed", "Cancelled", "No Show"});
        statusComboBox.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        commentsArea = new JTextArea(3, 20);
        commentsArea.setLineWrap(true);
        commentsArea.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        commentsArea.setForeground(new Color(50, 50, 50));
        commentsArea.setBackground(Color.WHITE);
        commentsArea.setCaretColor(new Color(130, 90, 255));

        // Initialize buttons
        addButton = new JButton("Add Appointment");
        updateButton = new JButton("Update Appointment");
        deleteButton = new JButton("Delete Appointment");
        clearButton = new JButton("Clear Form");
        refreshButton = new JButton("Refresh");
        
        JButton[] buttons = {addButton, updateButton, deleteButton, clearButton, refreshButton};
        for (JButton btn : buttons) {
            btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
            btn.setBackground(new Color(130, 90, 255));
            btn.setForeground(Color.WHITE);
        }

        // Initialize table
        String[] columnNames = {"ID", "Student Name", "Counselor", "Date", "Time", "Status", "Created By"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table read-only
            }
        };
        appointmentsTable = new JTable(tableModel);
        appointmentsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        appointmentsTable.setBackground(Color.WHITE);
        appointmentsTable.setForeground(new Color(50, 50, 50));
        appointmentsTable.setSelectionBackground(new Color(130, 90, 255));
        appointmentsTable.setSelectionForeground(Color.WHITE);
        appointmentsTable.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        appointmentsTable.getTableHeader().setBackground(new Color(130, 90, 255));
        appointmentsTable.getTableHeader().setForeground(Color.WHITE);
        appointmentsTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 15));
    }

    private void setupLayout() {
        setLayout(new BorderLayout());
        // Form Panel
        JPanel formPanel = createFormPanel();
        formPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(130, 90, 255)), "Appointment Details", 0, 0, new Font("Segoe UI", Font.BOLD, 14), new Color(130, 90, 255)));
        formPanel.setBackground(new Color(248, 250, 252));
        // Buttons Panel
        JPanel buttonsPanel = createButtonsPanel();
        buttonsPanel.setBackground(new Color(248, 250, 252));
        // Table Panel
        JPanel tablePanel = createTablePanel();
        tablePanel.setBackground(new Color(248, 250, 252));
        // Main layout
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(248, 250, 252));
        topPanel.add(formPanel, BorderLayout.CENTER);
        topPanel.add(buttonsPanel, BorderLayout.SOUTH);
        add(topPanel, BorderLayout.NORTH);
        add(tablePanel, BorderLayout.CENTER);
    }

    private JPanel createFormPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(248, 250, 252));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // Student Name
        gbc.gridx = 0; gbc.gridy = 0;
        JLabel studentNameLabel = new JLabel("Student Name:");
        studentNameLabel.setForeground(new Color(70, 70, 70));
        panel.add(studentNameLabel, gbc);
        gbc.gridx = 1;
        panel.add(studentNameField, gbc);

        // Counselor
        gbc.gridx = 0; gbc.gridy = 1;
        JLabel counselorLabel = new JLabel("Counselor:");
        counselorLabel.setForeground(new Color(70, 70, 70));
        panel.add(counselorLabel, gbc);
        gbc.gridx = 1;
        panel.add(counselorComboBox, gbc);

        // Date
        gbc.gridx = 0; gbc.gridy = 2;
        JLabel dateLabel = new JLabel("Date (YYYY-MM-DD):");
        dateLabel.setForeground(new Color(70, 70, 70));
        panel.add(dateLabel, gbc);
        gbc.gridx = 1;
        panel.add(dateField, gbc);

        // Time
        gbc.gridx = 0; gbc.gridy = 3;
        JLabel timeLabel = new JLabel("Time (HH:MM):");
        timeLabel.setForeground(new Color(70, 70, 70));
        panel.add(timeLabel, gbc);
        gbc.gridx = 1;
        panel.add(timeField, gbc);

        // Status
        gbc.gridx = 0; gbc.gridy = 4;
        JLabel statusLabel = new JLabel("Status:");
        statusLabel.setForeground(new Color(70, 70, 70));
        panel.add(statusLabel, gbc);
        gbc.gridx = 1;
        panel.add(statusComboBox, gbc);

        // Comments
        gbc.gridx = 0; gbc.gridy = 5;
        JLabel commentsLabel = new JLabel("Comments:");
        commentsLabel.setForeground(new Color(70, 70, 70));
        panel.add(commentsLabel, gbc);
        gbc.gridx = 1;
        panel.add(new JScrollPane(commentsArea), gbc);

        return panel;
    }

    private JPanel createButtonsPanel() {
        JPanel panel = new JPanel(new FlowLayout());
        panel.add(addButton);
        panel.add(updateButton);
        panel.add(deleteButton);
        panel.add(clearButton);
        panel.add(refreshButton);
        return panel;
    }

    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(130, 90, 255)), "Appointments List", 0, 0, new Font("Segoe UI", Font.BOLD, 14), new Color(130, 90, 255)));
        JScrollPane scrollPane = new JScrollPane(appointmentsTable);
        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }

    private void setupEventHandlers() {
        // Table selection listener
        appointmentsTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                loadSelectedAppointment();
            }
        });

        // Button listeners
        addButton.addActionListener(e -> addAppointment());
        updateButton.addActionListener(e -> updateAppointment());
        deleteButton.addActionListener(e -> deleteAppointment());
        clearButton.addActionListener(e -> clearForm());
        refreshButton.addActionListener(e -> refreshTable());

        // Set current date and time as default
        dateField.setText(LocalDate.now().toString());
        timeField.setText(LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm")));
        
        // Load counselors
        loadCounselors();
    }

    private void loadCounselors() {
        counselorComboBox.removeAllItems();
        // Get counselors from the staff repository
        for (Staff staff : WellnessSystem.staffRepo.getAllStaff()) {
            if (staff instanceof CounselorStaff) {
                counselorComboBox.addItem((CounselorStaff) staff);
            }
        }
        // Set custom renderer to show only counselor name
        counselorComboBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof CounselorStaff) {
                    setText(((CounselorStaff) value).getName());
                }
                return this;
            }
        });
    }

    private void refreshTable() {
        try {
            tableModel.setRowCount(0);
            for (Appointment appointment : AppointmentDAO.getAllAppointments()) {
                Vector<Object> row = new Vector<>();
                row.add(appointment.getId());
                row.add(appointment.getStudentName());
                row.add(appointment.getCounselorName());
                row.add(appointment.getDate());
                row.add(appointment.getTime());
                row.add(appointment.getStatus());
                row.add(appointment.getCreatedByName());
                tableModel.addRow(row);
            }
        } catch (SQLException e) {
            System.err.println("Error refreshing table: " + e.getMessage());
            JOptionPane.showMessageDialog(this, "Error loading appointments: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadSelectedAppointment() {
        int selectedRow = appointmentsTable.getSelectedRow();
        if (selectedRow >= 0) {
            studentNameField.setText((String) tableModel.getValueAt(selectedRow, 1));
            // Find and select the counselor
            String counselorName = (String) tableModel.getValueAt(selectedRow, 2);
            for (int i = 0; i < counselorComboBox.getItemCount(); i++) {
                CounselorStaff counselor = counselorComboBox.getItemAt(i);
                if (counselor.getName().equals(counselorName)) {
                    counselorComboBox.setSelectedIndex(i);
                    break;
                }
            }
            dateField.setText(tableModel.getValueAt(selectedRow, 3).toString());
            timeField.setText(tableModel.getValueAt(selectedRow, 4).toString());
            statusComboBox.setSelectedItem(tableModel.getValueAt(selectedRow, 5));
            commentsArea.setText(""); // Comments not shown in table, clear by default
        }
    }

    private void addAppointment() {
        if (!validateForm()) return;
        try {
            CounselorStaff selectedCounselor = (CounselorStaff) counselorComboBox.getSelectedItem();
            System.out.println("[DEBUG] Adding appointment:");
            System.out.println("  Student: " + studentNameField.getText().trim());
            System.out.println("  Counselor: " + (selectedCounselor != null ? selectedCounselor.getName() : "null"));
            System.out.println("  Date: " + dateField.getText());
            System.out.println("  Time: " + timeField.getText());
            System.out.println("  Status: " + (String) statusComboBox.getSelectedItem());
            System.out.println("  CreatedBy: " + (currentStaff != null ? currentStaff.getName() : "null"));
            System.out.println("  Comments: " + commentsArea.getText().trim());
            Appointment appointment = new Appointment(
                0, // ID will be generated by database
                studentNameField.getText().trim(),
                selectedCounselor,
                dateField.getText(),
                timeField.getText(),
                (String) statusComboBox.getSelectedItem(),
                currentStaff,
                commentsArea.getText().trim()
            );
            int id = AppointmentDAO.addAppointment(appointment);
            if (id > 0) {
                JOptionPane.showMessageDialog(this, "Appointment added successfully!", 
                    "Success", JOptionPane.INFORMATION_MESSAGE);
                clearForm();
                refreshTable();
            } else {
                JOptionPane.showMessageDialog(this, "Error adding appointment to database", 
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database error: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error adding appointment: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateAppointment() {
        int selectedRow = appointmentsTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Please select an appointment to update.", 
                "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (!validateForm()) return;
        try {
            int appointmentId = (Integer) tableModel.getValueAt(selectedRow, 0);
            // Load the appointment from DB
            Appointment appointment = AppointmentDAO.getAppointmentById(appointmentId);
            if (appointment != null) {
                CounselorStaff selectedCounselor = (CounselorStaff) counselorComboBox.getSelectedItem();
                appointment.setStudentName(studentNameField.getText().trim());
                appointment.setCounselor(selectedCounselor);
                appointment.setDate(dateField.getText());
                appointment.setTime(timeField.getText());
                appointment.setStatus((String) statusComboBox.getSelectedItem());
                appointment.setComments(commentsArea.getText().trim());
                // Update in database
                if (AppointmentDAO.updateAppointment(appointment)) {
                    JOptionPane.showMessageDialog(this, "Appointment updated successfully!", 
                        "Success", JOptionPane.INFORMATION_MESSAGE);
                    clearForm();
                    refreshTable();
                } else {
                    JOptionPane.showMessageDialog(this, "Error updating appointment in database", 
                        "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Database error: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error updating appointment: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteAppointment() {
        int selectedRow = appointmentsTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Please select an appointment to delete.", 
                "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int choice = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this appointment?", 
            "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (choice == JOptionPane.YES_OPTION) {
            try {
                int appointmentId = (Integer) tableModel.getValueAt(selectedRow, 0);
                // Delete from database
                if (AppointmentDAO.deleteAppointment(appointmentId)) {
                    JOptionPane.showMessageDialog(this, "Appointment deleted successfully!", 
                        "Success", JOptionPane.INFORMATION_MESSAGE);
                    clearForm();
                    refreshTable();
                } else {
                    JOptionPane.showMessageDialog(this, "Error deleting appointment from database", 
                        "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Database error: " + e.getMessage(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error deleting appointment: " + e.getMessage(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void clearForm() {
        studentNameField.setText("");
        counselorComboBox.setSelectedIndex(-1);
        dateField.setText(LocalDate.now().toString());
        timeField.setText(LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm")));
        statusComboBox.setSelectedItem("Scheduled");
        commentsArea.setText("");
        appointmentsTable.clearSelection();
    }

    private boolean validateForm() {
        if (studentNameField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Student name is required.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        if (counselorComboBox.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Please select a counselor.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        if (dateField.getText().trim().isEmpty() || timeField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Date and time are required.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        // --- Date/time validation ---
        try {
            LocalDate appointmentDate = LocalDate.parse(dateField.getText().trim());
            LocalDate today = LocalDate.now();
            if (appointmentDate.isBefore(today)) {
                JOptionPane.showMessageDialog(this, "Appointment date cannot be in the past.",
                        "Validation Error", JOptionPane.WARNING_MESSAGE);
                return false;
            }
            if (appointmentDate.isEqual(today)) {
                LocalTime appointmentTime = LocalTime.parse(timeField.getText().trim());
                LocalTime now = LocalTime.now();
                if (appointmentTime.isBefore(now)) {
                    JOptionPane.showMessageDialog(this, "Appointment time cannot be in the past.",
                            "Validation Error", JOptionPane.WARNING_MESSAGE);
                    return false;
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Invalid date or time format.",
                    "Validation Error", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        // --- End new validation ---
        return true;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // For testing purposes
            Admin testAdmin = new Admin(100001, "Test Admin", "password");
            new AppointmentsInterface(testAdmin).setVisible(true);
        });
    }
} 