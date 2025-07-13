package wellnesssystem.model;

/**
 * Base Staff class representing all staff members in the wellness system
 * Uses inheritance and abstraction for role-based permissions
 */
public abstract class Staff {
    protected int id; // 6-digit ID
    protected String name;
    protected String password;
    protected String role;

    // Constructor
    public Staff(int id, String name, String password, String role) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.role = role;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    // Abstract methods for role-based permissions
    public abstract boolean canManageAppointments();
    public abstract boolean canManageCounselors();
    public abstract boolean canManageFeedback();
    public abstract boolean canViewAllData();
    public abstract boolean canBookAppointments();
    public abstract boolean canViewSchedule();

    // Method to validate 6-digit ID
    public static boolean isValidId(int id) {
        return id >= 100000 && id <= 999999;
    }

    // Method to validate password strength
    public static boolean isValidPassword(String password) {
        return password != null && password.length() >= 6;
    }

    @Override
    public String toString() {
        return "Staff{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
} 