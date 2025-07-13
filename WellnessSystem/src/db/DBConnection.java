package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Database connection manager for JavaDB (Apache Derby)
 * Uses local file-based database storage
 */
public class DBConnection {
    private static final String DB_URL = "jdbc:derby:WellnessDB;create=true";
    private static final String DRIVER = "org.apache.derby.jdbc.EmbeddedDriver";
    
    private static Connection connection = null;
    
    /**
     * Get database connection
     */
    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            try {
                // Load the Derby driver
                Class.forName(DRIVER);
                // Print DB path for debug
                String dbPath = new java.io.File("WellnessDB").getAbsolutePath();
                System.out.println("[DEBUG] Derby DB absolute path: " + dbPath);
                // Create connection
                connection = DriverManager.getConnection(DB_URL);
                System.out.println("✅ Database connected successfully");
            } catch (ClassNotFoundException e) {
                throw new SQLException("Derby driver not found: " + e.getMessage());
            }
        }
        return connection;
    }
    
    /**
     * Close database connection
     */
    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("✅ Database connection closed");
            }
        } catch (SQLException e) {
            System.err.println("Error closing database connection: " + e.getMessage());
        }
    }
    
    /**
     * Shutdown Derby database
     */
    public static void shutdownDatabase() {
        try {
            closeConnection();
            DriverManager.getConnection("jdbc:derby:;shutdown=true");
        } catch (SQLException e) {
            // Expected exception when shutting down Derby
            if (e.getSQLState().equals("XJ015")) {
                System.out.println("✅ Database shutdown successfully");
            } else {
                System.err.println("Error shutting down database: " + e.getMessage());
            }
        }
    }
} 