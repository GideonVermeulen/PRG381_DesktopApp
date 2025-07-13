package db;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.SQLException;

/**
 * Database table creation for Wellness System
 * Creates all necessary tables for staff, appointments, and feedback
 */
public class CreateTables {
    
    public static void main(String[] args) {
        createAllTables();
    }
    
    /**
     * Create all database tables
     */
    public static void createAllTables() {
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement()) {
            
            // Create Staff table
            createStaffTable(stmt);

            // Create Appointments table
            createAppointmentsTable(stmt);

            // Create Feedback table
            createFeedbackTable(stmt);
            
            System.out.println("✅ All tables created successfully!");
            
        } catch (SQLException e) {
            System.err.println("Error creating tables: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Create Staff table
     */
    private static void createStaffTable(Statement stmt) throws SQLException {
        String sql = "CREATE TABLE staff (" +
            "id INT PRIMARY KEY," +
            "name VARCHAR(100) NOT NULL," +
            "password VARCHAR(100) NOT NULL," +
            "role VARCHAR(50) NOT NULL," +
            "specialization VARCHAR(100)," +
            "is_available BOOLEAN DEFAULT true," +
            "created_date TIMESTAMP DEFAULT CURRENT TIMESTAMP" +
            ")";
        
        try {
            stmt.execute(sql);
            System.out.println("✅ Staff table created");
        } catch (SQLException e) {
            if (e.getSQLState().equals("X0Y32")) {
                System.out.println("Staff table already exists");
            } else {
                throw e;
            }
        }
    }
    
    /**
     * Create Appointments table
     */
    private static void createAppointmentsTable(Statement stmt) throws SQLException {
        String sql = "CREATE TABLE appointments (" +
            "id INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1)," +
            "student_name VARCHAR(100) NOT NULL," +
            "counselor_id INT NOT NULL," +
            "appointment_date DATE NOT NULL," +
            "appointment_time TIME NOT NULL," +
            "status VARCHAR(20) DEFAULT 'Scheduled'," +
            "comments CLOB," +
            "created_by_id INT NOT NULL," +
            "created_date TIMESTAMP DEFAULT CURRENT TIMESTAMP," +
            "FOREIGN KEY (counselor_id) REFERENCES staff(id)," +
            "FOREIGN KEY (created_by_id) REFERENCES staff(id)" +
            ")";
        
        try {
            stmt.execute(sql);
            System.out.println("✅ Appointments table created");
        } catch (SQLException e) {
            if (e.getSQLState().equals("X0Y32")) {
                System.out.println("Appointments table already exists");
            } else {
                throw e;
            }
        }
    }
    
    /**
     * Create Feedback table
     */
    private static void createFeedbackTable(Statement stmt) throws SQLException {
        String sql = "CREATE TABLE feedback (" +
            "id INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1)," +
            "student_name VARCHAR(100) NOT NULL," +
            "counselor_id INT NOT NULL," +
            "feedback_date DATE NOT NULL," +
            "rating INT NOT NULL CHECK (rating >= 1 AND rating <= 5)," +
            "feedback_text CLOB," +
            "created_by_id INT NOT NULL," +
            "created_date TIMESTAMP DEFAULT CURRENT TIMESTAMP," +
            "FOREIGN KEY (counselor_id) REFERENCES staff(id)," +
            "FOREIGN KEY (created_by_id) REFERENCES staff(id)" +
            ")";
        
        try {
            stmt.execute(sql);
            System.out.println("✅ Feedback table created");
        } catch (SQLException e) {
            if (e.getSQLState().equals("X0Y32")) {
                System.out.println("Feedback table already exists");
            } else {
                throw e;
            }
        }
    }
} 