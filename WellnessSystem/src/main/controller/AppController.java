package main.controller;

import main.dao.FeedbackDAO;
import main.dao.AppointmentDAO;
import main.dao.StaffDAO;
import main.dao.UserDAO;
import main.model.Feedback;
import main.model.Appointment;
import main.model.User;
import java.util.List;
import java.util.ArrayList;
import main.model.Counselor;

public class AppController {
    private FeedbackDAO feedbackDAO = new FeedbackDAO();
    private AppointmentDAO appointmentDAO = new AppointmentDAO();
    private StaffDAO staffDAO = new StaffDAO();
    private UserDAO userDAO = new UserDAO();

    // Feedback methods
    public List<Feedback> getAllFeedback() {
        return feedbackDAO.getAllFeedback();
    }
    public List<Feedback> getFeedbackForCounselor(String counselorName) {
        return feedbackDAO.getFeedbackForCounselor(counselorName);
    }
    public void addFeedback(Feedback f) {
        feedbackDAO.addFeedback(f);
    }
    public void updateFeedback(Feedback f) {
        feedbackDAO.updateFeedback(f);
    }
    public void deleteFeedback(int id) {
        feedbackDAO.deleteFeedback(id);
    }
    public int getNextFeedbackId() {
        return feedbackDAO.getAllFeedback().stream().mapToInt(Feedback::getId).max().orElse(0) + 1;
    }

    // Appointment methods
    public List<Appointment> getAllAppointments() {
        return appointmentDAO.getAllAppointments();
    }
    public List<Appointment> getAppointmentsForCounselor(String counselorName) {
        List<Appointment> all = appointmentDAO.getAllAppointments();
        return all.stream().filter(a -> a.getCounselorName().equals(counselorName)).toList();
    }
    public void addAppointment(Appointment a) {
        appointmentDAO.addAppointment(a);
    }
    public void updateAppointment(Appointment a) {
        appointmentDAO.updateAppointment(a);
    }
    public void deleteAppointment(int id) {
        appointmentDAO.deleteAppointment(id);
    }
    public int getNextAppointmentId() {
        return appointmentDAO.getAllAppointments().stream().mapToInt(Appointment::getId).max().orElse(0) + 1;
    }

    // Staff methods
    public List<User> getAllStaff() {
        return staffDAO.getAllStaff();
    }
    public List<User> getStaffByRole(String role) {
        return staffDAO.getStaffByRole(role);
    }
    public void addStaff(User u) {
        staffDAO.addStaff(u);
    }
    public void updateStaff(User u) {
        staffDAO.updateStaff(u);
    }
    public void deleteStaff(int id) {
        staffDAO.deleteStaff(id);
    }
    public int getNextStaffId() {
        return staffDAO.getAllStaff().stream().mapToInt(User::getId).max().orElse(0) + 1;
    }

    // Update counselor availability
    public void setCounselorAvailability(int counselorId, boolean available) {
        List<User> counselors = staffDAO.getStaffByRole("Counselor");
        for (User u : counselors) {
            if (u.getId() == counselorId && u instanceof Counselor) {
                Counselor c = (Counselor) u;
                c.setAvailable(available);
                staffDAO.updateStaff(c);
                break;
            }
        }
    }
    // Get only available counselors
    public List<User> getAvailableCounselors() {
        List<User> counselors = staffDAO.getStaffByRole("Counselor");
        List<User> available = new ArrayList<>();
        for (User u : counselors) {
            if (u instanceof Counselor && ((Counselor) u).isAvailable()) {
                available.add(u);
            }
        }
        return available;
    }

    // Authentication
    public User authenticateUser(int id, String password) {
        return userDAO.authenticateUser(id, password);
    }
} 