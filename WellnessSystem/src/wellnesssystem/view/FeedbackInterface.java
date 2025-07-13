package wellnesssystem.view;

import wellnesssystem.WellnessSystem;
import wellnesssystem.model.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Vector;
import java.sql.SQLException;

/**
 * Feedback Management Interface
 * Uses database storage - no in-memory fallbacks
 */
public class FeedbackInterface extends JFrame {
    private Staff currentStaff;
    private JTable feedbackTable;
    private DefaultTableModel tableModel;
    private JTextField studentNameField;
    private JComboBox<CounselorStaff> counselorComboBox;
    private JTextField dateField;
    private JComboBox<String> ratingComboBox;
    private JTextArea feedbackArea;
    
    // Buttons
    private JButton addButton;
    private JButton updateButton;
    private JButton deleteButton;
    private JButton clearButton;
    private JButton refreshButton;
    
    // Database-based data storage
    private Vector<Feedback> feedbacks;

    public FeedbackInterface(Staff staff) {
        this.currentStaff = staff;
        this.feedbacks = new Vector<>();
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        loadSampleData();
        refreshTable();
    }

    private void initializeComponents() {
        setTitle("Feedback Management - " + currentStaff.getName());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(900, 600);
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
        // Custom renderer to show only counselor name
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
        dateField = new JTextField(10);
        dateField.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        dateField.setForeground(new Color(50, 50, 50));
        dateField.setBackground(Color.WHITE);
        dateField.setCaretColor(new Color(130, 90, 255));
        ratingComboBox = new JComboBox<>(new String[]{"1", "2", "3", "4", "5"});
        ratingComboBox.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        feedbackArea = new JTextArea(6, 30); // Make it larger
        feedbackArea.setLineWrap(true);
        feedbackArea.setWrapStyleWord(true);
        feedbackArea.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        feedbackArea.setForeground(new Color(50, 50, 50));
        feedbackArea.setBackground(Color.WHITE);
        feedbackArea.setCaretColor(new Color(130, 90, 255));
        feedbackArea.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(130, 90, 255)),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));

        // Initialize buttons
        addButton = new JButton("Add Feedback");
        updateButton = new JButton("Update Feedback");
        deleteButton = new JButton("Delete Feedback");
        clearButton = new JButton("Clear Form");
        refreshButton = new JButton("Refresh");
        
        JButton[] buttons = {addButton, updateButton, deleteButton, clearButton, refreshButton};
        for (JButton btn : buttons) {
            btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
            btn.setBackground(new Color(130, 90, 255));
            btn.setForeground(Color.WHITE);
        }

        // Initialize table
        String[] columnNames = {"ID", "Student Name", "Counselor", "Date", "Rating", "Feedback", "Created By"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table read-only
            }
        };
        feedbackTable = new JTable(tableModel);
        feedbackTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        feedbackTable.setBackground(Color.WHITE);
        feedbackTable.setForeground(new Color(50, 50, 50));
        feedbackTable.setSelectionBackground(new Color(130, 90, 255));
        feedbackTable.setSelectionForeground(Color.WHITE);
        feedbackTable.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        feedbackTable.getTableHeader().setBackground(new Color(130, 90, 255));
        feedbackTable.getTableHeader().setForeground(Color.WHITE);
        feedbackTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 15));
    }

    private void setupLayout() {
        setLayout(new BorderLayout());

        // Form Panel
        JPanel formPanel = createFormPanel();
        formPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(130, 90, 255)), "Feedback Details", 0, 0, new Font("Segoe UI", Font.BOLD, 14), new Color(130, 90, 255)));
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
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.EAST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int labelWidth = 120;

        // Student Name
        gbc.gridx = 0; gbc.gridy = 0;
        JLabel studentNameLabel = new JLabel("Student Name:");
        studentNameLabel.setPreferredSize(new Dimension(labelWidth, 28));
        studentNameLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        studentNameLabel.setForeground(new Color(70, 70, 70));
        panel.add(studentNameLabel, gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        panel.add(studentNameField, gbc);

        // Counselor
        gbc.gridx = 0; gbc.gridy = 1;
        JLabel counselorLabel = new JLabel("Counselor:");
        counselorLabel.setPreferredSize(new Dimension(labelWidth, 28));
        counselorLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        counselorLabel.setForeground(new Color(70, 70, 70));
        panel.add(counselorLabel, gbc);
        gbc.gridx = 1;
        panel.add(counselorComboBox, gbc);

        // Date
        gbc.gridx = 0; gbc.gridy = 2;
        JLabel dateLabel = new JLabel("Date (YYYY-MM-DD):");
        dateLabel.setPreferredSize(new Dimension(labelWidth, 28));
        dateLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        dateLabel.setForeground(new Color(70, 70, 70));
        panel.add(dateLabel, gbc);
        gbc.gridx = 1;
        panel.add(dateField, gbc);

        // Rating
        gbc.gridx = 0; gbc.gridy = 3;
        JLabel ratingLabel = new JLabel("Rating (1-5):");
        ratingLabel.setPreferredSize(new Dimension(labelWidth, 28));
        ratingLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        ratingLabel.setForeground(new Color(70, 70, 70));
        panel.add(ratingLabel, gbc);
        gbc.gridx = 1;
        panel.add(ratingComboBox, gbc);

        // Feedback
        gbc.gridx = 0; gbc.gridy = 4;
        JLabel feedbackLabel = new JLabel("Feedback:");
        feedbackLabel.setPreferredSize(new Dimension(labelWidth, 28));
        feedbackLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        feedbackLabel.setForeground(new Color(70, 70, 70));
        panel.add(feedbackLabel, gbc);
        gbc.gridx = 1;
        JScrollPane feedbackScroll = new JScrollPane(feedbackArea);
        feedbackScroll.setPreferredSize(new Dimension(340, 120));
        feedbackScroll.setMinimumSize(new Dimension(340, 80));
        feedbackScroll.setBorder(BorderFactory.createLineBorder(new Color(130, 90, 255)));
        panel.add(feedbackScroll, gbc);

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
        panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(130, 90, 255)), "Feedback List", 0, 0, new Font("Segoe UI", Font.BOLD, 14), new Color(130, 90, 255)));
        
        JScrollPane scrollPane = new JScrollPane(feedbackTable);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }

    private void setupEventHandlers() {
        // Table selection listener
        feedbackTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                loadSelectedFeedback();
            }
        });

        // Button listeners
        addButton.addActionListener(e -> addFeedback());
        updateButton.addActionListener(e -> updateFeedback());
        deleteButton.addActionListener(e -> deleteFeedback());
        clearButton.addActionListener(e -> clearForm());
        refreshButton.addActionListener(e -> refreshTable());

        // Set current date as default
        dateField.setText(LocalDate.now().toString());
        
        // Load counselors
        loadCounselors();
    }

    private void loadCounselors() {
        counselorComboBox.removeAllItems();
        
        // Get counselors from the staff manager
        for (Staff staff : WellnessSystem.staffRepo.getAllStaff()) {
            if (staff instanceof CounselorStaff) {
                counselorComboBox.addItem((CounselorStaff) staff);
            }
        }
    }

    private void loadSampleData() {
        // Load feedback from database
        try {
            feedbacks.clear();
            feedbacks.addAll(FeedbackDAO.getAllFeedback());
            System.out.println("Loaded " + feedbacks.size() + " feedback entries from database");
        } catch (SQLException e) {
            System.err.println("Error loading feedback: " + e.getMessage());
            JOptionPane.showMessageDialog(this, "Error loading feedback: " + e.getMessage(), 
                "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addSampleFeedback(String studentName, String counselorName, String date, String rating, String feedback) {
        try {
            CounselorStaff counselor = findCounselorByName(counselorName);
            if (counselor != null) {
                Feedback feedbackObj = new Feedback(
                    0, // ID will be generated by database
                    studentName,
                    counselor,
                    date,
                    Integer.parseInt(rating),
                    feedback,
                    currentStaff
                );
                int id = FeedbackDAO.addFeedback(feedbackObj);
                if (id > 0) {
                    feedbackObj = new Feedback(id, studentName, counselor, date, Integer.parseInt(rating), feedback, currentStaff);
                    feedbacks.add(feedbackObj);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error adding sample feedback: " + e.getMessage());
        }
    }

    private CounselorStaff findCounselorByName(String name) {
        for (Staff staff : WellnessSystem.staffRepo.getAllStaff()) {
            if (staff instanceof CounselorStaff && staff.getName().equals(name)) {
                return (CounselorStaff) staff;
            }
        }
        return null;
    }

    private void refreshTable() {
        try {
            // Reload from database
            feedbacks.clear();
            feedbacks.addAll(FeedbackDAO.getAllFeedback());
            
            tableModel.setRowCount(0);
            for (Feedback feedback : feedbacks) {
                Vector<Object> row = new Vector<>();
                row.add(feedback.getId());
                row.add(feedback.getStudentName());
                row.add(feedback.getCounselorName());
                row.add(feedback.getDate());
                row.add(feedback.getRating());
                row.add(feedback.getFeedback());
                row.add(feedback.getCreatedByName());
                tableModel.addRow(row);
            }
        } catch (SQLException e) {
            System.err.println("Error refreshing table: " + e.getMessage());
            JOptionPane.showMessageDialog(this, "Error loading feedback: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadSelectedFeedback() {
        int selectedRow = feedbackTable.getSelectedRow();
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
            ratingComboBox.setSelectedItem(tableModel.getValueAt(selectedRow, 4).toString());
            feedbackArea.setText((String) tableModel.getValueAt(selectedRow, 5));
        }
    }

    private void addFeedback() {
        if (!validateForm()) return;

        try {
            CounselorStaff selectedCounselor = (CounselorStaff) counselorComboBox.getSelectedItem();
            int rating = Integer.parseInt((String) ratingComboBox.getSelectedItem());
            
            Feedback feedback = new Feedback(
                0, // ID will be generated by database
                studentNameField.getText().trim(),
                selectedCounselor,
                dateField.getText(),
                rating,
                feedbackArea.getText().trim(),
                currentStaff
            );
            
            int id = FeedbackDAO.addFeedback(feedback);
            if (id > 0) {
                feedback = new Feedback(id, studentNameField.getText().trim(), selectedCounselor, 
                    dateField.getText(), rating, feedbackArea.getText().trim(), currentStaff);
                feedbacks.add(feedback);
                
                JOptionPane.showMessageDialog(this, "Feedback added successfully!", 
                    "Success", JOptionPane.INFORMATION_MESSAGE);
                clearForm();
                refreshTable();
            } else {
                JOptionPane.showMessageDialog(this, "Error adding feedback to database", 
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
            
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Database error: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error adding feedback: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateFeedback() {
        int selectedRow = feedbackTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Please select a feedback to update.", 
                "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!validateForm()) return;

        try {
            int feedbackId = (Integer) tableModel.getValueAt(selectedRow, 0);
            
            // Find and update the feedback
            for (int i = 0; i < feedbacks.size(); i++) {
                Feedback feedback = feedbacks.get(i);
                if (feedback.getId() == feedbackId) {
                    CounselorStaff selectedCounselor = (CounselorStaff) counselorComboBox.getSelectedItem();
                    int rating = Integer.parseInt((String) ratingComboBox.getSelectedItem());
                    
                    feedback.setStudentName(studentNameField.getText().trim());
                    feedback.setCounselor(selectedCounselor);
                    feedback.setDate(dateField.getText());
                    feedback.setRating(rating);
                    feedback.setFeedback(feedbackArea.getText().trim());
                    
                    // Update in database
                    if (FeedbackDAO.updateFeedback(feedback)) {
                        JOptionPane.showMessageDialog(this, "Feedback updated successfully!", 
                            "Success", JOptionPane.INFORMATION_MESSAGE);
                        clearForm();
                        refreshTable();
                    } else {
                        JOptionPane.showMessageDialog(this, "Error updating feedback in database", 
                            "Error", JOptionPane.ERROR_MESSAGE);
                    }
                    break;
                }
            }
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Database error: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error updating feedback: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteFeedback() {
        int selectedRow = feedbackTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Please select a feedback to delete.", 
                "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int choice = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to delete this feedback?", 
            "Confirm Delete", JOptionPane.YES_NO_OPTION);
        
        if (choice == JOptionPane.YES_OPTION) {
            try {
                int feedbackId = (Integer) tableModel.getValueAt(selectedRow, 0);
                
                // Delete from database
                if (FeedbackDAO.deleteFeedback(feedbackId)) {
                    // Remove from local list
                    feedbacks.removeIf(feedback -> feedback.getId() == feedbackId);
                    
                    JOptionPane.showMessageDialog(this, "Feedback deleted successfully!", 
                        "Success", JOptionPane.INFORMATION_MESSAGE);
                    clearForm();
                    refreshTable();
                } else {
                    JOptionPane.showMessageDialog(this, "Error deleting feedback from database", 
                        "Error", JOptionPane.ERROR_MESSAGE);
                }
                
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Database error: " + e.getMessage(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error deleting feedback: " + e.getMessage(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void clearForm() {
        studentNameField.setText("");
        counselorComboBox.setSelectedIndex(-1);
        dateField.setText(LocalDate.now().toString());
        ratingComboBox.setSelectedIndex(0);
        feedbackArea.setText("");
        feedbackTable.clearSelection();
    }

    private boolean validateForm() {
        if (studentNameField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter student name.", 
                "Validation Error", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        if (counselorComboBox.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Please select a counselor.", 
                "Validation Error", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        if (dateField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter feedback date.", 
                "Validation Error", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        if (feedbackArea.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter feedback content.", 
                "Validation Error", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        return true;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // For testing purposes
            Admin testAdmin = new Admin(100001, "Test Admin", "password");
            new FeedbackInterface(testAdmin).setVisible(true);
        });
    }
} 