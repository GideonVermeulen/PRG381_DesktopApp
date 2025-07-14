package main.model;

public class Counselor extends User {
    private String specialization;
    private boolean available;

    public Counselor(int id, String name, String password, String specialization, boolean available) {
        super(id, name, password, "Counselor");
        this.specialization = specialization;
        this.available = available;
    }

    public String getSpecialization() { return specialization; }
    public boolean isAvailable() { return available; }
    public void setAvailable(boolean available) { this.available = available; }

    @Override public boolean canManageAppointments() { return false; }
    @Override public boolean canManageCounselors() { return false; }
    @Override public boolean canManageFeedback() { return false; }
    @Override public boolean canViewAllData() { return false; }
    @Override public boolean canBookAppointments() { return false; }
    @Override public boolean canViewSchedule() { return true; }
} 