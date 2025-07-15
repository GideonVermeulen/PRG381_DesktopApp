package view;

import main.model.*;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.*;
import java.util.stream.Collectors;
import main.controller.AppController;

public class CounselorPanel extends JPanel {
    public CounselorPanel(User user, java.util.List<Appointment> appointments, java.util.List<Feedback> feedbackList, java.util.List<User> staffList) {
        AppController controller = new AppController();
        if (user.getRole().equals("Admin")) {
            setLayout(new BorderLayout());
            String[] cols = {"ID", "Name", "Specialization", "Available"};
            javax.swing.table.DefaultTableModel model = new javax.swing.table.DefaultTableModel(cols, 0) {
                public boolean isCellEditable(int r, int c) { return false; }
            };
            for (User u : staffList) {
                if (u.getRole().equals("Counselor")) {
                    Counselor c = (Counselor) u;
                    model.addRow(new Object[]{c.getId(), c.getName(), c.getSpecialization(), c.isAvailable()});
                }
            }
            JTable table = new JTable(model);
            add(new JLabel("All Counselors"), BorderLayout.NORTH);
            add(new JScrollPane(table), BorderLayout.CENTER);
        } else {
            setLayout(new BorderLayout());
            JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            Counselor self = null;
            for (User u : staffList) {
                if (u.getId() == user.getId() && u instanceof Counselor) {
                    self = (Counselor) u;
                    break;
                }
            }
            JLabel statusLabel = new JLabel();
            JToggleButton availToggle = new JToggleButton();
            if (self != null && self.isAvailable()) {
                availToggle.setSelected(true);
                availToggle.setText("Available");
                statusLabel.setText("Status: Available");
            } else {
                availToggle.setSelected(false);
                availToggle.setText("Unavailable");
                statusLabel.setText("Status: Unavailable");
            }
            availToggle.addActionListener(e -> {
                boolean available = availToggle.isSelected();
                controller.setCounselorAvailability(user.getId(), available);
                availToggle.setText(available ? "Available" : "Unavailable");
                statusLabel.setText("Status: " + (available ? "Available" : "Unavailable"));
            });
            topPanel.add(availToggle);
            topPanel.add(statusLabel);
            add(topPanel, BorderLayout.NORTH);
            JPanel centerPanel = new JPanel(new GridLayout(2, 1));
            // Appointments table
            java.util.List<Appointment> myAppointments = appointments.stream().filter(a -> a.getCounselorName().equals(user.getName())).collect(Collectors.toList());
            String[] appCols = {"ID", "Student", "Date", "Time", "Status", "Comments"};
            DefaultTableModel appModel = new DefaultTableModel(appCols, 0) { public boolean isCellEditable(int r, int c) { return false; } };
            for (Appointment a : myAppointments) {
                appModel.addRow(new Object[]{a.getId(), a.getStudentName(), a.getDate(), a.getTime(), a.getStatus(), a.getComments()});
            }
            JTable appTable = new JTable(appModel);
            JPanel appPanel = new JPanel(new BorderLayout());
            appPanel.add(new JLabel("My Appointments"), BorderLayout.NORTH);
            appPanel.add(new JScrollPane(appTable), BorderLayout.CENTER);
            centerPanel.add(appPanel);
            // Feedback table
            java.util.List<Feedback> myFeedback = feedbackList.stream().filter(f -> f.getCounselorName().equals(user.getName())).collect(Collectors.toList());
            String[] fbCols = {"ID", "Student", "Date", "Rating", "Comments"};
            DefaultTableModel fbModel = new DefaultTableModel(fbCols, 0) { public boolean isCellEditable(int r, int c) { return false; } };
            for (Feedback f : myFeedback) {
                fbModel.addRow(new Object[]{f.getId(), f.getStudentName(), f.getDate(), f.getRating(), f.getComments()});
            }
            JTable fbTable = new JTable(fbModel);
            JPanel fbPanel = new JPanel(new BorderLayout());
            fbPanel.add(new JLabel("My Feedback"), BorderLayout.NORTH);
            fbPanel.add(new JScrollPane(fbTable), BorderLayout.CENTER);
            centerPanel.add(fbPanel);
            add(centerPanel, BorderLayout.CENTER);
        }
    }
} 