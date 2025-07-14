package main.model;

public abstract class User {
    protected int id;
    protected String name;
    protected String password;
    protected String role;

    public User(int id, String name, String password, String role) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.role = role;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getPassword() { return password; }
    public String getRole() { return role; }

    public abstract boolean canManageAppointments();
    public abstract boolean canManageCounselors();
    public abstract boolean canManageFeedback();
    public abstract boolean canViewAllData();
    public abstract boolean canBookAppointments();
    public abstract boolean canViewSchedule();
} 