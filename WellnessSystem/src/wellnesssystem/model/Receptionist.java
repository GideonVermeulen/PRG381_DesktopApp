package wellnesssystem.model;

/**
 * Receptionist class representing reception staff with limited permissions
 * Inherits from Staff and implements role-based permissions
 */
public class Receptionist extends Staff {

    // Constructor
    public Receptionist(int id, String name, String password) {
        super(id, name, password, "Receptionist");
    }

    // Receptionist permissions - limited to appointment booking and viewing
    @Override
    public boolean canManageAppointments() {
        return false; // Cannot manage (add/edit/delete) appointments
    }

    @Override
    public boolean canManageCounselors() {
        return false; // Cannot manage counselors
    }

    @Override
    public boolean canManageFeedback() {
        return false; // Cannot manage feedback
    }

    @Override
    public boolean canViewAllData() {
        return false; // Cannot view all data
    }

    @Override
    public boolean canBookAppointments() {
        return true; // Can book appointments for students
    }

    @Override
    public boolean canViewSchedule() {
        return true; // Can view schedules
    }

    // Receptionist-specific methods
    public void checkInStudent(String studentName) {
        // Method for checking in students for appointments
        System.out.println("Checking in student: " + studentName + " by Receptionist: " + getName());
    }

    public void checkOutStudent(String studentName) {
        // Method for checking out students after appointments
        System.out.println("Checking out student: " + studentName + " by Receptionist: " + getName());
    }

    public void handleWalkInAppointment(String studentName) {
        // Method for handling walk-in appointments
        System.out.println("Handling walk-in appointment for: " + studentName + " by Receptionist: " + getName());
    }

    @Override
    public String toString() {
        return "Receptionist{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
} 