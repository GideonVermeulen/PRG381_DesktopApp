package main.dao;

import main.db.DBConnection;
import main.model.*;
import java.sql.*;
import java.util.*;
import encryption.Encryptor;

public class StaffDAO {
    public StaffDAO() {
        try (Connection conn = DBConnection.getConnection()) {
            DatabaseMetaData dbm = conn.getMetaData();
            ResultSet tables = dbm.getTables(null, null, "COUNSELORS", null);
            if (!tables.next()) {
                // Table does not exist, create and insert dummy data
                try (Statement st = conn.createStatement()) {
                    st.executeUpdate("CREATE TABLE Counselors (id INT PRIMARY KEY, name VARCHAR(100), password VARCHAR(100), specialization VARCHAR(100), available BOOLEAN, role VARCHAR(20))");
                    st.executeUpdate("INSERT INTO Counselors VALUES (300001, 'Dr. Smith', '" + Encryptor.hashPassword("counselor123") + "', 'Anxiety', true, 'Counselor')");
                    st.executeUpdate("INSERT INTO Counselors VALUES (300002, 'Dr. Johnson', '" + Encryptor.hashPassword("counselor456") + "', 'Depression', true, 'Counselor')");
                    st.executeUpdate("INSERT INTO Counselors VALUES (300003, 'Dr. Williams', '" + Encryptor.hashPassword("counselor789") + "', 'Stress', false, 'Counselor')");
                    st.executeUpdate("INSERT INTO Counselors VALUES (300004, 'Dr. Brown', '" + Encryptor.hashPassword("counselor321") + "', 'Relationships', true, 'Counselor')");
                    st.executeUpdate("INSERT INTO Counselors VALUES (300005, 'Dr. Lee', '" + Encryptor.hashPassword("counselor654") + "', 'Career', true, 'Counselor')");
                    st.executeUpdate("INSERT INTO Counselors VALUES (200001, 'Alice Front', '" + Encryptor.hashPassword("reception789") + "', '', true, 'Receptionist')");
                    st.executeUpdate("INSERT INTO Counselors VALUES (200002, 'Bob Desk', '" + Encryptor.hashPassword("reception456") + "', '', true, 'Receptionist')");
                    st.executeUpdate("INSERT INTO Counselors VALUES (200003, 'Carol Greet', '" + Encryptor.hashPassword("reception123") + "', '', true, 'Receptionist')");
                    st.executeUpdate("INSERT INTO Counselors VALUES (100001, 'John Admin', '" + Encryptor.hashPassword("admin123") + "', '', true, 'Admin')");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<User> getAllStaff() {
        List<User> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM Counselors ORDER BY id")) {
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String password = rs.getString("password");
                String specialization = rs.getString("specialization");
                boolean available = rs.getBoolean("available");
                String role = rs.getString("role");
                switch (role) {
                    case "Admin":
                        list.add(new Admin(id, name, password));
                        break;
                    case "Receptionist":
                        list.add(new Receptionist(id, name, password));
                        break;
                    case "Counselor":
                        list.add(new Counselor(id, name, password, specialization, available));
                        break;
                }
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    public List<User> getStaffByRole(String role) {
        List<User> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM Counselors WHERE role=? ORDER BY id")) {
            ps.setString(1, role);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("id");
                    String name = rs.getString("name");
                    String password = rs.getString("password");
                    String specialization = rs.getString("specialization");
                    boolean available = rs.getBoolean("available");
                    switch (role) {
                        case "Admin":
                            list.add(new Admin(id, name, password));
                            break;
                        case "Receptionist":
                            list.add(new Receptionist(id, name, password));
                            break;
                        case "Counselor":
                            list.add(new Counselor(id, name, password, specialization, available));
                            break;
                    }
                }
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    public void addStaff(User user) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement("INSERT INTO Counselors VALUES (?, ?, ?, ?, ?, ?)");) {
            ps.setInt(1, user.getId());
            ps.setString(2, user.getName());
            ps.setString(3, Encryptor.hashPassword(user.getPassword()));
            if (user instanceof Counselor) {
                Counselor c = (Counselor) user;
                ps.setString(4, c.getSpecialization());
                ps.setBoolean(5, c.isAvailable());
                ps.setString(6, "Counselor");
            } else if (user instanceof Receptionist) {
                ps.setString(4, "");
                ps.setBoolean(5, true);
                ps.setString(6, "Receptionist");
            } else if (user instanceof Admin) {
                ps.setString(4, "");
                ps.setBoolean(5, true);
                ps.setString(6, "Admin");
            }
            ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    public void updateStaff(User user) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement("UPDATE Counselors SET name=?, password=?, specialization=?, available=?, role=? WHERE id=?")) {
            ps.setString(1, user.getName());
            ps.setString(2, Encryptor.hashPassword(user.getPassword()));
            if (user instanceof Counselor) {
                Counselor c = (Counselor) user;
                ps.setString(3, c.getSpecialization());
                ps.setBoolean(4, c.isAvailable());
                ps.setString(5, "Counselor");
            } else if (user instanceof Receptionist) {
                ps.setString(3, "");
                ps.setBoolean(4, true);
                ps.setString(5, "Receptionist");
            } else if (user instanceof Admin) {
                ps.setString(3, "");
                ps.setBoolean(4, true);
                ps.setString(5, "Admin");
            }
            ps.setInt(6, user.getId());
            ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    public void deleteStaff(int id) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement("DELETE FROM Counselors WHERE id=?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }
}
