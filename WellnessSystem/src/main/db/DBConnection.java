package main.db;

import java.sql.*;

public class DBConnection {
    private static final String DB_URL = "jdbc:derby:WellnessDB;create=true";
    private static final String USER = "app";
    private static final String PASS = "app";

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("Derby driver not found", e);
        }
        return DriverManager.getConnection(DB_URL, USER, PASS);
    }

    public static void closeQuietly(AutoCloseable c) {
        if (c != null) {
            try { c.close(); } catch (Exception ignored) {}
        }
    }

    // TEMP: Drop all main tables for a clean reset
    public static void dropAllTables() {
        try (Connection conn = getConnection(); Statement st = conn.createStatement()) {
            st.executeUpdate("DROP TABLE Feedback");
        } catch (Exception e) { /* ignore if not exist */ }
        try (Connection conn = getConnection(); Statement st = conn.createStatement()) {
            st.executeUpdate("DROP TABLE Appointments");
        } catch (Exception e) { /* ignore if not exist */ }
        try (Connection conn = getConnection(); Statement st = conn.createStatement()) {
            st.executeUpdate("DROP TABLE Counselors");
        } catch (Exception e) { /* ignore if not exist */ }
    }
} 