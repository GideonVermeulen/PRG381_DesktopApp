package view;

import main.model.*;
import main.dao.FeedbackDAO;
import main.controller.AppController;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.stream.Collectors;

public class FeedbackPanel extends JPanel {
    private AppController controller = new AppController();
    private java.util.List<String> counselorNames;
    private JTable table;
    private DefaultTableModel tableModel;
    private User user;

    public FeedbackPanel(User user, java.util.List<String> counselorNames) {
        this.user = user;
        this.counselorNames = counselorNames;
        setLayout(new BorderLayout());
        String[] columns = {"ID", "Student", "Counselor", "Date", "Rating", "Comments"};
        tableModel = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int row, int col) { return false; }
        };
        table = new JTable(tableModel);
        refreshTable();
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel btnPanel = new JPanel();
        JButton addBtn = new JButton("Add");
        JButton editBtn = new JButton("Edit");
        JButton delBtn = new JButton("Delete");
        btnPanel.add(addBtn);
        btnPanel.add(editBtn);
        btnPanel.add(delBtn);
        add(btnPanel, BorderLayout.SOUTH);

        // Role-based permissions
        boolean isAdmin = user.getRole().equals("Admin");
        boolean isReceptionist = user.getRole().equals("Receptionist");
        boolean isCounselor = user.getRole().equals("Counselor");
        addBtn.setEnabled(isAdmin || isReceptionist);
        editBtn.setEnabled(isAdmin);
        delBtn.setEnabled(isAdmin);
        if (isReceptionist) {
            // Receptionist: only add feedback, no view/edit/delete
            table.setVisible(false);
            editBtn.setVisible(false);
            delBtn.setVisible(false);
        } else if (isCounselor) {
            addBtn.setVisible(false);
            editBtn.setVisible(false);
            delBtn.setVisible(false);
        }

        addBtn.addActionListener(e -> addFeedback());
        editBtn.addActionListener(e -> editFeedback());
        delBtn.addActionListener(e -> deleteFeedback());
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        java.util.List<Feedback> filtered;
        if (user.getRole().equals("Counselor")) {
            filtered = controller.getFeedbackForCounselor(user.getName());
        } else if (user.getRole().equals("Receptionist")) {
            // Receptionist cannot view feedback
            return;
        } else {
            filtered = controller.getAllFeedback();
        }
        for (Feedback f : filtered) {
            tableModel.addRow(new Object[]{f.getId(), f.getStudentName(), f.getCounselorName(), f.getDate(), f.getRating(), f.getComments()});
        }
    }

    private void addFeedback() {
        FeedbackForm form = new FeedbackForm(null, counselorNames);
        int res = JOptionPane.showConfirmDialog(this, form, "Add Feedback", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (res == JOptionPane.OK_OPTION) {
            int nextId = controller.getNextFeedbackId();
            Feedback f = form.getFeedback(nextId);
            String err = validateFeedback(f);
            if (err != null) {
                JOptionPane.showMessageDialog(this, err);
                return;
            }
            controller.addFeedback(f);
            refreshTable();
        }
    }

    private void editFeedback() {
        int row = table.getSelectedRow();
        if (row == -1) return;
        int id = (int) tableModel.getValueAt(row, 0);
        Feedback orig = controller.getAllFeedback().stream().filter(f -> f.getId() == id).findFirst().orElse(null);
        if (orig == null) return;
        FeedbackForm form = new FeedbackForm(orig, counselorNames);
        int res = JOptionPane.showConfirmDialog(this, form, "Edit Feedback", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (res == JOptionPane.OK_OPTION) {
            Feedback updated = form.getFeedback(id);
            String err = validateFeedback(updated);
            if (err != null) {
                JOptionPane.showMessageDialog(this, err);
                return;
            }
            controller.updateFeedback(updated);
            refreshTable();
        }
    }

    private void deleteFeedback() {
        int row = table.getSelectedRow();
        if (row == -1) return;
        int id = (int) tableModel.getValueAt(row, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this feedback?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;
        controller.deleteFeedback(id);
        refreshTable();
    }

    private String validateFeedback(Feedback f) {
        if (f.getStudentName().trim().isEmpty() || f.getCounselorName().trim().isEmpty() || f.getDate().trim().isEmpty() || String.valueOf(f.getRating()).trim().isEmpty() || f.getComments().trim().isEmpty()) {
            return "All fields must be filled.";
        }
        try {
            java.time.LocalDate d = java.time.LocalDate.parse(f.getDate());
            if (d.isBefore(java.time.LocalDate.now())) {
                return "Cannot add feedback for a date in the past.";
            }
        } catch (Exception e) {
            return "Invalid date format.";
        }
        return null;
    }

    // Inner form panel for add/edit
    private static class FeedbackForm extends JPanel {
        private JTextField studentField, dateField, commentsField;
        private JComboBox<String> counselorDropdown;
        private JComboBox<Integer> ratingDropdown;
        public FeedbackForm(Feedback f, java.util.List<String> counselorNames) {
            setLayout(new GridLayout(5,2));
            add(new JLabel("Student:")); studentField = new JTextField(f != null ? f.getStudentName() : ""); add(studentField);
            add(new JLabel("Counselor:"));
            counselorDropdown = new JComboBox<>(counselorNames.toArray(new String[0]));
            if (f != null) counselorDropdown.setSelectedItem(f.getCounselorName());
            add(counselorDropdown);
            add(new JLabel("Date:"));
            String today = new java.text.SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date());
            dateField = new JTextField(f != null ? f.getDate() : today); add(dateField);
            add(new JLabel("Rating (1-5):"));
            ratingDropdown = new JComboBox<>(new Integer[]{1,2,3,4,5});
            if (f != null) ratingDropdown.setSelectedItem(f.getRating());
            add(ratingDropdown);
            add(new JLabel("Comments:")); commentsField = new JTextField(f != null ? f.getComments() : ""); add(commentsField);
        }
        public Feedback getFeedback(int id) {
            int rating = (Integer)ratingDropdown.getSelectedItem();
            return new Feedback(id, studentField.getText(), (String)counselorDropdown.getSelectedItem(), dateField.getText(), rating, commentsField.getText());
        }
    }
} 