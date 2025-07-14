package main.dao;

import main.db.DBConnection;
import main.model.*;
import java.sql.*;
import java.util.*;

public class UserDAO {
    private StaffDAO staffDAO = new StaffDAO();

    public UserDAO() {
        // Constructor - tables are created by StaffDAO
    }

    public User authenticateUser(int id, String password) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                 "SELECT * FROM Counselors WHERE id = ? AND password = ?")) {

            ps.setInt(1, id);
            ps.setString(2, password);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String role = rs.getString("role");
                    String name = rs.getString("name");
                    String specialization = rs.getString("specialization");
                    boolean available = rs.getBoolean("available");

                    switch (role) {
                        case "Admin":
                            return new Admin(id, name, password);
                        case "Receptionist":
                            return new Receptionist(id, name, password);
                        case "Counselor":
                            return new Counselor(id, name, password, specialization, available);
                        default:
                            return null;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<User> getAllUsers() {
        return staffDAO.getAllStaff();
    }

    public List<User> getUsersByRole(String role) {
        return staffDAO.getStaffByRole(role);
    }
} 