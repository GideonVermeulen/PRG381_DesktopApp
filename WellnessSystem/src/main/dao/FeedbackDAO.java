package main.dao;

import main.db.DBConnection;
import main.model.Feedback;
import java.sql.*;
import java.util.*;

public class FeedbackDAO {
    public FeedbackDAO() {
        try (Connection conn = DBConnection.getConnection()) {
            DatabaseMetaData dbm = conn.getMetaData();
            ResultSet tables = dbm.getTables(null, null, "FEEDBACK", null);
            if (!tables.next()) {
                // Table does not exist, create and insert dummy data
                try (Statement st = conn.createStatement()) {
                    st.executeUpdate("CREATE TABLE Feedback (id INT PRIMARY KEY, student VARCHAR(100), counselor VARCHAR(100), date VARCHAR(20), rating INT, comments VARCHAR(255))");
                    ResultSet appts = st.executeQuery("SELECT * FROM Appointments WHERE status='Completed' ORDER BY id FETCH FIRST 50 ROWS ONLY");
                    PreparedStatement insert = conn.prepareStatement("INSERT INTO Feedback VALUES (?, ?, ?, ?, ?, ?)");
                    int i = 1;
                    while (appts.next() && i <= 50) {
                        String student = appts.getString("student");
                        String counselor = appts.getString("counselor");
                        String date = appts.getString("date");
                        int rating = 1 + (i % 5);
                        String comments = "Feedback for completed appointment " + i;
                        insert.setInt(1, i);
                        insert.setString(2, student);
                        insert.setString(3, counselor);
                        insert.setString(4, date);
                        insert.setInt(5, rating);
                        insert.setString(6, comments);
                        insert.executeUpdate();
                        i++;
                    }
                    insert.close();
                    appts.close();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Feedback> getAllFeedback() {
        List<Feedback> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM Feedback ORDER BY date ASC, id ASC")) {
            while (rs.next()) {
                list.add(new Feedback(
                    rs.getInt("id"),
                    rs.getString("student"),
                    rs.getString("counselor"),
                    rs.getString("date"),
                    rs.getInt("rating"),
                    rs.getString("comments")
                ));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    public List<Feedback> getFeedbackForCounselor(String counselorName) {
        List<Feedback> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM Feedback WHERE counselor=? ORDER BY date ASC, id ASC")) {
            ps.setString(1, counselorName);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new Feedback(
                        rs.getInt("id"),
                        rs.getString("student"),
                        rs.getString("counselor"),
                        rs.getString("date"),
                        rs.getInt("rating"),
                        rs.getString("comments")
                    ));
                }
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    public void addFeedback(Feedback f) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement("INSERT INTO Feedback VALUES (?, ?, ?, ?, ?, ?)");) {
            ps.setInt(1, f.getId());
            ps.setString(2, f.getStudentName());
            ps.setString(3, f.getCounselorName());
            ps.setString(4, f.getDate());
            ps.setInt(5, f.getRating());
            ps.setString(6, f.getComments());
            ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    public void updateFeedback(Feedback f) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement("UPDATE Feedback SET student=?, counselor=?, date=?, rating=?, comments=? WHERE id=?")) {
            ps.setString(1, f.getStudentName());
            ps.setString(2, f.getCounselorName());
            ps.setString(3, f.getDate());
            ps.setInt(4, f.getRating());
            ps.setString(5, f.getComments());
            ps.setInt(6, f.getId());
            ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    public void deleteFeedback(int id) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement("DELETE FROM Feedback WHERE id=?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }
}
 