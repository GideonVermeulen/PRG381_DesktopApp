package wellnesssystem.model;

import db.DBConnection;
import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * StaffRepository: Handles all staff DB operations and business logic (Admin, Counselor, Receptionist)
 */
public class StaffRepository {
    // Add staff
    public void addStaff(Staff staff) throws IllegalArgumentException {
        if (staff == null) throw new IllegalArgumentException("Staff cannot be null");
        if (!Staff.isValidId(staff.getId())) throw new IllegalArgumentException("Invalid staff ID: " + staff.getId() + ". Must be 6 digits.");
        if (!Staff.isValidPassword(staff.getPassword())) throw new IllegalArgumentException("Invalid password. Must be at least 6 characters.");
        try {
            if (findStaffById(staff.getId()) != null) throw new IllegalArgumentException("Staff with ID " + staff.getId() + " already exists.");
            String sql = "INSERT INTO staff (id, name, password, role, specialization, is_available) VALUES (?, ?, ?, ?, ?, ?)";
            try (Connection conn = DBConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, staff.getId());
                pstmt.setString(2, staff.getName());
                pstmt.setString(3, staff.getPassword());
                pstmt.setString(4, staff.getRole());
                if (staff instanceof CounselorStaff) {
                    pstmt.setString(5, ((CounselorStaff) staff).getSpecialization());
                    pstmt.setBoolean(6, ((CounselorStaff) staff).isAvailable());
                } else {
                    pstmt.setString(5, null);
                    pstmt.setBoolean(6, true);
                }
                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            throw new IllegalArgumentException("Database error: " + e.getMessage());
        }
    }

    // Remove staff
    public boolean removeStaff(int staffId) {
        try {
            if (findStaffById(staffId) != null) {
                String sql = "DELETE FROM staff WHERE id = ?";
                try (Connection conn = DBConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    pstmt.setInt(1, staffId);
                    pstmt.executeUpdate();
                }
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Database error removing staff: " + e.getMessage());
        }
        return false;
    }

    // Find staff by ID
    public Staff findStaffById(int staffId) {
        String sql = "SELECT * FROM staff WHERE id = ?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, staffId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) return createStaffFromResultSet(rs);
        } catch (SQLException e) {
            System.err.println("Database error finding staff: " + e.getMessage());
        }
        return null;
    }

    // Find staff by name
    public List<Staff> findStaffByName(String name) {
        return getAllStaff().stream().filter(staff -> staff.getName().toLowerCase().contains(name.toLowerCase())).collect(Collectors.toList());
    }

    // Get all staff by role
    public List<Staff> getStaffByRole(String role) {
        return getAllStaff().stream().filter(staff -> staff.getRole().equals(role)).collect(Collectors.toList());
    }

    // Get all admins
    public List<Admin> getAllAdmins() {
        return getAllStaff().stream().filter(staff -> staff instanceof Admin).map(staff -> (Admin) staff).collect(Collectors.toList());
    }

    // Get all receptionists
    public List<Receptionist> getAllReceptionists() {
        return getAllStaff().stream().filter(staff -> staff instanceof Receptionist).map(staff -> (Receptionist) staff).collect(Collectors.toList());
    }

    // Get all counselors
    public List<CounselorStaff> getAllCounselors() {
        List<CounselorStaff> counselors = new ArrayList<>();
        String sql = "SELECT * FROM staff WHERE role = 'Counselor' ORDER BY id";
        try (Connection conn = DBConnection.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Staff staff = createStaffFromResultSet(rs);
                if (staff instanceof CounselorStaff) counselors.add((CounselorStaff) staff);
            }
        } catch (SQLException e) {
            System.err.println("Database error getting counselors: " + e.getMessage());
        }
        return counselors;
    }

    // Get available counselors
    public List<CounselorStaff> getAvailableCounselors() {
        return getAllCounselors().stream().filter(CounselorStaff::isAvailable).collect(Collectors.toList());
    }

    // Get counselors by specialization
    public List<CounselorStaff> getCounselorsBySpecialization(String specialization) {
        return getAllCounselors().stream().filter(c -> c.getSpecialization().equalsIgnoreCase(specialization)).collect(Collectors.toList());
    }

    // Authenticate staff member
    public Staff authenticate(int staffId, String password) {
        String sql = "SELECT * FROM staff WHERE id = ? AND password = ?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, staffId);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) return createStaffFromResultSet(rs);
        } catch (SQLException e) {
            System.err.println("Database authentication error: " + e.getMessage());
        }
        return null;
    }

    // Update staff
    public void updateStaff(Staff staff) throws SQLException {
        if (staff == null) throw new IllegalArgumentException("Staff cannot be null");
        String sql = "UPDATE staff SET name = ?, password = ?, role = ?, specialization = ?, is_available = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, staff.getName());
            pstmt.setString(2, staff.getPassword());
            pstmt.setString(3, staff.getRole());
            if (staff instanceof CounselorStaff) {
                pstmt.setString(4, ((CounselorStaff) staff).getSpecialization());
                pstmt.setBoolean(5, ((CounselorStaff) staff).isAvailable());
            } else {
                pstmt.setString(4, null);
                pstmt.setBoolean(5, true);
            }
            pstmt.setInt(6, staff.getId());
            pstmt.executeUpdate();
        }
    }

    // Get all staff
    public List<Staff> getAllStaff() {
        List<Staff> staffList = new ArrayList<>();
        String sql = "SELECT * FROM staff ORDER BY id";
        try (Connection conn = DBConnection.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            System.out.println("[DEBUG] StaffRepository: Executing SQL: " + sql);
            int count = 0;
            while (rs.next()) {
                count++;
                Staff staff = createStaffFromResultSet(rs);
                System.out.println("[DEBUG] StaffRepository: Loaded staff #" + count + ": ID=" + staff.getId() + ", Name=" + staff.getName() + ", Role=" + staff.getRole());
                staffList.add(staff);
            }
            System.out.println("[DEBUG] StaffRepository: Total staff loaded from database: " + count);
        } catch (SQLException e) {
            System.err.println("Database error loading staff: " + e.getMessage());
            e.printStackTrace();
        }
        return staffList;
    }

    // Get staff count by role
    public Map<String, Integer> getStaffCountByRole() {
        return getAllStaff().stream().collect(Collectors.groupingBy(Staff::getRole, Collectors.collectingAndThen(Collectors.counting(), Long::intValue)));
    }

    // Get total staff count
    public int getTotalStaffCount() {
        return getAllStaff().size();
    }

    // --- Utility ---
    private Staff createStaffFromResultSet(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String name = rs.getString("name");
        String password = rs.getString("password");
        String role = rs.getString("role");
        String specialization = rs.getString("specialization");
        boolean isAvailable = rs.getBoolean("is_available");
        switch (role) {
            case "Admin": return new Admin(id, name, password);
            case "Receptionist": return new Receptionist(id, name, password);
            case "Counselor": return new CounselorStaff(id, name, password, specialization, isAvailable);
            default: throw new SQLException("Unknown role: " + role);
        }
    }
} 