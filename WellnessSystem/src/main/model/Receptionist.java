package main.model;

public class Receptionist extends User {
    public Receptionist(int id, String name, String password) {
        super(id, name, password, "Receptionist");
    }
    @Override public boolean canManageAppointments() { return true; }
    @Override public boolean canManageCounselors() { return false; }
    @Override public boolean canManageFeedback() { return false; }
    @Override public boolean canViewAllData() { return false; }
    @Override public boolean canBookAppointments() { return true; }
    @Override public boolean canViewSchedule() { return true; }
} 