package main.model;

public class Admin extends User {
    public Admin(int id, String name, String password) {
        super(id, name, password, "Admin");
    }
    @Override public boolean canManageAppointments() { return true; }
    @Override public boolean canManageCounselors() { return true; }
    @Override public boolean canManageFeedback() { return true; }
    @Override public boolean canViewAllData() { return true; }
    @Override public boolean canBookAppointments() { return true; }
    @Override public boolean canViewSchedule() { return true; }
} 