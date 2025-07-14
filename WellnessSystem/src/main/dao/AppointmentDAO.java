package main.dao;

import main.db.DBConnection;
import main.model.Appointment;
import java.sql.*;
import java.util.*;

public class AppointmentDAO {
    public AppointmentDAO() {
        try (Connection conn = DBConnection.getConnection()) {
            DatabaseMetaData dbm = conn.getMetaData();
            ResultSet tables = dbm.getTables(null, null, "APPOINTMENTS", null);
            if (!tables.next()) {
                // Table does not exist, create and insert dummy data
                try (Statement st = conn.createStatement()) {
                    st.executeUpdate("CREATE TABLE Appointments (id INT PRIMARY KEY, student VARCHAR(100), counselor VARCHAR(100), date VARCHAR(20), time VARCHAR(10), status VARCHAR(20), comments VARCHAR(255))");
                    // 50 completed appointments (past)
                    for (int i = 1; i <= 50; i++) {
                        String student = "Student" + i;
                        String counselor = "Dr. Smith";
                        if (i % 5 == 2) counselor = "Dr. Johnson";
                        if (i % 5 == 3) counselor = "Dr. Williams";
                        if (i % 5 == 4) counselor = "Dr. Brown";
                        if (i % 5 == 0) counselor = "Dr. Lee";
                        String date = "2024-10-" + String.format("%02d", (i % 30) + 1);
                        String time = String.format("%02d:00", 8 + (i % 8));
                        String comments = "Completed appointment " + i;
                        st.executeUpdate(String.format("INSERT INTO Appointments VALUES (%d, '%s', '%s', '%s', '%s', 'Completed', '%s')", i, student, counselor, date, time, comments));
                    }
                    // 30 scheduled appointments (October 2025)
                    for (int i = 51; i <= 80; i++) {
                        String student = "Student" + i;
                        String counselor = "Dr. Smith";
                        if (i % 5 == 2) counselor = "Dr. Johnson";
                        if (i % 5 == 3) counselor = "Dr. Williams";
                        if (i % 5 == 4) counselor = "Dr. Brown";
                        if (i % 5 == 0) counselor = "Dr. Lee";
                        String date = "2025-10-" + String.format("%02d", ((i-50) % 30) + 1);
                        String time = String.format("%02d:00", 8 + ((i-50) % 8));
                        String comments = "Scheduled appointment " + i;
                        st.executeUpdate(String.format("INSERT INTO Appointments VALUES (%d, '%s', '%s', '%s', '%s', 'Scheduled', '%s')", i, student, counselor, date, time, comments));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Appointment> getAllAppointments() {
        List<Appointment> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM Appointments")) {
            while (rs.next()) {
                list.add(new Appointment(
                    rs.getInt("id"),
                    rs.getString("student"),
                    rs.getString("counselor"),
                    rs.getString("date"),
                    rs.getString("time"),
                    rs.getString("status"),
                    rs.getString("comments")
                ));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    public void addAppointment(Appointment a) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement("INSERT INTO Appointments VALUES (?, ?, ?, ?, ?, ?, ?)");) {
            ps.setInt(1, a.getId());
            ps.setString(2, a.getStudentName());
            ps.setString(3, a.getCounselorName());
            ps.setString(4, a.getDate());
            ps.setString(5, a.getTime());
            ps.setString(6, a.getStatus());
            ps.setString(7, a.getComments());
            ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    public void updateAppointment(Appointment a) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement("UPDATE Appointments SET student=?, counselor=?, date=?, time=?, status=?, comments=? WHERE id=?")) {
            ps.setString(1, a.getStudentName());
            ps.setString(2, a.getCounselorName());
            ps.setString(3, a.getDate());
            ps.setString(4, a.getTime());
            ps.setString(5, a.getStatus());
            ps.setString(6, a.getComments());
            ps.setInt(7, a.getId());
            ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    public void deleteAppointment(int id) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement("DELETE FROM Appointments WHERE id=?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }
}
