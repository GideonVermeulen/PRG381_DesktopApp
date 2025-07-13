package wellnesssystem.model;

import db.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Appointment operations
 * Handles all database interactions for appointments
 */
public class AppointmentDAO {
    
    /**
     * Add a new appointment to the database
     */
    public static int addAppointment(Appointment appointment) throws SQLException {
        String sql = "INSERT INTO appointments (student_name, counselor_id, appointment_date, appointment_time, status, comments, created_by_id) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setString(1, appointment.getStudentName());
            pstmt.setInt(2, appointment.getCounselor().getId());
            pstmt.setDate(3, java.sql.Date.valueOf(appointment.getDate()));
            // Fix time format: ensure 'HH:mm:ss'
            String timeStr = appointment.getTime();
            if (timeStr.matches("^\\d{2}:\\d{2}$")) {
                timeStr += ":00";
            }
            pstmt.setTime(4, java.sql.Time.valueOf(timeStr));
            pstmt.setString(5, appointment.getStatus());
            pstmt.setString(6, appointment.getComments());
            pstmt.setInt(7, appointment.getCreatedBy().getId());
            
            pstmt.executeUpdate();
            
            // Get the generated ID
            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                int id = rs.getInt(1);
                System.out.println("Appointment added to database with ID: " + id);
                return id;
            }
        }
        
        return -1;
    }
    
    /**
     * Get all appointments from the database
     */
    public static List<Appointment> getAllAppointments() throws SQLException {
        List<Appointment> appointments = new ArrayList<>();
        String sql = "SELECT a.*, s1.name as counselor_name, s2.name as created_by_name " +
                    "FROM appointments a " +
                    "JOIN staff s1 ON a.counselor_id = s1.id " +
                    "JOIN staff s2 ON a.created_by_id = s2.id " +
                    "ORDER BY a.appointment_date, a.appointment_time";
        
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Appointment appointment = createAppointmentFromResultSet(rs);
                appointments.add(appointment);
            }
        }
        
        return appointments;
    }
    
    /**
     * Get appointment by ID
     */
    public static Appointment getAppointmentById(int id) throws SQLException {
        String sql = "SELECT a.*, s1.name as counselor_name, s2.name as created_by_name " +
                    "FROM appointments a " +
                    "JOIN staff s1 ON a.counselor_id = s1.id " +
                    "JOIN staff s2 ON a.created_by_id = s2.id " +
                    "WHERE a.id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return createAppointmentFromResultSet(rs);
            }
        }
        
        return null;
    }
    
    /**
     * Update an existing appointment
     */
    public static boolean updateAppointment(Appointment appointment) throws SQLException {
        String sql = "UPDATE appointments SET student_name = ?, counselor_id = ?, appointment_date = ?, " +
                    "appointment_time = ?, status = ?, comments = ? WHERE id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, appointment.getStudentName());
            pstmt.setInt(2, appointment.getCounselor().getId());
            pstmt.setDate(3, java.sql.Date.valueOf(appointment.getDate()));
            pstmt.setTime(4, java.sql.Time.valueOf(appointment.getTime()));
            pstmt.setString(5, appointment.getStatus());
            pstmt.setString(6, appointment.getComments());
            pstmt.setInt(7, appointment.getId());
            
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Appointment updated in database: " + appointment.getId());
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * Delete an appointment
     */
    public static boolean deleteAppointment(int id) throws SQLException {
        String sql = "DELETE FROM appointments WHERE id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            int rowsAffected = pstmt.executeUpdate();
            
            if (rowsAffected > 0) {
                System.out.println("Appointment deleted from database: " + id);
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * Get appointments by counselor
     */
    public static List<Appointment> getAppointmentsByCounselor(int counselorId) throws SQLException {
        List<Appointment> appointments = new ArrayList<>();
        String sql = "SELECT a.*, s1.name as counselor_name, s2.name as created_by_name " +
                    "FROM appointments a " +
                    "JOIN staff s1 ON a.counselor_id = s1.id " +
                    "JOIN staff s2 ON a.created_by_id = s2.id " +
                    "WHERE a.counselor_id = ? " +
                    "ORDER BY a.appointment_date, a.appointment_time";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, counselorId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Appointment appointment = createAppointmentFromResultSet(rs);
                appointments.add(appointment);
            }
        }
        
        return appointments;
    }
    
    /**
     * Create Appointment object from ResultSet
     */
    private static Appointment createAppointmentFromResultSet(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String studentName = rs.getString("student_name");
        int counselorId = rs.getInt("counselor_id");
        String counselorName = rs.getString("counselor_name");
        String date = rs.getDate("appointment_date").toString();
        String time = rs.getTime("appointment_time").toString();
        String status = rs.getString("status");
        String comments = rs.getString("comments");
        int createdById = rs.getInt("created_by_id");
        String createdByName = rs.getString("created_by_name");
        
        // Create counselor object
        CounselorStaff counselor = new CounselorStaff(counselorId, counselorName, "", "", true);
        
        // Create created by staff object
        Staff createdBy = new Admin(createdById, createdByName, "");
        
        return new Appointment(id, studentName, counselor, date, time, status, createdBy, comments);
    }
    
    /**
     * Delete all appointments (for testing/reset)
     */
    public static void deleteAllAppointments() throws SQLException {
        String sql = "DELETE FROM appointments";
        
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement()) {
            
            stmt.executeUpdate(sql);
            System.out.println("All appointments deleted from database");
        }
    }
} 