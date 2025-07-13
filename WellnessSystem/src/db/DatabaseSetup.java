package db;

import wellnesssystem.model.*;
import wellnesssystem.WellnessSystem;
import java.sql.SQLException;

/**
 * Database setup utility
 * Creates tables and adds sample data
 */
public class DatabaseSetup {
    
    public static void main(String[] args) {
        System.out.println("🔧 Setting up Wellness System Database...");
        
        try {
            // Create tables
            CreateTables.createAllTables();
            
            // Add sample staff
            addSampleStaff();
            
            System.out.println("✅ Database setup completed successfully!");
            
        } catch (Exception e) {
            System.err.println("❌ Database setup failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private static void addSampleStaff() throws SQLException {
        // Clear existing staff
        for (Staff s : WellnessSystem.staffRepo.getAllStaff()) {
            WellnessSystem.staffRepo.removeStaff(s.getId());
        }
        // Add sample Admin
        Admin admin = new Admin(100001, "John Admin", "admin123");
        WellnessSystem.staffRepo.addStaff(admin);
        // Add sample Receptionist
        Receptionist receptionist = new Receptionist(200001, "Alice Front", "reception789");
        WellnessSystem.staffRepo.addStaff(receptionist);
        // Add sample Counselors
        CounselorStaff counselor1 = new CounselorStaff(300001, "Dr. Smith", "counselor123", "Anxiety", true);
        CounselorStaff counselor2 = new CounselorStaff(300002, "Dr. Johnson", "counselor456", "Depression", true);
        CounselorStaff counselor3 = new CounselorStaff(300003, "Dr. Williams", "counselor789", "Stress", false);
        WellnessSystem.staffRepo.addStaff(counselor1);
        WellnessSystem.staffRepo.addStaff(counselor2);
        WellnessSystem.staffRepo.addStaff(counselor3);
        System.out.println("✅ Sample staff added to database");
    }
} 