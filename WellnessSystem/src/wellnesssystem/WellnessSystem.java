package wellnesssystem;
import wellnesssystem.view.*;
import wellnesssystem.model.*;
import db.*;

import javax.swing.*;

/**
 * Main application class for the Wellness System
 * Database-only implementation - requires Derby database to be available
 */
public class WellnessSystem {
    public static StaffRepository staffRepo = new StaffRepository();
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // Set modern look and feel
        try {
            // Try FlatLaf first
            UIManager.setLookAndFeel("com.formdev.flatlaf.FlatLightLaf");
        } catch (Exception e1) {
            try {
                // Fallback to Nimbus
                for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                    if ("Nimbus".equals(info.getName())) {
                        UIManager.setLookAndFeel(info.getClassName());
                        break;
                    }
                }
            } catch (Exception e2) {
                // Fallback to system default
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (Exception ignore) {}
            }
        }
        // Initialize the system with database
        initializeSystem();
        // Show login form
        SwingUtilities.invokeLater(() -> {
            new LoginView().setVisible(true);
        });
    }
    
    /**
     * Initialize the system with database and sample data
     */
    private static void initializeSystem() {
        try {
            // Initialize database
            System.out.println("🔧 Initializing database...");
            CreateTables.createAllTables();
            
            // Add sample staff members to demonstrate the system
            addSampleStaff();
            
            System.out.println("Wellness System initialized successfully!");
            System.out.println("Total staff members: " + staffRepo.getTotalStaffCount());
            System.out.println("✅ Running with database storage");
            // Print all staff for debug
            for (Staff s : staffRepo.getAllStaff()) {
                System.out.println("[DEBUG] DB Staff: ID=" + s.getId() + ", Name=" + s.getName() + ", Role=" + s.getRole());
            }
            
        } catch (Exception e) {
            System.err.println("❌ Database initialization failed: " + e.getMessage());
            System.err.println("💡 Make sure Derby JARs are on the classpath:");
            System.err.println("   - Add src/lib/derby.jar, derbyshared.jar, derbytools.jar to your project libraries");
            System.err.println("   - Or use the launcher script: run-wellness.bat");
            System.err.println("   - Or run: java -cp \"src;src/lib/*\" wellnesssystem.WellnessSystem");
            System.exit(1); // Exit if database is not available
        }
    }
    
    /**
     * Add sample staff members for demonstration
     */
    private static void addSampleStaff() {
        try {
            // Check if sample staff already exist to avoid duplicates
            if (staffRepo.getTotalStaffCount() > 0) {
                System.out.println("Sample staff already exist in database, skipping...");
                return;
            }
            
            // Add sample Admin
            Admin admin = new Admin(100001, "John Admin", "admin123");
            staffRepo.addStaff(admin);
            
            // Add sample Receptionists
            Receptionist receptionist1 = new Receptionist(200001, "Alice Front", "reception789");
            Receptionist receptionist2 = new Receptionist(200002, "Bob Desk", "reception456");
            staffRepo.addStaff(receptionist1);
            staffRepo.addStaff(receptionist2);
            
            // Add sample Counselors
            CounselorStaff counselor1 = new CounselorStaff(300001, "Dr. Smith", "counselor123", "Anxiety", true);
            CounselorStaff counselor2 = new CounselorStaff(300002, "Dr. Johnson", "counselor456", "Depression", true);
            CounselorStaff counselor3 = new CounselorStaff(300003, "Dr. Williams", "counselor789", "Stress", false);
            CounselorStaff counselor4 = new CounselorStaff(300004, "Dr. Brown", "counselor321", "Trauma", true);
            CounselorStaff counselor5 = new CounselorStaff(300005, "Dr. Davis", "counselor654", "Addiction", true);
            
            staffRepo.addStaff(counselor1);
            staffRepo.addStaff(counselor2);
            staffRepo.addStaff(counselor3);
            staffRepo.addStaff(counselor4);
            staffRepo.addStaff(counselor5);
            
            System.out.println("✅ Sample staff added to database");
            
        } catch (Exception e) {
            System.err.println("Error adding sample staff: " + e.getMessage());
        }
    }
    
    /**
     * Get current authenticated staff member
     * This would be set after successful login
     */
    public static Staff getCurrentStaff() {
        // In a real application, this would return the currently logged-in staff member
        // For now, return null to indicate no one is logged in
        return null;
    }
    
    /**
     * Check if current staff has specific permission
     */
    public static boolean hasPermission(String permission) {
        Staff currentStaff = getCurrentStaff();
        if (currentStaff == null) {
            return false;
        }
        
        switch (permission.toLowerCase()) {
            case "manage_appointments":
                return currentStaff.canManageAppointments();
            case "manage_counselors":
                return currentStaff.canManageCounselors();
            case "manage_feedback":
                return currentStaff.canManageFeedback();
            case "view_all_data":
                return currentStaff.canViewAllData();
            case "book_appointments":
                return currentStaff.canBookAppointments();
            case "view_schedule":
                return currentStaff.canViewSchedule();
            default:
                return false;
        }
    }
}