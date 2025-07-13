package wellnesssystem.model;

/**
 * CounselorStaff class representing counseling staff with specialization and availability
 * Inherits from Staff and implements role-based permissions
 */
public class CounselorStaff extends Staff {
    private String specialization;
    private boolean available;

    // Constructor with all fields
    public CounselorStaff(int id, String name, String password, String specialization, boolean available) {
        super(id, name, password, "Counselor");
        this.specialization = specialization;
        this.available = available;
    }

    // Getters and Setters for specialization and availability
    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    // Counselor permissions - limited to viewing own schedule and feedback
    @Override
    public boolean canManageAppointments() {
        return false; // Cannot manage appointments
    }

    @Override
    public boolean canManageCounselors() {
        return false; // Cannot manage other counselors
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
        return false; // Cannot book appointments
    }

    @Override
    public boolean canViewSchedule() {
        return true; // Can view their own schedule
    }

    // Counselor-specific methods
    public void conductSession(String studentName) {
        // Method for conducting counseling sessions
        System.out.println("Conducting session with: " + studentName + " by Counselor: " + getName());
    }

    public void updateAvailability(boolean available) {
        this.available = available;
        System.out.println("Counselor " + getName() + " availability updated to: " + available);
    }

    public void viewMySchedule() {
        // Method for viewing own schedule
        System.out.println("Viewing schedule for Counselor: " + getName());
    }

    public void viewMyFeedback() {
        // Method for viewing feedback related to this counselor
        System.out.println("Viewing feedback for Counselor: " + getName());
    }

    @Override
    public String toString() {
        return "CounselorStaff{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", role='" + role + '\'' +
                ", specialization='" + specialization + '\'' +
                ", available=" + available +
                '}';
    }
} 