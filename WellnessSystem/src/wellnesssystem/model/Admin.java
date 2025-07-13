package wellnesssystem.model;

/**
 * Admin class representing administrative staff with full system permissions
 * Inherits from Staff and implements role-based permissions
 */
public class Admin extends Staff {

    // Constructor
    public Admin(int id, String name, String password) {
        super(id, name, password, "Admin");
    }

    // Admin has full permissions - all methods return true
    @Override
    public boolean canManageAppointments() {
        return true; // Can add, edit, delete, and view all appointments
    }

    @Override
    public boolean canManageCounselors() {
        return true; // Can add, edit, delete, and view all counselors
    }

    @Override
    public boolean canManageFeedback() {
        return true; // Can add, edit, delete, and view all feedback
    }

    @Override
    public boolean canViewAllData() {
        return true; // Can view all data in the system
    }

    @Override
    public boolean canBookAppointments() {
        return true; // Can book appointments for students
    }

    @Override
    public boolean canViewSchedule() {
        return true; // Can view all schedules
    }

    // Admin-specific methods
    public void generateSystemReport() {
        // Method for generating comprehensive system reports
        System.out.println("Generating system report for Admin: " + getName());
    }

    public void manageUserAccounts() {
        // Method for managing user accounts
        System.out.println("Managing user accounts for Admin: " + getName());
    }

    @Override
    public String toString() {
        return "Admin{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
} 