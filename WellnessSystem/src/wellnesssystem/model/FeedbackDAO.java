package wellnesssystem.model;

import db.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Feedback operations
 * Handles all database interactions for feedback
 */
public class FeedbackDAO {
    
    /**
     * Add a new feedback to the database
     */
    public static int addFeedback(Feedback feedback) throws SQLException {
        String sql = "INSERT INTO feedback (student_name, counselor_id, feedback_date, rating, feedback_text, created_by_id) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setString(1, feedback.getStudentName());
            pstmt.setInt(2, feedback.getCounselor().getId());
            pstmt.setDate(3, java.sql.Date.valueOf(feedback.getDate()));
            pstmt.setInt(4, feedback.getRating());
            pstmt.setString(5, feedback.getFeedback());
            pstmt.setInt(6, feedback.getCreatedBy().getId());
            
            pstmt.executeUpdate();
            
            // Get the generated ID
            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                int id = rs.getInt(1);
                System.out.println("Feedback added to database with ID: " + id);
                return id;
            }
        }
        
        return -1;
    }
    
    /**
     * Get all feedback from the database
     */
    public static List<Feedback> getAllFeedback() throws SQLException {
        List<Feedback> feedbacks = new ArrayList<>();
        String sql = "SELECT f.*, s1.name as counselor_name, s2.name as created_by_name " +
                    "FROM feedback f " +
                    "JOIN staff s1 ON f.counselor_id = s1.id " +
                    "JOIN staff s2 ON f.created_by_id = s2.id " +
                    "ORDER BY f.feedback_date DESC";
        
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Feedback feedback = createFeedbackFromResultSet(rs);
                feedbacks.add(feedback);
            }
        }
        
        return feedbacks;
    }
    
    /**
     * Get feedback by ID
     */
    public static Feedback getFeedbackById(int id) throws SQLException {
        String sql = "SELECT f.*, s1.name as counselor_name, s2.name as created_by_name " +
                    "FROM feedback f " +
                    "JOIN staff s1 ON f.counselor_id = s1.id " +
                    "JOIN staff s2 ON f.created_by_id = s2.id " +
                    "WHERE f.id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return createFeedbackFromResultSet(rs);
            }
        }
        
        return null;
    }
    
    /**
     * Update an existing feedback
     */
    public static boolean updateFeedback(Feedback feedback) throws SQLException {
        String sql = "UPDATE feedback SET student_name = ?, counselor_id = ?, feedback_date = ?, " +
                    "rating = ?, feedback_text = ? WHERE id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, feedback.getStudentName());
            pstmt.setInt(2, feedback.getCounselor().getId());
            pstmt.setDate(3, java.sql.Date.valueOf(feedback.getDate()));
            pstmt.setInt(4, feedback.getRating());
            pstmt.setString(5, feedback.getFeedback());
            pstmt.setInt(6, feedback.getId());
            
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Feedback updated in database: " + feedback.getId());
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * Delete a feedback
     */
    public static boolean deleteFeedback(int id) throws SQLException {
        String sql = "DELETE FROM feedback WHERE id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            int rowsAffected = pstmt.executeUpdate();
            
            if (rowsAffected > 0) {
                System.out.println("Feedback deleted from database: " + id);
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * Get feedback by counselor
     */
    public static List<Feedback> getFeedbackByCounselor(int counselorId) throws SQLException {
        List<Feedback> feedbacks = new ArrayList<>();
        String sql = "SELECT f.*, s1.name as counselor_name, s2.name as created_by_name " +
                    "FROM feedback f " +
                    "JOIN staff s1 ON f.counselor_id = s1.id " +
                    "JOIN staff s2 ON f.created_by_id = s2.id " +
                    "WHERE f.counselor_id = ? " +
                    "ORDER BY f.feedback_date DESC";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, counselorId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Feedback feedback = createFeedbackFromResultSet(rs);
                feedbacks.add(feedback);
            }
        }
        
        return feedbacks;
    }
    
    /**
     * Create Feedback object from ResultSet
     */
    private static Feedback createFeedbackFromResultSet(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String studentName = rs.getString("student_name");
        int counselorId = rs.getInt("counselor_id");
        String counselorName = rs.getString("counselor_name");
        String date = rs.getDate("feedback_date").toString();
        int rating = rs.getInt("rating");
        String feedbackText = rs.getString("feedback_text");
        int createdById = rs.getInt("created_by_id");
        String createdByName = rs.getString("created_by_name");
        
        // Create counselor object
        CounselorStaff counselor = new CounselorStaff(counselorId, counselorName, "", "", true);
        
        // Create created by staff object
        Staff createdBy = new Admin(createdById, createdByName, "");
        
        return new Feedback(id, studentName, counselor, date, rating, feedbackText, createdBy);
    }
    
    /**
     * Delete all feedback (for testing/reset)
     */
    public static void deleteAllFeedback() throws SQLException {
        String sql = "DELETE FROM feedback";
        
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement()) {
            
            stmt.executeUpdate(sql);
            System.out.println("All feedback deleted from database");
        }
    }
} 