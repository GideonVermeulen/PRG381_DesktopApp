package main.dao;

import main.db.DBConnection;
import main.model.*;
import java.sql.*;
import java.util.*;
import encryption.Encryptor;

public class UserDAO {
    private StaffDAO staffDAO = new StaffDAO();

    public UserDAO() {
        // Constructor - tables are created by StaffDAO
    }

    public User authenticateUser(int id, String password) {
        String hashed = Encryptor.hashPassword(password);
        try (Connection conn = DBConnection.getConnection()) {
            // 1. Try hashed password
            try (PreparedStatement ps = conn.prepareStatement(
                    "SELECT * FROM Counselors WHERE id = ? AND password = ?")) {
                ps.setInt(1, id);
                ps.setString(2, hashed);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        String role = rs.getString("role");
                        String name = rs.getString("name");
                        String specialization = rs.getString("specialization");
                        boolean available = rs.getBoolean("available");
                        switch (role) {
                            case "Admin": return new Admin(id, name, "");
                            case "Receptionist": return new Receptionist(id, name, "");
                            case "Counselor": return new Counselor(id, name, "", specialization, available);
                            default: return null;
                        }
                    }
                }
            }
            // 2. Try plain password (legacy)
            try (PreparedStatement ps = conn.prepareStatement(
                    "SELECT * FROM Counselors WHERE id = ? AND password = ?")) {
                ps.setInt(1, id);
                ps.setString(2, password);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        // Upgrade password to hash
                        try (PreparedStatement ups = conn.prepareStatement(
                                "UPDATE Counselors SET password = ? WHERE id = ?")) {
                            ups.setString(1, hashed);
                            ups.setInt(2, id);
                            ups.executeUpdate();
                        }
                        String role = rs.getString("role");
                        String name = rs.getString("name");
                        String specialization = rs.getString("specialization");
                        boolean available = rs.getBoolean("available");
                        switch (role) {
                            case "Admin": return new Admin(id, name, "");
                            case "Receptionist": return new Receptionist(id, name, "");
                            case "Counselor": return new Counselor(id, name, "", specialization, available);
                            default: return null;
                        }
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