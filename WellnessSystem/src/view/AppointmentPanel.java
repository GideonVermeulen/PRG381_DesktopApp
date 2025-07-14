package view;

import main.model.*;
import main.dao.AppointmentDAO;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.stream.Collectors;

public class AppointmentPanel extends JPanel {
    private AppointmentDAO dao = new AppointmentDAO();
    private java.util.List<Appointment> appointments;
    private JTable table;
    private DefaultTableModel tableModel;
    private User user;
    private java.util.List<String> counselorNames;

    public AppointmentPanel(User user, java.util.List<String> counselorNames) {
        this.user = user;
        this.counselorNames = counselorNames;
        setLayout(new BorderLayout());
        appointments = new ArrayList<>();
        String[] columns = {"ID", "Student", "Counselor", "Date", "Time", "Status", "Comments"};
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
        boolean canCrud = user.getRole().equals("Admin") || user.getRole().equals("Receptionist");
        addBtn.setEnabled(canCrud);
        editBtn.setEnabled(canCrud);
        delBtn.setEnabled(canCrud);
        if (!canCrud) {
            addBtn.setVisible(false);
            editBtn.setVisible(false);
            delBtn.setVisible(false);
        }

        addBtn.addActionListener(e -> addAppointment());
        editBtn.addActionListener(e -> editAppointment());
        delBtn.addActionListener(e -> deleteAppointment());
    }

    private void refreshTable() {
        appointments = dao.getAllAppointments();
        // Update statuses before displaying
        java.time.LocalDateTime now = java.time.LocalDateTime.now();
        for (Appointment a : appointments) {
            try {
                java.time.LocalDateTime start = java.time.LocalDateTime.parse(a.getDate() + "T" + a.getTime());
                java.time.LocalDateTime end = start.plusHours(1);
                if (now.isAfter(end)) {
                    if (!"Completed".equals(a.getStatus())) {
                        a.setStatus("Completed");
                        dao.updateAppointment(a);
                    }
                } else {
                    if (!"Scheduled".equals(a.getStatus())) {
                        a.setStatus("Scheduled");
                        dao.updateAppointment(a);
                    }
                }
            } catch (Exception e) { /* ignore parse errors */ }
        }
        tableModel.setRowCount(0);
        java.util.List<Appointment> filtered = appointments;
        if (user.getRole().equals("Counselor")) {
            filtered = appointments.stream().filter(a -> a.getCounselorName().equals(user.getName())).collect(Collectors.toList());
        }
        for (Appointment a : filtered) {
            tableModel.addRow(new Object[]{a.getId(), a.getStudentName(), a.getCounselorName(), a.getDate(), a.getTime(), a.getStatus(), a.getComments()});
        }
    }

    private void addAppointment() {
        AppointmentForm form = new AppointmentForm(null, counselorNames);
        int res = JOptionPane.showConfirmDialog(this, form, "Add Appointment", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (res == JOptionPane.OK_OPTION) {
            Appointment a = form.getAppointment(nextId());
            String err = validateAppointment(a);
            if (err != null) {
                JOptionPane.showMessageDialog(this, err);
                return;
            }
            if (isCounselorDoubleBooked(a, -1)) {
                JOptionPane.showMessageDialog(this, "This counselor is already booked for the selected time.");
                return;
            }
            dao.addAppointment(a);
            refreshTable();
        }
    }

    private void editAppointment() {
        int row = table.getSelectedRow();
        if (row == -1) return;
        int id = (int) tableModel.getValueAt(row, 0);
        Appointment orig = appointments.stream().filter(a -> a.getId() == id).findFirst().orElse(null);
        if (orig == null) return;
        AppointmentForm form = new AppointmentForm(orig, counselorNames);
        int res = JOptionPane.showConfirmDialog(this, form, "Edit Appointment", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (res == JOptionPane.OK_OPTION) {
            Appointment updated = form.getAppointment(id);
            String err = validateAppointment(updated);
            if (err != null) {
                JOptionPane.showMessageDialog(this, err);
                return;
            }
            if (isCounselorDoubleBooked(updated, id)) {
                JOptionPane.showMessageDialog(this, "This counselor is already booked for the selected time.");
                return;
            }
            dao.updateAppointment(updated);
            refreshTable();
        }
    }

    private void deleteAppointment() {
        int row = table.getSelectedRow();
        if (row == -1) return;
        int id = (int) tableModel.getValueAt(row, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this appointment?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            dao.deleteAppointment(id);
            refreshTable();
        }
    }

    private int nextId() {
        return appointments.stream().mapToInt(Appointment::getId).max().orElse(0) + 1;
    }

    private boolean isCounselorDoubleBooked(Appointment newApp, int ignoreId) {
        try {
            java.time.LocalDateTime newStart = java.time.LocalDateTime.parse(newApp.getDate() + "T" + newApp.getTime());
            java.time.LocalDateTime newEnd = newStart.plusHours(1);
            for (Appointment a : appointments) {
                if (a.getId() == ignoreId) continue;
                if (a.getCounselorName().equals(newApp.getCounselorName())) {
                    java.time.LocalDateTime start = java.time.LocalDateTime.parse(a.getDate() + "T" + a.getTime());
                    java.time.LocalDateTime end = start.plusHours(1);
                    if (newStart.isBefore(end) && newEnd.isAfter(start)) {
                        return true;
                    }
                }
            }
        } catch (Exception e) { /* ignore parse errors */ }
        return false;
    }

    private String validateAppointment(Appointment a) {
        if (a.getStudentName().trim().isEmpty() || a.getCounselorName().trim().isEmpty() || a.getDate().trim().isEmpty() || a.getTime().trim().isEmpty()) {
            return "All fields must be filled.";
        }
        try {
            java.time.LocalDateTime dt = java.time.LocalDateTime.parse(a.getDate() + "T" + a.getTime());
            if (dt.isBefore(java.time.LocalDateTime.now())) {
                return "Cannot book an appointment in the past.";
            }
        } catch (Exception e) {
            return "Invalid date or time format.";
        }
        return null;
    }

    // Inner form panel for add/edit
    private static class AppointmentForm extends JPanel {
        private JTextField studentField, dateField, timeField, commentsField;
        private JComboBox<String> counselorDropdown;
        public AppointmentForm(Appointment a, java.util.List<String> counselorNames) {
            setLayout(new GridLayout(6,2));
            add(new JLabel("Student:")); studentField = new JTextField(a != null ? a.getStudentName() : ""); add(studentField);
            add(new JLabel("Counselor:"));
            counselorDropdown = new JComboBox<>(counselorNames.toArray(new String[0]));
            if (a != null) counselorDropdown.setSelectedItem(a.getCounselorName());
            add(counselorDropdown);
            add(new JLabel("Date:"));
            String today = new java.text.SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date());
            dateField = new JTextField(a != null ? a.getDate() : today); add(dateField);
            add(new JLabel("Time:")); timeField = new JTextField(a != null ? a.getTime() : ""); add(timeField);
            add(new JLabel("Status:")); add(new JLabel("(Will be set automatically)"));
            add(new JLabel("Comments:")); commentsField = new JTextField(a != null ? a.getComments() : ""); add(commentsField);
        }
        public Appointment getAppointment(int id) {
            return new Appointment(id, studentField.getText(), (String)counselorDropdown.getSelectedItem(), dateField.getText(), timeField.getText(), "Scheduled", commentsField.getText());
        }
    }
} 