package wellnesssystem.view;

import wellnesssystem.model.*;
import wellnesssystem.WellnessSystem;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Vector;
import java.sql.SQLException;
import java.util.List;

/**
 * Staff Management Interface
 * Uses database storage - no in-memory fallbacks
 */
public class StaffManagementInterface extends JFrame {
    private Staff currentStaff;
    private JTable staffTable;
    private DefaultTableModel tableModel;
    private JTextField nameField;
    private JTextField passwordField;
    private JComboBox<String> roleComboBox;
    private JTextField specializationField;
    private JCheckBox availableCheckBox;
    
    // Buttons
    private JButton addButton;
    private JButton updateButton;
    private JButton deleteButton;
    private JButton clearButton;
    private JButton refreshButton;

    private boolean counselorOnly = false;

    public StaffManagementInterface(Staff staff) {
        this(staff, false);
    }

    public StaffManagementInterface(Staff staff, boolean counselorOnly) {
        this.currentStaff = staff;
        this.counselorOnly = counselorOnly;
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        refreshTable();
    }

    private void initializeComponents() {
        setTitle((counselorOnly ? "Counselor Management" : "Staff Management") + " - " + currentStaff.getName());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setResizable(true);

        // Initialize form fields
        nameField = new JTextField(20);
        nameField.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        nameField.setForeground(new Color(50, 50, 50));
        nameField.setBackground(Color.WHITE);
        nameField.setCaretColor(new Color(130, 90, 255));
        passwordField = new JTextField(20);
        passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        passwordField.setForeground(new Color(50, 50, 50));
        passwordField.setBackground(Color.WHITE);
        passwordField.setCaretColor(new Color(130, 90, 255));
        if (counselorOnly) {
            roleComboBox = new JComboBox<>(new String[]{"Counselor"});
            roleComboBox.setEnabled(false);
        } else {
            roleComboBox = new JComboBox<>(new String[]{"Admin", "Receptionist", "Counselor"});
        }
        roleComboBox.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        specializationField = new JTextField(20);
        specializationField.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        specializationField.setForeground(new Color(50, 50, 50));
        specializationField.setBackground(Color.WHITE);
        specializationField.setCaretColor(new Color(130, 90, 255));
        availableCheckBox = new JCheckBox("Available");
        availableCheckBox.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        availableCheckBox.setForeground(new Color(130, 90, 255));
        availableCheckBox.setBackground(new Color(248, 250, 252));

        // Initialize buttons
        addButton = new JButton(counselorOnly ? "Add Counselor" : "Add Staff");
        updateButton = new JButton(counselorOnly ? "Update Counselor" : "Update Staff");
        deleteButton = new JButton(counselorOnly ? "Delete Counselor" : "Delete Staff");
        clearButton = new JButton("Clear Form");
        refreshButton = new JButton("Refresh");
        JButton[] buttons = {addButton, updateButton, deleteButton, clearButton, refreshButton};
        for (JButton btn : buttons) {
            btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
            btn.setBackground(new Color(130, 90, 255));
            btn.setForeground(Color.WHITE);
        }

        // Initialize table - show all staff members
        String[] columnNames = {"ID", "Name", "Specialization", "Available"};
        if (!counselorOnly) {
            columnNames = new String[]{"ID", "Name", "Role", "Specialization", "Available"};
        }
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table read-only
            }
        };
        staffTable = new JTable(tableModel);
        staffTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        staffTable.setBackground(Color.WHITE);
        staffTable.setForeground(new Color(50, 50, 50));
        staffTable.setSelectionBackground(new Color(130, 90, 255));
        staffTable.setSelectionForeground(Color.WHITE);
        staffTable.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        staffTable.getTableHeader().setBackground(new Color(130, 90, 255));
        staffTable.getTableHeader().setForeground(Color.WHITE);
        staffTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 15));
    }

    private void setupLayout() {
        setLayout(new BorderLayout());

        // Form Panel
        JPanel formPanel = createFormPanel();
        formPanel.setBorder(BorderFactory.createTitledBorder("Staff Details"));

        // Buttons Panel
        JPanel buttonsPanel = createButtonsPanel();

        // Table Panel
        JPanel tablePanel = createTablePanel();

        // Main layout
        JPanel topPanel = new JPanel(new BorderLayout());
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

        // Name
        gbc.gridx = 0; gbc.gridy = 0;
        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setForeground(new Color(70, 70, 70));
        panel.add(nameLabel, gbc);
        gbc.gridx = 1;
        panel.add(nameField, gbc);

        // Password
        gbc.gridx = 0; gbc.gridy = 1;
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setForeground(new Color(70, 70, 70));
        panel.add(passwordLabel, gbc);
        gbc.gridx = 1;
        panel.add(passwordField, gbc);

        // Role
        gbc.gridx = 0; gbc.gridy = 2;
        JLabel roleLabel = new JLabel("Role:");
        roleLabel.setForeground(new Color(70, 70, 70));
        panel.add(roleLabel, gbc);
        gbc.gridx = 1;
        panel.add(roleComboBox, gbc);

        // Specialization
        gbc.gridx = 0; gbc.gridy = 3;
        JLabel specializationLabel = new JLabel("Specialization:");
        specializationLabel.setForeground(new Color(70, 70, 70));
        panel.add(specializationLabel, gbc);
        gbc.gridx = 1;
        panel.add(specializationField, gbc);

        // Available
        gbc.gridx = 0; gbc.gridy = 4;
        JLabel availableLabel = new JLabel("Available:");
        availableLabel.setForeground(new Color(70, 70, 70));
        panel.add(availableLabel, gbc);
        gbc.gridx = 1;
        panel.add(availableCheckBox, gbc);

        return panel;
    }

    private JPanel createButtonsPanel() {
        JPanel panel = new JPanel(new FlowLayout());
        panel.setBackground(new Color(248, 250, 252));
        panel.add(addButton);
        panel.add(updateButton);
        panel.add(deleteButton);
        panel.add(clearButton);
        panel.add(refreshButton);
        return panel;
    }

    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(248, 250, 252));
        panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(130, 90, 255)), "Staff List", 0, 0, new Font("Segoe UI", Font.BOLD, 14), new Color(130, 90, 255)));
        JScrollPane scrollPane = new JScrollPane(staffTable);
        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }

    private void setupEventHandlers() {
        // Table selection listener
        staffTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                loadSelectedStaff();
            }
        });

        // Button listeners
        addButton.addActionListener(e -> addStaff());
        updateButton.addActionListener(e -> updateStaff());
        deleteButton.addActionListener(e -> deleteStaff());
        clearButton.addActionListener(e -> clearForm());
        refreshButton.addActionListener(e -> refreshTable());

        // Role change listener
        roleComboBox.addActionListener(e -> updateSpecializationVisibility());
    }

    private void updateSpecializationVisibility() {
        String selectedRole = (String) roleComboBox.getSelectedItem();
        boolean isCounselor = "Counselor".equals(selectedRole);
        specializationField.setEnabled(isCounselor);
        availableCheckBox.setEnabled(isCounselor);
    }

    private void refreshTable() {
        try {
            tableModel.setRowCount(0);
            List<Staff> allStaff = WellnessSystem.staffRepo.getAllStaff();
            System.out.println("[DEBUG] StaffManagementInterface: Loading " + allStaff.size() + " staff members from database");
            
            for (Staff staff : allStaff) {
                if (counselorOnly && !(staff instanceof CounselorStaff)) continue;
                System.out.println("[DEBUG] Staff: ID=" + staff.getId() + ", Name=" + staff.getName() + ", Role=" + staff.getRole());
                Vector<Object> row = new Vector<>();
                row.add(staff.getId());
                row.add(staff.getName());
                if (counselorOnly) {
                    CounselorStaff counselor = (CounselorStaff) staff;
                    row.add(counselor.getSpecialization());
                    row.add(counselor.isAvailable() ? "Yes" : "No");
                } else {
                    row.add(staff.getRole());
                    if (staff instanceof CounselorStaff) {
                        CounselorStaff counselor = (CounselorStaff) staff;
                        row.add(counselor.getSpecialization());
                        row.add(counselor.isAvailable() ? "Yes" : "No");
                    } else {
                        row.add("N/A"); // No specialization for non-counselors
                        row.add("N/A"); // No availability for non-counselors
                    }
                }
                tableModel.addRow(row);
            }
            System.out.println("[DEBUG] StaffManagementInterface: Added " + tableModel.getRowCount() + " rows to table");
        } catch (Exception e) {
            System.err.println("[DEBUG] StaffManagementInterface: Error loading staff: " + e.getMessage());
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading staff: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadSelectedStaff() {
        int selectedRow = staffTable.getSelectedRow();
        if (selectedRow >= 0) {
            nameField.setText((String) tableModel.getValueAt(selectedRow, 1));
            if (!counselorOnly) {
                roleComboBox.setSelectedItem(tableModel.getValueAt(selectedRow, 2));
            }
            
            String specialization = (String) tableModel.getValueAt(selectedRow, counselorOnly ? 2 : 3);
            String available = (String) tableModel.getValueAt(selectedRow, counselorOnly ? 3 : 4);
            
            if (!"N/A".equals(specialization)) {
                specializationField.setText(specialization);
                availableCheckBox.setSelected("Yes".equals(available));
            } else {
                specializationField.setText("");
                availableCheckBox.setSelected(false);
            }
        }
    }

    private void addStaff() {
        if (!validateForm()) return;

        try {
            String name = nameField.getText().trim();
            String password = passwordField.getText().trim();
            String role = counselorOnly ? "Counselor" : (String) roleComboBox.getSelectedItem();
            
            Staff newStaff = null;
            
            switch (role) {
                case "Counselor":
                    String specialization = specializationField.getText().trim();
                    boolean available = availableCheckBox.isSelected();
                    newStaff = new CounselorStaff(generateId(), name, password, specialization, available);
                    break;
                case "Admin":
                    newStaff = new Admin(generateId(), name, password);
                    break;
                case "Receptionist":
                    newStaff = new Receptionist(generateId(), name, password);
                    break;
            }
            
            if (newStaff != null) {
                WellnessSystem.staffRepo.addStaff(newStaff);
                JOptionPane.showMessageDialog(this, (counselorOnly ? "Counselor" : "Staff member") + " added successfully!", 
                    "Success", JOptionPane.INFORMATION_MESSAGE);
                clearForm();
                refreshTable();
            }
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error adding staff: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateStaff() {
        int selectedRow = staffTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Please select a " + (counselorOnly ? "counselor" : "staff member") + " to update.", 
                "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!validateForm()) return;

        try {
            int staffId = (Integer) tableModel.getValueAt(selectedRow, 0);
            String name = nameField.getText().trim();
            String password = passwordField.getText().trim();
            String role = counselorOnly ? "Counselor" : (String) roleComboBox.getSelectedItem();
            String specialization = specializationField.getText().trim();
            boolean available = availableCheckBox.isSelected();

            Staff staffToUpdate = WellnessSystem.staffRepo.findStaffById(staffId);
            if (staffToUpdate == null) {
                JOptionPane.showMessageDialog(this, (counselorOnly ? "Counselor" : "Staff member") + " not found in database.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            staffToUpdate.setName(name);
            staffToUpdate.setPassword(password);
            staffToUpdate.setRole(role);
            if (staffToUpdate instanceof CounselorStaff) {
                ((CounselorStaff) staffToUpdate).setSpecialization(specialization);
                ((CounselorStaff) staffToUpdate).setAvailable(available);
            }

            // Persist update to DB
            WellnessSystem.staffRepo.updateStaff(staffToUpdate);

            JOptionPane.showMessageDialog(this, (counselorOnly ? "Counselor" : "Staff member") + " updated successfully!", 
                "Success", JOptionPane.INFORMATION_MESSAGE);
            clearForm();
            refreshTable();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error updating staff: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteStaff() {
        int selectedRow = staffTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Please select a " + (counselorOnly ? "counselor" : "staff member") + " to delete.", 
                "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int choice = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to delete this " + (counselorOnly ? "counselor" : "staff member") + "?", 
            "Confirm Delete", JOptionPane.YES_NO_OPTION);
        
        if (choice == JOptionPane.YES_OPTION) {
            try {
                int staffId = (Integer) tableModel.getValueAt(selectedRow, 0);
                
                // Remove the staff member
                WellnessSystem.staffRepo.removeStaff(staffId);
                
                JOptionPane.showMessageDialog(this, (counselorOnly ? "Counselor" : "Staff member") + " deleted successfully!", 
                    "Success", JOptionPane.INFORMATION_MESSAGE);
                clearForm();
                refreshTable();
                
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error deleting staff: " + e.getMessage(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void clearForm() {
        nameField.setText("");
        passwordField.setText("");
        if (!counselorOnly) {
            roleComboBox.setSelectedIndex(0);
        }
        specializationField.setText("");
        availableCheckBox.setSelected(false);
        staffTable.clearSelection();
    }

    private boolean validateForm() {
        if (nameField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter staff name.", 
                "Validation Error", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        if (passwordField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter password.", 
                "Validation Error", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        String role = counselorOnly ? "Counselor" : (String) roleComboBox.getSelectedItem();
        if ("Counselor".equals(role) && specializationField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter specialization for counselor.", 
                "Validation Error", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        return true;
    }

    private int generateId() {
        // Simple ID generation - in a real app, this would be more sophisticated
        int maxId = 0;
        for (Staff staff : WellnessSystem.staffRepo.getAllStaff()) {
            if (staff.getId() > maxId) {
                maxId = staff.getId();
            }
        }
        return maxId + 1;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // For testing purposes
            Admin testAdmin = new Admin(100001, "Test Admin", "password");
            new StaffManagementInterface(testAdmin).setVisible(true);
        });
    }
} 